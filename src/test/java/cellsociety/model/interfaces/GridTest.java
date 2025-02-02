package cellsociety.model.interfaces;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

enum GridTestState { ALIVE, DEAD; }   // Testing enum for states

/**
 * Test cell for testing Grid
 */
class TestGridCell extends Cell<GridTestState, TestGridCell> {
  public TestGridCell(GridTestState state) {
    super(state);
  }

  public TestGridCell(GridTestState state, int[] position) {
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
 * Tester for Grid class
 */
class GridTest {
  private Grid<GridTestState, TestGridCell> grid;
  private List<TestGridCell> cells;

  // Positive Tests

  @BeforeEach
  void setUp() {
    cells = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      cells.add(new TestGridCell(GridTestState.ALIVE));
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
    TestGridCell newCell = new TestGridCell(GridTestState.DEAD);
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
    assertThrows(IllegalArgumentException.class, () -> grid.setCell(-1, 1, new TestGridCell(GridTestState.ALIVE)));
  }

  @Test
  @DisplayName("Set cell at invalid position position throws exception")
  void setCell_InvalidPosPosition_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> grid.setCell(1, 4, new TestGridCell(GridTestState.ALIVE)));
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
