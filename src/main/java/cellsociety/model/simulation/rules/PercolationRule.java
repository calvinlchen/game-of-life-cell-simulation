package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.PERCOLATION_OPEN;
import static cellsociety.model.util.constants.CellStates.PERCOLATION_PERCOLATED;

import cellsociety.model.simulation.cell.PercolationCell;
import cellsociety.model.simulation.parameters.GenericParameters;

/**
 * Class for representing rules for Percolation simulation.
 *
 * @author Jessica Chen
 */
public class PercolationRule extends Rule<PercolationCell> {

  /**
   * Constructor for the Rule class.
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public PercolationRule(GenericParameters parameters) {
    super(parameters);
  }

  /**
   * Constructor for the Rule class.
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   * @param language   - name of language, for error message display
   */
  public PercolationRule(GenericParameters parameters, String language) {
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
