package cellsociety.model.simulation.grid.edgehandler;

import java.util.List;
import java.util.Optional;

/**
 * EdgeHandler defines a contract for managing grid edge conditions in simulations.
 *
 * <p>Implementations of this interface determine how neighbors are handled at grid boundaries.
 * Different strategies include:
 * <ul>
 *     <li><b>NONE</b> - Out-of-bounds neighbors are ignored (returns empty Optional).</li>
 *     <li><b>TOROIDAL</b> - Grid wraps around edges, connecting opposite borders.</li>
 *     <li><b>REFLECTIVE</b> - Edge neighbors mirror positions within the grid.</li>
 * </ul>
 *
 * <p>This interface is used in grid-based computations to ensure consistent neighbor access.
 * Implementations should define how edge cases are handled.</p>
 *
 * <p><b>Contract:</b> Implementations must:
 * <ul>
 *     <li>Correctly compute valid neighbor positions based on edge rules.</li>
 *     <li>Return an empty {@link Optional} when no valid neighbor exists.</li>
 *     <li>Ensure thread safety if used in concurrent simulations.</li>
 * </ul>
 * </p>
 *
 * <p><b>Example Usage:</b>
 * <pre>
 * EdgeHandler handler = new ToroidalEdgeHandler();
 * Optional<List<Integer>> neighbor = handler.handleEdgeNeighbor(0, 0, 10, 10, new int[]{-1, 0});
 * neighbor.ifPresent(pos -> System.out.println("Wrapped neighbor at: " + pos));
 * </pre>
 * </p>
 *
 * @author Jessica Chen
 */
public interface EdgeHandler {

  /**
   * Determines the adjusted position of a neighboring cell when the current cell is at the boundary
   * of the grid. The method accounts for different edge handling behaviors such as none, toroidal
   * wrapping, or mirroring, based on the specific implementation of the EdgeHandler interface.
   *
   * <p>Handles edge cases based on the specific implementation (e.g., toroidal, reflective, none).
   * If the neighbor is out of bounds and cannot be resolved, returns an empty {@link Optional}.
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
