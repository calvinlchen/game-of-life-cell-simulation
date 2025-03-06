package cellsociety.model.util;

import cellsociety.model.statefactory.CellStateFactory;
import cellsociety.model.statefactory.handler.CellStateHandler;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.CellStates;
import cellsociety.model.util.exceptions.XmlException;

import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A utility class for reading and writing simulation data to/from XML files. Provides functionality
 * to read simulation details, grid data, and parameters from XML files, and also to write
 * simulation data to XML files.
 *
 * @author Kyaira Boughton
 */
public class XmlUtils {

  private static final Logger logger = LogManager.getLogger(XmlUtils.class);

  /**
   * Constructs an XMLUtils object with the current program language.
   */
  public XmlUtils() {

  }

  /**
   * Reads an XML file and parses it into an XMLData object.
   *
   * @param fxmlFile the XML file to be read
   * @return an XMLData object containing the parsed simulation data
   * @throws XmlException if there is an error reading or parsing the XML file
   */
  public XmlData readXml(File fxmlFile) {
    XmlData xmlObject = new XmlData();

    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(fxmlFile);
      doc.getDocumentElement().normalize();

      NodeList simulationList = doc.getElementsByTagName("simulation");
      if (simulationList.getLength() == 0) {
        throw new XmlException("NoSimTag");
      }

      for (int i = 0; i < simulationList.getLength(); i++) {
        Node simulationNode = simulationList.item(i);
        if (simulationNode.getNodeType() == Node.ELEMENT_NODE) {
          Element simulationElement = (Element) simulationNode;

          // Extract metadata
          Element metadataElement = (Element) simulationElement.getElementsByTagName("metadata")
              .item(0);
          String simulationType = simulationElement.getElementsByTagName("type").item(0)
              .getTextContent();
          xmlObject.setType(simTypeFromString(simulationType));

          xmlObject.setTitle(
              metadataElement.getElementsByTagName("title").item(0).getTextContent());
          xmlObject.setAuthor(
              metadataElement.getElementsByTagName("author").item(0).getTextContent());
          xmlObject.setDescription(
              metadataElement.getElementsByTagName("description").item(0).getTextContent());

          NodeList languageNodes = metadataElement.getElementsByTagName("language");
          if (languageNodes.getLength() > 0) {
            xmlObject.setLanguage(languageNodes.item(0).getTextContent());
          } else {
            xmlObject.setLanguage(
                "English"); //if no language in og xml, will use the input language as default.
          }

          // Extract parameters
          Element parametersElement = (Element) simulationElement.getElementsByTagName("parameters")
              .item(0);
          if (parametersElement != null) {
            NodeList paramList = parametersElement.getElementsByTagName("parameter");
            Map<String, Object> params = parameterToMap(paramList, xmlObject.getType());
            xmlObject.setParameters(params);
          } else {
            xmlObject.setParameters(new HashMap<>());  // Prevent parameters from being null
          }

          CellStateHandler handler = CellStateFactory.getHandler(xmlObject.getId(),
              xmlObject.getType(), xmlObject.getNumStates());
          if (handler == null) {
            throw new XmlException("UnknownSimType", xmlObject.getType());
          }

          NodeList colorsList = metadataElement.getElementsByTagName("color");
          if (colorsList.getLength() > 0) {
            xmlObject.setCustomColorMap(colorsToMap(colorsList));
          }

          // Extract grid info
          Element gridElement = (Element) simulationElement.getElementsByTagName("grid").item(0);
          int rows = Integer.parseInt(gridElement.getAttribute("rows"));
          int columns = Integer.parseInt(gridElement.getAttribute("columns"));
          xmlObject.setGridRowNum(rows);
          xmlObject.setGridColNum(columns);

          // Check for variation
          Node variationNode = gridElement.getElementsByTagName("variation").item(0);

          if (variationNode != null) {
            xmlObject.setCellStateList(setCellStatesByVariation(variationNode, (rows*columns), xmlObject.getType()));

          } else {
            // No variation: use explicitly defined cell states
            NodeList cellList = gridElement.getElementsByTagName("cell");
            if (cellList.getLength() != rows * columns) {
              throw new XmlException("ExpectedDifferentNumber", cellList.getLength());
            }
            xmlObject.setCellStateList(cellStatesToEnum(cellList, handler));
          }
        }
      }
    } catch (Exception e) {
      throw new XmlException("SimulationSetupFailed", e.getMessage());
    }

