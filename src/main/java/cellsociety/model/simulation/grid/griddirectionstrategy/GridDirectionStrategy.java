package cellsociety.model.simulation.grid.griddirectionstrategy;


/**
 * The {@code GridDirectionStrategy} interface defines a contract for retrieving neighbor directions
 * based on a given row index.
 *
 * <p>This strategy is useful for handling different grid structures where neighbor directions
 * may depend on row parity (e.g., in hexagonal or triangular grids).</p>
 *
 * <p><b>Design Pattern:</b> This interface follows the <b>Strategy Pattern</b>, allowing different
 * grid structures to define their own neighbor rules without modifying existing code.</p>
 *
 * <p><b>Intended Use:</b></p>
 * <ul>
 *     <li>For rectangular grids, the neighbor directions remain the same
 *     regardless of row index.</li>
 *     <li>For hexagonal and triangular grids, neighbor directions may vary depending on whether
 *     the row is even or odd.</li>
 * </ul>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * GridDirectionStrategy strategy = new EvenOddParityGridDirectionStrategy();
 * int[][] directions = strategy.getDirections(2);  // Retrieves neighbor directions for row index 2
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public interface GridDirectionStrategy {

  /**
   * Retrieves the neighbor directions for a given row index.
   *
   * <p><b>Intended Use:</b> This method allows different grid structures to return appropriate
   * neighbor directions based on their row index. For some grids (e.g., hexagons and triangles),
   * neighbor rules may differ for even and odd rows.</p>
   *
   * <p><b>Example Usage:</b></p>
   * <pre>
   * int[][] directions = strategy.getDirections(3);
   * </pre>
   *
   * @param row The row index of the grid for which neighbor directions are requested.
   * @param col The col index of the grid for which neighbor directions are requested.
   * @return A 2D array representing neighbor offsets for the specified row or column.
   */
  int[][] getDirections(int row, int col);
}