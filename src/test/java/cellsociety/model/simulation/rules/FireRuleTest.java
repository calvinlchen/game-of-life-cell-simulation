package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.util.constants.CellStates.FireStates;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test for testing Fire Rules
 */
class FireRuleTest {

  private FireRule ruleDefault;
  private FireRule ruleMax;

  @BeforeEach
  void setUp() {
    ruleDefault = new FireRule(Map.of("ignitionLikelihood", 0.0, "treeSpawnLikelihood", 0.0)); // default probability to be 0
    ruleMax = new FireRule(
        Map.of("ignitionLikelihood", 1.0, "treeSpawnLikelihood",
            1.0));                        // max probability to be 1 to see difference
  }

  @Test
  @DisplayName("Tree middle cell with burning neighbor always catches fire (max f)")
  void treeMiddleCell_CatchesFire_MaxF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 1, 4);
    assertEquals(2, ruleMax.apply(cell));
  }

  @Test
  @DisplayName("Tree middle cell without burning neighbor never ignites (default f)")
  void treeMiddleCell_RemainsTree_DefaultF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 0, 4);
    assertEquals(1, ruleDefault.apply(cell));
  }

  @Test
  @DisplayName("Burning middle cell becomes empty")
  void burningMiddleCell_BecomesEmpty() {
    FireCell cell = createCellWithStateAndNeighbors(2, 2, 4);
    assertEquals(0, ruleDefault.apply(cell));
  }

  @Test
  @DisplayName("Tree edge cell with burning neighbor always catches fire (max f)")
  void treeEdgeCell_CatchesFire_MaxF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 1, 3);
    assertEquals(2, ruleMax.apply(cell));
  }

  @Test
  @DisplayName("Tree edge cell without burning neighbor never ignites (default f)")
  void treeEdgeCell_RemainsTree_DefaultF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 0, 3);
    assertEquals(1, ruleDefault.apply(cell));
  }

  @Test
  @DisplayName("Burning edge cell becomes empty")
  void burningEdgeCell_BecomesEmpty() {
    FireCell cell = createCellWithStateAndNeighbors(2, 2, 3);
    assertEquals(0, ruleDefault.apply(cell));
  }

  @Test
  @DisplayName("Tree corner cell with burning neighbor always catches fire (max f)")
  void treeCornerCell_CatchesFire_MaxF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 1, 2);
    assertEquals(2, ruleMax.apply(cell));
  }

  @Test
  @DisplayName("Tree corner cell without burning neighbor never ignites (default f)")
  void treeCornerCell_RemainsTree_DefaultF() {
    FireCell cell = createCellWithStateAndNeighbors(1, 0, 2);
    assertEquals(1, ruleDefault.apply(cell));
  }

  @Test
  @DisplayName("Burning corner cell becomes empty")
  void burningCornerCell_BecomesEmpty() {
    FireCell cell = createCellWithStateAndNeighbors(2, 2, 2);
    assertEquals(0, ruleDefault.apply(cell));
  }

  private FireCell createCellWithStateAndNeighbors(int state, int burningNeighbors,
      int totalNeighbors) {
    // the rule here doesn't matter
    FireCell cell = new FireCell(state, ruleDefault);
    List<FireCell> neighbors = new ArrayList<>();
    for (int i = 0; i < burningNeighbors; i++) {
      neighbors.add(new FireCell(2, ruleDefault));
    }
    for (int i = burningNeighbors; i < totalNeighbors; i++) {
      neighbors.add(new FireCell(1, ruleDefault));
    }
    cell.setNeighbors(neighbors);
    return cell;
  }
}