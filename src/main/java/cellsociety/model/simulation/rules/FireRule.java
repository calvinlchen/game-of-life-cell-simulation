package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.FIRE_BURNING;
import static cellsociety.model.util.constants.CellStates.FIRE_EMPTY;
import static cellsociety.model.util.constants.CellStates.FIRE_TREE;

import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.simulation.parameters.FireParameters;
import java.util.Map;


/**
 * Class for representing rules for Spreading of Fire simulation
 *
 * @author Jessica Chen
 */
public class FireRule extends Rule<FireCell, FireParameters> {

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public FireRule(FireParameters parameters) {
    super(parameters);
  }

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   * @param language   - name of language, for error message display
   */
  public FireRule(FireParameters parameters, String language) {
    super(parameters, language);
  }

  @Override
  public int apply(FireCell cell) {
    return switch (cell.getCurrentState()) {
      case FIRE_BURNING -> FIRE_EMPTY;
      case FIRE_TREE -> handleTree(cell);
      case FIRE_EMPTY -> handleEmpty(cell);
      default -> cell.getCurrentState();
    };
  }

  private int handleTree(FireCell cell) {
    if (neighborIsBurning(cell)) {
      return FIRE_BURNING;
    }

    double f = getParameters().getParameter("ignitionLikelihood");
    if (Math.random() < f) {
      return FIRE_BURNING;
    }

    return cell.getCurrentState();
  }

  private int handleEmpty(FireCell cell) {
    double p = getParameters().getParameter("treeSpawnLikelihood");
    if (Math.random() < p) {
      return FIRE_TREE;
    }
    return cell.getCurrentState();
  }

  private boolean neighborIsBurning(FireCell cell) {
    return cell.getNeighbors().stream()
        .anyMatch(neighbor -> neighbor.getCurrentState() == FIRE_BURNING);
  }
}
