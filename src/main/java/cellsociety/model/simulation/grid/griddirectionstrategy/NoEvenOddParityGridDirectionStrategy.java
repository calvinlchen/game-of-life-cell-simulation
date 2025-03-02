package cellsociety.model.simulation.grid.griddirectionstrategy;

/**
 * The {@code NoEvenOddParityGridDirectionStrategy} class implements the
 * {@link GridDirectionStrategy} interface for grids where movement directions are independent of
 * row parity.
 *
 * <p>This strategy is typically used for **rectangular grids**, where movement rules remain the
 * same regardless of whether the row index is even or odd.</p>
 *
 * <p><b>Design Pattern:</b> Implements the <b>Strategy Pattern</b>, allowing a flexible and
 * extensible approach to handling different grid structures returns.</p>
 *
 * <p><b>Intended Use:</b></p>
 * <ul>
 *     <li>For grids that do **not** require different movement rules for even vs. odd rows.</li>
 *     <li>Ideal for rectangular grids using **Moore**, **Von Neumann**,
 *     or **Extended Moore** neighborhoods.</li>
 * </ul>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
 * GridDirectionStrategy strategy = new NoEvenOddParityGridDirectionStrategy(directions);
 * int[][] result = strategy.getDirections(2);  // Will return the same directions for any row
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class NoEvenOddParityGridDirectionStrategy implements GridDirectionStrategy {

  private final int[][] directions;

  /**
   * Constructs a {@code NoEvenOddParityGridDirectionStrategy} with predefined movement directions.
   *
   * @param predefinedDirections A 2D array representing the movement offsets for the grid.
   */
  public NoEvenOddParityGridDirectionStrategy(int[][] predefinedDirections) {
    this.directions = predefinedDirections;
  }

  /**
   * Retrieves the movement directions for the given row index.
   *
   * <p>Since this strategy does not consider row parity, the same set of directions is returned
   * for any row index.</p>
   *
   * <p><b>Example Usage:</b></p>
   * <pre>
   * int[][] directions = strategy.getDirections(3);
   * </pre>
   *
   * @param row The row index (ignored in this implementation).
   * @return A 2D array representing movement offsets.
   */
  @Override
  public int[][] getDirections(int row) {
    return directions;
  }
}
