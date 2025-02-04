package cellsociety.model.util;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.CellStates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class XMLUtilsTest {

    private XMLUtils xmlUtils;
    private static final String TEST_FILE_NAME = "testSimulation";
    private static final String TEST_FILE_PATH = TEST_FILE_NAME + ".xml";
    private static final String INVALID_FILE_NAME = "invalidSimulation";
    private static final String INVALID_FILE_PATH = INVALID_FILE_NAME + ".xml";

    @BeforeEach
    void setUp() {
        xmlUtils = new XMLUtils();
    }

    @Test
    void testReadXML() {
        //create test XML file
        createTestXMLFile();

        //read XML file
        XMLData xmlData = xmlUtils.readXML(TEST_FILE_NAME);

        //verify metadata
        assertEquals(SimType.FIRE, xmlData.getType());
        assertEquals("Fire Sim", xmlData.getTitle());
        assertEquals("Vincent Price", xmlData.getAuthor());
        assertEquals("This simulation is fire", xmlData.getDescription());

        //verify grid info
        assertEquals(2, xmlData.getGridRowNum());
        assertEquals(2, xmlData.getGridColNum());

        //verify cell states
        ArrayList<Enum> cellStates = xmlData.getCellStateList();
        assertEquals(4, cellStates.size());
        assertEquals(CellStates.FireStates.BURNING, cellStates.get(0));
        assertEquals(CellStates.FireStates.TREE, cellStates.get(1));
        assertEquals(CellStates.FireStates.EMPTY, cellStates.get(2));
        assertEquals(CellStates.FireStates.TREE, cellStates.get(3));

        //verify parameters
        Map<String, Double> parameters = xmlData.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(0.5, parameters.get("ignitionLikelihood"));

        //clean up test file
        new File(TEST_FILE_PATH).delete();
    }

    @Test
    void testWriteToXML() {
        //create simulation object with test data
        Simulation<?, ?> simulation = createTestSimulation();

        //write simulation data to an XML file
        xmlUtils.writeToXML(TEST_FILE_NAME, "Fire Sim", "Vincent Price", "This simulation is fire", simulation);

        //read XML file back to verify contents
        XMLData xmlData = xmlUtils.readXML(TEST_FILE_NAME);

        //verify metadata
        assertEquals(SimType.FIRE, xmlData.getType());
        assertEquals("Fire Sim", xmlData.getTitle());
        assertEquals("Vincent Price", xmlData.getAuthor());
        assertEquals("This simulation is fire", xmlData.getDescription());

        //verify grid info
        assertEquals(2, xmlData.getGridRowNum());
        assertEquals(2, xmlData.getGridColNum());

        //verify cell states
        ArrayList<Enum> cellStates = xmlData.getCellStateList();
        assertEquals(4, cellStates.size());
        assertEquals(CellStates.FireStates.BURNING, cellStates.get(0));
        assertEquals(CellStates.FireStates.TREE, cellStates.get(1));
        assertEquals(CellStates.FireStates.EMPTY, cellStates.get(2));
        assertEquals(CellStates.FireStates.TREE, cellStates.get(3));

        //verify parameters
        Map<String, Double> parameters = xmlData.getParameters();
        assertEquals(1, parameters.size());
        assertEquals(0.5, parameters.get("ignitionLikelihood"));

        //clean up test file
        new File(TEST_FILE_PATH).delete();
    }

    @Test
    void testReadInvalidXML() {
        //create invalid XML file
        createInvalidXMLFile();

        //verify reading invalid XML file throws exception
        Exception exception = assertThrows(RuntimeException.class, () -> xmlUtils.readXML(INVALID_FILE_NAME));
        assertTrue(exception.getMessage().contains("Error parsing XML file"));

        //clean up invalid file
        new File(INVALID_FILE_PATH).delete();
    }

    @Test
    void testReadInvalidSimulationType() {
        //create XML file with invalid simulation type
        createInvalidSimulationTypeXMLFile();

        //verify reading XML file with invalid simulation type throws exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> xmlUtils.readXML(INVALID_FILE_NAME));
        assertTrue(exception.getMessage().contains("Unknown simulation type"));

        //clean up invalid file
        new File(INVALID_FILE_PATH).delete();
    }

    @Test
    void testReadInvalidCellState() {
        //create XML file with invalid cell state
        createInvalidCellStateXMLFile();

        //verify reading XML file with invalid cell state throws exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> xmlUtils.readXML(INVALID_FILE_NAME));
        assertTrue(exception.getMessage().contains("Unknown cell state"));

        //clean up invalid file
        new File(INVALID_FILE_PATH).delete();
    }

    private void createTestXMLFile() {
        String xmlContent = """
                <simulation>
                    <metadata>
                        <type>Spreading of Fire</type>
                        <title>Fire Sim</title>
                        <author>Vincent Price</author>
                        <description>This simulation is fire</description>
                    </metadata>
                    <grid rows="2" columns="2">
                        <cell row="0" col="0" state="burning"/>
                        <cell row="0" col="1" state="tree"/>
                        <cell row="1" col="0" state="empty"/>
                        <cell row="1" col="1" state="tree"/>
                    </grid>
                    <parameters>
                        <parameter name="ignitionLikelihood" value="0.5"/>
                    </parameters>
                </simulation>
                """;

        try {
            File file = new File(TEST_FILE_PATH);
            file.createNewFile();
            java.nio.file.Files.write(file.toPath(), xmlContent.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createInvalidXMLFile() {
        String invalidXmlContent = """
                <simulation>
                    <metadata>
                        <type>Spreading of Fire</type>
                        <title>Fire Sim</title>
                        <author>Vincent Price</author>
                        <description>This simulation is fire</description>
                    </metadata>
                    <grid rows="2" columns="2">
                        <cell row="0" col="0" state="burning"/>
                        <cell row="0" col="1" state="tree"/>
                        <cell row="1" col="0" state="empty"/>
                        <cell row="1" col="1" state="tree"/>
                    </grid>
                    <parameters>
                        <parameter name="ignitionLikelihood" value="0.5"/>
                """; // Missing closing tags

        try {
            File file = new File(INVALID_FILE_PATH);
            file.createNewFile();
            java.nio.file.Files.write(file.toPath(), invalidXmlContent.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createInvalidSimulationTypeXMLFile() {
        String invalidSimulationTypeXmlContent = """
                <simulation>
                    <metadata>
                        <type>Invalid Simulation Type</type>
                        <title>Fire Sim</title>
                        <author>Vincent Price</author>
                        <description>This simulation is fire</description>
                    </metadata>
                    <grid rows="2" columns="2">
                        <cell row="0" col="0" state="burning"/>
                        <cell row="0" col="1" state="tree"/>
                        <cell row="1" col="0" state="empty"/>
                        <cell row="1" col="1" state="tree"/>
                    </grid>
                    <parameters>
                        <parameter name="ignitionLikelihood" value="0.5"/>
                    </parameters>
                </simulation>
                """;

        try {
            File file = new File(INVALID_FILE_PATH);
            file.createNewFile();
            java.nio.file.Files.write(file.toPath(), invalidSimulationTypeXmlContent.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createInvalidCellStateXMLFile() {
        String invalidCellStateXmlContent = """
                <simulation>
                    <metadata>
                        <type>Spreading of Fire</type>
                        <title>Fire Sim</title>
                        <author>Vincent Price</author>
                        <description>This simulation is fire</description>
                    </metadata>
                    <grid rows="2" columns="2">
                        <cell row="0" col="0" state="burning"/>
                        <cell row="0" col="1" state="tree"/>
                        <cell row="1" col="0" state="invalidState"/>
                        <cell row="1" col="1" state="tree"/>
                    </grid>
                    <parameters>
                        <parameter name="ignitionLikelihood" value="0.5"/>
                    </parameters>
                </simulation>
                """;

        try {
            File file = new File(INVALID_FILE_PATH);
            file.createNewFile();
            java.nio.file.Files.write(file.toPath(), invalidCellStateXmlContent.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Simulation<?, ?> createTestSimulation() {
        //create XMLData with test data
        XMLData xmlData = new XMLData();
        xmlData.setType(SimType.FIRE);
        xmlData.setTitle("Fire Sim");
        xmlData.setAuthor("Vincent Price");
        xmlData.setDescription("This simulation is fire");
        xmlData.setGridRowNum(2);
        xmlData.setGridColNum(2);

        //set cell states
        ArrayList<Enum> cellStates = new ArrayList<>();
        cellStates.add(CellStates.FireStates.BURNING);
        cellStates.add(CellStates.FireStates.TREE);
        cellStates.add(CellStates.FireStates.EMPTY);
        cellStates.add(CellStates.FireStates.TREE);
        xmlData.setCellStateList(cellStates);

        //set parameters
        Map<String, Double> parameters = new HashMap<>();
        parameters.put("ignitionLikelihood", 0.5);
        xmlData.setParameters(parameters);

        //create mock Simulation object
        return new Simulation(xmlData) {
            @Override
            public Enum<?> getCurrentState(int row, int col) {
                //mock cell states for 2x2 grid
                if (row == 0 && col == 0) return CellStates.FireStates.BURNING;
                if (row == 0 && col == 1) return CellStates.FireStates.TREE;
                if (row == 1 && col == 0) return CellStates.FireStates.EMPTY;
                if (row == 1 && col == 1) return CellStates.FireStates.TREE;
                throw new IllegalArgumentException("Invalid row or column");
            }
        };
    }
}