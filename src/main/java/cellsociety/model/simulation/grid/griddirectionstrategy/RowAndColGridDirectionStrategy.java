package cellsociety.model.simulation.grid.griddirectionstrategy;

import java.util.Map;

/**
 * The {@code EvenOddParityGridDirectionStrategy} class implements the {@link GridDirectionStrategy}
 * interface for grids where neighbor directions depend on whether the row index is even or odd.
 *
 * <p>This strategy is used for **triangular grids**, where the structure causes
 * neighbor patterns to alternate between even and odd rows.</p>
 *
 * <p><b>Design Pattern:</b> Implements the <b>Strategy Pattern</b>, providing a flexible way to
 * apply different neighbor rules based on row parity.</p>
 *
 * <p>This is good for future extensibility (aka if later on want to make dynamic strategies for
 * how to handle neighbors on different rows beyond just even and odd)
 *
 * <p><b>Intended Use:</b></p>
 * <ul>
 *     <li>For grids where **neighbor rules differ between even and odd rows and columns**.</li>
 *     <li>Commonly used for **triangular** grid structures.</li>
 * </ul>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * Map&lt;String, int[][]&gt; parityMap = Map.of(
 *     "true", new int[][] { {-1, 0}, {1, 0}, {0, -1}, {0, 1} },  // Even row & col or odd row & col
 *     "false, new int[][] { {-1, -1}, {1, 1}, {0, -1}, {0, 1} } // Even row odd col or vs
 * );
 * GridDirectionStrategy strategy = new EvenOddParityGridDirectionStrategy(parityMap);
 * int[][] evenRowDirections = strategy.getDirections(2);  // Gets "even" row movement
 * int[][] oddRowDirections = strategy.getDirections(3);   // Gets "odd" row movement
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class RowAndColGridDirectionStrategy implements GridDirectionStrategy {

  private final Map<Boolean, int[][]> parityDirections;

  /**
   * Constructs an {@code EvenOddParityGridDirectionStrategy} with movement rules for even and
   * odd rows and columns.
   *
   * @param parityDirections A map where EvenOdd represents directions for even rows and
   *                         odd cols.
   */
  public RowAndColGridDirectionStrategy(Map<Boolean, int[][]> parityDirections) {
    this.parityDirections = parityDirections;
  }

  @Override
  public int[][] getDirections(int row, int col) {
    return parityDirections.get(row % 2 == col % 2);
  }
}
