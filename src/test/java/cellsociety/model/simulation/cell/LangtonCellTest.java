package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static cellsociety.model.util.constants.CellStates.LANGTON_MAXSTATE;

import cellsociety.model.simulation.parameters.LangtonParameters;
import cellsociety.model.simulation.rules.LangtonRule;
import cellsociety.model.util.constants.exceptions.SimulationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.mockito.Mockito;

/**
 * Test class for LangtonCell
 */
class LangtonCellTest {
  private LangtonRule rule;
  private LangtonParameters mockParameters;

  @BeforeEach
  void setUp() {
    rule = mock(LangtonRule.class);
    mockParameters = Mockito.mock(LangtonParameters.class);
    when(rule.getParameters()).thenReturn(mockParameters);
    when(mockParameters.getParameter("maxHistorySize")).thenReturn(3.);
  }

  @Test
  @DisplayName("Cell initializes with correct state")
  void cellInitializesWithCorrectState() {
    LangtonCell cell = new LangtonCell(1, rule);
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("Cell correctly calculates next state using mocked rule")
  void cellCorrectlyCalculatesNextState() {
    LangtonCell cell = new LangtonCell(1, rule);
    List<LangtonCell> neighbors = List.of(
        new LangtonCell(1, rule),
        new LangtonCell(2, rule),
        new LangtonCell(2, rule),
        new LangtonCell(0, rule)
    );
    cell.setNeighbors(neighbors);
    when(rule.apply(cell)).thenReturn(3);
    cell.calcNextState();
    assertEquals(3, cell.getNextState());
  }

  @Test
  @DisplayName("Cell updates to next state on step")
  void cellUpdatesToNextStateOnStep() {
    LangtonCell cell = new LangtonCell(1, rule);
    cell.setNextState(0);
    cell.step();
    assertEquals(0, cell.getCurrentState());
  }

  @Test
  @DisplayName("Cell enforces valid state constraints")
  void cellEnforcesValidStateConstraints() {
    assertThrows(SimulationException.class, () -> new LangtonCell(LANGTON_MAXSTATE + 1, rule));
  }

  @Test
  @DisplayName("Cell enforces valid state constraints on setting next state")
  void calcNextState_cellEnforcesValidStateConstraints() {
    LangtonCell cell = new LangtonCell(1, rule);
    List<LangtonCell> neighbors = List.of(
        new LangtonCell(1, rule),
        new LangtonCell(2, rule),
        new LangtonCell(2, rule),
        new LangtonCell(0, rule)
    );
    cell.setNeighbors(neighbors);
    when(rule.apply(cell)).thenReturn(LANGTON_MAXSTATE + 1);
    assertThrows(SimulationException.class, () -> cell.calcNextState());
  }
}
