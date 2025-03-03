package cellsociety.model.simulation.grid;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.grid.edgehandler.EdgeHandler;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.constants.GridTypes.EdgeType;
import cellsociety.model.util.constants.GridTypes.NeighborhoodType;
import cellsociety.model.util.constants.GridTypes.ShapeType;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code Grid} class represents a 2D grid structure containing cells of type {@link Cell}.
 *
 * <p>This class is responsible for:
 * <ul>
 *   <li>Managing grid dimensions and cell placement.</li>
 *   <li>Setting neighbors for cells based on shape, neighborhood, and edge behavior.</li>
 *   <li>Providing access and modification methods for cells.</li>
 *   <li>Uses {@link DirectionRegistry} to determine directionality of neighbors.</li>
 *   <li>Uses {@link EdgeFactory} and {@link GridDirectionRegistry} to handle the different
 *   grid topologies.</li>
 * </ul>
 *
 * <p>The main purpose for this class is to abstract the implementation of the grid and thier
 * neighbors while providing other classes access to the cells.
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * Grid&lt;Cell> grid = new Grid(cells, 10, 10, ShapeType.RECTANGLE, NeighborhoodType.MOORE,
 * EdgeType.TOROIDAL);
 * Cell cell = grid.getCell(3, 5);
 * List&lt;Cell> neighbors = grid.getNeighbors(new int[]{3, 5});
 * </pre>
 *
 * @param <T> The type of cell used in the grid, must extend {@link Cell}.
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class Grid<T extends Cell<T, ?>> {

  private static final Logger logger = LogManager.getLogger(Grid.class);

  private final List<List<T>> myGrid;
  private int myRows;
  private int myCols;

  /**
   * Constructs a Grid with specified dimensions, shape type, neighborhood type, and edge type.
   * Initializes the grid with the given cells and sets the neighbors based on the provided
   * configuration.
   *
   * @param cells            - the list of cells to be added to the grid
   * @param rows             - the number of rows in the grid
   * @param cols             - the number of columns in the grid
   * @param shape            - the shape type of the cells in the grid (e.g., RECTANGLE, HEXAGON,
   *                         TRIANGLE)
   * @param neighborhoodType - the neighborhood type defining how neighbors are determined (e.g.,
   *                         MOORE, VON_NEUMANN, EXTENDED_MOORE)
   * @param edgeType         - the edge type specifying the behavior at the boundaries of the grid
   *                         (e.g., NONE, MIRROR, TOROIDAL)
   */
  public Grid(List<T> cells, int rows, int cols, ShapeType shape, NeighborhoodType neighborhoodType,
      EdgeType edgeType) {
    try {
      myGrid = initializeGrid(rows, cols);
      initializeCells(cells);
      setNeighborsAllCells(shape, neighborhoodType, edgeType);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  // Start of Initialize Grid and Cells in Grid ------

  private List<List<T>> initializeGrid(int rows, int cols) {
    final List<List<T>> grid;
    if (rows <= 0 || cols <= 0) {
      logger.error("Grid initialization failed: Invalid dimensions {}x{}", rows, cols);
      throw new SimulationException("InvalidGridDimensions",
          List.of(String.valueOf(rows), String.valueOf(cols)));
    }

    myRows = rows;
    myCols = cols;
    grid = new ArrayList<>();
    return grid;
  }

  private void initializeCells(List<T> cells) {
    try {
      validateCells(cells);
      myGrid.clear();
      fillGridWithCells(cells);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  private void validateCells(List<T> cells) {
    if (cells == null) {
      logger.error("Grid initialization failed: Cannot create grid with null cells list.");
      throw new SimulationException("NullParameter",
          List.of("cells", "Grid()"));
    }
    if (cells.size() != myRows * myCols) {
      logger.error(
          "Grid initialization failed: Mismatched number of {} cells for expected size of {}x{}.",
          cells.size(), myRows, myCols);
      throw new SimulationException("MismatchedCellCount",
          List.of(String.valueOf(cells.size()), String.valueOf(myRows * myCols)));
    }
  }

  private void fillGridWithCells(List<T> cells) {
    try {
      int index = 0;
      for (int i = 0; i < myRows; i++) {
        myGrid.add(createRow(cells, i, index));
        index += myCols;
      }
    } catch (SimulationException e) {
      // should never hit because create row should not error
      throw new SimulationException(e);
    }
  }

  private List<T> createRow(List<T> cells, int rowIndex, int startIndex) {
    try {
      List<T> row = new ArrayList<>();
      int endIndex = Math.min(startIndex + myCols, cells.size());
      for (int j = startIndex; j < endIndex; j++) {
        T cell = cells.get(j);
        cell.setPosition(new int[]{(j - startIndex), rowIndex});
        row.add(cell);
      }
      return row;
    } catch (SimulationException e) {
      // should never hit because only way is if you tried to set an invalid position
      // this shouldn't happen because of validations before
      throw new SimulationException(e);
    }
  }

  // Start of Neighborhood Calculations ------

  /**
   * Sets the neighbors for each cell in the grid based on the specified shape, neighborhood type,
   * and edge type.
   *
   * <p>Simplification: even if the shape and neighborhood remains the same, clears current
   * neighbors and just recalculates them again.
   *
   * @param shape        - The shape type of the cells in the grid (e.g., RECTANGLE, HEXAGON,
   *                     TRIANGLE).
   * @param neighborhood - The neighborhood type defining how neighbors are determined (e.g., MOORE,
   *                     VON_NEUMANN, EXTENDED_MOORE).
   * @param edge         - The edge type specifying the behavior at the boundaries of the grid
   *                     (e.g., NONE, MIRROR, TOROIDAL).
   */
  public void setNeighborsAllCells(ShapeType shape, NeighborhoodType neighborhood, EdgeType edge) {
    try {
      getCells().forEach(Cell::clearNeighbors);

      for (int i = 0; i < myRows; i++) {
        Optional<int[][]> directions = GridDirectionRegistry.getDirections(shape, neighborhood, i);

        if (directions.isEmpty()) {
          // because of enums should usually never hit this case
          logger.error("Invalid shape/neighborhood combination: {} {}", shape.name(),
              neighborhood.name());
          throw new SimulationException("InvalidGridShapeNeighborhood",
              List.of(shape.name(), neighborhood.name()));
        }

        for (int j = 0; j < myCols; j++) {
          setNeighbors(i, j, directions.get(), edge);
        }
      }
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Finds the neighbors for a specific cell in the grid based on its position, allowed directions,
   * and edge behavior, and assigns these neighbors to the cell.
   *
   * @param i          - the row index of the cell
   * @param j          - the column index of the cell
   * @param directions - a 2D array defining the
   */
  private void setNeighbors(int i, int j, int[][] directions, EdgeType edge) {
    try {
      T cell = myGrid.get(i).get(j);
      if (cell == null) {
        return;
      }

      Map<DirectionType, List<T>> neighbors = findNeighbors(i, j, directions, edge);
      cell.setDirectionalNeighbors(neighbors);
      cell.setNeighbors(neighbors.values().stream().flatMap(List::stream).toList());
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Finds the neighbors for a specific cell in the grid based on its position, allowed directions,
   * and edge behavior.
   *
   * @param i          - the row index of the target cell
   * @param j          - the column index of the target cell
   * @param directions - a 2D array defining the relative directions to check for neighbors
   * @param edge       - the edge type specifying the behavior at grid boundaries
   * @return a map containing the neighbors categorized by their direction type
   * @throws SimulationException if the provided edge type is invalid
   */
  private Map<DirectionType, List<T>> findNeighbors(int i, int j, int[][] directions,
      EdgeType edge) {
    try {
      Optional<EdgeHandler> edgeHandler = EdgeFactory.getHandler(edge);
      if (edgeHandler.isEmpty()) {
        // should never happen because enums
        logger.error("Invalid edge type: {}", edge);
        throw new SimulationException("InvalidEdgeType", List.of(edge.name()));
      }

      Map<DirectionType, List<T>> neighbors = new HashMap<>();
      for (int[] dir : directions) {
        int newRow = i + dir[0];
        int newCol = j + dir[1];

        DirectionType directionType = determineDirection(dir);
        neighbors.put(directionType, neighbors.getOrDefault(directionType, new ArrayList<>()));

        if (isValidPosition(newRow, newCol)) {
          neighbors.get(directionType).add(myGrid.get(newRow).get(newCol));
        } else {
          Optional<List<Integer>> replacementCell = edgeHandler.get()
              .handleEdgeNeighbor(i, j, myRows, myCols, dir);
          replacementCell.ifPresent(integers -> neighbors.get(directionType)
              .add(myGrid.get(integers.get(0)).get(integers.get(1))));
        }
      }
      return neighbors;
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Determines the direction type based on the provided directional vector.
   *
   * @param dir - an array of two integers representing the directional vector where dir[0] is the
   *            x-component and dir[1] is the y-component.
   * @return the corresponding {@code DirectionType} for the given directional vector.
   * @throws SimulationException if the direction vector does not map to a valid
   *                             {@code DirectionType}.
   */
  private DirectionType determineDirection(int[] dir) {
    int x = Integer.compare(dir[0], 0);
    int y = Integer.compare(dir[1], 0);

    Optional<DirectionType> direction = DirectionRegistry.getDirection(x + "," + y);
    if (direction.isPresent()) {
      return direction.get();
    }

    // should never reach this case since compare makes it always
    // a valid vector but this be just in case
    logger.error("Invalid direction vector: {} {} from dir {}", x, y, dir[0] + "," + dir[1]);
    throw new SimulationException("InvalidDirectionVector",
        List.of(String.valueOf(x), String.valueOf(y), dir[0] + "," + dir[1]));
  }

  // Start of Getters and Setters for Grid ------

  /**
   * Get neighbors of a specific cell.
   *
   * <p>Used for testing since you can do this by combining get cell with
   * get neighbors yourself, this just makes it easier to write in tests
   *
   * @param row - the row index of the cell to retrieve
   * @param col - the column index of the cell to retrieve
   * @return - list of neighboring cells
   */
  List<T> getNeighbors(int row, int col) {
    try {
      return getCell(row, col).getNeighbors();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Retrieves the cell at the specified position in the grid.
   *
   * @param row - the row index of the cell to retrieve
   * @param col - the column index of the cell to retrieve
   * @return the cell at the specified (row, col) position
   * @throws SimulationException if the specified position is invalid
   */
  public T getCell(int row, int col) {
    if (!isValidPosition(row, col)) {
      logger.error("Cannot get cell at: {} {}", row, col);
      throw new SimulationException("InvalidGridPosition",
          List.of(String.valueOf(row), String.valueOf(col),
              String.valueOf(myRows), String.valueOf(myCols)));
    }
    return myGrid.get(row).get(col);
  }

  /**
   * Get all cells in the grid as a list.
   *
   * <p>Hides implementation details ias it just returns all cells regardless of order.
   *
   * @return A list of all cells in the grid as a list.
   */
  public List<T> getCells() {
    return myGrid.stream().flatMap(List::stream).toList();
  }

  // Start of Misc ------

  /**
   * Checks if the given position specified by row and column indices is within the valid bounds of
   * the grid. A position is considered valid if it is within the range of 0 (inclusive) to the
   * total number of rows and columns (exclusive).
   *
   * @param row the row index of the position to check
   * @param col the column index of the position to check
   * @return true if the position (row, col) is within the bounds of the grid; false otherwise
   */
  boolean isValidPosition(int row, int col) {
    return row >= 0 && row < myRows && col >= 0 && col < myCols;
  }

  int getRows() {
    return myRows;
  }

  int getCols() {
    return myCols;
  }
}
