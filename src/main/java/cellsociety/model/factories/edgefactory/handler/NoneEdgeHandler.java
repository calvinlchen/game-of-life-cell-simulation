package cellsociety.model.factories.edgefactory.handler;

import java.util.List;
import java.util.Optional;

/**
 * The NoneEdgeHandler class implements the EdgeHandler interface and provides functionality for
 * handling grid boundary cases where no edge behavior is applied. When a neighboring cell is
 * requested beyond the boundaries of the grid, this handler always returns an empty Optional,
 * indicating that the requested neighbor is invalid and does not exist.
 * <p>
 * This class is useful in simulations or grid-based computations where boundary cells are treated
 * as inaccessible, effectively ignoring any out-of-bound neighbors.
 *
 * @author Jessica Chen
 * @author ChatGPT- helped with the java doc
 */
public class NoneEdgeHandler implements EdgeHandler {

  @Override
  public Optional<List<Integer>> handleEdgeNeighbor(int currRow, int curCol, int totalRow,
      int totalCol, int[] dir) {
    // don't care if it invalid its just invalid
    return Optional.empty();
  }

}
