package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.rules.PercolationRule;

/**
 * Class for representing cell for Percolation simulation
 *
 * @author Jessica Chen
 */
public class PercolationCell extends Cell<PercolationCell> {

  private final PercolationRule myRule;

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Percolation rule to calculate the next state
   */
  public PercolationCell(int state, PercolationRule rule) {
    super(state);
    myRule = rule;
  }

  @Override
  public void calcNextState() {
    setNextState(myRule.apply(this));
  }

  @Override
  public void step() {
    setCurrentState(getNextState());
  }
}
