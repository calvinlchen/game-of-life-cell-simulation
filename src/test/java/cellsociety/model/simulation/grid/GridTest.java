package cellsociety.model.simulation.grid;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.rules.Rule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * Test cell for testing Grid
 */
class TestGridCell extends Cell<TestGridCell, TestRule> {
  public TestGridCell(int state, TestRule rule) {
    super(state, rule);
  }

  @Override
  public void calcNextState() {
    return;
  }

  @Override
  public void step() {
    return;
  }

  @Override
  protected TestGridCell getSelf() {
    return this;
  }
}

class TestRule extends Rule<TestGridCell> {

  public TestRule(Map<String, Double> parameters) {
    super(parameters);
  }

  @Override
  public int apply(TestGridCell cell) {
    return 0;
  }
}

/**
 * Tester for Grid class
 */
class GridTest {
  private Grid<TestGridCell> grid;
  private List<TestGridCell> cells;
  private TestRule rule;

  // Positive Tests

  @BeforeEach
  void setUp() {
    rule = new TestRule(new HashMap<>());
    cells = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      cells.add(new TestGridCell(1, rule));
    }
    grid = new Grid<>(cells, 3, 3) {
      @Override
      public void setNeighbors() {
        return;
      }

    };
  }

  @Test
  @DisplayName("Grid initializes correctly")
  void grid_Initialization_Verified() {
    assertEquals(3, grid.getRows());
    assertEquals(3, grid.getCols());
    assertNotNull(grid.getGrid());
  }

  @Test
  @DisplayName("Get cell from valid position")
  void getCell_ValidPosition_Verified() {
    assertNotNull(grid.getCell(1, 1));
  }

  @Test
  @DisplayName("Set cell at valid position")
  void setCell_ValidPosition_Verified() {
    TestGridCell newCell = new TestGridCell(0, rule);
    grid.setCell(1, 1, newCell);
    assertEquals(newCell, grid.getCell(1, 1));
  }

  @Test
  @DisplayName("Check valid position")
  void isValidPosition_Check_Verified() {
    assertTrue(grid.isValidPosition(2, 2));
    assertFalse(grid.isValidPosition(-1, 0));
  }

  @Test
  @DisplayName("Retrieve neighbors from valid position")
  void getNeighbors_ValidPosition_Verified() {
    assertNotNull(grid.getNeighbors(new int[]{1, 1}));
  }

  @Test
  @DisplayName("Set neighbors correctly")
  void setNeighbors_Correctly_Verified() {
    int[][] directions = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} }; // Right, Down, Left, Up
    grid.setNeighbors(directions);

    TestGridCell centerCell = grid.getCell(1, 1);
    List<TestGridCell> neighbors = centerCell.getNeighbors();
    assertEquals(4, neighbors.size());
    assertTrue(neighbors.contains(grid.getCell(1, 2))); // Right
    assertTrue(neighbors.contains(grid.getCell(2, 1))); // Down
    assertTrue(neighbors.contains(grid.getCell(1, 0))); // Left
    assertTrue(neighbors.contains(grid.getCell(0, 1))); // Up
  }

  // Negative Tests

  @Test
  @DisplayName("Get cell from invalid negative position throws exception")
  void getCell_InvalidNegPosition_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> grid.getCell(-1, 1));
  }
  @Test
  @DisplayName("Get cell from invalid positive position throws exception")
  void getCell_InvalidPosPosition_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> grid.getCell(1, 4));
  }

  @Test
  @DisplayName("Set cell at invalid negative position throws exception")
  void setCell_InvalidNegPosition_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> grid.setCell(-1, 1, new TestGridCell(1, rule)));
  }

  @Test
  @DisplayName("Set cell at invalid position position throws exception")
  void setCell_InvalidPosPosition_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> grid.setCell(1, 4, new TestGridCell(1, rule)));
  }

  @Test
  @DisplayName("Set cell to null throws exception")
  void setCell_Null_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> grid.setCell(1, 1, null));
  }

  @Test
  @DisplayName("Retrieve neighbors from invalid negative position throws exception")
  void getNeighbors_InvalidNegPosition_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> grid.getNeighbors(new int[]{-1, 1}));
  }

  @Test
  @DisplayName("Retrive neighbors from invalid positive position throws exception")
  void getNeighbors_InvalidPosPosition_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> grid.getNeighbors(new int[]{1, 4}));
  }
}
