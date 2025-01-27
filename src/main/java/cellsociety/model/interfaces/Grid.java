package cellsociety.model.interfaces;

import java.util.List;

/**
 * Abstract class for general grid.
 *
 * <p> Grid configures cells to the given pattern and sets the neighbors for the cells.
 *
 * @author Jessica Chen
 */
abstract class Grid {

  private final int rows;
  private final int cols;

  /**
   * Constructs a  grid with the configuration of row and col
   *
   * @param rows - the number of rows
   * @param cols - the number of cols
   */
  protected Grid(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
  }

  /**
   * Initialize the grid with cells.
   */
  protected abstract void initializeCells();

  /**
   * Set neighbors for all cells in the grid.
   */
  protected abstract void setNeighbors();

  /**
   * Get neighbors of a specific cell.
   *
   * @param position - position of a cell given in (row, col) pair
   * @return         - list of neighboring cells
   */
  protected abstract List<Cell> getNeighbors(int[] position);
}
