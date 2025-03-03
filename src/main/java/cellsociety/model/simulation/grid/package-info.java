/**
 * The {@code cellsociety.model.simulation.grid} package provides the core infrastructure for
 * managing a grid-based simulation environment. Its main goal is to abstract the details of
 * neighbor handling for cells and the underlining grid structure.
 *
 * <p>This package is responsible for handling:
 * <ul>
 *   <li>Grid creation and management via {@link cellsociety.model.simulation.grid.Grid}.</li>
 *   <li>Neighbor determination based on grid shape and neighborhood type via
 *       {@link cellsociety.model.simulation.grid.GridDirectionRegistry}.</li>
 *   <li>Boundary handling through {@link cellsociety.model.simulation.grid.EdgeFactory}
 *       and its associated {@link cellsociety.model.simulation.grid.edgehandler.EdgeHandler}
 *       implementations.</li>
 *   <li>Mapping direction types from {@link
 *   cellsociety.model.simulation.grid.DirectionRegistry}.</li>
 * </ul>
 *
 * <h2>Package Overview</h2>
 * <p>This package abstracts grid logic while allowing flexibility in grid shapes,
 * neighbor connections, and edge handling. It integrates several design patterns:
 * <ul>
 *   <li><b>Factory Pattern</b>:
 *     <ul>
 *       <li>{@link cellsociety.model.simulation.grid.EdgeFactory}
 *       - Provides different {@link cellsociety.model.simulation.grid.edgehandler.EdgeHandler}
 *       implementations(e.g., Mirror, Toroidal).</li>
 *     </ul>
 *   </li>
 *   <li><b>Strategy Pattern</b>:
 *     <ul>
 *       <li>{@link cellsociety.model.simulation.grid.GridDirectionRegistry}
 *       - Uses {@link
 *       cellsociety.model.simulation.grid.griddirectionstrategy.GridDirectionStrategy}
 *       implementations to determine neighbor directions for different grid topology.</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <pre>
 * List&lt;Cell> cells = ...;  // Initialize list of cells
 * Grid&lt;Cell> grid = new Grid(cells, 10, 10, ShapeType.RECTANGLE,
 * NeighborhoodType.MOORE, EdgeType.NONE);
 * Cell cell = grid.getCell(3, 5);
 * List&lt;Cell> neighbors = grid.getNeighbors(new int[]{3, 5});
 * </pre>
 *
 * <h2>Key Classes</h2>
 * <ul>
 *   <li>{@link cellsociety.model.simulation.grid.Grid}
 *   - Main public-facing class for grid creation and management.</li>
 *   <li>{@link cellsociety.model.simulation.grid.GridDirectionRegistry}
 *   - Determines neighbor directions based on grid type.</li>
 *   <li>{@link cellsociety.model.simulation.grid.EdgeFactory}
 *   - Retrieves appropriate {@link cellsociety.model.simulation.grid.edgehandler.EdgeHandler}
 *   based on edge behavior.</li>
 *   <li>{@link cellsociety.model.simulation.grid.DirectionRegistry}
 *   - Maps original Cartesian coordinate offsets to {@link cellsociety.model.util.constants.GridTypes.DirectionType}.</li>
 * </ul>
 *
 * <h2>Edge Handling</h2>
 * <p>Different edge behaviors can be configured using {@link cellsociety.model.simulation.grid.EdgeFactory}:</p>
 * <ul>
 *   <li>{@code NONE} - No special edge handling, cells at the boundary have fewer neighbors.</li>
 *   <li>{@code MIRROR} - Boundary cells reflect their positions for neighbors.</li>
 *   <li>{@code TOROIDAL} - Grid wraps around, allowing seamless neighbor connections.</li>
 * </ul>
 *
 * <p><b>Note:</b> The grid itself does not store the neighbors. Each cell holds its own neighbor.
 * <p><b>Note:</b> The term position is more a formality, can also consider it a unique id for
 * each cell.
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
package cellsociety.model.simulation.grid;