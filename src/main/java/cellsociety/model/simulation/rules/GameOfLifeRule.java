package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.GAMEOFLIFE_ALIVE;
import static cellsociety.model.util.constants.CellStates.GAMEOFLIFE_DEAD;

import cellsociety.model.simulation.cell.GameOfLifeCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class for representing rules for Game of Life simulation.
 *
 * @author Jessica Chen
 */
public class GameOfLifeRule extends Rule<GameOfLifeCell> {

  /**
   * Constructor for the Rule class.
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public GameOfLifeRule(GenericParameters parameters) {
    super(parameters);
  }

  /**
   * Constructor for the Rule class.
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   * @param language   - name of language, for error message display
   */
  public GameOfLifeRule(GenericParameters parameters, String language) {
    super(parameters, language);
  }

  @Override
  public int apply(GameOfLifeCell cell) {
    long aliveNeighbors = countAliveNeighbors(cell);

    Optional<List> survive = getParameters().getAdditionalParameter("S", List.class);
    boolean surviveContains = survive.map(list -> list.contains((int) aliveNeighbors))
        .orElseGet(() -> false);

    Optional<List> birth = getParameters().getAdditionalParameter("B", List.class);
    boolean birthContains = birth.map(list -> list.contains((int) aliveNeighbors))
        .orElseGet(() -> false);

    if (cell.getCurrentState() == GAMEOFLIFE_ALIVE && !surviveContains) {
      return GAMEOFLIFE_DEAD;
    } else if (cell.getCurrentState() == GAMEOFLIFE_DEAD && birthContains) {
      return GAMEOFLIFE_ALIVE;
    }

    return cell.getCurrentState();
  }

  private long countAliveNeighbors(GameOfLifeCell cell) {
    return cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == GAMEOFLIFE_ALIVE).count();
  }
}
