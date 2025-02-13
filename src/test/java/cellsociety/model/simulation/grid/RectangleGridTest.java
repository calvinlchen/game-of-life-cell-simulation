package cellsociety.model.simulation.grid;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.Cell;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

enum GridTestState { ALIVE, DEAD; }   // Testing enum for states

/**
 * Test cell for testing RectangularGrid
 */
class TestRectangularGridCell extends Cell<TestRectangularGridCell> {
  public TestRectangularGridCell(int state) {
    super(state);
  }

  public TestRectangularGridCell(int state, int[] position) {
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
 * Tester for RectangularGrid class
 */
class RectangularGridTest {
  private RectangularGrid<TestRectangularGridCell> grid;
  private List<TestRectangularGridCell> cells;

  @BeforeEach
  void setUp() {
    cells = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      cells.add(new TestRectangularGridCell(1));
    }
    grid = new RectangularGrid<>(cells, 3, 3);
  }

  @Test
  @DisplayName("Neighbors correctly set for middle cell")
  void middleCell_Neighbors_Verified() {
    TestRectangularGridCell middleCell = grid.getCell(1, 1);
    assertEquals(8, middleCell.getNeighbors().size());
    checkNeighborPositions(middleCell, new int[][]{{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 2}, {2, 0}, {2, 1}, {2, 2}});
  }

  @Test
  @DisplayName("Neighbors correctly set for top-left corner cell")
  void topLeftCornerCell_Neighbors_Verified() {
    TestRectangularGridCell cornerCell = grid.getCell(0, 0);
    assertEquals(3, cornerCell.getNeighbors().size());
    checkNeighborPositions(cornerCell, new int[][]{{0, 1}, {1, 0}, {1, 1}});
  }

  @Test
  @DisplayName("Neighbors correctly set for top-right corner cell")
  void topRightCornerCell_Neighbors_Verified() {
    TestRectangularGridCell cornerCell = grid.getCell(0, 2);
    assertEquals(3, cornerCell.getNeighbors().size());
    checkNeighborPositions(cornerCell, new int[][]{{0, 1}, {1, 1}, {1, 2}});
  }

  @Test
  @DisplayName("Neighbors correctly set for bottom-left corner cell")
  void bottomLeftCornerCell_Neighbors_Verified() {
    TestRectangularGridCell cornerCell = grid.getCell(2, 0);
    assertEquals(3, cornerCell.getNeighbors().size());
    checkNeighborPositions(cornerCell, new int[][]{{1, 0}, {1, 1}, {2, 1}});
  }

  @Test
  @DisplayName("Neighbors correctly set for bottom-right corner cell")
  void bottomRightCornerCell_Neighbors_Verified() {
    TestRectangularGridCell cornerCell = grid.getCell(2, 2);
    assertEquals(3, cornerCell.getNeighbors().size());
    checkNeighborPositions(cornerCell, new int[][]{{1, 1}, {1, 2}, {2, 1}});
  }

  @Test
  @DisplayName("Neighbors correctly set for side (non-corner) cells")
  void sideCells_Neighbors_Verified() {
    TestRectangularGridCell topMiddle = grid.getCell(0, 1);
    TestRectangularGridCell leftMiddle = grid.getCell(1, 0);
    TestRectangularGridCell rightMiddle = grid.getCell(1, 2);
    TestRectangularGridCell bottomMiddle = grid.getCell(2, 1);

    assertEquals(5, topMiddle.getNeighbors().size());
    checkNeighborPositions(topMiddle, new int[][]{{0, 0}, {0, 2}, {1, 0}, {1, 1}, {1, 2}});
    assertEquals(5, leftMiddle.getNeighbors().size());
    checkNeighborPositions(leftMiddle, new int[][]{{0, 0}, {0, 1}, {1, 1}, {2, 0}, {2, 1}});
    assertEquals(5, rightMiddle.getNeighbors().size());
    checkNeighborPositions(rightMiddle, new int[][]{{0, 1}, {0, 2}, {1, 1}, {2, 1}, {2, 2}});
    assertEquals(5, bottomMiddle.getNeighbors().size());
    checkNeighborPositions(bottomMiddle, new int[][]{{1, 0}, {1, 1}, {1, 2}, {2, 0}, {2, 2}});
  }

  private void checkNeighborPositions(TestRectangularGridCell cell, int[][] expectedPositions) {
    List<TestRectangularGridCell> neighbors = cell.getNeighbors();
    List<int[]> actualPositions = new ArrayList<>();
    for (TestRectangularGridCell neighbor : neighbors) {
      actualPositions.add(neighbor.getPosition());
    }
    for (int[] expected : expectedPositions) {
      assertTrue(actualPositions.stream().anyMatch(pos -> pos[0] == expected[0] && pos[1] == expected[1]));
    }
  }
}
