package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.GameOfLifeCell;
import cellsociety.model.simulation.parameters.GameOfLifeParameters;
import java.util.Map;

/**
 * Class for representing rules for Game of Life simulation
 *
 * @author Jessica Chen
 */
public class GameOfLifeRule extends Rule<GameOfLifeCell, GameOfLifeParameters> {

  private final int GAMEOFLIFE_DEAD;
  private final int GAMEOFLIFE_ALIVE;

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public GameOfLifeRule(GameOfLifeParameters parameters) {
    super(parameters);

    GAMEOFLIFE_ALIVE = super.getStateProperty("GAMEOFLIFE_ALIVE");
    GAMEOFLIFE_DEAD = super.getStateProperty("GAMEOFLIFE_DEAD");
  }

  @Override
  public int apply(GameOfLifeCell cell) {
    long aliveNeighbors = countAliveNeighbors(cell);

    if (cell.getCurrentState() == GAMEOFLIFE_ALIVE && !getParameters().getSurviveRules()
        .contains((int) aliveNeighbors)) {
      return GAMEOFLIFE_DEAD;
    } else if (cell.getCurrentState() == GAMEOFLIFE_DEAD && getParameters().getBornRules()
        .contains((int) aliveNeighbors)) {
      return GAMEOFLIFE_ALIVE;
    }

    return cell.getCurrentState();
  }

  private long countAliveNeighbors(GameOfLifeCell cell) {
    return cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == GAMEOFLIFE_ALIVE)
        .count();
  }
}
