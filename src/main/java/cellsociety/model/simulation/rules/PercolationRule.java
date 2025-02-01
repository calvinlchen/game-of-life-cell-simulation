package cellsociety.model.simulation.rules;

import cellsociety.model.interfaces.Rule;
import cellsociety.model.simulation.cell.PercolationCell;
import cellsociety.model.util.constants.CellStates.PercolationStates;
import java.util.Map;

/**
 * Class for representing rules for Percolation simulation
 *
 * @author Jessica Chen
 */
public class PercolationRule extends Rule<PercolationStates, PercolationCell> {

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public PercolationRule(Map<String, Double> parameters) {
    super(parameters);
  }

  @Override
  public PercolationStates apply(PercolationCell cell) {
    if (cell.getCurrentState() == PercolationStates.OPEN && neighborIsPercolated(cell)) {
      return PercolationStates.PERCOLATED;
    }

    return cell.getCurrentState();
  }

  private boolean neighborIsPercolated(PercolationCell cell) {
    return cell.getNeighbors().stream()
        .anyMatch(neighbor -> neighbor.getCurrentState() == PercolationStates.PERCOLATED);
  }
}
