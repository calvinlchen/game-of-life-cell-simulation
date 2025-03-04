package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code Rule} class defines the logic governing cell state transitions in a simulation.
 *
 * <p>Each simulation implements its own rule subclass, defining how cell states evolve based
 * on their neighbors and associated parameters.</p>
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Apply simulation rules to compute the next state of a cell.</li>
 *   <li>Store and manage rule parameters using {@link GenericParameters}.</li>
 *   <li>Provide shared helper methods for the rule subclasses.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * GenericParameters params = new GenericParameters(SimType.GameOfLife);
 * Rule&lt;GameOfLifeCell&gt; rule = new GameOfLifeRule(params);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * @param <C> The type of cell, must extend {@link Cell}.
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public abstract class Rule<C extends Cell<C, ?>> {

  private static final Logger logger = LogManager.getLogger(Rule.class);
  private final GenericParameters parameters;

  /**
   * Constructs a {@code Rule} object and initializes it with the provided parameters. The
   * parameters are checked and set to ensure validity.
   *
   * @param parameters the {@code GenericParameters} object containing the configuration and
   *                   settings required for the rule. Must not be {@code null}.
   * @throws SimulationException if the parameters are {@code null}.
   */
  public Rule(GenericParameters parameters) {
    if (parameters == null) {
      logger.warn("Rule initialization failed: Parameters cannot be null.");
      throw new SimulationException("NullParameter",
          List.of("parameters", "Rule"));
    }
    this.parameters = parameters;
  }

  // Abstract Methods ------

  /**
   * Apply the rules to determine the next state of a cell.
   *
   * @param cell - cell to apply the rules to
   * @return next state of the cell
   */
  public abstract int apply(C cell);

  // Start of Rules setters and getters ------

  /**
   * Retrieves the current configuration and settings associated with this rule.
   *
   * <p>Will never be null since cannot initialize rule with null parameters.</p>
   *
   * @return a {@code GenericParameters} object containing the parameters for this rule.
   */
  public GenericParameters getParameters() {
    try {
      return parameters;
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  // Start of Shared Helper methods for rules ------

  /**
   * Generates a state key for a given cell by concatenating the current state of the cell and the
   * states of its neighboring cells in the specified directions.
   *
   * <p>This method is useful for rules that require encoding neighbor states into a single
   * key, such as for Langton's Loops.</p>
   *
   * <p><b>Example Output:</b> If a cell's state is 1 and its neighbors have states in the
   * order [2,3,0], the generated key would be {@code "1230"}.</p>
   *
   * <p><b>Example Usage:</b></p>
   * <pre>
   * String stateKey =
   * rule.getStateKey(cell, new DirectionType[]{DirectionType.N, DirectionType.E});
   * </pre>
   *
   * @param cell       the cell whose state and neighbors' states are used to generate the state
   *                   key; must not be null
   * @param directions an array of {@code DirectionType} values representing the directions in which
   *                   to look for neighbors; must not be null
   * @return a {@code String} representation of the state key consisting of the cell's state and its
   * neighbors' states in the specified directions
   * @throws SimulationException if an error occurs while retrieving the state or neighbors of the
   *                             cell
   */
  String getStateKey(C cell, DirectionType[] directions) {
    try {
      if (cell == null) {
        logger.error("Direction match failed: Null cell.");
        throw new SimulationException("NullParameter",
            List.of("cell", "matchesDirection()"));
      }

      StringBuilder stateBuilder = new StringBuilder();

      stateBuilder.append(cell.getCurrentState());
      for (DirectionType dir : directions) {
        cell.getNeighbors().stream().filter(neighbor -> matchesDirection(cell, neighbor, dir))
            .findFirst()
            .ifPresentOrElse(
                neighbor -> stateBuilder.append(neighbor.getCurrentState()),
                () -> logger.warn("No neighbor found in direction {}.", dir));
      }

      return stateBuilder.toString();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Checks if a given cell has a specified neighbor in the given direction.
   *
   * <p>This method verifies whether the provided neighbor exists among the directional neighbors
   * of the specified cell in the given direction.
   *
   * @param cell      the cell for which the check is performed; must not be null
   * @param neighbor  the neighbor cell to check for; must not be null
   * @param direction the direction in which the neighbor is expected to be located
   * @return true if the given neighbor exists in the directional neighbors of the cell in the
   * specified direction; false otherwise
   * @throws SimulationException if the input cell or neighbor is null
   */
  boolean matchesDirection(C cell, C neighbor, DirectionType direction) {
    if (neighbor == null) {
      // should never be able to hit
      logger.error("Direction match failed: Null neighbor.");
      throw new SimulationException("NullParameter",
          List.of("neighbor", "matchesDirection()"));
    }

    try {
      // should not be able to hit since directionalNeighbors shoould never be null
      return cell.getDirectionalNeighbors(direction).stream()
          .anyMatch(neighbor1 -> neighbor1.equals(neighbor));
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Rotates an array of {@code DirectionType} elements clockwise.
   *
   * <p>The main use for this one is to rotate the directional elements for Langton's Loops
   * related rules.</p>
   *
   * @param directions an array of {@code DirectionType} representing the original directions. Must
   *                   contain exactly four elements.
   * @return a new array of {@code DirectionType} representing the rotated directions, where the
   * original elements are shifted one position in a clockwise order.
   */
  DirectionType[] rotateClockwise(DirectionType[] directions) {
    return new DirectionType[]{directions[3], directions[0], directions[1], directions[2]};
  }

}
