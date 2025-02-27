package cellsociety.model.simulation.grid;

import static cellsociety.model.util.constants.GridTypes.NeighborhoodType.MOORE;
import static cellsociety.model.util.constants.GridTypes.ShapeType.RECTANGLE;

import cellsociety.model.simulation.cell.Cell;
import java.util.List;

/**
 * RectangularGrid represents a 2D grid of cells.
 *
 * <p>Default like what you think of when you think of a grid</p>
 *
 * @param <T> - the type of cell in the grid, must extend Cell
 * @author Jessica Chen
 */
public class RectangularGrid<T extends Cell<T, ?, ?>> extends Grid<T> {

  /**
   * Constructs a RectangularGrid with specified dimensions.
   *
   * @param cells - cells to be added
   * @param rows  - number of rows in the grid
   * @param cols  - number of columns in the grid
   */
  public RectangularGrid(List<T> cells, int rows, int cols) {
    super(cells, rows, cols);
    setNeighbors();
  }

  /**
   * Constructs a RectangularGrid with specified dimensions.
   *
   * @param cells    - cells to be added
   * @param rows     - number of rows in the grid
   * @param cols     - number of columns in the grid
   * @param language - name of language, for error message display
   */
  public RectangularGrid(List<T> cells, int rows, int cols, String language) {
    super(cells, rows, cols, language);
    setNeighbors();
  }

  /**
   * Set neighbors for all cells in the grid.
   *
   * <p> Neighbors are in all 8 directions
   */
  @Override
  public void setNeighbors() {
    setNeighbors(RECTANGLE, MOORE);
  }
}
