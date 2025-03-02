package cellsociety.model.simulation.grid.griddirectionstrategy;

import java.util.Map;

/**
 * The {@code EvenOddParityGridDirectionStrategy} class implements the {@link GridDirectionStrategy}
 * interface for grids where movement directions depend on whether the row index is even or odd.
 *
 * <p>This strategy is used for **hexagonal and triangular grids**, where the structure causes
 * movement patterns to alternate between even and odd rows.</p>
 *
 * <p><b>Design Pattern:</b> Implements the <b>Strategy Pattern</b>, providing a flexible way to
 * apply different movement rules based on row parity.</p>
 *
 * <p>This is good for future extensibility (aka if later on want to make dynamic strategies for
 * how to handle neighbors on different rows beyond just even and odd)
 *
 * <p><b>Intended Use:</b></p>
 * <ul>
 *     <li>For grids where **movement rules differ between even and odd rows**.</li>
 *     <li>Commonly used for **hexagonal** and **triangular** grid structures.</li>
 * </ul>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * Map&lt;Boolean, int[][]&gt; parityMap = Map.of(
 *     true, new int[][] { {-1, 0}, {1, 0}, {0, -1}, {0, 1} },  // Even row directions
 *     false, new int[][] { {-1, -1}, {1, 1}, {0, -1}, {0, 1} } // Odd row directions
 * );
 * GridDirectionStrategy strategy = new EvenOddParityGridDirectionStrategy(parityMap);
 * int[][] evenRowDirections = strategy.getDirections(2);  // Gets "even" row movement
 * int[][] oddRowDirections = strategy.getDirections(3);   // Gets "odd" row movement
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class EvenOddParityGridDirectionStrategy implements GridDirectionStrategy {

  private final Map<Boolean, int[][]> parityDirections;

  /**
   * Constructs an {@code EvenOddParityGridDirectionStrategy} with movement rules for even and
   * odd rows.
   *
   * @param parityDirections A map where {@code true} represents directions for even rows and
   *                         {@code false} represents directions for odd rows.
   */
  public EvenOddParityGridDirectionStrategy(Map<Boolean, int[][]> parityDirections) {
    this.parityDirections = parityDirections;
  }

  @Override
  public int[][] getDirections(int row) {
    return parityDirections.get(row % 2 == 0);
  }
}
