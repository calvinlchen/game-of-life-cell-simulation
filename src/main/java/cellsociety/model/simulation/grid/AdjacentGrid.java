package cellsociety.model.simulation.grid;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.List;

/**
 * AdjacentGrid represents a 2D grid where each cell only considers its four direct neighbors.
 *
 * @param <T> - the type of cell in the grid, must extend Cell<S>
 * @author Jessica Chen
 */
public class AdjacentGrid<T extends Cell<T, ?, ?>> extends Grid<T> {

  /**
   * Constructs an AdjacentGrid with specified dimensions.
   *
   * @param cells - cells to be added
   * @param rows  - number of rows in the grid
   * @param cols  - number of columns in the grid
   */
  public AdjacentGrid(List<T> cells, int rows, int cols) {
    super(cells, rows, cols);
    setNeighbors();
  }

  /**
   * Set neighbors for all cells in the grid.
   *
   * <p> Neighbors are only the four directly adjacent cells (up, down, left, right)
   */
  @Override
  public void setNeighbors() {
      int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
      setNeighbors(directions);
  }
}


