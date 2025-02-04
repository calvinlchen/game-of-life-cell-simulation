package cellsociety.model.util;

import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.CellStates;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

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

            for (int i = 0; i < simulationList.getLength(); i++) {
                Node simulationNode = simulationList.item(i);
                if (simulationNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element simulationElement = (Element) simulationNode;

                    //extract metadata
                    String SimulationType = simulationElement.getElementsByTagName("type").item(0).getTextContent();

                    switch (SimulationType){ //swtich case to determine enum type

                        case "Game of Life" -> xmlObject.setType(SimType.GAMEOFLIFE);
                        case "Spreading of Fire" -> xmlObject.setType(SimType.FIRE);
                        case "Percolation" -> xmlObject.setType(SimType.PERCOLATION);
                        case "Model of Segregation" -> xmlObject.setType(SimType.SEGREGATION);
                        case "Wa-Tor World" -> xmlObject.setType(SimType.WATOR);

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
                    for (int j = 0; j < cellList.getLength(); j++) {
                        Element cellElement = (Element) cellList.item(j);

                        String currentCellState = cellElement.getAttribute("state");

                        switch (xmlObject.getType()){

                            case GAMEOFLIFE -> {
                                switch (currentCellState){
                                    case ("alive") -> xmlObject.getCellStateList().add(CellStates.GameOfLifeStates.ALIVE);
                                    case ("dead") -> xmlObject.getCellStateList().add(CellStates.GameOfLifeStates.DEAD);
                                }
                            }

                            case PERCOLATION -> {
                                switch (currentCellState){
                                    case ("blocked") -> xmlObject.getCellStateList().add(CellStates.PercolationStates.BLOCKED);
                                    case ("open") -> xmlObject.getCellStateList().add(CellStates.PercolationStates.OPEN);
                                    case ("percolated") -> xmlObject.getCellStateList().add(CellStates.PercolationStates.PERCOLATED);
                                }
                            }

                            case FIRE -> {
                                switch (currentCellState){
                                    case ("tree") -> xmlObject.getCellStateList().add(CellStates.FireStates.TREE);
                                    case ("empty") -> xmlObject.getCellStateList().add(CellStates.FireStates.EMPTY);
                                    case ("burning") -> xmlObject.getCellStateList().add(CellStates.FireStates.BURNING);
                                }
                            }

                            case SEGREGATION -> {
                                switch (currentCellState){
                                    case ("agentA") -> xmlObject.getCellStateList().add(CellStates.SegregationStates.AGENT_A);
                                    case ("empty") -> xmlObject.getCellStateList().add(CellStates.SegregationStates.EMPTY);
                                    case ("agentB") -> xmlObject.getCellStateList().add(CellStates.SegregationStates.AGENT_B);
                                }
                            }

                            case WATOR -> {
                                switch (currentCellState){
                                    case ("fish") -> xmlObject.getCellStateList().add(CellStates.WaTorStates.FISH);
                                    case ("empty") -> xmlObject.getCellStateList().add(CellStates.WaTorStates.EMPTY);
                                    case ("agentB") -> xmlObject.getCellStateList().add(CellStates.WaTorStates.SHARK);
                                }
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return xmlObject;
    }

    public static void main(String argv[]) {

        XMLUtils xmlUtils = new XMLUtils();
        XMLData data = xmlUtils.readXML("GameOfLife1");

        System.out.println(data.getDescription());

    }

}
