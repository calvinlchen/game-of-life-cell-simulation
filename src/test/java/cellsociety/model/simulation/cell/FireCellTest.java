package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.parameters.FireParameters;
import cellsociety.model.simulation.rules.FireRule;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tester for FireCell
 */
class FireCellTest {

  private FireRule ruleDefault;
  private FireRule ruleMax;
  private FireParameters defaultParams;
  private FireParameters maxParams;

  @BeforeEach
  void setUp() {
    // Use FireParameters instead of raw maps
    defaultParams = new FireParameters();
    defaultParams.setParameter("ignitionLikelihood", 0.0);
    defaultParams.setParameter("treeSpawnLikelihood", 0.0);
    ruleDefault = new FireRule(defaultParams);

    maxParams = new FireParameters();
    maxParams.setParameter("ignitionLikelihood", 1.0);
    maxParams.setParameter("treeSpawnLikelihood", 1.0);
    ruleMax = new FireRule(maxParams);
  }

  @Test
  @DisplayName("FireCell correctly initializes state")
  void cell_Initializes_Correctly() {
    FireCell cell = new FireCell(1, ruleDefault);
    assertEquals(1, cell.getCurrentState());
  }

  // Test that fire propagation works as expected
  @Test
  @DisplayName("FireCell correctly updates state based on rule")
  void cell_Updates_State_Correctly() {
    FireCell cell = new FireCell(1, ruleMax);
    List<FireCell> neighbors = createNeighbors(1, ruleMax);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(2, cell.getCurrentState()); // Should ignite
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
    assertEquals(1, cell.getCurrentState()); // Should remain tree
  }

  /**
   * Creates a list of FireCell neighbors with a given number of burning trees.
   *
   * @param burningCount - number of burning neighbors
   * @param rule - FireRule to be used for neighbors
   * @return List of FireCell neighbors
   */
  private List<FireCell> createNeighbors(int burningCount, FireRule rule) {
    List<FireCell> neighbors = new ArrayList<>();
    for (int i = 0; i < burningCount; i++) {
      neighbors.add(new FireCell(2, rule)); // Burning trees
    }
    for (int i = burningCount; i < 4; i++) {
      neighbors.add(new FireCell(1, rule)); // Unburned trees
    }
    return neighbors;
  }
}
