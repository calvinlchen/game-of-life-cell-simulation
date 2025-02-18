package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.PERCOLATION_OPEN;
import static cellsociety.model.util.constants.CellStates.PERCOLATION_PERCOLATED;

import cellsociety.model.simulation.cell.PercolationCell;
import cellsociety.model.simulation.parameters.PercolationParameters;
import java.util.Map;

/**
 * Class for representing rules for Percolation simulation
 *
 * @author Jessica Chen
 */
public class PercolationRule extends Rule<PercolationCell, PercolationParameters> {

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public PercolationRule(PercolationParameters parameters) {
    super(parameters);
  }

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   * @param language - name of language, for error message display
   */
  public PercolationRule(PercolationParameters parameters, String language) {
    super(parameters, language);
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
