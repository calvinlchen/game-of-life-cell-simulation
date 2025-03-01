/**
 * Provides the core simulation functionality for the Cell Society application.
 *
 * <p>This package contains the {@link cellsociety.model.simulation.Simulation} class, which serves
 * as the main service for the view client. The Simulation class encapsulates all simulation logic,
 * including:
 * <ul>
 *   <li>Managing simulation state and progression</li>
 *   <li>Interacting with simulation rules and parameters</li>
 *   <li>Handling grid topology and cell interactions</li>
 *   <li>Providing read-only metadata access for visualization</li>
 * </ul>
 *
 * <p><b>Design Goals:</b>
 * <ul>
 *   <li>Ensure modularity by encapsulating grid, rules, and parameter logic.</li>
 *   <li>Support extensibility for new simulation types.</li>
 *   <li>Maintain a clear API for communication between the model and view layers.</li>
 *   <li>Provide a robust exception-handling system to prevent invalid operations.</li>
 * </ul>
 *
 * <p><b>Contracts:</b>
 * <ul>
 *   <li>The view should only interact with {@link cellsociety.model.simulation.Simulation}.</li>
 *   <li>Simulation state should be updated through defined API methods.</li>
 *   <li>Parameter modifications should be controlled and validated.</li>
 *   <li>Grid topology can be dynamically updated.</li>
 * </ul>
 *
 * @author Jessica Chen
 */
package cellsociety.model.simulation;