package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.GAMEOFLIFE_ALIVE;
import static cellsociety.model.util.constants.CellStates.GAMEOFLIFE_DEAD;

import cellsociety.model.simulation.cell.GameOfLifeCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code GameOfLifeRule} class defines the state transition logic for Conway's Game of Life.
 *
 * <p>This rule determines whether a cell should live, die, or be born based on the number of
 * neighboring alive cells and predefined survival/birth rules.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Cells survive if they meet the conditions in the "S" (Survival) parameter list.</li>
 *   <li>Dead cells become alive if they match the values in the "B" (Birth) parameter list.</li>
 *   <li>Default rules follow standard Conway's Game of Life rules if no parameters are set.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * GameOfLifeRule rule = new GameOfLifeRule(parameters);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class GameOfLifeRule extends Rule<GameOfLifeCell> {

  private static final Logger logger = LogManager.getLogger(GameOfLifeRule.class);

  /**
   * Constructs a game rule for the Game of Life simulation using the specified parameters. This
   * constructor initializes the rule with configuration and settings provided in the
   * {@code GenericParameters} object.
   *
   * @param parameters the {@code GenericParameters} object containing the simulation configuration,
   *                   including survival and birth rules. Must not be {@code null}.
   */
  public GameOfLifeRule(GenericParameters parameters) {
    super(parameters);
  }

  /**
   * Applies the Game of Life transition rules to determine the next state of a cell.
   *
   * <p><b>Rule Logic:</b></p>
   * <ul>
   *   <li>Alive cells survive if their neighbor count is in the "S" (Survival) list.</li>
   *   <li>Dead cells become alive if their neighbor count is in the "B" (Birth) list.</li>
   *   <li>Otherwise, the cell remains in its current state.</li>
   * </ul>
   *
   * @param cell The {@code GameOfLifeCell} to evaluate.
   * @return The next state of the cell.
   */
  @Override
  public int apply(GameOfLifeCell cell) {
    try {
      long aliveNeighbors = countAliveNeighbors(cell);

      List<Integer> survive = retrieveParameterList("S");
      List<Integer> birth = retrieveParameterList("B");

      if (cell.getCurrentState() == GAMEOFLIFE_ALIVE && !survive.contains((int) aliveNeighbors)) {
        return GAMEOFLIFE_DEAD;
      } else if (cell.getCurrentState() == GAMEOFLIFE_DEAD && birth.contains(
          (int) aliveNeighbors)) {
        return GAMEOFLIFE_ALIVE;
      }

      return cell.getCurrentState();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  private long countAliveNeighbors(GameOfLifeCell cell) {
    try {
      return cell.getNeighbors().stream()
          .filter(neighbor -> neighbor.getCurrentState() == GAMEOFLIFE_ALIVE).count();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  private List<Integer> retrieveParameterList(String key) {
    List<?> parameterList = getParameters().getAdditionalParameter(key, List.class)
        .orElse(List.of());

    if (parameterList.stream().allMatch(e -> e instanceof Number)) {
      return parameterList.stream().map(e -> (Integer) e).toList();
    } else {
      logger.error("Invalid parameter format: '{}' must be a list of integers. Found: {}", key,
          parameterList);
      throw new SimulationException("InvalidGameOfLifeParameters");
    }
  }
}
