package cellsociety.model.simulation.grid;

import static cellsociety.model.util.constants.GridTypes.EdgeType.NONE;
import static cellsociety.model.util.constants.ResourcePckg.getErrorSimulationResourceBundle;

import cellsociety.model.factories.GridFactory;
import cellsociety.model.factories.edgefactory.EdgeFactory;
import cellsociety.model.factories.edgefactory.handler.EdgeHandler;
import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.util.constants.GridTypes.EdgeType;
import cellsociety.model.util.constants.GridTypes.NeighborhoodType;
import cellsociety.model.util.constants.GridTypes.ShapeType;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.ResourceBundle;

/**
 * Abstract class for general grid.
 *
 * <p> Grid configures cells to the given pattern and sets the neighbors for the cells, the purpose
 * is to abstract away creation of neighbors for a given cell
 *
 * <p> Reference for generics: https://www.geeksforgeeks.org/generics-in-java/
 *
 * @param <T> - the type of cell in the grid, must extend Cell
 * @author Jessica Chen
 */
public abstract class Grid<T extends Cell<T, ?, ?>> {

  private final List<List<T>> myGrid;
  private int myRows;
  private int myCols;

  private final ResourceBundle myResources;

  /**
   * Constructs a Grid with specified dimensions.
   *
   * @param cells - cells to be added
   * @param rows  - number of rows in the grid
   * @param cols  - number of columns in the grid
   */
  public Grid(List<T> cells, int rows, int cols) {
    myResources = getErrorSimulationResourceBundle("English");

    myGrid = initializeGrid(rows, cols);
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
    myResources = getErrorSimulationResourceBundle(language);

    myGrid = initializeGrid(rows, cols);
    initializeCells(cells);
  }

  private List<List<T>> initializeGrid(int rows, int cols) {
    final List<List<T>> grid;
    if (rows <= 0 || cols <= 0) {
      throw new SimulationException(myResources.getString("InvalidGridDimensions"));
    }

    myRows = rows;
    myCols = cols;
    grid = new ArrayList<>();
    return grid;
  }

  /**
   * Set neighbors for all cells in the grid.
   */
  public abstract void setNeighbors();

  /**
   * Sets the neighbors for each cell in the grid based on the specified shape, neighborhood type,
   * and edge type.
   *
   * @param shape        - The shape type of the cells in the grid (e.g., RECTANGLE, HEXAGON,
   *                     TRIANGLE).
   * @param neighborhood - The neighborhood type defining how neighbors are determined (e.g., MOORE,
   *                     VON_NEUMANN, EXTENDED_MOORE).
   * @param edge         - The edge type specifying the behavior at the boundaries of the grid
   *                     (e.g., NONE, MIRROR, TOROIDAL).
   */
  public void setNeighbors(ShapeType shape, NeighborhoodType neighborhood, EdgeType edge) {
    List<List<T>> grid = getGrid();
    int rows = getRows();
    int cols = getCols();

    Map<String, int[][]> directionsMap = GridFactory.getDirections(shape, neighborhood);

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        T cell = grid.get(i).get(j);
        if (cell != null) {
          if (i % 2 == 0) {
            cell.setNeighbors(getValidNeighbors(grid, i, j, directionsMap.get("even"), edge));
          } else {
            cell.setNeighbors(getValidNeighbors(grid, i, j, directionsMap.get("odd"), edge));
          }
        }
      }
    }
  }

  /**
   * Helper function to set the neighbors for a given shape in a specified neighborhood.
   *
   * <p>This method iterates over each cell in the given shape and sets its neighboring cells
   * according to the specified directions provided by the neighborhood. It abstracts the
   * neighbor-setting logic for consistent and reusable functionality.
   *
   * @param shape        - The structure or grid representing the cells (e.g., a 2D array or other
   *                     collection of cells) for which neighbors are to be set.
   * @param neighborhood - An object or collection representing the directions or rules that specify
   *                     how neighbors should be assigned (e.g., relative coordinates, adjacency
   *                     lists).
   */
  public void setNeighbors(ShapeType shape, NeighborhoodType neighborhood) {
    setNeighbors(shape, neighborhood, NONE);
  }

  private List<T> getValidNeighbors(List<List<T>> grid, int row, int col, int[][] directions,
      EdgeType edge) {
    EdgeHandler edgeHandler = EdgeFactory.getHandler(edge);

    List<T> neighbors = new ArrayList<>();
    for (int[] dir : directions) {
      int newRow = row + dir[0];
      int newCol = col + dir[1];
      if (isValidPosition(newRow, newCol)) {
        neighbors.add(grid.get(newRow).get(newCol));
      } else {
        Optional<List<Integer>> replacementCell = edgeHandler.handleEdgeNeighbor(row, col, myRows,
            myCols, dir);
        replacementCell.ifPresent(
            integers -> neighbors.add(grid.get(integers.get(0)).get(integers.get(1))));
      }
    }
    return neighbors;
  }


  /**
   * Initialize the grid with correct cells.
   *
   * @param cells - cells to be added
   */
  public void initializeCells(List<T> cells) {
    validateCells(cells);
    myGrid.clear();
    fillGridWithCells(cells);
  }

  private void validateCells(List<T> cells) {
    if (cells == null) {
      throw new SimulationException(myResources.getString("NullCellsList"));
    }
    if (cells.size() != myRows * myCols) {
      throw new SimulationException(
          String.format(myResources.getString("MismatchedCellCount"), cells.size(),
              myRows * myCols));
    }
  }

  private void fillGridWithCells(List<T> cells) {
    int index = 0;
    for (int i = 0; i < myRows; i++) {
      myGrid.add(createRow(cells, i, index));
      index += myCols;
    }
  }

  private List<T> createRow(List<T> cells, int rowIndex, int startIndex) {
    List<T> row = new ArrayList<>();
    int endIndex = Math.min(startIndex + myCols, cells.size());
    for (int j = startIndex; j < endIndex; j++) {
      T cell = cells.get(j);
      cell.setPosition(new int[]{(j - startIndex), rowIndex});
      row.add(cell);
    }
    return row;
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
      throw new SimulationException(
          String.format(myResources.getString("InvalidGridPosition"), row, col));
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
      throw new SimulationException(
          String.format(myResources.getString("InvalidGridPosition"), row, col));
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
   * Get all cells in the grid as a flattened list.
   *
   * @return A list of all cells in the grid as a flattened grid
   */
  public List<T> getCells() {
    return myGrid.stream().flatMap(List::stream).toList();
  }

  /**
   * return resource bundle associated for exceptions.
   *
   * @return resource bundle associated for exception
   */
  public ResourceBundle getResources() {
    return myResources;
  }
}
