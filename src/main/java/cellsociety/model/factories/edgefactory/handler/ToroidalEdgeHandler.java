package cellsociety.model.factories.edgefactory.handler;

import java.util.List;
import java.util.Optional;

/**
 * The ToroidalEdgeHandler class implements the EdgeHandler interface, providing functionality for
 * handling grid boundary cases using a toroidal wrapping approach. In this approach, the grid is
 * treated as if its edges are connected, forming a continuous surface like that of a torus. When a
 * neighboring cell is requested beyond the boundaries of the grid, the handler wraps around to the
 * opposite edge of the grid to determine the neighbor's position.
 * <p>
 * This approach is useful in grid-based simulations or systems (e.g., cellular automata, games)
 * where seamless continuity is required across the grid edges.
 *
 * @author Jessica Chen
 * @author ChatGPT - helped with javadoc
 */
public class ToroidalEdgeHandler implements EdgeHandler {

  // TODO: once verify math works, fix this to be nicer
  @Override
  public Optional<List<Integer>> handleEdgeNeighbor(int currRow, int curCol, int totalRow,
      int totalCol, int[] dir) {
    // this is the one that threw the error
    int newRow = currRow + dir[0];
    int newCol = curCol + dir[1];

    return Optional.of(List.of(newRow, newCol));
  }
}