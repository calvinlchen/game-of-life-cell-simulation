package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.simulation.parameters.FireParameters;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test for FireRule logic.
 */
class FireRuleTest {

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
  @DisplayName("Tree middle cell with burning neighbor always catches fire (max f)")
  void treeMiddleCell_CatchesFire_MaxF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 1, 4, ruleMax);
    assertEquals(2, ruleMax.apply(cell));
  }

  @Test
  @DisplayName("Tree middle cell without burning neighbor never ignites (default f)")
  void treeMiddleCell_RemainsTree_DefaultF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 0, 4, ruleDefault);
    assertEquals(1, ruleDefault.apply(cell));
  }

  @Test
  @DisplayName("Burning middle cell becomes empty")
  void burningMiddleCell_BecomesEmpty() {
    FireCell cell = createCellWithStateAndNeighbors(2, 2, 4, ruleDefault);
    assertEquals(0, ruleDefault.apply(cell));
  }

  @Test
  @DisplayName("Tree edge cell with burning neighbor always catches fire (max f)")
  void treeEdgeCell_CatchesFire_MaxF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 1, 3, ruleMax);
    assertEquals(2, ruleMax.apply(cell));
  }

  @Test
  @DisplayName("Tree edge cell without burning neighbor never ignites (default f)")
  void treeEdgeCell_RemainsTree_DefaultF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 0, 3, ruleDefault);
    assertEquals(1, ruleDefault.apply(cell));
  }

  @Test
  @DisplayName("Burning edge cell becomes empty")
  void burningEdgeCell_BecomesEmpty() {
    FireCell cell = createCellWithStateAndNeighbors(2, 2, 3, ruleDefault);
    assertEquals(0, ruleDefault.apply(cell));
  }

  @Test
  @DisplayName("Tree corner cell with burning neighbor always catches fire (max f)")
  void treeCornerCell_CatchesFire_MaxF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 1, 2, ruleMax);
    assertEquals(2, ruleMax.apply(cell));
  }

  @Test
  @DisplayName("Tree corner cell without burning neighbor never ignites (default f)")
  void treeCornerCell_RemainsTree_DefaultF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 0, 2, ruleDefault);
    assertEquals(1, ruleDefault.apply(cell));
  }

  @Test
  @DisplayName("Burning corner cell becomes empty")
  void burningCornerCell_BecomesEmpty() {
    FireCell cell = createCellWithStateAndNeighbors(2, 2, 2, ruleDefault);
    assertEquals(0, ruleDefault.apply(cell));
  }

  /**
   * Creates a FireCell with a specified state and number of burning neighbors.
   *
   * @param state            - The state of the FireCell (0 = empty, 1 = tree, 2 = burning)
   * @param burningNeighbors - Number of neighbors that are burning
   * @param totalNeighbors   - Total number of neighbors
   * @param rule             - The FireRule to apply to the FireCell
   * @return A FireCell instance with specified state and neighbors.
   */
  private FireCell createCellWithStateAndNeighbors(int state, int burningNeighbors,
      int totalNeighbors, FireRule rule) {
    FireCell cell = new FireCell(state, rule);
    List<FireCell> neighbors = new ArrayList<>();
    for (int i = 0; i < burningNeighbors; i++) {
      neighbors.add(new FireCell(2, rule)); // Burning neighbors
    }
    for (int i = burningNeighbors; i < totalNeighbors; i++) {
      neighbors.add(new FireCell(1, rule)); // Tree neighbors
    }
    cell.setNeighbors(neighbors);
    return cell;
  }
}
