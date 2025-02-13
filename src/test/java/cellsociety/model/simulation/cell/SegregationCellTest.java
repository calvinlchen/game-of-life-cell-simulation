package cellsociety.model.simulation.cell;


import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.rules.SegregationRule;
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
    rule = new SegregationRule(Map.of("toleranceThreshold", 0.5));
  }

  @Test
  @DisplayName("Cell initializes with correct state")
  void cellInitializesWithCorrectState() {
    SegregationCell cell = new SegregationCell(1, rule);
    assertEquals(1, cell.getCurrentState());
  }

  // actual test for correctness is really on the rules, this is just to check if the update is good
  @Test
  @DisplayName("Cell correctly calculates next state")
  void cellCorrectlyCalculatesNextState() {
    SegregationCell cell = new SegregationCell(1, rule);
    List<SegregationCell> neighbors = List.of(
        new SegregationCell(1, rule),
        new SegregationCell(2, rule),
        new SegregationCell(2, rule),
        new SegregationCell(0, rule)
    );
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    assertEquals(1, cell.getCurrentState());
    assertEquals(0, cell.getNextState());
  }

  @Test
  @DisplayName("Cell updates to next state on step")
  void cellUpdatesToNextStateOnStep() {
    SegregationCell cell = new SegregationCell(1, rule);
    cell.setNextState(0);
    cell.step();
    assertEquals(0, cell.getCurrentState());
  }
}
