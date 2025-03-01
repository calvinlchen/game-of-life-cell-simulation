package cellsociety.model.factories.edgefactory.handler;

import java.util.List;
import java.util.Optional;

/**
 * EdgeHandler defines a contract for handling edge cases in grid-based simulations. Implementations
 * of this interface manage how neighboring cells are determined when a given cell is located at the
 * boundary of the grid.
 * <p>
 * The interface is typically utilized in grid computations to handle edge conditions such as
 * boundaries where wrapping, reflection, or no edge behavior might be desired.
 *
 * @author Jessica Chen
 * @author ChatGPT helped with java docs
 */
public interface EdgeHandler {

  /**
   * Determines the adjusted position of a neighboring cell when the current cell is at the boundary
   * of the grid. The method accounts for different edge handling behaviors such as none, toroidal
   * wrapping, or mirroring, based on the specific implementation of the EdgeHandler interface.
   *
   * @param currRow  the row index of the current cell
   * @param curCol   the column index of the current cell
   * @param totalRow the total number of rows in the grid
   * @param totalCol the total number of columns in the grid
   * @param dir      the directional offset indicating the neighboring cell to be accessed,
   *                 expressed as an array [rowOffset, colOffset]
   * @return an Optional containing a list of two integers [neighborRow, neighborCol] representing
   * the adjusted position of the neighboring cell. If the neighbor cannot be resolved (e.g., for a
   * "none" edge type), an empty Optional is returned.
   */
  Optional<List<Integer>> handleEdgeNeighbor(int currRow, int curCol, int totalRow, int totalCol,
      int[] dir);

}
