package cellsociety.model.simulation;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.*;
import cellsociety.model.util.XMLData;
import cellsociety.model.util.constants.CellStates.*;
import cellsociety.model.util.SimulationTypes.SimType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimulationTest {

  private Simulation<GameOfLifeStates, GameOfLifeCell> gameOfLifeSimulation;
  private Simulation<SegregationStates, SegregationCell> segregationSimulation;
  private Simulation<FireStates, FireCell> fireSimulation;
  private Simulation<PercolationStates, PercolationCell> percolationSimulation;
  private Simulation<WaTorStates, WaTorCell> watorSimulation;
  private XMLData xmlData;

  @BeforeEach
  void setUp() {
    xmlData = new XMLData();
    xmlData.setGridRowNum(3);
    xmlData.setGridColNum(3);
    xmlData.setParameters(Map.of());

    xmlData.setType(SimType.GAMEOFLIFE);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        GameOfLifeStates.ALIVE, GameOfLifeStates.DEAD, GameOfLifeStates.ALIVE,
        GameOfLifeStates.DEAD, GameOfLifeStates.ALIVE, GameOfLifeStates.DEAD,
        GameOfLifeStates.ALIVE, GameOfLifeStates.DEAD, GameOfLifeStates.ALIVE)));
    gameOfLifeSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.SEGREGATION);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        SegregationStates.EMPTY, SegregationStates.AGENT_A, SegregationStates.EMPTY,
        SegregationStates.AGENT_B, SegregationStates.AGENT_B, SegregationStates.EMPTY,
        SegregationStates.AGENT_A, SegregationStates.AGENT_A, SegregationStates.EMPTY)));
    segregationSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.FIRE);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        FireStates.BURNING, FireStates.TREE, FireStates.EMPTY,
        FireStates.TREE, FireStates.BURNING, FireStates.EMPTY,
        FireStates.TREE, FireStates.TREE, FireStates.EMPTY)));
    fireSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.PERCOLATION);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        PercolationStates.BLOCKED, PercolationStates.OPEN, PercolationStates.PERCOLATED,
        PercolationStates.OPEN, PercolationStates.BLOCKED, PercolationStates.PERCOLATED,
        PercolationStates.OPEN, PercolationStates.OPEN, PercolationStates.PERCOLATED)));
    percolationSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.WATOR);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        WaTorStates.FISH, WaTorStates.SHARK, WaTorStates.EMPTY,
        WaTorStates.SHARK, WaTorStates.FISH, WaTorStates.EMPTY,
        WaTorStates.FISH, WaTorStates.SHARK, WaTorStates.EMPTY)));
    watorSimulation = new Simulation<>(xmlData);
  }

  @Test
  void testInitialization() {
    assertEquals(GameOfLifeStates.ALIVE, gameOfLifeSimulation.getCurrentState(0, 0));
    assertEquals(SegregationStates.EMPTY, segregationSimulation.getCurrentState(0, 0));
    assertEquals(FireStates.BURNING, fireSimulation.getCurrentState(0, 0));
    assertEquals(PercolationStates.BLOCKED, percolationSimulation.getCurrentState(0, 0));
    assertEquals(WaTorStates.FISH, watorSimulation.getCurrentState(0, 0));
  }

  // TODO: update once we fix what the parameters should be
  @Test
  void testStep() {
    gameOfLifeSimulation.step();
    segregationSimulation.step();
    fireSimulation.step();
    percolationSimulation.step();
    watorSimulation.step();
    assertNotNull(gameOfLifeSimulation.getCurrentState(0, 0));
    assertNotNull(segregationSimulation.getCurrentState(0, 0));
    assertNotNull(fireSimulation.getCurrentState(0, 0));
    assertNotNull(percolationSimulation.getCurrentState(0, 0));
    assertNotNull(watorSimulation.getCurrentState(0, 0));
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
