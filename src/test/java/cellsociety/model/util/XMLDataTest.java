package cellsociety.model.util;

import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.CellStates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XMLDataTest {

    private XMLData xmlData;

    @BeforeEach
    void setUp() {
        xmlData = new XMLData();
    }

    @Test
    void testGetAndSetType() {
        SimType expectedType = SimType.GAMEOFLIFE;
        xmlData.setType(expectedType);
        assertEquals(expectedType, xmlData.getType(), "Type should be set and retrieved correctly.");
    }

    @Test
    void testGetAndSetTitle() {
        String expectedTitle = "Game of Life Simulation";
        xmlData.setTitle(expectedTitle);
        assertEquals(expectedTitle, xmlData.getTitle(), "Title should be set and retrieved correctly.");
    }

    @Test
    void testGetAndSetAuthor() {
        String expectedAuthor = "John Doe";
        xmlData.setAuthor(expectedAuthor);
        assertEquals(expectedAuthor, xmlData.getAuthor(), "Author should be set and retrieved correctly.");
    }

    @Test
    void testGetAndSetDescription() {
        String expectedDescription = "A simulation of Conway's Game of Life.";
        xmlData.setDescription(expectedDescription);
        assertEquals(expectedDescription, xmlData.getDescription(), "Description should be set and retrieved correctly.");
    }

    @Test
    void testGetAndSetGridRowNum() {
        int expectedRowNum = 10;
        xmlData.setGridRowNum(expectedRowNum);
        assertEquals(expectedRowNum, xmlData.getGridRowNum(), "Grid row number should be set and retrieved correctly.");
    }

    @Test
    void testGetAndSetGridColNum() {
        int expectedColNum = 10;
        xmlData.setGridColNum(expectedColNum);
        assertEquals(expectedColNum, xmlData.getGridColNum(), "Grid column number should be set and retrieved correctly.");
    }

    @Test
    void testGetAndSetCellStateList() {
        ArrayList<Integer> expectedCellStateList = new ArrayList<>();
        expectedCellStateList.add(1);
        expectedCellStateList.add(1);
        expectedCellStateList.add(0);
        xmlData.setCellStateList(expectedCellStateList);
        assertEquals(expectedCellStateList, xmlData.getCellStateList(), "Cell state list should be set and retrieved correctly.");
    }

    @Test
    void testGetAndSetParameters() {
        Map<String, Double> expectedParameters = new HashMap<>();
        expectedParameters.put("probability", 0.5);
        expectedParameters.put("threshold", 0.75);
        xmlData.setParameters(expectedParameters);
        assertEquals(expectedParameters, xmlData.getParameters(), "Parameters should be set and retrieved correctly.");
    }
}