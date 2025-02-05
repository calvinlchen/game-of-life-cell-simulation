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
public class GameOfLifeRule extends Rule<GameOfLifeStates, GameOfLifeCell> {
  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public GameOfLifeRule(Map<String, Double> parameters) {
    super(parameters);
  }

  @Override
  public GameOfLifeStates apply(GameOfLifeCell cell) {
    long aliveNeighbors = countAliveNeighbors(cell);

    if (cell.getCurrentState() == GameOfLifeStates.ALIVE &&
        (aliveNeighbors < 2 || aliveNeighbors > 3)) {
      return GameOfLifeStates.DEAD;
    } else if (cell.getCurrentState() == GameOfLifeStates.DEAD && aliveNeighbors == 3) {
      return GameOfLifeStates.ALIVE;
    }

    return cell.getCurrentState();
  }

  private long countAliveNeighbors(GameOfLifeCell cell) {
    return cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == GameOfLifeStates.ALIVE)
        .count();
  }
}
