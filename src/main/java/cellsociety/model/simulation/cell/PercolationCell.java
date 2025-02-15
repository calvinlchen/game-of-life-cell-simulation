package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.rules.PercolationRule;

/**
 * Class for representing cell for Percolation simulation
 *
 * @author Jessica Chen
 */
public class PercolationCell extends Cell<PercolationCell, PercolationRule> {
  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Percolation rule to calculate the next state
   */
  public PercolationCell(int state, PercolationRule rule) {
    super(state, rule);
  }

  @Override
  protected PercolationCell getSelf() {
    return this;
  }
}