    return xmlObject;
  }

  /**
   * Writes simulation data to an XML file.
   *
   * @param file        the file to store the XML data
   * @param title       the title of the simulation
   * @param author      the author of the simulation
   * @param description the description of the simulation
   * @param simulation  the simulation object containing the simulation data
   * @throws XmlException if there is an error writing to the XML file
   */
  public void writeToXml(File file, String title, String author, String description,
      Simulation<?> simulation) {
    if (file == null) {
      throw new XmlException("NoFileSelectedSave");
    }

    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      // Create root element
      Element rootElement = doc.createElement("simulation");
      doc.appendChild(rootElement);

      // Add metadata section
      Element metadataElement = doc.createElement("metadata");
      rootElement.appendChild(metadataElement);

      Element typeElement = doc.createElement("type");
      typeElement.appendChild(
          doc.createTextNode(simulation.getXmlDataObject().getType().toString()));
      metadataElement.appendChild(typeElement);

      Element titleElement = doc.createElement("title");
      titleElement.appendChild(doc.createTextNode(title));
      metadataElement.appendChild(titleElement);

      Element authorElement = doc.createElement("author");
      authorElement.appendChild(doc.createTextNode(author));
      metadataElement.appendChild(authorElement);

      Element languageElement = doc.createElement("language");
      languageElement.appendChild(doc.createTextNode(simulation.getXmlDataObject().getLanguage()));
      metadataElement.appendChild(languageElement);

      Element descriptionElement = doc.createElement("description");
      descriptionElement.appendChild(doc.createTextNode(description));
      metadataElement.appendChild(descriptionElement);

      // Write custom color mappings if they exist
      Map<Integer, String> customColors = simulation.getXmlDataObject().getCustomColorMap();
      if (customColors != null && !customColors.isEmpty()) {
        Element colorsElement = doc.createElement("colors");
        metadataElement.appendChild(colorsElement);
        for (Map.Entry<Integer, String> entry : customColors.entrySet()) {
          Element colorElement = doc.createElement("color");
          colorElement.setAttribute("cellType", String.valueOf(entry.getKey()));
          colorElement.setAttribute("value", entry.getValue());
          colorsElement.appendChild(colorElement);
        }
      }

      // Add grid info
      Element gridElement = doc.createElement("grid");
      gridElement.setAttribute("rows",
          String.valueOf(simulation.getXmlDataObject().getGridRowNum()));
      gridElement.setAttribute("columns",
          String.valueOf(simulation.getXmlDataObject().getGridColNum()));
      rootElement.appendChild(gridElement);

      // Add cell states
      ArrayList<String> cellStateList = cellStatesToString(
          simulation.getXmlDataObject().getGridRowNum(),
          simulation.getXmlDataObject().getGridColNum(), simulation);
      for (String state : cellStateList) {
        Element cellElement = doc.createElement("cell");
        cellElement.setAttribute("state", state);
        gridElement.appendChild(cellElement);
      }

      // Add parameters section
      Element parametersElement = doc.createElement("parameters");
      rootElement.appendChild(parametersElement);

      // Add parameters
      Map<String, Object> parameters = simulation.getXmlDataObject().getParameters();
      for (Map.Entry<String, Object> entry : parameters.entrySet()) {
        Element paramElement = doc.createElement("parameter");
        paramElement.setAttribute("name", entry.getKey());
        paramElement.setAttribute("value", String.valueOf(entry.getValue()));
        parametersElement.appendChild(paramElement);
      }

      // Write the content to the selected XML file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(file);
      transformer.transform(source, result);

    } catch (Exception e) {
      throw new XmlException("XMLSaveError", e.getMessage());
    }
  }

  /**
   * Converts the cell states from a simulation object into a list of strings.
   *
   * @param rowNum     the number of rows in the grid
   * @param colNum     the number of columns in the grid
   * @param simulation the simulation object containing the grid's cell states
   * @return a list of strings representing the state of each cell in the grid
   * @throws IllegalArgumentException if an invalid cell state is encountered
   */
  private ArrayList<String> cellStatesToString(int rowNum, int colNum, Simulation<?> simulation) {
    ArrayList<String> cellStateList = new ArrayList<>();

    // Get the handler for the simulation's cell states
    CellStateHandler handler = CellStateFactory.getHandler(simulation.getSimulationId(),
        simulation.getSimulationType(), simulation.getNumStates());

    if (handler == null) {
      throw new XmlException("UnknownSimType", simulation.getSimulationType());
    }

    for (int i = 0; i < rowNum; i++) {
      for (int j = 0; j < colNum; j++) {
        int currentCellState = simulation.getCurrentState(i, j);

        if (!handler.isValidState(currentCellState)) {
          throw new XmlException("UnknownCellState", currentCellState);
        }

        cellStateList.add(handler.statetoString(currentCellState));
      }
    }

    return cellStateList;
  }

  /**
   * A method that extracts the cellstates from the simulation xml and turns them into a string
   * list.
   *
   * @param cellList an NodeList variable that holds the xml data's cell data
   * @param handler  the handler to convert the string to the appropriate cell state
   * @return a list of integers representing the cell states as enums
   * @throws IllegalArgumentException if an invalid cell state is encountered
   */
  private ArrayList<Integer> cellStatesToEnum(NodeList cellList, CellStateHandler handler) {
    ArrayList<Integer> cellStateEnums = new ArrayList<>();

    for (int j = 0; j < cellList.getLength(); j++) {
      Element cellElement = (Element) cellList.item(j);
      String currentCellState = cellElement.getAttribute("state");

      try {
        int stateEnum = handler.stateFromString(currentCellState);
        cellStateEnums.add(stateEnum);
      } catch (IllegalArgumentException e) {
        throw new XmlException("UnknownCellState", currentCellState);
      }
    }

    return cellStateEnums;
  }

  /**
   * Converts the parameters from the XML file into a map.
   *
   * @param paramList      a NodeList containing the parameter data
   * @param simulationType the type of the simulation
   * @return a map of parameter names to their corresponding values
   */
  private Map<String, Object> parameterToMap(NodeList paramList, Enum<?> simulationType) {
    Map<String, Object> parameters = new HashMap<>();

    for (int i = 0; i < paramList.getLength(); i++) {
      Element paramElement = (Element) paramList.item(i);
      String paramName = paramElement.getAttribute("name");
      String paramValue = paramElement.getAttribute("value");

      // Special handling for Game of Life's "rulestring" parameter
      if (simulationType == SimType.GameOfLife && paramName.equalsIgnoreCase("rulestring")) {
        // Split the rulestring based on '/'
        String[] ruleParts = paramValue.split("/");

        if (ruleParts.length == 2) {
          // Process the first part (e.g., "B1")
          if (ruleParts[0].startsWith("B")) {
            String birthRules = ruleParts[0].substring(1); // Extract "1"
            parameters.put("B", Double.parseDouble(birthRules));
          }
          // Process the second part (e.g., "S12345")
          if (ruleParts[1].startsWith("S")) {
            String survivalRules = ruleParts[1].substring(1); // Extract "12345"

            // Convert each character to an integer and collect them into a list
            List<Integer> survivalList = new ArrayList<>();
            for (char c : survivalRules.toCharArray()) {
              survivalList.add(Character.getNumericValue(c)); // Convert char to int
            }

            // Put the list of survival rules into the map with the key "S"
            parameters.put("S", survivalList);
          }
        } else {
          throw new XmlException("RulestringFormat");
        }
      } else {
        // Default behavior for other parameters
        parameters.put(paramName, Double.parseDouble(paramValue));
      }
    }

    return parameters;
  }

  /**
   * Converts color definitions from an XML node list into a mapping of cell states to colors.
   *
   * @param colorList - a NodeList containing the color definitions
   * @return a map of cell states to their corresponding colors
   */
  public static Map<Integer, String> colorsToMap(NodeList colorList) {
    Map<Integer, String> colors = new HashMap<>();

    for (int i = 0; i < colorList.getLength(); i++) {
      Element colorElement = (Element) colorList.item(i);
      String colorName = colorElement.getAttribute("cellType");
      String colorValue = colorElement.getAttribute("value");

      if (colorName.isEmpty() || colorValue.isEmpty()) {
        continue;
      }
      try {
        colors.put(Integer.parseInt(colorName), colorValue);
      }
      catch (IllegalArgumentException e) {
        throw new XmlException("InvalidState", colorName);
      }
    }

    return colors;
  }

  /**
   * Generates random cell states based on the grid size and variation information in the XML file.
   *
   * @return a list of random cell states for the grid
   * @throws IllegalArgumentException if there are issues with the grid's variation data
   */
  private List<Integer> setCellStatesByVariation(Node variationNode, int totalCells, SimType simType) {
    ArrayList<Integer> cellList = new ArrayList<>();

    // Get the variation type from the node
    String variationType = ((Element) variationNode).getAttribute("type");

    Map<Integer, Integer> cellStateMap = new HashMap<>(); //first int is cell state, second is number of cells of that state

    // Get all cell elements within the variation
    NodeList cellNodes = ((Element) variationNode).getElementsByTagName("cell");

    switch (variationType) {
      case "explicit": {
        // Iterate through each cell element
        for (int i = 0; i < cellNodes.getLength(); i++) {
          Element cellElement = (Element) cellNodes.item(i);
          String cellType = cellElement.getAttribute("cellType");
          int cellCount = Integer.parseInt(cellElement.getTextContent()); // Get the cell count from the text content

          logger.debug("Found " + cellType + " as the cell type after explicit");

          // Add the cell type and count to the cellStateMap
          cellStateMap.put(1, cellCount);
        }
        break;
      } case "ratio": {
        for (int i = 0; i < cellNodes.getLength(); i++) {
          Element cellElement = (Element) cellNodes.item(i);
          String cellType = cellElement.getAttribute("cellType");
          float cellRatio = Float.parseFloat(cellElement.getTextContent()); // Get the cell count from the text content

          int cellCount = (int) (cellRatio * totalCells);

          logger.debug("Found " + cellType + " as the cell type after ratio");

          // Add the cell type and count to the cellStateMap
          cellStateMap.put(1, cellCount);
        }

        break;
      }
      default:
        throw new IllegalArgumentException("Unsupported variation type: " + variationType);
    }

    // Calculate the total number of explicitly stated cells
    int totalExplicitCells = cellStateMap.values().stream().mapToInt(Integer::intValue).sum();

    // Calculate the remaining cells that are not explicitly stated
    int remainingCells = totalCells - totalExplicitCells;


    List<Integer> remainingStates = new ArrayList<>(); //adding the remaining cell states!
    Set<Integer> explicitlyProvidedStates = cellStateMap.keySet();
    for (int i = 0; i < maxFromSimType(simType); i++) {
      if (!explicitlyProvidedStates.contains(i)){
        remainingStates.add(i);
      }
    }

    // Distribute the remaining cells among the missing states
    int cellsPerState = remainingCells / remainingStates.size();
    int remainder = remainingCells % remainingStates.size();

    for (int i = 0; i < remainingStates.size(); i++) {
      int state = remainingStates.get(i);
      int cellCount = cellsPerState + (i < remainder ? 1 : 0); // Distribute remainder
      cellStateMap.put(state, cellCount);
    }

    System.out.println(cellStateMap);

    // Fill the cellList with randomly chosen cell types
    for (int i = 0; i < totalCells; i++) {
      // Convert the map keys (cell types) to a list for random selection
      List<Integer> cellTypes = new ArrayList<>(cellStateMap.keySet());

      // Randomly select a cell type from the list
      Random random = new Random();
      int chosenCellType = cellTypes.get(random.nextInt(cellTypes.size()));

      cellList.add(chosenCellType);

      // Decrement the count for the chosen cell type in the map
      int updatedCount = cellStateMap.get(chosenCellType) - 1;
      cellStateMap.put(chosenCellType, updatedCount);

      // If the count for the chosen cell type reaches zero, remove it from the map
      if (updatedCount == 0) {
        cellStateMap.remove(chosenCellType);
      }
    }

    return cellList;
  }

  SimType simTypeFromString(String simTypeString) {
    return switch (simTypeString.toLowerCase()) { //switch case to determine enum type
      case "game of life", "gameoflife" -> SimType.GameOfLife;
      case "spreading of fire", "fire" -> SimType.Fire;
      case "percolation" -> SimType.Percolation;
      case "model of segregation", "segregation" -> SimType.Segregation;
      case "wa-tor world", "wator" -> SimType.WaTor;
      case "falling sand", "fallingsand" -> SimType.FallingSand;
      case "langton", "langton's loop", "langtonsloop" -> SimType.Langton;
      case "chou-reggia loop", "choureggialoop", "choureg", "choureg2", "chou" -> SimType.ChouReg2;
      case "petelka" -> SimType.Petelka;
      case "rock paper scissors", "rps" -> SimType.RockPaperSciss;
      default -> throw new XmlException("UnknownSimType", simTypeString);
    };
  }

  int maxFromSimType(SimType simType) {
    // Initialize the HashMap for mapping SimType to max states
    Map<SimType, Integer> maxConnector = new HashMap<>();

    // Populate the map with max states for each simulation type
    maxConnector.put(SimType.GameOfLife, CellStates.GAMEOFLIFE_MAXSTATE);
    maxConnector.put(SimType.Fire, CellStates.FIRE_MAXSTATE);
    maxConnector.put(SimType.Percolation, CellStates.PERCOLATION_MAXSTATE);
    maxConnector.put(SimType.Segregation, CellStates.SEGREGATION_MAXSTATE);
    maxConnector.put(SimType.WaTor, CellStates.WATOR_MAXSTATE);
    maxConnector.put(SimType.FallingSand, CellStates.FALLINGSAND_MAXSTATE);
    maxConnector.put(SimType.Langton, CellStates.LANGTON_MAXSTATE);
    maxConnector.put(SimType.ChouReg2, CellStates.CHOUREG2_MAXSTATE);
    maxConnector.put(SimType.Petelka, CellStates.PETELKA_MAXSTATE);
    maxConnector.put(SimType.RockPaperSciss, CellStates.FIRE_MAXSTATE); // assuming it's the same as Fire, adjust if necessary

    // Return the max state for the given SimType
    return maxConnector.getOrDefault(simType, -1); // If no matching type found, return -1 (or another default value)
  }
}
