package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static cellsociety.model.util.constants.CellStates.PETELKA_MAXSTATE;

import cellsociety.model.simulation.parameters.PetelkaParameters;
import cellsociety.model.simulation.rules.PetelkaRule;
import cellsociety.model.util.constants.exceptions.SimulationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.mockito.Mockito;

/**
 * Test class for PetelkaCell
 */
class PetelkaCellTest {
  private PetelkaRule rule;
  private PetelkaParameters mockParameters;

  @BeforeEach
  void setUp() {
    rule = mock(PetelkaRule.class);
    mockParameters = Mockito.mock(PetelkaParameters.class);
    when(rule.getParameters()).thenReturn(mockParameters);
    when(mockParameters.getParameter("maxHistorySize")).thenReturn(3.);
  }

  @Test
  @DisplayName("Cell initializes with correct state")
  void cellInitializesWithCorrectState() {
    PetelkaCell cell = new PetelkaCell(1, rule);
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("Cell correctly calculates next state using mocked rule")
  void cellCorrectlyCalculatesNextState() {
    PetelkaCell cell = new PetelkaCell(1, rule);
    List<PetelkaCell> neighbors = List.of(
        new PetelkaCell(1, rule),
        new PetelkaCell(2, rule),
        new PetelkaCell(2, rule),
        new PetelkaCell(0, rule)
    );
    cell.setNeighbors(neighbors);
    when(rule.apply(cell)).thenReturn(3);
    cell.calcNextState();
    assertEquals(3, cell.getNextState());
  }

  @Test
  @DisplayName("Cell updates to next state on step")
  void cellUpdatesToNextStateOnStep() {
    PetelkaCell cell = new PetelkaCell(1, rule);
    cell.setNextState(0);
    cell.step();
    assertEquals(0, cell.getCurrentState());
  }

  @Test
  @DisplayName("Cell enforces valid state constraints")
  void cellEnforcesValidStateConstraints() {
    assertThrows(SimulationException.class, () -> new PetelkaCell(PETELKA_MAXSTATE + 1, rule));
  }

  @Test
  @DisplayName("Cell enforces valid state constraints on setting next state")
  void calcNextState_cellEnforcesValidStateConstraints() {
    PetelkaCell cell = new PetelkaCell(1, rule);
    List<PetelkaCell> neighbors = List.of(
        new PetelkaCell(1, rule),
        new PetelkaCell(2, rule),
        new PetelkaCell(2, rule),
        new PetelkaCell(0, rule)
    );
    cell.setNeighbors(neighbors);
    when(rule.apply(cell)).thenReturn(PETELKA_MAXSTATE + 1);
    assertThrows(SimulationException.class, () -> cell.calcNextState());
  }
}