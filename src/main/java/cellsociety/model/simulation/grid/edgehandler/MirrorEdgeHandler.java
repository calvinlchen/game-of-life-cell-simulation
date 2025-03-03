package cellsociety.model.simulation.grid.edgehandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * The {@code MirrorEdgeHandler} class implements the {@link EdgeHandler} interface and provides
 * functionality for handling grid boundary cases using a "mirroring" approach.
 *
 * <p>When a neighboring cell is requested beyond the boundaries of the grid, this class reflects
 * the position back into the grid, ensuring boundary-adjacent cells mirror positions across the
 * edge.</p>
 *
 * <p><b>Reflection Logic:</b></p>
 * <ul>
 *   <li>If a neighbor is out-of-bounds above the grid, it mirrors downward.</li>
 *   <li>If a neighbor is out-of-bounds below the grid, it mirrors upward.</li>
 *   <li>If a neighbor is out-of-bounds to the left, it mirrors to the right.</li>
 *   <li>If a neighbor is out-of-bounds to the right, it mirrors to the left.</li>
 * </ul>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * MirrorEdgeHandler handler = new MirrorEdgeHandler();
 * Optional&lt;List&lt;Integer&gt;&gt; mirrored =
 * handler.handleEdgeNeighbor(-1, 2, 5, 5, new int[]{-1, 0});
 * mirrored.ifPresent(pos -> System.out.println("Mirrored position: " + pos));
 * // Output: Mirrored position: [0,2] (since -1 reflects to 0)
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with java document
 */
public class MirrorEdgeHandler implements EdgeHandler {

  private static final Logger logger = LogManager.getLogger(MirrorEdgeHandler.class);

  @Override
  public Optional<List<Integer>> handleEdgeNeighbor(int currRow, int curCol, int totalRow,
      int totalCol, int[] dir) {

    // debuggers to help with making sure this math is right
    logger.debug("Handling edge neighbor for ({}, {}) with direction [{}, {}] in grid size {}x{}",
        currRow, curCol, dir[0], dir[1], totalRow, totalCol);

    int newRow = currRow + dir[0];
    int newCol = curCol + dir[1];

    if (newRow < 0) {
      logger.debug("Row out of bounds (top), mirroring from {} to {}", newRow, -newRow - 1);
      newRow = -newRow - 1;
    } else if (newRow >= totalRow) {
      logger.debug("Row out of bounds (bottom), mirroring from {} to {}", newRow,
          totalRow - 1 - (newRow - totalRow));
      newRow = totalRow - 1 - (newRow - totalRow);
    }

    if (newCol < 0) {
      logger.debug("Column out of bounds (left), mirroring from {} to {}", newCol, -newCol - 1);
      newCol = -newCol - 1;
    } else if (newCol >= totalCol) {
      logger.debug("Column out of bounds (right), mirroring from {} to {}", newCol,
          totalCol - 1 - (newCol - totalCol));
      newCol = totalCol - 1 - (newCol - totalCol);
    }

    logger.debug("Final mirrored position: [{}, {}]", newRow, newCol);
    return Optional.of(List.of(newRow, newCol));
  }
}
