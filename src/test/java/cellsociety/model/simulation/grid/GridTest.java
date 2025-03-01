package cellsociety.model.simulation.grid;

import static cellsociety.model.util.constants.GridTypes.NeighborhoodType.VON_NEUMANN;
import static cellsociety.model.util.constants.GridTypes.ShapeType.RECTANGLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GridTest {

  private TestGrid grid;
  private List<TestCell> mockCells;
  private TestRule mockRule;
  private Parameters mockParameters;

  static class TestCell extends Cell<TestCell, TestRule, Parameters> {
    public TestCell(int state, TestRule rule) {
      super(state, rule);
    }
    @Override
    protected TestCell getSelf() {
      return this;
    }
  }

  static class TestRule extends Rule<TestCell, Parameters> {
    public TestRule(Parameters parameters) {
      super(parameters);
    }

    @Override
    public int apply(TestCell cell) {
      return 1; // Dummy implementation
    }
  }

  static class TestGrid extends Grid<TestCell> {
    public TestGrid(List<TestCell> cells, int rows, int cols) {
      super(cells, rows, cols);
    }
    @Override
    public void setNeighbors() {}
  }

  @BeforeEach
  void setUp() {
    mockRule = Mockito.mock(TestRule.class);
    mockParameters = Mockito.mock(Parameters.class);
    when(mockRule.getParameters()).thenReturn(mockParameters);
    when(mockParameters.getParameter("maxHistorySize")).thenReturn(3.);

    mockCells = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      TestCell cell = Mockito.mock(TestCell.class);
      when(cell.getCurrentState()).thenReturn(i);
      mockCells.add(cell);
    }
    grid = new TestGrid(mockCells, 3, 3);
  }

  @Test
  @DisplayName("Grid initializes correctly with valid dimensions")
  void grid_ValidDimensions_InitializesCorrectly() {
    assertEquals(3, grid.getRows());
    assertEquals(3, grid.getCols());
    assertEquals(9, grid.getCells().size());
  }

  @Test
  @DisplayName("Throws exception for invalid grid dimensions")
  void grid_InvalidDimensions_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> new TestGrid(mockCells, 0, 3));
  }

  @Test
  @DisplayName("Throws exception for mismatched cell count")
  void grid_MismatchedCellCount_ThrowsSimulationException() {
    List<TestCell> smallCellList = new ArrayList<>(mockCells.subList(0, 5));
    assertThrows(SimulationException.class, () -> new TestGrid(smallCellList, 3, 3));
  }

  @Test
  @DisplayName("Get cell returns correct cell")
  void getCell_ValidPosition_ReturnsCorrectCell() {
    when(mockCells.get(0).getPosition()).thenReturn(new int[]{0, 0});
    assertNotNull(grid.getCell(0, 0));
  }

  @Test
  @DisplayName("Get cell throws exception for invalid position")
  void getCell_InvalidPosition_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> grid.getCell(-1, 0));
  }

  @Test
  @DisplayName("Check if position is valid")
  void isValidPosition_ChecksBounds_ReturnsCorrectResult() {
    assertTrue(grid.isValidPosition(1, 1));
    assertFalse(grid.isValidPosition(3, 3));
  }

  @Test
  @DisplayName("Set cell updates the grid correctly")
  void setCell_ValidPosition_UpdatesGrid() {
    TestCell newCell = new TestCell(5, mockRule);
    grid.setCell(1, 1, newCell);
    assertEquals(newCell, grid.getCell(1, 1));
  }

  @Test
  @DisplayName("Set cell throws exception for invalid position")
  void setCell_InvalidPosition_ThrowsSimulationException() {
    TestCell newCell = new TestCell(5, mockRule);
    assertThrows(SimulationException.class, () -> grid.setCell(-1, 1, newCell));
  }

  @Test
  @DisplayName("Set cell throws exception for null cell")
  void setCell_NullCell_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> grid.setCell(1, 1, null));
  }

  @Test
  @DisplayName("Get rows and columns return correct values")
  void getRowsAndCols_CheckForCorrectness_ReturnCorrectValues() {
    assertEquals(3, grid.getRows());
    assertEquals(3, grid.getCols());
  }

  @Test
  @DisplayName("Get grid returns correct structure")
  void getGrid_CheckForCorrectness_ReturnsCorrectStructure() {
    assertNotNull(grid.getGrid());
    assertEquals(3, grid.getGrid().size());
    assertEquals(3, grid.getGrid().get(0).size());
  }

  @Test
  @DisplayName("Get all cells returns correct flattened list")
  void getCells_CheckForCorrectness_ReturnsFlattenedList() {
    List<TestCell> allCells = grid.getCells();
    assertEquals(9, allCells.size());
    for (int i = 0; i < 9; i++) {
      assertEquals(i, allCells.get(i).getCurrentState());
    }
  }

  @Test
  @DisplayName("Test setting neighbors correctly updates cells")
  void setNeighbors_ChecksForCorectness_CorrectlySetsNeighbors() {
    grid.setNeighbors(RECTANGLE, VON_NEUMANN);
    for (TestCell cell : mockCells) {
      verify(cell, atLeastOnce()).setNeighbors(anyList());
    }
  }

  @Test
  @DisplayName("Initialize cells assigns correct positions")
  void initializeCells_CheckForCorrectness_AssignsCorrectPositions() {
    for (int i = 0; i < 9; i++) {
      verify(mockCells.get(i)).setPosition(new int[]{i % 3, i / 3});
    }
  }

  @Test
  @DisplayName("Throws exception when initializing with null cell list")
  void initializeCells_NullCellsList_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> new TestGrid(null, 3, 3));
  }

  @Test
  @DisplayName("Get neighbors returns valid neighbors")
  void getNeighbors_ValidPosition_ReturnsNeighbors() {
    int[] position = {1, 1};
    List<TestCell> expectedNeighbors = List.of(mockCells.get(0), mockCells.get(2), mockCells.get(3), mockCells.get(5));
    when(mockCells.get(4).getNeighbors()).thenReturn(expectedNeighbors);
    assertEquals(expectedNeighbors, grid.getNeighbors(position));
  }

  @Test
  @DisplayName("Get neighbors throws exception for invalid position")
  void getNeighbors_InvalidPosition_ThrowsSimulationException() {
    int[] position = {-1, 1};
    assertThrows(SimulationException.class, () -> grid.getNeighbors(position));
  }


}


