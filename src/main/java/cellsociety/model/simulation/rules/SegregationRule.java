package cellsociety.model.simulation.rules;

import cellsociety.model.interfaces.Rule;
import cellsociety.model.simulation.cell.SegregationCell;
import cellsociety.model.util.constants.CellStates.SegregationStates;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class for representing rules for Segregation simulation
 *
 * @author Jessica Chen
 */
public class SegregationRule extends Rule<SegregationStates, SegregationCell> {

  private final Random random = new Random();

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public SegregationRule(Map<String, Double> parameters) {
    super(parameters);
  }

  /**
   * <p> If cell is not satisfied, will attempt to move to an empty adjacent space
   *
   * @param cell - cell to apply the rules to
   * @return next state for cell to go to on step
   */
  @Override
  public SegregationStates apply(SegregationCell cell) {
    if (cell.getCurrentState() == SegregationStates.EMPTY) {
      return SegregationStates.EMPTY;
    }

    double satisfactionThreshold = getParameters().getOrDefault("toleranceThreshold", 0.5);
    if (isSatisfied(cell, satisfactionThreshold)) {
      return cell.getCurrentState();
    }

    SegregationCell emptyCell = findEmptyCell(cell);

    if (emptyCell != null) {
      emptyCell.setNextState(cell.getCurrentState());
      return SegregationStates.EMPTY;
    }

    return cell.getCurrentState();
  }

  private boolean isSatisfied(SegregationCell cell, double threshold) {
    List<SegregationCell> neighbors = cell.getNeighbors();
    long totalNeighbors = neighbors.size();

    if (totalNeighbors == 0) {
      return true;
    }

    long similarNeighbors = neighbors.stream()
        .filter(neighbor -> neighbor.getCurrentState() == cell.getCurrentState())
        .count();

    double similarityRatio = (double) similarNeighbors / totalNeighbors;
    return similarityRatio >= threshold;
  }

  private SegregationCell findEmptyCell(SegregationCell cell) {
    List<SegregationCell> emptyNeighbors = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == SegregationStates.EMPTY
            && neighbor.getNextState() == SegregationStates.EMPTY)
        .toList();

    if (emptyNeighbors.isEmpty()) return null;
    return emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
  }
}
