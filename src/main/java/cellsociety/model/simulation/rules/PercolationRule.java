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
public class PercolationRule extends Rule<PercolationCell> {
  private final int PERCOLATION_PERCOLATED;
  private final int PERCOLATION_OPEN;

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public PercolationRule(Map<String, Double> parameters) {
    super(parameters);
    PERCOLATION_OPEN = super.getStateProperty("PERCOLATION_OPEN");
    PERCOLATION_PERCOLATED = super.getStateProperty("PERCOLATION_PERCOLATED");
  }

  @Override
  public int apply(PercolationCell cell) {
    if (cell.getCurrentState() == PERCOLATION_OPEN && neighborIsPercolated(cell)) {
      return PERCOLATION_PERCOLATED;
    }

    return cell.getCurrentState();
  }

  private boolean neighborIsPercolated(PercolationCell cell) {
    return cell.getNeighbors().stream()
        .anyMatch(neighbor -> neighbor.getCurrentState() == PERCOLATION_PERCOLATED);
  }
}
