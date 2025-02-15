package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.rules.FireRule;

/**
 * Class for representing cell for Spreading of Fire simulation
 *
 * @author Jessica Chen
 */
public class FireCell extends Cell<FireCell, FireRule> {

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - Spreading of Fire Rule to calculate next state
   */
  public FireCell(int state, FireRule rule) {
    super(state, rule);
  }

  @Override
  protected FireCell getSelf() {
    return this;
  }
}
