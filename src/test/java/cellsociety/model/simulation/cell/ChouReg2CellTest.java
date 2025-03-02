package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static cellsociety.model.util.constants.CellStates.CHOUREG2_MAXSTATE;

import cellsociety.model.simulation.rules.ChouReg2Rule;
import cellsociety.model.util.constants.exceptions.SimulationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.mockito.Mockito;

/**
 * Test class for ChouReg2Cell
 */
class ChouReg2CellTest {
  private ChouReg2Rule rule;
  private ChouReg2Parameters mockParameters;

  @BeforeEach
  void setUp() {
    rule = mock(ChouReg2Rule.class);
    mockParameters = Mockito.mock(ChouReg2Parameters.class);
    when(rule.getParameters()).thenReturn(mockParameters);
    when(mockParameters.getParameter("maxHistorySize")).thenReturn(3.);
  }

  @Test
  @DisplayName("Cell initializes with correct state")
  void cellInitializesWithCorrectState() {
    ChouReg2Cell cell = new ChouReg2Cell(1, rule);
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("Cell correctly calculates next state using mocked rule")
  void cellCorrectlyCalculatesNextState() {
    ChouReg2Cell cell = new ChouReg2Cell(1, rule);
    List<ChouReg2Cell> neighbors = List.of(
        new ChouReg2Cell(1, rule),
        new ChouReg2Cell(2, rule),
        new ChouReg2Cell(2, rule),
        new ChouReg2Cell(0, rule)
    );
    cell.setNeighbors(neighbors);
    when(rule.apply(cell)).thenReturn(3);
    cell.calcNextState();
    assertEquals(3, cell.getNextState());
  }

  @Test
  @DisplayName("Cell updates to next state on step")
  void cellUpdatesToNextStateOnStep() {
    ChouReg2Cell cell = new ChouReg2Cell(1, rule);
    cell.setNextState(0);
    cell.step();
    assertEquals(0, cell.getCurrentState());
  }

  @Test
  @DisplayName("Cell enforces valid state constraints")
  void cellEnforcesValidStateConstraints() {
    assertThrows(SimulationException.class, () -> new ChouReg2Cell(CHOUREG2_MAXSTATE + 1, rule));
  }

  @Test
  @DisplayName("Cell enforces valid state constraints on setting next state")
  void calcNextState_cellEnforcesValidStateConstraints() {
    ChouReg2Cell cell = new ChouReg2Cell(1, rule);
    List<ChouReg2Cell> neighbors = List.of(
        new ChouReg2Cell(1, rule),
        new ChouReg2Cell(2, rule),
        new ChouReg2Cell(2, rule),
        new ChouReg2Cell(0, rule)
    );
    cell.setNeighbors(neighbors);
    when(rule.apply(cell)).thenReturn(CHOUREG2_MAXSTATE + 1);
    assertThrows(SimulationException.class, () -> cell.calcNextState());
  }

}
