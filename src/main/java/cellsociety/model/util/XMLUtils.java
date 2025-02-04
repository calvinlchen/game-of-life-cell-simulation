package cellsociety.model.util;

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
        try {
            File fXmlFile = new File(FILE_PATH_PREFIX + fileName + ".xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList simulationList = doc.getElementsByTagName("simulation");
            System.out.println("----------------------------");

            for (int i = 0; i < simulationList.getLength(); i++) {
                Node simulationNode = simulationList.item(i);
                if (simulationNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element simulationElement = (Element) simulationNode;

                    // Extract metadata
                    System.out.println("Simulation Type: "
                            + simulationElement.getElementsByTagName("type").item(0).getTextContent());
                    System.out.println("Simulation Title: "
                            + simulationElement.getElementsByTagName("title").item(0).getTextContent());
                    System.out.println("Author Name: "
                            + simulationElement.getElementsByTagName("author").item(0).getTextContent());
                    System.out.println("Simulation Description: "
                            + simulationElement.getElementsByTagName("description").item(0).getTextContent());

                    // Extract grid information
                    Element gridElement = (Element) simulationElement.getElementsByTagName("grid").item(0);
                    int rows = Integer.parseInt(gridElement.getAttribute("rows"));
                    int columns = Integer.parseInt(gridElement.getAttribute("columns"));
                    System.out.println("Grid: " + rows + "x" + columns);

                    // Extract cell information
                    NodeList cellList = gridElement.getElementsByTagName("cell");
                    StringBuilder cellStates = new StringBuilder("Cell: ");
                    for (int j = 0; j < cellList.getLength(); j++) {
                        Element cellElement = (Element) cellList.item(j);
                        cellStates.append(cellElement.getAttribute("state"));
                        if (j < cellList.getLength() - 1) {
                            cellStates.append(", ");
                        }
                    }
                    System.out.println(cellStates.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new XMLData();
    }



    public static void main(String argv[]) {

        XMLUtils xmlUtils = new XMLUtils();
        xmlUtils.readXML("GameOfLife1");

    }

}

