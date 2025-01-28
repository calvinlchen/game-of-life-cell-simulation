package cellsociety.model.simulation.rules;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.interfaces.Rule;
import cellsociety.model.util.CellStates.PercolationStates;
import java.util.Map;

/**
 * Class for representing rules for Percolation simulation
 *
 * @author Jessica Chen
 */
public class PercolationRule extends Rule<PercolationStates> {

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public PercolationRule(Map<String, Double> parameters) {
    super(parameters);
  }

  @Override
  public PercolationStates apply(Cell<PercolationStates> cell) {
    if (cell.getCurrentState() == PercolationStates.OPEN && neighborIsPercolated(cell)) {
      return PercolationStates.PERCOLATED;
    }

    return cell.getCurrentState();
  }

  private boolean neighborIsPercolated(Cell<PercolationStates> cell) {
    return cell.getNeighbors().stream()
        .anyMatch(neighbor -> neighbor.getCurrentState() == PercolationStates.PERCOLATED);
  }
}
