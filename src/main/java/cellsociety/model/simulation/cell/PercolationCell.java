package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.PERCOLATION_MAXSTATE;

import cellsociety.model.simulation.parameters.PercolationParameters;
import cellsociety.model.simulation.rules.PercolationRule;

/**
 * Class for representing cell for Percolation simulation.
 *
 * @author Jessica Chen
 */
public class PercolationCell extends Cell<PercolationCell, PercolationRule, PercolationParameters> {

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Percolation rule to calculate the next state
   */
  public PercolationCell(int state, PercolationRule rule) {
    super(state, rule);
    validateState(state, PERCOLATION_MAXSTATE);
  }

  @Override
  protected PercolationCell getSelf() {
    return this;
  }

  @Override
  public void setCurrentState(int state) {
    validateState(state, PERCOLATION_MAXSTATE);
    super.setCurrentState(state);
  }

  @Override
  public void setNextState(int state) {
    validateState(state, PERCOLATION_MAXSTATE);
    super.setNextState(state);
  }
}
