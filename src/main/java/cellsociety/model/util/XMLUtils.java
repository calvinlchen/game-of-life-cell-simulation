package cellsociety.model.util;

import cellsociety.model.factories.statefactory.CellStateFactory;
import cellsociety.model.factories.statefactory.handler.CellStateHandler;
import cellsociety.model.factories.statefactory.handler.CellStateHandlerStatic;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.exceptions.XMLException;
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
import java.util.*;

/**
 * A class that interacts with xml files, either by reading them or writing to them.
 *
 * @author Kyaira Boughton
 */
public class XMLUtils {

  /**
   * A method that reads a pre-existing xml file.
   *
   * @param fXmlFile a File object containing the XML
   * @return an XMLData object with all information found from the provided xml file
   */
  public XMLData readXML(File fXmlFile) {
    XMLData xmlObject = new XMLData();

    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);
      doc.getDocumentElement().normalize();

      NodeList simulationList = doc.getElementsByTagName("simulation");
      if (simulationList.getLength() == 0) {
        throw new IllegalArgumentException("No <simulation> tag found in the XML file.");
      }

      for (int i = 0; i < simulationList.getLength();
          i++) { //just in case there is somehow more than one simulation?
        Node simulationNode = simulationList.item(i);
        if (simulationNode.getNodeType() == Node.ELEMENT_NODE) {
          Element simulationElement = (Element) simulationNode;

          //extract metadata
          String SimulationType = simulationElement.getElementsByTagName("type").item(0)
              .getTextContent();

          switch (SimulationType.toLowerCase()) { //swtich case to determine enum type

            case "game of life":
            case "gameoflife":
              xmlObject.setType(SimType.GameOfLife);
              break;
            case "spreading of fire":
            case "fire":
              xmlObject.setType(SimType.Fire);
              break;
            case "percolation":
              xmlObject.setType(SimType.Percolation);
              break;
            case "model of segregation":
            case "segregation":
              xmlObject.setType(SimType.Segregation);
              break;
            case "wa-tor world":
            case "wator":
              xmlObject.setType(SimType.WaTor);
              break;
            default:
              throw new IllegalArgumentException("Unknown simulation type: " + SimulationType);

          }

          xmlObject.setTitle(
              simulationElement.getElementsByTagName("title").item(0).getTextContent());
          xmlObject.setAuthor(
              simulationElement.getElementsByTagName("author").item(0).getTextContent());
          xmlObject.setDescription(
              simulationElement.getElementsByTagName("description").item(0).getTextContent());

          //extract grid info
          Element gridElement = (Element) simulationElement.getElementsByTagName("grid").item(0);
          int rows = Integer.parseInt(gridElement.getAttribute("rows"));
          int columns = Integer.parseInt(gridElement.getAttribute("columns"));

          xmlObject.setGridRowNum(rows);
          xmlObject.setGridColNum(columns);

          //extract parameter info
          NodeList paramList;
          Element parametersElement = (Element) simulationElement.getElementsByTagName("parameters")
              .item(0);

          //parameters are nested under <parameters> vs nested directly under <grid>
          paramList = Objects.requireNonNullElse(parametersElement, gridElement)
              .getElementsByTagName("parameter");
          xmlObject.setParameters(parameterToMap(paramList, xmlObject.getType()));

          //extract cell info
          NodeList cellList = gridElement.getElementsByTagName("cell");
          if (cellList.getLength() != rows * columns) {
            throw new XMLException(
                "Error: Expected " + (rows * columns) + " <cell> elements, but found "
                    + cellList.getLength());
          }
          xmlObject.setCellStateList(cellStatesToEnum(cellList, xmlObject.getType(), xmlObject.getId(), xmlObject.getNumStates()));

        }
      }
    } catch (Exception e) {
      throw new XMLException(e.getMessage());
    }

    return xmlObject;
  }

  /**
   * up     * A method that writes a simulation data to both pre-existing and non-existing xml
   * files.
   *
   * @param file        a File object where the XML should be stored
   * @param title       a String variable of the title of the simulation
   * @param author      a String variable of the author of the simulation
   * @param description a String variable of the description of the simulation
   * @param simulation  a simulation object that holds the current state of all cells.
   */
  public void writeToXML(File file, String title, String author, String description,
      Simulation simulation) {
    if (file == null) {
      throw new XMLException("No file selected for saving.");
    }

    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      //create root element
      Element rootElement = doc.createElement("simulation");
      doc.appendChild(rootElement);

      //add metadata
      Element typeElement = doc.createElement("type");
      typeElement.appendChild(doc.createTextNode(simulation.getXMLData().getType().toString()));
      rootElement.appendChild(typeElement);

      Element titleElement = doc.createElement("title");
      titleElement.appendChild(doc.createTextNode(title));
      rootElement.appendChild(titleElement);

      Element authorElement = doc.createElement("author");
      authorElement.appendChild(doc.createTextNode(author));
      rootElement.appendChild(authorElement);

      Element descriptionElement = doc.createElement("description");
      descriptionElement.appendChild(doc.createTextNode(description));
      rootElement.appendChild(descriptionElement);

      //add grid info
      Element gridElement = doc.createElement("grid");
      gridElement.setAttribute("rows", String.valueOf(simulation.getXMLData().getGridRowNum()));
      gridElement.setAttribute("columns", String.valueOf(simulation.getXMLData().getGridColNum()));
      rootElement.appendChild(gridElement);

      //add cell states
      ArrayList<String> cellStateList = cellStatesToString(simulation.getXMLData().getGridRowNum(),
          simulation.getXMLData().getGridColNum(), simulation);
      for (String state : cellStateList) {
        Element cellElement = doc.createElement("cell");
        cellElement.setAttribute("state", state);
        gridElement.appendChild(cellElement);
      }

      //add parameters
      Map<String, Double> parameters = simulation.getXMLData().getParameters();
      for (Map.Entry<String, Double> entry : parameters.entrySet()) {
        Element paramElement = doc.createElement("parameter");
        paramElement.setAttribute("name", entry.getKey());
        paramElement.setAttribute("value", String.valueOf(entry.getValue()));
        gridElement.appendChild(paramElement);
      }

      // Write the content into the selected XML file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(file);
      transformer.transform(source, result);

    } catch (Exception e) {
      throw new XMLException("Error saving XML file: " + e.getMessage());
    }

  }

  /**
   * A method that extracts the cellstates from the simulation data and turns them into a string
   * list
   *
   * @param rowNum     an int variable of the number of rows in the grid
   * @param colNum     an int variable of the number of rows in the grid@
   * @param simulation a simulation object that holds the current state of all cells.
   */
  private ArrayList<String> cellStatesToString(int rowNum, int colNum, Simulation simulation) {

    ArrayList<String> cellStateList = new ArrayList<>();

    // TODO: if it dynamic the last thing should be number of states to make it with
    CellStateHandler handler = CellStateFactory.getHandler(simulation.getSimulationID(), simulation.getSimulationType(),
        simulation.getNumStates());
    // TODO: make better exceptions
    if (handler == null) {
      throw new IllegalArgumentException(
          "Unknown simulation type: " + simulation.getSimulationType());
    }

    for (int i = 0; i < rowNum; i++) {
      for (int j = 0; j < colNum; j++) {
        int currentCellState = simulation.getCurrentState(i, j);

        if (!handler.isValidState(currentCellState)) {
          throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
        }

        cellStateList.add(handler.statetoString(currentCellState));
      }
    }

    return cellStateList;
  }

  /**
   * A method that extracts the cellstates from the simulation xml and turns them into a string
   * list
   *
   * @param cellList       an NodeList variable that holds the xml data's cell data
   * @param simulationType an Enum representing the intended simulation type
   */
  private ArrayList<Integer> cellStatesToEnum(NodeList cellList, SimType simulationType, int id, int numStates) {

    ArrayList<Integer> cellStateEnums = new ArrayList<>();

    CellStateHandler handler = CellStateFactory.getHandler(id, simulationType, numStates);
    if (handler == null) {
      throw new IllegalArgumentException("Unknown simulation type: " + simulationType);
    }

    for (int j = 0; j < cellList.getLength(); j++) {
      Element cellElement = (Element) cellList.item(j);
      String currentCellState = cellElement.getAttribute("state");

      try {
        int stateEnum = handler.stateFromString(currentCellState);
        cellStateEnums.add(stateEnum);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Unknown cell state: " + currentCellState, e);
      }
    }

    return cellStateEnums;

  }

  /**
   * A method that extracts the parameters from the simulation xml and turns them into a string
   * list
   *
   * @param paramList      an NodeList variable that holds the xml data's parameter data
   * @param simulationType an Enum representing the intended simulation type
   */
  private Map<String, Double> parameterToMap(NodeList paramList, Enum simulationType) {
    Map<String, Double> parameters = new HashMap<>();

    for (int i = 0; i < paramList.getLength(); i++) {
      Element paramElement = (Element) paramList.item(i);
      String paramName = paramElement.getAttribute("name");
      String paramValue = paramElement.getAttribute("value");

      // Special handling for Game of Life's "rulestring" parameter
      if (simulationType == SimType.GameOfLife && paramName.toLowerCase().equals("rulestring")) {
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
            parameters.put("S", Double.parseDouble(survivalRules));
          }
        } else {
          throw new IllegalArgumentException("Invalid rulestring format. Expected format: Bx/Sy");
        }
      } else {
        // Default behavior for other parameters
        parameters.put(paramName, Double.parseDouble(paramValue));
      }
    }

    return parameters;
  }
}
