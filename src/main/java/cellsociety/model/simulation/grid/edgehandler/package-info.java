/**
 * Provides implementations for handling edge cases in grid-based simulations.
 *
 * <p>This package defines different strategies for determining neighbors when a cell is located
 * at the boundary of the grid. This API mainly provides different edge handling strategies for
 * handleEdgeNeighbor(). EdgeFactory handles which of these handlers are chosen, and it is used
 * in Grid to determine topology
 *
 * <p><b>Edge Handling Strategies:</b></p>
 * <ul>
 *   <li>{@link cellsociety.model.simulation.grid.edgehandler.NoneEdgeHandler}
 *   - Ignores out-of-bounds neighbors.</li>
 *   <li>{@link cellsociety.model.simulation.grid.edgehandler.MirrorEdgeHandler}
 *   - Reflects neighbors symmetrically across edges.</li>
 *   <li>{@link cellsociety.model.simulation.grid.edgehandler.ToroidalEdgeHandler}
 *   - Wraps neighbors around grid edges (toroidal behavior).</li>
 * </ul>
 *
 * <p><b>Design Goals & Contracts:</b></p>
 * <ul>
 *   <li>Encapsulate edge handling logic separately from grid computations.</li>
 *   <li>Ensure that all implementations conform to the
 *   {@link cellsociety.model.simulation.grid.edgehandler.EdgeHandler} interface.</li>
 *   <li>Provide flexible edge handling mechanisms for various simulation needs.</li>
 *   <li>Guarantee that out-of-bounds behavior is handled consistently.</li>
 * </ul>
 *
 * @author Jessica Chen
 */
package cellsociety.model.simulation.grid.edgehandler;