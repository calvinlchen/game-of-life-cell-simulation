package cellsociety.model.simulation.grid;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.interfaces.Cell;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

enum AdjacentGridTestState { ALIVE, DEAD; }   // Testing enum for states

/**
 * Test cell for testing AdjacentGrid
 */
class TestAdjacentGridCell extends Cell<AdjacentGridTestState, TestAdjacentGridCell> {
  public TestAdjacentGridCell(AdjacentGridTestState state) {
    super(state);
  }

  public TestAdjacentGridCell(AdjacentGridTestState state, int[] position) {
    super(state, position);
  }

  @Override
  public void calcNextState() {
    return;
  }

  @Override
  public void step() {
    return;
  }
}

/**
 * Tester for AdjacentGrid class
 */
class AdjacentGridTest {
  private AdjacentGrid<AdjacentGridTestState, TestAdjacentGridCell> grid;
  private List<TestAdjacentGridCell> cells;

  @BeforeEach
  void setUp() {
    cells = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      cells.add(new TestAdjacentGridCell(AdjacentGridTestState.ALIVE));
    }
    grid = new AdjacentGrid<>(cells, 3, 3);
  }

  @Test
  @DisplayName("Neighbors correctly set for middle cell")
  void middleCell_Neighbors_Verified() {
    TestAdjacentGridCell middleCell = grid.getCell(1, 1);
    assertEquals(4, middleCell.getNeighbors().size());
    checkNeighborPositions(middleCell, new int[][]{{0, 1}, {1, 0}, {1, 2}, {2, 1}});
  }

  @Test
  @DisplayName("Neighbors correctly set for top-left corner cell")
  void topLeftCornerCell_Neighbors_Verified() {
    TestAdjacentGridCell cornerCell = grid.getCell(0, 0);
    assertEquals(2, cornerCell.getNeighbors().size());
    checkNeighborPositions(cornerCell, new int[][]{{0, 1}, {1, 0}});
  }

  @Test
  @DisplayName("Neighbors correctly set for top-right corner cell")
  void topRightCornerCell_Neighbors_Verified() {
    TestAdjacentGridCell cornerCell = grid.getCell(0, 2);
    assertEquals(2, cornerCell.getNeighbors().size());
    checkNeighborPositions(cornerCell, new int[][]{{0, 1}, {1, 2}});
  }

  @Test
  @DisplayName("Neighbors correctly set for bottom-left corner cell")
  void bottomLeftCornerCell_Neighbors_Verified() {
    TestAdjacentGridCell cornerCell = grid.getCell(2, 0);
    assertEquals(2, cornerCell.getNeighbors().size());
    checkNeighborPositions(cornerCell, new int[][]{{1, 0}, {2, 1}});
  }

  @Test
  @DisplayName("Neighbors correctly set for bottom-right corner cell")
  void bottomRightCornerCell_Neighbors_Verified() {
    TestAdjacentGridCell cornerCell = grid.getCell(2, 2);
    assertEquals(2, cornerCell.getNeighbors().size());
    checkNeighborPositions(cornerCell, new int[][]{{1, 2}, {2, 1}});
  }

  @Test
  @DisplayName("Neighbors correctly set for side (non-corner) cells")
  void sideCells_Neighbors_Verified() {
    TestAdjacentGridCell topMiddle = grid.getCell(0, 1);
    TestAdjacentGridCell leftMiddle = grid.getCell(1, 0);
    TestAdjacentGridCell rightMiddle = grid.getCell(1, 2);
    TestAdjacentGridCell bottomMiddle = grid.getCell(2, 1);

    assertEquals(3, topMiddle.getNeighbors().size());
    checkNeighborPositions(topMiddle, new int[][]{{0, 0}, {0, 2}, {1, 1}});
    assertEquals(3, leftMiddle.getNeighbors().size());
    checkNeighborPositions(leftMiddle, new int[][]{{0, 0}, {1, 1}, {2, 0}});
    assertEquals(3, rightMiddle.getNeighbors().size());
    checkNeighborPositions(rightMiddle, new int[][]{{0, 2}, {1, 1}, {2, 2}});
    assertEquals(3, bottomMiddle.getNeighbors().size());
    checkNeighborPositions(bottomMiddle, new int[][]{{1, 1}, {2, 0}, {2, 2}});
  }

  private void checkNeighborPositions(TestAdjacentGridCell cell, int[][] expectedPositions) {
    List<TestAdjacentGridCell> neighbors = cell.getNeighbors();
    List<int[]> actualPositions = new ArrayList<>();
    for (TestAdjacentGridCell neighbor : neighbors) {
      actualPositions.add(neighbor.getPosition());
    }
    for (int[] expected : expectedPositions) {
      assertTrue(actualPositions.stream().anyMatch(pos -> pos[0] == expected[0] && pos[1] == expected[1]));
    }
  }
}
