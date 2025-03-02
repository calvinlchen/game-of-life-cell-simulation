package cellsociety.model.simulation.grid.edgehandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * The {@code ToroidalEdgeHandler} class implements the {@link EdgeHandler} interface, providing
 * functionality for handling grid boundary cases using a toroidal wrapping approach.
 *
 * <p>In this approach, the grid behaves as if its edges are seamlessly connected,
 * forming a continuous surface like that of a torus. When a neighboring cell is requested beyond
 * the boundaries of the grid, the handler wraps around to the opposite edge of the grid to
 * determine the neighbor's position.</p>
 *
 * <p><b>Toroidal Wrapping Logic:</b></p>
 * <ul>
 *   <li>Going above the top row wraps to the bottom row.</li>
 *   <li>Going below the bottom row wraps to the top row.</li>
 *   <li>Going left beyond the first column wraps to the last column.</li>
 *   <li>Going right beyond the last column wraps to the first column.</li>
 * </ul>
 *
 * <p><b>Example:</b></p>
 * <pre>
 * ToroidalEdgeHandler handler = new ToroidalEdgeHandler();
 * Optional<List<Integer>> wrapped = handler.handleEdgeNeighbor(0, 4, 5, 5, new int[]{-1, 0});
 * wrapped.ifPresent(pos -> System.out.println("Wrapped position: " + pos));
 * // Output: Wrapped position: [4, 4] (since row 0 wraps to row 4)
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT - helped with JavaDoc
 */
public class ToroidalEdgeHandler implements EdgeHandler {

  private static final Logger logger = LogManager.getLogger(ToroidalEdgeHandler.class);

  @Override
  public Optional<List<Integer>> handleEdgeNeighbor(int currRow, int curCol, int totalRow,
      int totalCol, int[] dir) {

    logger.debug("Handling toroidal edge for ({}, {}) with direction [{}, {}] in grid size {}x{}",
        currRow, curCol, dir[0], dir[1], totalRow, totalCol);

    int newRow = currRow + dir[0];
    int newCol = curCol + dir[1];

    if (newRow < 0) {
      logger.debug("Row out of bounds (top), wrapping from {} to {}", newRow, totalRow + newRow);
      newRow = totalRow + newRow;
    } else if (newRow >= totalRow) {
      logger.debug("Row out of bounds (bottom), wrapping from {} to {}", newRow, newRow - totalRow);
      newRow = newRow - totalRow;
    }

    if (newCol < 0) {
      logger.debug("Column out of bounds (left), wrapping from {} to {}", newCol,
          totalCol + newCol);
      newCol = totalCol + newCol;
    } else if (newCol >= totalCol) {
      logger.debug("Column out of bounds (right), wrapping from {} to {}", newCol,
          newCol - totalCol);
      newCol = newCol - totalCol;
    }

    logger.debug("Final wrapped position: [{}, {}]", newRow, newCol);
    return Optional.of(List.of(newRow, newCol));
  }
}
