/**
 * Provides the rule set for defining simulation logic in various cellular automata models.
 *
 * <p>This package contains the abstract {@link cellsociety.model.simulation.rules.Rule} class
 * and its concrete subclasses, each representing the unique update rules for different types of
 * simulations.</p>
 *
 * <h2>📌 API Overview</h2>
 *
 * <p>The core of this package is the {@link cellsociety.model.simulation.rules.Rule}
 * class, which defines:</p>
 * <ul>
 *   <li>A generic method {@code apply()} that computes the next state of a given cell.</li>
 *   <li>Shared helper methods for processing neighborhood-based state changes.</li>
 *   <li>Integration with {@link cellsociety.model.simulation.parameters.GenericParameters}
 *   to provide flexible and configurable rule behaviors.</li>
 * </ul>
 *
 * <p>Each simulation rule extends {@code Rule}, providing its own logic for state transitions.
 * This design enables new rules to be implemented with minimal modifications to the existing
 * framework.</p>
 *
 * <h2>🛠 Open-Closed Principle (OCP) Design</h2>
 *
 * <p>This package follows the <b>Open-Closed Principle (OCP)</b> by allowing the creation of
 * new rules without modifying existing code:</p>
 * <ul>
 *   <li><b>Extension:</b> New simulation rules can be introduced by simply subclassing
 *       {@link cellsociety.model.simulation.rules.Rule} and implementing the {@code apply()}
 *       method.</li>
 *   <li><b>Encapsulation:</b> The core rule logic remains unchanged, ensuring stability and
 *       preventing unintended side effects.</li>
 *   <li><b>Dynamic Instantiation:</b> Can use reflection to instantiate rule classes at runtime,
 *       reducing the need for manual instantiation.</li>
 * </ul>
 *
 * <h2>💡 Example Usage</h2>
 *
 * <p>Each rule follows the same instantiation pattern:</p>
 * <pre>
 * GenericParameters parameters = new GenericParameters(SimType.GameOfLife);
 * Rule&lt;GameOfLifeCell&gt; rule = new GameOfLifeRule(parameters);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * <h2>📜 Rule Implementations</h2>
 *
 * <p>Currently implemented simulation rules:</p>
 * <ul>
 *   <li>{@link cellsociety.model.simulation.rules.GameOfLifeRule}
 *   - Implements Conway’s Game of Life.</li>
 *   <li>{@link cellsociety.model.simulation.rules.FireRule}
 *   - Simulates the spread of fire.</li>
 *   <li>{@link cellsociety.model.simulation.rules.PercolationRule}
 *   - Models percolation in porous material.</li>
 *   <li>{@link cellsociety.model.simulation.rules.RockPaperScissRule}
 *   - A Rock-Paper-Scissors model.</li>
 *   <li>{@link cellsociety.model.simulation.rules.SegregationRule}
 *   - Based on Schelling’s segregation model.</li>
 *   <li>{@link cellsociety.model.simulation.rules.WaTorRule}
 *   - Simulates predator-prey dynamics.</li>
 *   <li>{@link cellsociety.model.simulation.rules.FallingSandRule}
 *   - Models granular material behavior.</li>
 *   <li>{@link cellsociety.model.simulation.rules.LangtonRule}
 *   - Represents Langton’s Loop automata.</li>
 *   <li>{@link cellsociety.model.simulation.rules.ChouReg2Rule}
 *   - A variant of Langton’s Loop.</li>
 *   <li>{@link cellsociety.model.simulation.rules.PetelkaRule}
 *   - Implements the Petelka self-replicating automaton.</li>
 * </ul>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
package cellsociety.model.simulation.rules;