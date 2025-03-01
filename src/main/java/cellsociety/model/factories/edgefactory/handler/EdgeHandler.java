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
   * Handles the edge neighbor for a specific grid cell at the boundaries of the grid. Determines
   * the appropriate neighbor for a cell depending on the edge behavior implementation and returns
   * it as a potential list of row and column indices.
   *
   * @param row - the row index of the current cell
   * @param col - the column index of the current cell
   * @param dir - the directional offset array indicating the intended neighbor's relative position
   * @return an Optional containing a list of row and column indices for the resolved neighbor, or
   * an empty Optional if no neighbor exists based on the edge handling logic
   */
  Optional<List<Integer>> handleEdgeNeighbor(int row, int col,
      int[] dir);

}
