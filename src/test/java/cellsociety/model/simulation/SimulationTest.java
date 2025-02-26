package cellsociety.model.simulation;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.*;
import cellsociety.model.util.XMLData;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimulationTest {

  private Simulation<GameOfLifeCell> gameOfLifeSimulation;
  private Simulation<SegregationCell> segregationSimulation;
  private Simulation<FireCell> fireSimulation;
  private Simulation<PercolationCell> percolationSimulation;
  private Simulation<WaTorCell> watorSimulation;
  // eventually a game of life general
  private XMLData xmlData;

  @BeforeEach
  @DisplayName("Initialize simulations with correct setup")
  void setup_initializeSimulations_correctSetup() {
    xmlData = new XMLData();
    xmlData.setGridRowNum(3);
    xmlData.setGridColNum(3);
    xmlData.setParameters(Map.of());

    xmlData.setType(SimType.GameOfLife);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        1, 0, 1,
        0, 1, 0,
        1, 0, 1)));
    gameOfLifeSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.Segregation);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        0, 1, 2,
        2, 2, 2,
        2, 2, 2)));
    segregationSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.Fire);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        2, 1, 0,
        1, 2, 0,
        1, 1, 0)));
    fireSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.Percolation);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        1, 2, 2,
        0, 0, 2,
        1, 1, 2)));
    percolationSimulation = new Simulation<>(xmlData);

    xmlData.setType(SimType.WaTor);
    xmlData.setCellStateList(new ArrayList<>(List.of(
        1, 2, 0,
        0, 0, 0,
        0, 0, 0)));
    watorSimulation = new Simulation<>(xmlData);


  }

  @Test
  @DisplayName("Retrieve correct initial states of simulations")
  void getCurrentState_initialStates_correctStateReturned() {
    assertEquals(1, gameOfLifeSimulation.getCurrentState(0, 0));
    assertEquals(0, segregationSimulation.getCurrentState(0, 0));
    assertEquals(2, fireSimulation.getCurrentState(0, 0));
    assertEquals(1, percolationSimulation.getCurrentState(0, 0));
    assertEquals(1, watorSimulation.getCurrentState(0, 0));
  }

  @Test
  @DisplayName("Step forward updates simulation state as expected")
  void step_updateSimulationState_expectedStateChange() {
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
  @DisplayName("Step back restores previous simulation state")
  void stepBack_restorePreviousState_previousStateReturned() {
    gameOfLifeSimulation.step();
    segregationSimulation.step();
    fireSimulation.step();
    percolationSimulation.step();
    watorSimulation.step();

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
  @DisplayName("Verify XML data is correctly retrieved from simulation")
  void getXMLData_verifyXmlData_correctDataReturned() {
    assertNotNull(gameOfLifeSimulation.getXmlData());
    assertNotNull(segregationSimulation.getXmlData());
    assertNotNull(fireSimulation.getXmlData());
    assertNotNull(percolationSimulation.getXmlData());
    assertNotNull(watorSimulation.getXmlData());
  }

  @Test
  @DisplayName("Attempt to access invalid coordinates throws exception")
  void getCurrentState_invalidCoordinates_exceptionThrown() {
    assertThrows(SimulationException.class, () -> gameOfLifeSimulation.getCurrentState(5, 5));
    assertThrows(SimulationException.class, () -> segregationSimulation.getCurrentState(5, 5));
    assertThrows(SimulationException.class, () -> fireSimulation.getCurrentState(5, 5));
    assertThrows(SimulationException.class, () -> percolationSimulation.getCurrentState(5, 5));
    assertThrows(SimulationException.class, () -> watorSimulation.getCurrentState(5, 5));
  }

  @Test
  @DisplayName("Update a valid parameter successfully updates the value")
  void updateParameter_validKey_parameterUpdated() {
    segregationSimulation.updateParameter("toleranceThreshold", 0.7);
    assertEquals(0.7, segregationSimulation.getParameter("toleranceThreshold"), 0.001);
  }

  @Test
  @DisplayName("Updating an null parameter key throws exception")
  void updateParameter_nullKey_exceptionThrown() {
    assertThrows(SimulationException.class, () -> segregationSimulation.updateParameter(null, 0.7));
  }

  @Test
  @DisplayName("Verify parameter keys are correctly retrieved")
  void getParameterKeys_verifyKeys_expectedKeysReturned() {
    List<String> keys = watorSimulation.getParameterKeys();
    assertTrue(keys.contains("sharkInitialEnergy"));
    assertTrue(keys.contains("fishReproductionTime"));
    assertTrue(keys.contains("sharkEnergyGain"));
    assertTrue(keys.contains("sharkReproductionTime"));
    assertFalse(keys.contains("toleranceThreshold"));
  }

  @Test
  @DisplayName("Geting a null parameter key throws exception")
  void getParameterKeys_nullKey_exceptionThrown() {
    assertThrows(SimulationException.class, () -> watorSimulation.getParameter(null));
    assertThrows(SimulationException.class, () -> watorSimulation.getParameter("toleranceThreshold"));
  }

  @Test
  @DisplayName("Catch exceptions during simulation setup")
  void simulation_exceptionHandling_correctExceptionThrown() {
    assertThrows(SimulationException.class, () -> new Simulation<>(null));
  }

  @Test
  @DisplayName("Catch exceptions when creating cells dynamically")
  void createCells_invalidSetup_exceptionThrown() {
    XMLData invalidCellData = new XMLData();
    invalidCellData.setGridRowNum(3);
    invalidCellData.setGridColNum(3);
    invalidCellData.setType(SimType.GameOfLife);
    invalidCellData.setCellStateList(new ArrayList<>(List.of(1, 0, 1)));

    assertThrows(SimulationException.class, () -> new Simulation<>(invalidCellData));
  }

}
