package cellsociety.model.util;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.exceptions.XMLException;
import java.util.ResourceBundle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A class that interacts with xml files, either by reading them or writing to them.
 *
 * @author Kyaira Boughton
 */
public class XMLUtils {
    private ResourceBundle myResources;
    public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.constants.CellStates";

    public XMLUtils() {
        myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE);
    }

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

            for (int i = 0; i < simulationList.getLength(); i++) { //just in case there is somehow more than one simulation?
                Node simulationNode = simulationList.item(i);
                if (simulationNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element simulationElement = (Element) simulationNode;

                    //extract metadata
                    String SimulationType = simulationElement.getElementsByTagName("type").item(0).getTextContent();

                    switch (SimulationType){ //swtich case to determine enum type

                        case "Game of Life":
                        case "GAMEOFLIFE":
                            xmlObject.setType(SimType.GAMEOFLIFE);
                            break;
                        case "Spreading of Fire":
                        case "FIRE":
                            xmlObject.setType(SimType.FIRE);
                            break;
                        case "Percolation":
                        case "PERCOLATION":
                            xmlObject.setType(SimType.PERCOLATION);
                            break;
                        case "Model of Segregation":
                        case "SEGREGATION":
                            xmlObject.setType(SimType.SEGREGATION);
                            break;
                        case "Wa-Tor World":
                        case "WATOR":
                            xmlObject.setType(SimType.WATOR);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown simulation type: " + SimulationType);

                    }

                    xmlObject.setTitle(simulationElement.getElementsByTagName("title").item(0).getTextContent());
                    xmlObject.setAuthor(simulationElement.getElementsByTagName("author").item(0).getTextContent());
                    xmlObject.setDescription(simulationElement.getElementsByTagName("description").item(0).getTextContent());

                    //extract grid info
                    Element gridElement = (Element) simulationElement.getElementsByTagName("grid").item(0);
                    int rows = Integer.parseInt(gridElement.getAttribute("rows"));
                    int columns = Integer.parseInt(gridElement.getAttribute("columns"));

                    xmlObject.setGridRowNum(rows);
                    xmlObject.setGridColNum(columns);

                    //extract cell info
                    NodeList cellList = gridElement.getElementsByTagName("cell");
                    if (cellList.getLength() != rows * columns) {
                        throw new XMLException("Error: Expected " + (rows * columns) + " <cell> elements, but found " + cellList.getLength());
                    }
                    xmlObject.setCellStateList(cellStatesToEnum(cellList, xmlObject.getType()));

                    //extract parameter info
                    NodeList paramList;
                    Element parametersElement = (Element) simulationElement.getElementsByTagName("parameters").item(0);

                    //parameters are nested under <parameters> vs nested directly under <grid>
                    paramList = Objects.requireNonNullElse(parametersElement, gridElement).getElementsByTagName("parameter");
                    xmlObject.setParameters(parameterToMap(paramList, xmlObject.getType()));

                }
            }
        } catch (Exception e) {
            throw new XMLException(e.getMessage());
        }

        return xmlObject;
    }

    /**
up     * A method that writes a simulation data to both pre-existing and non-existing xml files.
     *
     * @param file a File object where the XML should be stored
     * @param title a String variable of the title of the simulation
     * @param author a String variable of the author of the simulation
     * @param description a String variable of the description of the simulation
     * @param simulation a simulation object that holds the current state of all cells.
     */
    public void writeToXML(File file, String title, String author, String description, Simulation simulation) {
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
            ArrayList<String> cellStateList = cellStatesToString(simulation.getXMLData().getGridRowNum(), simulation.getXMLData().getGridColNum(), simulation);
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
     * A method that extracts the cellstates from the simulation data and turns them into a string list
     *
     * @param rowNum an int variable of the number of rows in the grid
     * @param colNum an int variable of the number of rows in the grid@
     * @param simulation a simulation object that holds the current state of all cells.
     */
    private ArrayList<String> cellStatesToString (int rowNum, int colNum, Simulation simulation){

        ArrayList<String> cellStateList = new ArrayList<>();

        for (int i=0; i<rowNum; i++){ //for loop to build array list of cellstates
            for (int j=0; j<colNum; j++){

                int currentCellState = simulation.getCurrentState(i, j); //returns the current state of a specific cell

                switch (simulation.getSimulationType()) {

                    case SimType.GAMEOFLIFE -> {
                        switch (currentCellState){
                            case 1 -> cellStateList.add("alive");
                            case 0 -> cellStateList.add("dead");
                            default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
                        }
                    }

                    case SimType.PERCOLATION -> {
                        switch (currentCellState){
                            case 0 -> cellStateList.add("blocked");
                            case 1 -> cellStateList.add("open");
                            case 2-> cellStateList.add("percolated");
                            default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
                        }
                    }

                    case SimType.FIRE -> {
                        switch (currentCellState){
                            case 1 -> cellStateList.add("tree");
                            case 0 -> cellStateList.add("empty");
                            case 2 -> cellStateList.add("burning");
                            default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
                        }
                    }

                    case SimType.SEGREGATION -> {
                        switch (currentCellState){
                            case 0 -> cellStateList.add("empty");
                            case 1 -> cellStateList.add("agentA");
                            case 2 -> cellStateList.add("agentB");
                            default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
                        }
                    }

                    case SimType.WATOR -> {
                        switch (currentCellState) {
                            case 0 -> cellStateList.add("empty");
                            case 1 -> cellStateList.add("fish");
                            case 2 -> cellStateList.add("shark");
                            default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
                        }
                    }

                    default -> throw new IllegalArgumentException("Unknown simulation type: " + simulation.getSimulationType());

                }
            }
        }

        return cellStateList;
    }

    /**
     * A method that extracts the cellstates from the simulation xml and turns them into a string list
     *
     * @param cellList an NodeList variable that holds the xml data's cell data
     * @param SimulationType an Enum representing the intended simulation type
     */
    private ArrayList<Integer> cellStatesToEnum (NodeList cellList, Enum SimulationType){

        ArrayList<Integer> cellStateEnums = new ArrayList<>();

        for (int j = 0; j < cellList.getLength(); j++) {
            Element cellElement = (Element) cellList.item(j);

            String currentCellState = cellElement.getAttribute("state");

            switch (SimulationType) {

                case SimType.GAMEOFLIFE -> {
                    switch (currentCellState){
                        case ("alive") -> cellStateEnums.add(getStateProperty("GAMEOFLIFE_ALIVE"));
                        case ("dead") -> cellStateEnums.add(getStateProperty("GAMEOFLIFE_DEAD"));
                        default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
                    }
                }

                case SimType.PERCOLATION -> {
                    switch (currentCellState){
                        case ("blocked") -> cellStateEnums.add(getStateProperty("PERCOLATION_BLOCKED"));
                        case ("open") -> cellStateEnums.add(getStateProperty("PERCOLATION_OPEN"));
                        case ("percolated") -> cellStateEnums.add(getStateProperty("PERCOLATION_PERCOLATED"));
                        default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
                    }
                }

                case SimType.FIRE -> {
                    switch (currentCellState){
                        case ("tree") -> cellStateEnums.add(getStateProperty("FIRE_TREE"));
                        case ("empty") -> cellStateEnums.add(getStateProperty("FIRE_EMPTY"));
                        case ("burning") -> cellStateEnums.add(getStateProperty("FIRE_BURNING"));
                        default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
                    }
                }

                case SimType.SEGREGATION -> {
                    switch (currentCellState){
                        case ("agentA") -> cellStateEnums.add(getStateProperty("SEGREGATION_A"));
                        case ("empty") -> cellStateEnums.add(getStateProperty("SEGREGATION_EMPTY"));
                        case ("agentB") -> cellStateEnums.add(getStateProperty("SEGREGATION_B"));
                        default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
                    }
                }

                case SimType.WATOR -> {
                    switch (currentCellState) {
                        case ("fish") -> cellStateEnums.add(getStateProperty("WATOR_FISH"));
                        case ("empty") -> cellStateEnums.add(getStateProperty("WATOR_EMPTY"));
                        case ("shark") -> cellStateEnums.add(getStateProperty("WATOR_SHARK"));
                        default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);
                    }
                }

                default -> throw new IllegalArgumentException("Unknown simulation type: " + SimulationType);

            }
        }

        return cellStateEnums;

    }

    /**
     * A method that extracts the parameters from the simulation xml and turns them into a string list
     *
     * @param paramList an NodeList variable that holds the xml data's parameter data
     * @param SimulationType an Enum representing the intended simulation type
     */
    private Map<String, Double> parameterToMap (NodeList paramList, Enum SimulationType) {

        Map<String, Double> parameters = new HashMap<>();

        for (int i = 0; i < paramList.getLength(); i++) {
            Element paramElement = (Element) paramList.item(i);
            String paramName = paramElement.getAttribute("name");
            double paramValue = Double.parseDouble(paramElement.getAttribute("value"));
            parameters.put(paramName, paramValue);
        }

        return parameters;

    }

    /**
     * Returns the int associated with the state from the resource property
     *
     * TODO: refactor this to be a single utils files
     *
     * @param key - the String key associated with the state
     * @return the int associated with the property's key
     */
    private int getStateProperty(String key) {
        return Integer.parseInt(myResources.getString(key));
    }
}
