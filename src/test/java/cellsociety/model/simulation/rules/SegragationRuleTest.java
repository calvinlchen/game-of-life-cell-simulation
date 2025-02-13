package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.SegregationCell;
import cellsociety.model.util.constants.CellStates.SegregationStates;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for SegregationRule
 */
class SegregationRuleTest {

  private SegregationRule rule;

  @BeforeEach
  void setUp() {
    rule = new SegregationRule(Map.of("toleranceThreshold", 0.5)); // need to have more than half to be satisfied
  }

  @Test
  @DisplayName("Satisfied middle cell remains in place")
  void satisfiedMiddleCell_RemainsInPlace() {
    // 5 A - 2 B - 1 Empty
    SegregationCell cell = createCellWithStateAndNeighbors(1, 5, 8);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Unsatisfied middle cell moves to empty space")
  void unsatisfiedMiddleCell_MovesToEmptySpace() {
    // 5 A - 2 B - 1 Empty
    SegregationCell cell = createCellWithStateAndNeighbors(1, 2, 8);
    assertEquals(0, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().noneMatch(neighbor -> neighbor.getNextState() == 0));
  }

  @Test
  @DisplayName("Unsatisfied middle cell stays in place because no empty spaces")
  void unsatisfiedMiddleCell_StaysInPlace_NoEmptySpaces() {
    SegregationCell cell = createCellWithStateAndNeighbors(1, 2, 8, false);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Satisfied edge cell remains in place")
  void satisfiedEdgeCell_RemainsInPlace() {
    // 3 A - 1 B - 1 Empty
    SegregationCell cell = createCellWithStateAndNeighbors(1, 3, 5);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Unsatisfied edge cell moves to empty space")
  void unsatisfiedEdgeCell_MovesToEmptySpace() {
    // 1 A - 3 B - 1 Empty
    SegregationCell cell = createCellWithStateAndNeighbors(1, 1, 5);
    assertEquals(0, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().noneMatch(neighbor -> neighbor.getNextState() == 0));
  }

  @Test
  @DisplayName("Unsatisfied edge cell stays in place because no empty spaces")
  void unsatisfiedEdgeCell_StaysInPlace_NoEmptySpaces() {
    SegregationCell cell = createCellWithStateAndNeighbors(1, 1, 5, false);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Satisfied corner cell remains in place")
  void satisfiedCornerCell_RemainsInPlace() {
    // 2 A - 0 B - 1 Empty
    SegregationCell cell = createCellWithStateAndNeighbors(1, 2, 3);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Unsatisfied corner cell moves to empty space")
  void unsatisfiedCornerCell_MovesToEmptySpace() {
    // 0 A - 2 B - 1 Empty
    SegregationCell cell = createCellWithStateAndNeighbors(1, 0, 3);
    assertEquals(0, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().noneMatch(neighbor -> neighbor.getNextState() == 0));
  }

  @Test
  @DisplayName("Unsatisfied corner cell stays in place because no empty spaces")
  void unsatisfiedCornerCell_StaysInPlace_NoEmptySpaces() {
    SegregationCell cell = createCellWithStateAndNeighbors(1, 0, 3, false);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Empty cell remains empty")
  void emptyCell_RemainsEmpty() {
    SegregationCell cell = createCellWithStateAndNeighbors(0, 0, 8);
    assertEquals(0, rule.apply(cell));
  }

  private SegregationCell createCellWithStateAndNeighbors(int state,
      int aNeighbors, int totalNeighbors, boolean empty) {
    SegregationCell cell = new SegregationCell(state, rule);
    List<SegregationCell> neighbors = new ArrayList<>();

    for (int i = 0; i < aNeighbors; i++) {
      neighbors.add(new SegregationCell(state, rule));
    }

    for (int i = aNeighbors; i < totalNeighbors - 1; i++) {
      neighbors.add(new SegregationCell(2, rule));
    }

    int nextState = empty ? 0 : 2;
    neighbors.add(new SegregationCell(nextState, rule));

    cell.setNeighbors(neighbors);
    return cell;
  }

  private SegregationCell createCellWithStateAndNeighbors(int state,
      int aNeighbors, int totalNeighbors) {
    return createCellWithStateAndNeighbors(state, aNeighbors, totalNeighbors, true);
  }
}

