package cellsociety.model.simulation.grid;

import cellsociety.model.simulation.cell.Cell;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for general grid.
 *
 * <p> Grid configures cells to the given pattern and sets the neighbors for the cells, the purpose
 * is to abstract away creation of neighbors for a given cell
 *
 * <p> Reference for generics: https://www.geeksforgeeks.org/generics-in-java/
 *
 * @param <T> - the type of cell in the grid, must extend Cell<S>
 * @author Jessica Chen
 */
public abstract class Grid<T extends Cell<T>> {

  private final List<List<T>> myGrid;
  private final int myRows;
  private final int myCols;

  /**
   * Constructs a Grid with specified dimensions.
   *
   * @param cells - cells to be added
   * @param rows  - number of rows in the grid
   * @param cols  - number of columns in the grid
   * @throws IllegalArgumentException if grid dimensions are negative or cells are null
   */
  public Grid(List<T> cells, int rows, int cols) {
    if (rows <= 0 || cols <= 0) {
      throw new IllegalArgumentException("Grid dimensions must be positive.");
    }

    myRows = rows;
    myCols = cols;
    myGrid = new ArrayList<>();

    initializeCells(cells);
  }

  /**
   * Set neighbors for all cells in the grid.
   */
  public abstract void setNeighbors();

  /**
   * Helper function for abstracted set neighbor
   *
   * <p>for each cell, set neighbor in the given directions
   * @param directions - directions to set neighbors
   */
  public void setNeighbors(int[][] directions) {
    for (int i = 0; i < getRows(); i++) {
      for (int j = 0; j < getCols(); j++) {
        T cell = getGrid().get(i).get(j);
        if (cell != null) {
          List<T> neighbors = new ArrayList<>();
          for (int[] dir : directions) {
            int newRow = i + dir[0];
            int newCol = j + dir[1];
            if (isValidPosition(newRow, newCol)) {
              neighbors.add(getGrid().get(newRow).get(newCol));
            }
          }
          cell.setNeighbors(neighbors);
        }
      }
    }
  }

  /**
   * Initialize the grid with
   *
   * @param cells - cells to be added
   * @throws IllegalArgumentException if cells is null
   */
  public void initializeCells(List<T> cells) {
    if (cells == null) {
      throw new IllegalArgumentException("Cells list cannot be null.");
    }
    myGrid.clear();

    int index = 0;
    for (int i = 0; i < myRows && index < cells.size(); i++) {
      List<T> row = new ArrayList<>();
      for (int j = 0; j < myCols && index < cells.size(); j++, index++) {
        cells.get(index).setPosition(new int[]{i, j});
        row.add(cells.get(index));
      }
      myGrid.add(row);
    }
  }

  /**
   * Get neighbors of a specific cell.
   *
   * @param position - position of a cell given in (row, col) pair
   * @return - list of neighboring cells
   * @throws IllegalArgumentException if invalid position on the grid
   */
  public List<T> getNeighbors(int[] position) {
    return getCell(position[0], position[1]).getNeighbors();
  }

  /**
   * Checks if the given row and column are within grid bounds.
   *
   * @param row - x row to check if within bounds
   * @param col - y col to check if within bounds
   */
  public boolean isValidPosition(int row, int col) {
    return row >= 0 && row < myRows && col >= 0 && col < myCols;
  }

  /**
   * Gets a cell from the grid.
   *
   * @param row - x row to get cell from
   * @param col - y col to get cell from
   * @return the cell in a given place or null if no cell there but valid position
   * @throws IllegalArgumentException if x and y are an invalid position in the grid
   */
  public T getCell(int row, int col) {
    if (!isValidPosition(row, col)) {
      throw new IllegalArgumentException("Invalid position in the grid");
    }
    return myGrid.get(row).get(col);
  }

  /**
   * Sets a cell in the grid.
   *
   * @param row  - row index
   * @param col  - column index
   * @param cell - cell to be placed
   * @throws IllegalArgumentException if row or col are out of bounds or cell is null
   */
  public void setCell(int row, int col, T cell) {
    if (!isValidPosition(row, col)) {
      throw new IllegalArgumentException("Invalid position in the grid.");
    } else if (cell == null) {
      throw new IllegalArgumentException("Cell cannot be null.");
    }
    myGrid.get(row).set(col, cell);
  }

  /**
   * Gets the number of rows in the grid.
   *
   * @return number of rows
   */
  public int getRows() {
    return myRows;
  }

  /**
   * Gets the number of columns in the grid.
   *
   * @return number of columns
   */
  public int getCols() {
    return myCols;
  }

  /**
   * Gets the entire grid.
   *
   * @return the grid as a list of lists
   */
  public List<List<T>> getGrid() {
    return myGrid;
  }

}
