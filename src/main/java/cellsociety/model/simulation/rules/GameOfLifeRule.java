package cellsociety.model.simulation.rules;

import cellsociety.model.interfaces.Rule;
import cellsociety.model.simulation.cell.GameOfLifeCell;
import cellsociety.model.util.constants.CellStates.GameOfLifeStates;
import java.util.Map;

/**
 * Class for representing rules for Game of Life simulation
 *
 * @author Jessica Chen
 */
public class GameOfLifeRule extends Rule<GameOfLifeCell> {
  private final int GAMEOFLIFE_DEAD;
  private final int GAMEOFLIFE_ALIVE;

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public GameOfLifeRule(Map<String, Double> parameters) {
    super(parameters);

    GAMEOFLIFE_ALIVE = super.getStateProperty("GAMEOFLIFE_ALIVE");
    GAMEOFLIFE_DEAD = super.getStateProperty("GAMEOFLIFE_DEAD");
  }

  @Override
  public int apply(GameOfLifeCell cell) {
    long aliveNeighbors = countAliveNeighbors(cell);

    if (cell.getCurrentState() == GAMEOFLIFE_ALIVE &&
        (aliveNeighbors < 2 || aliveNeighbors > 3)) {
      return GAMEOFLIFE_DEAD;
    } else if (cell.getCurrentState() == GAMEOFLIFE_DEAD && aliveNeighbors == 3) {
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
