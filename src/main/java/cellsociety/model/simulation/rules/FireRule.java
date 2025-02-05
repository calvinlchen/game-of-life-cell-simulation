package cellsociety.model.simulation.rules;

import cellsociety.model.interfaces.Rule;
import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.util.constants.CellStates.FireStates;
import java.util.Map;


/**
 * Class for representing rules for Spreading of Fire simulation
 *
 * @author Jessica Chen
 */
public class FireRule extends Rule<FireStates, FireCell> {

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public FireRule(Map<String, Double> parameters) {
    super(parameters);
  }

  @Override
  public FireStates apply(FireCell cell) {
    if (cell.getCurrentState() == FireStates.BURNING) {
      return FireStates.EMPTY;
    } else if (cell.getCurrentState() == FireStates.TREE && neighborIsBurning(cell)) {
      return FireStates.BURNING;
    } else if (cell.getCurrentState() == FireStates.TREE) {
      double f = getParameters().getOrDefault("ignitionLikelihood", 0.0);
      if (Math.random() < f) {
        return FireStates.BURNING;
      }
    } else {
      double p = getParameters().getOrDefault("treeSpawnLikelihood", 0.0);
      if (Math.random() < p) {
        return FireStates.TREE;
      }
    }
    return cell.getCurrentState();
  }

  private boolean neighborIsBurning(FireCell cell) {
    return cell.getNeighbors().stream()
        .anyMatch(neighbor -> neighbor.getCurrentState() == FireStates.BURNING);
  }
}
