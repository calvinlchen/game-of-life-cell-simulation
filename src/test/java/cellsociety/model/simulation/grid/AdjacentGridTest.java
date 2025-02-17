package cellsociety.model.simulation.grid;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class AdjacentGridTest {

  private AdjacentGrid<TestCell> grid;
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
      when(cell.getPosition()).thenReturn(new int[]{i / 3, i % 3});
      mockCells.add(cell);
    }
    grid = new AdjacentGrid<>(mockCells, 3, 3);
  }

  @Test
  @DisplayName("AdjacentGrid initializes correctly with valid dimensions")
  void adjacentGrid_ValidDimensions_InitializesCorrectly() {
    assertEquals(3, grid.getRows());
    assertEquals(3, grid.getCols());
    assertEquals(9, grid.getCells().size());
  }

  @Test
  @DisplayName("Throws exception for invalid grid dimensions")
  void adjacentGrid_InvalidDimensions_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> new AdjacentGrid<>(mockCells, 0, 3));
  }

  @Test
  @DisplayName("Throws exception for mismatched cell count")
  void adjacentGrid_MismatchedCellCount_ThrowsSimulationException() {
    List<TestCell> smallCellList = new ArrayList<>(mockCells.subList(0, 5));
    assertThrows(SimulationException.class, () -> new AdjacentGrid<>(smallCellList, 3, 3));
  }

  // generated with chatGPT to help check neighbors set correctly
  @Test
  @DisplayName("Set neighbors correctly updates cells")
  void setNeighbors_CheckForCorrectness_CorrectlySetsNeighbors() {
    grid.setNeighbors();

    // Capture the neighbors list set on each cell
    ArgumentCaptor<List<TestCell>> captor = ArgumentCaptor.forClass(List.class);

    for (TestCell cell : mockCells) {
      int[] pos = cell.getPosition();
      List<TestCell> expectedNeighbors = new ArrayList<>();
      int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

      for (int[] dir : directions) {
        int newRow = pos[0] + dir[0];
        int newCol = pos[1] + dir[1];
        if (grid.isValidPosition(newRow, newCol)) {
          expectedNeighbors.add(grid.getCell(newRow, newCol));
        }
      }

      // Capture the neighbors set on the cell
      verify(cell, atLeastOnce()).setNeighbors(captor.capture());

      // Compare captured neighbors with expected neighbors
      assertEquals(expectedNeighbors.size(), captor.getValue().size());
      assertTrue(captor.getValue().containsAll(expectedNeighbors));
    }
  }


  @Test
  @DisplayName("Throws exception when initializing with null cell list")
  void adjacentGrid_NullCellsList_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> new AdjacentGrid<>(null, 3, 3));
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
