package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;
import static cellsociety.model.util.constants.CellStates.*;

import cellsociety.model.simulation.rules.FallingSandRule;
import cellsociety.model.util.constants.exceptions.SimulationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for FallingSandCell
 */
class FallingSandCellTest {

  private FallingSandRule rule;
  private FallingSandCell cell;

  @BeforeEach
  void setUp() {
    rule = new FallingSandRule(new FallingSandParameters());
    cell = new FallingSandCell(FALLINGSAND_SAND, rule);
  }

  @Test
  @DisplayName("Cell initializes with correct state")
  void testCellInitialization() {
    assertEquals(FALLINGSAND_SAND, cell.getCurrentState());
  }

  @Test
  @DisplayName("Set current state within valid range")
  void testSetCurrentState_Valid() {
    cell.setCurrentState(FALLINGSAND_WATER);
    assertEquals(FALLINGSAND_WATER, cell.getCurrentState());
  }

  @Test
  @DisplayName("Set current state outside valid range throws exception")
  void testSetCurrentState_Invalid() {
    assertThrows(SimulationException.class, () -> cell.setCurrentState(FALLINGSAND_MAXSTATE + 1));
  }

  @Test
  @DisplayName("Set next state within valid range")
  void testSetNextState_Valid() {
    cell.setNextState(FALLINGSAND_EMPTY);
    assertEquals(FALLINGSAND_EMPTY, cell.getNextState());
  }

  @Test
  @DisplayName("Set next state outside valid range throws exception")
  void testSetNextState_Invalid() {
    assertThrows(SimulationException.class, () -> cell.setNextState(FALLINGSAND_MAXSTATE + 1));
  }

  @Test
  @DisplayName("Cell calculates next state correctly")
  void testCalcNextState() {
    cell.calcNextState();
    assertNotNull(cell.getNextState());
  }

  @Test
  @DisplayName("Cell doesn't calculate next state if its current state and next state don't equal")
  void calcNextState_misMatchStates_noCalculate() {
    cell.setNextState(FALLINGSAND_WATER);
    cell.calcNextState();
    assertEquals(FALLINGSAND_WATER, cell.getNextState());
  }

  @Test
  @DisplayName("Cell maintains its instance")
  void testGetSelf() {
    assertEquals(cell, cell.getSelf());
  }
}
