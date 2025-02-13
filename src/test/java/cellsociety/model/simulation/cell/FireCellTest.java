package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.rules.FireRule;
import cellsociety.model.util.constants.CellStates.FireStates;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tester for Fire Cell
 */
class FireCellTest {

  private FireRule ruleDefault;
  private FireRule ruleMax;

  @BeforeEach
  void setUp() {
    ruleDefault = new FireRule(Map.of("ignitionLikelihood", 0.0, "treeSpawnLikelihood", 0.0));
    ruleMax = new FireRule(Map.of("ignitionLikelihood", 1.0, "treeSpawnLikelihood", 1.0));
  }

  @Test
  @DisplayName("FireCell correctly initializes state")
  void cell_Initializes_Correctly() {
    FireCell cell = new FireCell(1, ruleDefault);
    assertEquals(1, cell.getCurrentState());
  }

  // again just some simple tests to test calc next state and step updates correctly,
  // validity of the calc next state step is found in rules
  @Test
  @DisplayName("FireCell correctly updates state based on rule")
  void cell_Updates_State_Correctly() {
    FireCell cell = new FireCell(1, ruleMax);
    List<FireCell> neighbors = createNeighbors(1, ruleMax);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(2, cell.getCurrentState());
  }

  @Test
  @DisplayName("FireCell transitions from BURNING to EMPTY")
  void cell_Transitions_From_Burning_To_Empty() {
    FireCell cell = new FireCell(2, ruleDefault);
    cell.calcNextState();
    cell.step();
    assertEquals(0, cell.getCurrentState());
  }

  @Test
  @DisplayName("Tree FireCell without burning neighbor does not ignite (default f)")
  void treeCell_RemainsTree_WithoutBurningNeighbor_DefaultF() {
    FireCell cell = new FireCell(1, ruleDefault);
    List<FireCell> neighbors = createNeighbors(0, ruleDefault);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(1, cell.getCurrentState());
  }

  private List<FireCell> createNeighbors(int burningCount, FireRule rule) {
    List<FireCell> neighbors = new ArrayList<>();
    for (int i = 0; i < burningCount; i++) {
      neighbors.add(new FireCell(2, rule));
    }
    for (int i = burningCount; i < 4; i++) {
      neighbors.add(new FireCell(1, rule));
    }
    return neighbors;
  }
}

