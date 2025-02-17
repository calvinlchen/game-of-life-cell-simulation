package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.parameters.RPSParameters;
import cellsociety.model.simulation.rules.RPSRule;

/**
 * Class for representing cell for Rock Paper Scissors simulation
 *
 * @author Jessica Chen
 */
public class RPSCell extends Cell<RPSCell, RPSRule, RPSParameters> {
  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Rock Paper Scissors rule to calculate the next state
   */
  public RPSCell(int state, RPSRule rule) {
    super(state, rule);
  }

  @Override
  protected RPSCell getSelf() {
    return this;
  }
}
