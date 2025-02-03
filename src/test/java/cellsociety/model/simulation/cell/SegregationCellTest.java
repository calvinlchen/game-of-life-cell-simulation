package cellsociety.model.simulation.cell;


import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.rules.SegregationRule;
import cellsociety.model.util.constants.CellStates.SegregationStates;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for SegregationCell
 */
class SegregationCellTest {
  private SegregationRule rule;

  @BeforeEach
  void setUp() {
    rule = new SegregationRule(Map.of("satisfactionThreshold", 0.5));
  }

  @Test
  @DisplayName("Cell initializes with correct state")
  void cellInitializesWithCorrectState() {
    SegregationCell cell = new SegregationCell(SegregationStates.AGENT_A, rule);
    assertEquals(SegregationStates.AGENT_A, cell.getCurrentState());
  }

  // actual test for correctness is really on the rules, this is just to check if the update is good
  @Test
  @DisplayName("Cell correctly calculates next state")
  void cellCorrectlyCalculatesNextState() {
    SegregationCell cell = new SegregationCell(SegregationStates.AGENT_A, rule);
    List<SegregationCell> neighbors = List.of(
        new SegregationCell(SegregationStates.AGENT_A, rule),
        new SegregationCell(SegregationStates.AGENT_B, rule),
        new SegregationCell(SegregationStates.AGENT_B, rule),
        new SegregationCell(SegregationStates.EMPTY, rule)
    );
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    assertEquals(SegregationStates.AGENT_A, cell.getCurrentState());
    assertEquals(SegregationStates.EMPTY, cell.getNextState());
  }

  @Test
  @DisplayName("Cell updates to next state on step")
  void cellUpdatesToNextStateOnStep() {
    SegregationCell cell = new SegregationCell(SegregationStates.AGENT_A, rule);
    cell.setNextState(SegregationStates.EMPTY);
    cell.step();
    assertEquals(SegregationStates.EMPTY, cell.getCurrentState());
  }
}
