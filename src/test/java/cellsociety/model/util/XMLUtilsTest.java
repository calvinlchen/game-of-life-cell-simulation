package cellsociety.model.util;

import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.exceptions.XMLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class XMLUtilsTest {

    private XMLUtils xmlUtils;
    private static final String TEST_FILE_NAME = "testSimulation";
    private static final String TEST_FILE_PATH = TEST_FILE_NAME + ".xml";
    private static final String INVALID_FILE_PATH = "invalidSimulation.xml";

    @BeforeEach
    void setUp() {
        xmlUtils = new XMLUtils();
    }

    @Test
    void testReadXMLMissingMetadata() {
        createXMLFileMissingMetadata();
        File file = new File(INVALID_FILE_PATH);
        assertThrows(XMLException.class, () -> xmlUtils.readXML(file));
        file.delete();
    }

    @Test
    void testReadXMLMissingGridAttributes() {
        createXMLFileMissingGridAttributes();
        File file = new File(INVALID_FILE_PATH);
        assertThrows(XMLException.class, () -> xmlUtils.readXML(file));
        file.delete();
    }

    @Test
    void testReadXMLMissingParameters() {
        createXMLFileWithoutParameters();
        File file = new File(TEST_FILE_PATH);
        XMLData xmlData = xmlUtils.readXML(file);

        assertEquals(SimType.Fire, xmlData.getType());
        assertEquals("Fire Sim", xmlData.getTitle());
        assertEquals("Vincent Price", xmlData.getAuthor());
        assertEquals("This simulation is fire", xmlData.getDescription());
        assertEquals(2, xmlData.getGridRowNum());
        assertEquals(2, xmlData.getGridColNum());

        // Parameters should be empty but valid
        Map<String, Double> parameters = xmlData.getParameters();
        assertNotNull(parameters);
        assertEquals(0, parameters.size());

        file.delete();
    }

    @Test
    void testReadEmptyXML() {
        createEmptyXMLFile();
        File file = new File(INVALID_FILE_PATH);
        assertThrows(XMLException.class, () -> xmlUtils.readXML(file));
        file.delete();
    }

    @Test
    void testReadMalformedXML() {
        createMalformedXMLFile();
        File file = new File(INVALID_FILE_PATH);
        assertThrows(XMLException.class, () -> xmlUtils.readXML(file));
        file.delete();
    }

    @Test
    void testReadXMLWithExtraElements() {
        createXMLWithExtraElements();
        File file = new File(TEST_FILE_PATH);
        XMLData xmlData = xmlUtils.readXML(file);

        assertEquals(SimType.Fire, xmlData.getType());
        assertEquals("Fire Sim", xmlData.getTitle());

        file.delete();
    }

    @Test
    void testPrivateMethod_parseCellState() throws Exception {
        Method method = XMLUtils.class.getDeclaredMethod("parseCellState", String.class);
        method.setAccessible(true);

        int result1 = (int) method.invoke(xmlUtils, "burning");
        int result2 = (int) method.invoke(xmlUtils, "tree");
        int result3 = (int) method.invoke(xmlUtils, "empty");

        assertEquals(2, result1);
        assertEquals(1, result2);
        assertEquals(0, result3);

        assertThrows(Exception.class, () -> method.invoke(xmlUtils, "invalidState"));
    }

    private void createXMLFileMissingMetadata() {
        String xmlContent = """
                <simulation>
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
        writeFile(INVALID_FILE_PATH, xmlContent);
    }

    private void createXMLFileMissingGridAttributes() {
        String xmlContent = """
                <simulation>
                    <metadata>
                        <type>Spreading of Fire</type>
                        <title>Fire Sim</title>
                        <author>Vincent Price</author>
                        <description>This simulation is fire</description>
                    </metadata>
                    <grid>
                        <cell row="0" col="0" state="burning"/>
                        <cell row="0" col="1" state="tree"/>
                    </grid>
                    <parameters>
                        <parameter name="ignitionLikelihood" value="0.5"/>
                    </parameters>
                </simulation>
                """;
        writeFile(INVALID_FILE_PATH, xmlContent);
    }

    private void createXMLFileWithoutParameters() {
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
                </simulation>
                """;
        writeFile(TEST_FILE_PATH, xmlContent);
    }

    private void createEmptyXMLFile() {
        writeFile(INVALID_FILE_PATH, "");
    }

    private void createMalformedXMLFile() {
        String xmlContent = """
                <simulation>
                    <metadata>
                        <type>Spreading of Fire
                        <title>Fire Sim</title>
                """; // Missing closing tags
        writeFile(INVALID_FILE_PATH, xmlContent);
    }

    private void createXMLWithExtraElements() {
        String xmlContent = """
                <simulation>
                    <metadata>
                        <type>Spreading of Fire</type>
                        <title>Fire Sim</title>
                        <author>Vincent Price</author>
                        <description>This simulation is fire</description>
                    </metadata>
                    <extra>
                        <info>Unexpected Data</info>
                    </extra>
                    <grid rows="2" columns="2">
                        <cell row="0" col="0" state="burning"/>
                        <cell row="0" col="1" state="tree"/>
                        <cell row="1" col="0" state="empty"/>
                        <cell row="1" col="1" state="tree"/>
                    </grid>
                </simulation>
                """;
        writeFile(TEST_FILE_PATH, xmlContent);
    }

    private void writeFile(String path, String content) {
        try {
            File file = new File(path);
            file.createNewFile();
            java.nio.file.Files.write(file.toPath(), content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
