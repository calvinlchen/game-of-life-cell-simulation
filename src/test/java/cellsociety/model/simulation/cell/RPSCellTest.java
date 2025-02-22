package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.parameters.RPSParameters;
import cellsociety.model.simulation.rules.RPSRule;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tester for RPSCell
 */
class RPSCellTest {
  private RPSRule rule;

  @BeforeEach
  void setUp() {
    rule = new RPSRule(new RPSParameters());
  }

  @Test
  @DisplayName("RPSCell correctly initializes state")
  void cell_Initializes_Correctly() {
    RPSCell cell = new RPSCell(0, rule);
    assertEquals(0, cell.getCurrentState());
  }

  @Test
  @DisplayName("RPSCell correctly updates state based on rule")
  void cell_Updates_State_Correctly() {
    RPSCell cell = new RPSCell(1, rule);
    List<RPSCell> neighbors = createNeighbors(2, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertNotEquals(1, cell.getCurrentState()); // State should change based on rule
  }

  @Test
  @DisplayName("RPSCell remains the same if no winning neighbor")
  void cell_Remains_Same_Without_Stronger_Neighbor() {
    RPSCell cell = new RPSCell(1, rule);
    List<RPSCell> neighbors = createNeighbors(1, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("RPSCell correctly transitions to a stronger neighbor's state")
  void cell_Transitions_To_Stronger_Neighbor() {
    RPSCell cell = new RPSCell(0, rule);
    List<RPSCell> neighbors = createNeighbors(1, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(1, cell.getCurrentState());
  }

  private List<RPSCell> createNeighbors(int state, RPSRule rule) {
    List<RPSCell> neighbors = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      neighbors.add(new RPSCell(state, rule));
    }
    return neighbors;
  }
}