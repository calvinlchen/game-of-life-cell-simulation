package cellsociety.model.simulation.cell;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.simulation.rules.PercolationRule;
import cellsociety.model.util.constants.CellStates.PercolationStates;

/**
 * Class for representing cell for Percolation simulation
 *
 * @author Jessica Chen
 */
public class PercolationCell extends Cell<PercolationStates, PercolationCell> {

  private final PercolationRule myRule;

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Percolation rule to calculate the next state
   */
  public PercolationCell(PercolationStates state, PercolationRule rule) {
    super(state);
    myRule = rule;
  }

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state    - the initial state of the cell
   * @param position - the initial position of the cell
   * @param rule     - the Percolation rule to calculate the next state
   */
  public PercolationCell(PercolationStates state, int[] position, PercolationRule rule) {
    super(state, position);
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
