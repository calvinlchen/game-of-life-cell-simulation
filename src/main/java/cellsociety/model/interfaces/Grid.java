package cellsociety.model.interfaces;

import java.util.List;

/**
 * Abstract class for general grid.
 *
 * <p> Grid configures cells to the given pattern and sets the neighbors for the cells.
 *
 * <p> Reference for generics: https://www.geeksforgeeks.org/generics-in-java/
 *
 * @param <S> - the type of state for the cells, must be an Enum
 * @param <T> - the type of cell in the grid, must extend Cell<S>
 * @author Jessica Chen
 */
public abstract class Grid<S extends Enum<S>, T extends Cell<S, T>> {

  /**
   * Constructs a grid
   */
  public Grid() {
  }

  /**
   * Initialize the grid with cells.
   */
  public abstract void initializeCells();

  /**
   * Set neighbors for all cells in the grid.
   */
  public abstract void setNeighbors();

  /**
   * Get neighbors of a specific cell.
   *
   * @param position - position of a cell given in (row, col) pair
   * @return - list of neighboring cells
   */
  public abstract List<T> getNeighbors(int[] position);
}
