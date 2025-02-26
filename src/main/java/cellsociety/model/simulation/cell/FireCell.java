package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.FIRE_MAXSTATE;

import cellsociety.model.simulation.parameters.FireParameters;
import cellsociety.model.simulation.rules.FireRule;

/**
 * Class for representing cell for Spreading of Fire simulation
 *
 * @author Jessica Chen
 */
public class FireCell extends Cell<FireCell, FireRule, FireParameters> {

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

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state    - the initial state of the cell
   * @param rule     - Spreading of Fire Rule to calculate next state
   * @param language - name of language, for error message display
   */
  public FireCell(int state, FireRule rule, String language) {
    super(state, rule, language);
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
