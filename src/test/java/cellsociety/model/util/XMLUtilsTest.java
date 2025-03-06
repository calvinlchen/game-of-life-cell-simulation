package cellsociety.model.util;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.CellStates;
import cellsociety.model.util.exceptions.XmlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class XmlUtilsTest {

    private XmlUtils xmlUtils;

    @Mock
    private Simulation<?> mockSimulation;

    @Mock
    private XmlData mockXmlData;  // Mock the XmlData class

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        xmlUtils = new XmlUtils(); // Initialize the XmlUtils instance before each test
    }

    @Test
    void testReadXml_validXmlFile() {
        File validXmlFile = new File("valid_simulation.xml"); // A file with valid simulation data

        // Mock the behavior of xmlUtils.readXml() to return mockXmlData when a valid file is passed
        when(xmlUtils.readXml(validXmlFile)).thenReturn(mockXmlData);

        // Assuming that mockXmlData is set up to return valid data
        when(mockXmlData.getType()).thenReturn(SimType.GameOfLife);
        when(mockXmlData.getParameters()).thenReturn(Map.of()); // Mock parameters to be a non-null value

        assertDoesNotThrow(() -> {
            XmlData xmlData = xmlUtils.readXml(validXmlFile);
            assertNotNull(xmlData);
            assertEquals("GameOfLife", xmlData.getType().toString());  // Assuming the file contains a GameOfLife simulation
            assertNotNull(xmlData.getParameters());  // Check if parameters are not null
        });
    }

    @Test
    void testReadXml_invalidXmlFile() {
        File invalidXmlFile = new File("invalid_simulation.xml"); // A file with invalid simulation data

        // Mock the behavior of xmlUtils.readXml() to throw XmlException when an invalid file is passed
        when(xmlUtils.readXml(invalidXmlFile)).thenThrow(XmlException.class);

        assertThrows(XmlException.class, () -> {
            xmlUtils.readXml(invalidXmlFile);
        });
    }

    @Test
    void testWriteToXml_validData() {
        File outputFile = new File("output_simulation.xml");
        Simulation<?> simulation = createMockSimulation(); // Assuming there's a method to create a mock Simulation object

        assertDoesNotThrow(() -> {
            xmlUtils.writeToXml(outputFile, "Test Title", "Test Author", "Test Description", simulation);
        });
        assertTrue(outputFile.exists()); // Check if the file has been created
    }

    @Test
    void testWriteToXml_noFile() {
        Simulation<?> simulation = createMockSimulation(); // Assuming there's a method to create a mock Simulation object

        assertThrows(XmlException.class, () -> {
            xmlUtils.writeToXml(null, "Test Title", "Test Author", "Test Description", simulation);
        });
    }

    @Test
    void testMaxFromSimType_validSimType() {
        int maxState = xmlUtils.maxFromSimType(SimType.GameOfLife);
        assertEquals(CellStates.GAMEOFLIFE_MAXSTATE, maxState);  // Assuming CellStates.GAMEOFLIFE_MAXSTATE is a predefined constant
    }

    @Test
    void testMaxFromSimType_invalidSimType() {
        int maxState = xmlUtils.maxFromSimType(SimType.RockPaperSciss); // Assuming it's valid in the CellStates
        assertEquals(CellStates.FIRE_MAXSTATE, maxState); // Assuming it's the same as FIRE_MAXSTATE
    }

    @Test
    void testSimTypeFromString_valid() {
        assertEquals(SimType.GameOfLife, xmlUtils.simTypeFromString("game of life"));
        assertEquals(SimType.Fire, xmlUtils.simTypeFromString("fire"));
    }

    @Test
    void testSimTypeFromString_invalid() {
        assertThrows(XmlException.class, () -> {
            xmlUtils.simTypeFromString("nonexistent simulation");
        });
    }

    private Simulation<?> createMockSimulation() {
        // Create a mock Simulation object
        Simulation<?> mockSimulation = mock(Simulation.class);

        // Mock the behavior of the mockSimulation's methods
        when(mockSimulation.getXmlDataObject()).thenReturn(mockXmlData);
        when(mockSimulation.getSimulationType()).thenReturn(SimType.GameOfLife);
        when(mockSimulation.getNumStates()).thenReturn(8);
        when(mockSimulation.getCurrentState(anyInt(), anyInt())).thenReturn(0); // Returns 0 for any row and column

        return mockSimulation;
    }
}
