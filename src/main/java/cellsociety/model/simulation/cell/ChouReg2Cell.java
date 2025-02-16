package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.parameters.ChouReg2Parameters;
import cellsociety.model.simulation.rules.ChouReg2Rule;

/**
 * Class for representing cell for Percolation simulation
 *
 * @author Jessica Chen
 */
public class ChouReg2Cell extends Cell<ChouReg2Cell, ChouReg2Rule, ChouReg2Parameters> {
  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Percolation rule to calculate the next state
   */
  public ChouReg2Cell(int state, ChouReg2Rule rule) {
    super(state, rule);
  }

  @Override
  protected ChouReg2Cell getSelf() {
    return this;
  }
}
