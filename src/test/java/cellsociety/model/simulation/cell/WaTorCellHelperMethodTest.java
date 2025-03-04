package cellsociety.model.simulation.cell;

import static cellsociety.model.util.SimulationTypes.SimType.WaTor;

import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.simulation.rules.WaTorRule;
import cellsociety.model.util.constants.exceptions.SimulationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


// wator cell is the main one with helper methods, the rest are just changes to templates that can
// be tested with the simulation as a whole
public class WaTorCellHelperMethodTest {

  private GenericParameters parameters;
  private WaTorRule rule;
  private WaTorCell cell;

  @BeforeEach
  void setUp() {
    parameters = new GenericParameters(WaTor);
    rule = new WaTorRule(parameters);
    cell = new WaTorCell(0, rule);
  }

  @Test
  @DisplayName("SetNextState sets the next state of the cell to the given cell and sets up the next steps and energy")
  void setNextState_PassedInValidStates_CorrectlyUpdatesNextStateAndValues() {
    cell.setNextState(1, 100, 10);

    assertEquals(1, cell.getNextState());
    assertEquals(100, cell.getNextStepsSurvived());
    assertEquals(10, cell.getNextEnergy());

    assertEquals(0, cell.getStepsSurvived());
    assertEquals(0, cell.getEnergy());
  }

  @Test
  @DisplayName("SetNextState throws simulation exception if state is invalid")
  void setNextState_InvalidState_ThrowsException() {
    assertThrows(SimulationException.class, () -> cell.setNextState(-1, 0, 0));
  }

  // the other stuff is just kinda how the simulation works so will test it there

}
