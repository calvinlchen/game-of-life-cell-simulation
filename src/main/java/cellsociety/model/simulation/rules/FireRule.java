package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.FireCell;
import java.util.Map;


/**
 * Class for representing rules for Spreading of Fire simulation
 *
 * @author Jessica Chen
 */
public class FireRule extends Rule<FireCell> {
  private final int FIRE_EMPTY;
  private final int FIRE_TREE;
  private final int FIRE_BURNING;

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public FireRule(Map<String, Double> parameters) {
    super(parameters);

    FIRE_EMPTY = super.getStateProperty("FIRE_EMPTY");
    FIRE_TREE = super.getStateProperty("FIRE_TREE");
    FIRE_BURNING = super.getStateProperty("FIRE_BURNING");
  }

  @Override
  public int apply(FireCell cell) {
    return switch (cell.getCurrentState()) {
      case 2 ->                     // burning
          FIRE_EMPTY;
      case 1 ->                     // tree
          handleTree(cell);
      case 0 ->                     // empty
          handleEmpty(cell);
      default -> cell.getCurrentState();
    };
  }

  private int handleTree(FireCell cell) {
    if (neighborIsBurning(cell)) {
      return FIRE_BURNING;
    }

    double f = getParameters().getOrDefault("ignitionLikelihood", 0.0);
    if (Math.random() < f) {
      return FIRE_BURNING;
    }

    return cell.getCurrentState();
  }

  private int handleEmpty(FireCell cell) {
    double p = getParameters().getOrDefault("treeSpawnLikelihood", 0.0);
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
