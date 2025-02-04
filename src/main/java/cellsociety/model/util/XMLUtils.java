package cellsociety.model.util;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.CellStates;
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

/**
 * A class that interacts with xml files, either by reading them or writing to them.
 *
 * @author Kyaira Boughton
 */
public class XMLUtils {

    private final String FILE_PATH_PREFIX = ""; //makes the assumption that all files will be in the same location.

    /**
     * A method that reads a pre-existing xml file.
     *
     * @param fileName a string variable of the file's name
     * @return an XMLData object with all information found from the provided xml file
     */
    public XMLData readXML(String fileName) {
        XMLData xmlObject = new XMLData();

        try {
            File fXmlFile = new File(FILE_PATH_PREFIX + fileName + ".xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList simulationList = doc.getElementsByTagName("simulation");

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
                    xmlObject.setCellStateList(cellStatesToEnum(cellList, xmlObject.getType()));

                    //extract parameter info
                    Element parametersElement = (Element) simulationElement.getElementsByTagName("parameters").item(0);
                    if (parametersElement != null) {
                        NodeList paramList = parametersElement.getElementsByTagName("parameter");
                        xmlObject.setParameters(parameterToMap(paramList, xmlObject.getType()));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return xmlObject;
    }

    /**
     * A method that writes a simulation data to both pre-existing and non existing xml files.
     *
     * @param fileName a string variable of the file's name
     * @param title a String variable of the title of the simulation
     * @param author a String variable of the author of the simulation
     * @param description a String variable of the description of the simulation
     * @param simulation a simulation object that holds the current state of all cells.
     */
    public void writeToXML(String fileName, String title, String author, String description, Simulation simulation) {

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

            //write the content into XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FILE_PATH_PREFIX + fileName + ".xml"));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ArrayList<String> cellStatesToString (int rowNum, int colNum, Simulation simulation){

        ArrayList<String> cellStateList = new ArrayList<>();

        for (int i=0; i<rowNum; i++){ //for loop to build array list of cellstates
            for (int j=0; j<colNum; j++){

                Enum currentCellState = simulation.getCurrentState(i, j); //returns the current state of a specific cell

                switch (currentCellState) { //turns cell state enums into strings

                    case CellStates.GameOfLifeStates.ALIVE -> cellStateList.add("alive");
                    case CellStates.GameOfLifeStates.DEAD -> cellStateList.add("dead");
                    case CellStates.PercolationStates.BLOCKED -> cellStateList.add("blocked");
                    case CellStates.PercolationStates.OPEN -> cellStateList.add("open");
                    case CellStates.PercolationStates.PERCOLATED -> cellStateList.add("percolated");
                    case CellStates.FireStates.TREE -> cellStateList.add("tree");
                    case CellStates.FireStates.EMPTY -> cellStateList.add("empty");
                    case CellStates.FireStates.BURNING -> cellStateList.add("burning");
                    case CellStates.SegregationStates.EMPTY -> cellStateList.add("empty");
                    case CellStates.SegregationStates.AGENT_A -> cellStateList.add("agentA");
                    case CellStates.SegregationStates.AGENT_B -> cellStateList.add("agentB");
                    case CellStates.WaTorStates.EMPTY -> cellStateList.add("empty");
                    case CellStates.WaTorStates.FISH-> cellStateList.add("fish");
                    case CellStates.WaTorStates.SHARK -> cellStateList.add("shark");
                    default -> throw new IllegalArgumentException("Unknown cell state: " + currentCellState);

                }
            }
        }

        return cellStateList;
    }

    private ArrayList<Enum> cellStatesToEnum (NodeList cellList, Enum SimulationType){

        ArrayList<Enum> cellStateEnums = new ArrayList<>();

        for (int j = 0; j < cellList.getLength(); j++) {
            Element cellElement = (Element) cellList.item(j);

            String currentCellState = cellElement.getAttribute("state");

            switch (SimulationType) {

                case SimType.GAMEOFLIFE -> {
                    switch (currentCellState){
                        case ("alive") -> cellStateEnums.add(CellStates.GameOfLifeStates.ALIVE);
                        case ("dead") -> cellStateEnums.add(CellStates.GameOfLifeStates.DEAD);
                    }
                }

                case SimType.PERCOLATION -> {
                    switch (currentCellState){
                        case ("blocked") -> cellStateEnums.add(CellStates.PercolationStates.BLOCKED);
                        case ("open") -> cellStateEnums.add(CellStates.PercolationStates.OPEN);
                        case ("percolated") -> cellStateEnums.add(CellStates.PercolationStates.PERCOLATED);
                    }
                }

                case SimType.FIRE -> {
                    switch (currentCellState){
                        case ("tree") -> cellStateEnums.add(CellStates.FireStates.TREE);
                        case ("empty") -> cellStateEnums.add(CellStates.FireStates.EMPTY);
                        case ("burning") -> cellStateEnums.add(CellStates.FireStates.BURNING);
                    }
                }

                case SimType.SEGREGATION -> {
                    switch (currentCellState){
                        case ("agentA") -> cellStateEnums.add(CellStates.SegregationStates.AGENT_A);
                        case ("empty") -> cellStateEnums.add(CellStates.SegregationStates.EMPTY);
                        case ("agentB") -> cellStateEnums.add(CellStates.SegregationStates.AGENT_B);
                    }
                }

                case SimType.WATOR -> {
                    switch (currentCellState){
                        case ("fish") -> cellStateEnums.add(CellStates.WaTorStates.FISH);
                        case ("empty") -> cellStateEnums.add(CellStates.WaTorStates.EMPTY);
                        case ("shark") -> cellStateEnums.add(CellStates.WaTorStates.SHARK);
                    }
                }

                default -> throw new IllegalArgumentException("Unknown simulation type: " + SimulationType);

            }
        }

        return cellStateEnums;

    }

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
}
