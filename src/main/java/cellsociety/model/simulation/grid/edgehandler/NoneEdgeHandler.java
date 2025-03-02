package cellsociety.model.simulation.grid.edgehandler;

import java.util.List;
import java.util.Optional;

/**
 * The {@code NoneEdgeHandler} class implements the {@link EdgeHandler} interface and provides
 * functionality for handling grid boundary cases where no edge behavior is applied.
 *
 * <p>When a neighboring cell is requested beyond the boundaries of the grid, this handler
 * always returns an empty {@link Optional}, indicating that the requested neighbor is out-of-bounds
 * and does not exist.</p>
 *
 * <p><b>Intended Use:</b></p>
 * <ul>
 *     <li>Used in simulations where edges are considered "hard boundaries".</li>
 *     <li>Out-of-bounds neighbors are ignored rather than wrapped or mirrored.</li>
 *     <li>Ensures that only valid, in-grid neighbors are considered.</li>
 * </ul>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * NoneEdgeHandler handler = new NoneEdgeHandler();
 * Optional<List<Integer>> neighbor = handler.handleEdgeNeighbor(0, 0, 5, 5, new int[]{-1, 0});
 * System.out.println(neighbor.isPresent()); // Output: false
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT- helped with the JavaDoc
 */
public class NoneEdgeHandler implements EdgeHandler {

  @Override
  public Optional<List<Integer>> handleEdgeNeighbor(int currRow, int curCol, int totalRow,
      int totalCol, int[] dir) {
    // don't care if it invalid its just invalid
    return Optional.empty();
  }

}
