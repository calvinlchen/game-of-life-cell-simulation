package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.FIRE_MAXSTATE;

import cellsociety.model.simulation.rules.FireRule;

/**
 * Class for representing cell for Spreading of Fire simulation.
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
    validateState(state, FIRE_MAXSTATE);
  }

  @Override
  protected FireCell getSelf() {
    return this;
  }

  @Override
  public void setCurrentState(int state) {
    validateState(state, FIRE_MAXSTATE);
    super.setCurrentState(state);
  }

  @Override
  public void setNextState(int state) {
    validateState(state, FIRE_MAXSTATE);
    super.setNextState(state);
  }
}
