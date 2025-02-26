package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.parameters.RockPaperScissParameters;
import cellsociety.model.simulation.rules.RockPaperScissRule;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tester for RPSCell
 */
class RockPaperScissCellTest {
  private RockPaperScissRule rule;

  @BeforeEach
  void setUp() {
    rule = new RockPaperScissRule(new RockPaperScissParameters());
  }

  @Test
  @DisplayName("RPSCell correctly initializes state")
  void cell_Initializes_Correctly() {
    RockPaperScissCell cell = new RockPaperScissCell(0, rule);
    assertEquals(0, cell.getCurrentState());
  }

  @Test
  @DisplayName("RPSCell correctly updates state based on rule")
  void cell_Updates_State_Correctly() {
    RockPaperScissCell cell = new RockPaperScissCell(1, rule);
    List<RockPaperScissCell> neighbors = createNeighbors(2, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertNotEquals(1, cell.getCurrentState()); // State should change based on rule
  }

  @Test
  @DisplayName("RPSCell remains the same if no winning neighbor")
  void cell_Remains_Same_Without_Stronger_Neighbor() {
    RockPaperScissCell cell = new RockPaperScissCell(1, rule);
    List<RockPaperScissCell> neighbors = createNeighbors(1, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("RPSCell correctly transitions to a stronger neighbor's state")
  void cell_Transitions_To_Stronger_Neighbor() {
    RockPaperScissCell cell = new RockPaperScissCell(0, rule);
    List<RockPaperScissCell> neighbors = createNeighbors(1, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(1, cell.getCurrentState());
  }

  private List<RockPaperScissCell> createNeighbors(int state, RockPaperScissRule rule) {
    List<RockPaperScissCell> neighbors = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      neighbors.add(new RockPaperScissCell(state, rule));
    }
    return neighbors;
  }
}