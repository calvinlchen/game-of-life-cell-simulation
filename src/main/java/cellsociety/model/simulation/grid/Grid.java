package cellsociety.model.simulation.grid;

import static cellsociety.model.util.constants.ResourcePckg.ERROR_SIMULATION_RESOURCE_PACKAGE;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
public abstract class Grid<T extends Cell<T, ?, ?>> {

  private final List<List<T>> myGrid;
  private final int myRows;
  private final int myCols;

  private ResourceBundle myResources;

  /**
   * Constructs a Grid with specified dimensions.
   *
   * @param cells - cells to be added
   * @param rows  - number of rows in the grid
   * @param cols  - number of columns in the grid
   */
  public Grid(List<T> cells, int rows, int cols) {
    myResources = ResourceBundle.getBundle(ERROR_SIMULATION_RESOURCE_PACKAGE + "English");

    if (rows <= 0 || cols <= 0) {
      throw new SimulationException(myResources.getString("InvalidGridDimensions"));
    }

    myRows = rows;
    myCols = cols;
    myGrid = new ArrayList<>();

    initializeCells(cells);
  }

  /**
   * Constructs a Grid with specified dimensions.
   *
   * @param cells - cells to be added
   * @param rows  - number of rows in the grid
   * @param cols  - number of columns in the grid
   */
  public Grid(List<T> cells, int rows, int cols, String language) {
    myResources = ResourceBundle.getBundle(ERROR_SIMULATION_RESOURCE_PACKAGE + language);

    if (rows <= 0 || cols <= 0) {
      throw new SimulationException(myResources.getString("InvalidGridDimensions"));
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
   */
  public void initializeCells(List<T> cells) {
    if (cells == null) {
      throw new SimulationException(myResources.getString("NullCellsList"));
    }
    if (cells.size() != myRows * myCols) {
      throw new SimulationException(String.format(myResources.getString("MismatchedCellCount"), cells.size(), myRows * myCols));
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
   */
  public T getCell(int row, int col) {
    if (!isValidPosition(row, col)) {
      throw new SimulationException(String.format(myResources.getString("InvalidGridPosition"), row, col));
    }
    return myGrid.get(row).get(col);
  }

  /**
   * Sets a cell in the grid.
   *
   * @param row  - row index
   * @param col  - column index
   * @param cell - cell to be placed
   */
  public void setCell(int row, int col, T cell) {
    if (!isValidPosition(row, col)) {
      throw new SimulationException(String.format(myResources.getString("InvalidGridPosition"), row, col));
    }
    if (cell == null) {
      throw new SimulationException(myResources.getString("NullCell"));
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

  /**
   * Get all cells in the grid as a flattened list
   *
   * @return A list of all cells in the grid as a flattened grid
   */
  public List<T> getCells() {
    return myGrid.stream()
        .flatMap(List::stream)
        .toList();
  }

  /**
   * return resource bundle associated for exceptions
   *
   * @return resource bundle associated for exception
   */
  public ResourceBundle getResources() {
    return myResources;
  }
}
