package cellsociety.model.simulation;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.*;
import cellsociety.model.util.XMLData;
import cellsociety.model.util.SimulationTypes.SimType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationTest {

  private Simulation<GameOfLifeCell> gameOfLifeSimulation;
  private Simulation<SegregationCell> segregationSimulation;
  private Simulation<FireCell> fireSimulation;
  private Simulation<PercolationCell> percolationSimulation;
  private Simulation<WaTorCell> watorSimulation;
  private XMLData xmlData;

  @BeforeEach
  void setUp() {
    xmlData = new XMLData();
    xmlData.setGridRowNum(3);
    xmlData.setGridColNum(3);
    xmlData.setParameters(Map.of());

    xmlData.setType(SimType.GAMEOFLIFE);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        1, 0, 1,
        0, 1, 0,
        1, 0, 1)));
    gameOfLifeSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.SEGREGATION);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        0, 1, 2,
        2, 2, 2,
        2, 2, 2)));
    segregationSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.FIRE);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        2, 1, 0,
        1, 2, 0,
        1, 1, 0)));
    fireSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.PERCOLATION);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        1, 2, 2,
        0, 0, 2,
        1, 1, 2)));
    percolationSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.WATOR);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        1, 2, 0,
        0, 0, 0,
        0, 0, 0)));
    watorSimulation = new Simulation<>(xmlData);
  }

  @Test
  void testInitialization() {
    assertEquals(1, gameOfLifeSimulation.getCurrentState(0, 0));
    assertEquals(0, segregationSimulation.getCurrentState(0, 0));
    assertEquals(2, fireSimulation.getCurrentState(0, 0));
    assertEquals(1, percolationSimulation.getCurrentState(0, 0));
    assertEquals(1, watorSimulation.getCurrentState(0, 0));
  }

  @Test
  void testStep() {
    gameOfLifeSimulation.step();
    segregationSimulation.step();
    fireSimulation.step();
    percolationSimulation.step();
    watorSimulation.step();
    assertEquals(0, gameOfLifeSimulation.getCurrentState(0, 0));
    assertEquals(1, segregationSimulation.getCurrentState(0, 0));
    assertEquals(0, fireSimulation.getCurrentState(0, 0));
    assertEquals(2, percolationSimulation.getCurrentState(0, 0));
    assertEquals(2, watorSimulation.getCurrentState(0, 0));
  }

  @Test
  void testStepBack() {
    gameOfLifeSimulation.step();
    segregationSimulation.step();
    fireSimulation.step();
    percolationSimulation.step();
    watorSimulation.step();
    assertEquals(0, gameOfLifeSimulation.getCurrentState(0, 0));
    assertEquals(1, segregationSimulation.getCurrentState(0, 0));
    assertEquals(0, fireSimulation.getCurrentState(0, 0));
    assertEquals(2, percolationSimulation.getCurrentState(0, 0));
    assertEquals(2, watorSimulation.getCurrentState(0, 0));
    gameOfLifeSimulation.stepBack();
    segregationSimulation.stepBack();
    fireSimulation.stepBack();
    percolationSimulation.stepBack();
    watorSimulation.stepBack();
    assertEquals(1, gameOfLifeSimulation.getCurrentState(0, 0));
    assertEquals(0, segregationSimulation.getCurrentState(0, 0));
    assertEquals(2, fireSimulation.getCurrentState(0, 0));
    assertEquals(1, percolationSimulation.getCurrentState(0, 0));
    assertEquals(1, watorSimulation.getCurrentState(0, 0));
  }

  @Test
  void testGetXMLData() {
    assertEquals(xmlData, gameOfLifeSimulation.getXMLData());
    assertEquals(xmlData, segregationSimulation.getXMLData());
    assertEquals(xmlData, fireSimulation.getXMLData());
    assertEquals(xmlData, percolationSimulation.getXMLData());
    assertEquals(xmlData, watorSimulation.getXMLData());
  }

  @Test
  void testInvalidCellAccess() {
    assertThrows(IllegalArgumentException.class, () -> gameOfLifeSimulation.getCurrentState(5, 5));
    assertThrows(IllegalArgumentException.class, () -> segregationSimulation.getCurrentState(5, 5));
    assertThrows(IllegalArgumentException.class, () -> fireSimulation.getCurrentState(5, 5));
    assertThrows(IllegalArgumentException.class, () -> percolationSimulation.getCurrentState(5, 5));
    assertThrows(IllegalArgumentException.class, () -> watorSimulation.getCurrentState(5, 5));
  }
}
