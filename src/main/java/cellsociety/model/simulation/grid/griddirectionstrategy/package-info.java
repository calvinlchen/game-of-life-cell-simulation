/**
 * Provides a set of strategies for determining choosing neighbors directions in grid-based
 * simulations.
 *
 * <p>This package implements the **Strategy Pattern**, enabling flexible and extensible choosing
 * neighbors direction handling for different grid structures. It abstracts how neighbors are
 * determined based on the shape and topology of the grid, ensuring that each grid type (e.g.,
 * rectangle, hexagon, triangle) can define its own choosing neighbors logic.</p>
 *
 * <p><b>Key Components:</b></p>
 * <ul>
 *   <li>{@link cellsociety.model.simulation.grid.griddirectionstrategy.GridDirectionStrategy}
 *   - Interface defining the contract for choosing neighbors strategies.</li>
 *   <li>{@link cellsociety.model.simulation.grid.griddirectionstrategy.NoEvenOddParityGridDirectionStrategy}
 *   - Simple strategy where choosing neighbors does not depend on row parity (e.g., rectangular
 *   grids).</li>
 *   <li>{@link cellsociety.model.simulation.grid.griddirectionstrategy.EvenOddParityGridDirectionStrategy}
 *   - Strategy that assigns different choosing neighbors directions based on whether a row index
 *   is even or odd (e.g., hexagonal and triangular grids).</li>
 * </ul>
 *
 * <p><b>Design Advantages:</b></p>
 * <ul>
 *   <li><b>Modular:</b> New choosing neighbors strategies can be added without modifying
 *   existing logic.</li>
 *   <li><b>Extensible:</b> Allows for future enhancements such as dynamic or rule-based direction
 *       adjustments beyond even-odd parity.</li>
 *   <li><b>Encapsulated:</b> Grid choosing neighbors logic is decoupled from grid implementation,
 *   improving maintainability.</li>
 * </ul>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * GridDirectionStrategy strategy = new EvenOddParityGridDirectionStrategy(Map.of(
 *     true, new int[][] { {-1, 0}, {1, 0}, {0, -1}, {0, 1} },  // Even row choosing neighbors
 *     false, new int[][] { {-1, -1}, {1, 1}, {0, -1}, {0, 1} } // Odd row choosing neighbors
 * ));
 *
 * int[][] directions = strategy.getDirections(4);  // Retrieves even row directions
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
package cellsociety.model.simulation.grid.griddirectionstrategy;
