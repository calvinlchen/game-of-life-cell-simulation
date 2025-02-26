package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.LANGTON_MAXSTATE;

import cellsociety.model.simulation.parameters.LangtonParameters;
import cellsociety.model.simulation.rules.LangtonRule;

/**
 * Class for representing cell for Langton's Loop simulation.
 *
 * @author Jessica Chen
 */
public class LangtonCell extends Cell<LangtonCell, LangtonRule, LangtonParameters> {

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Langton's Loop rule to calculate the next state
   */
  public LangtonCell(int state, LangtonRule rule) {
    super(state, rule);
    validateState(state, LANGTON_MAXSTATE);
  }

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state    - the initial state of the cell
   * @param rule     - the Langton's Loop rule to calculate the next state
   * @param language - name of language, for error message display
   */
  public LangtonCell(int state, LangtonRule rule, String language) {
    super(state, rule, language);
    validateState(state, LANGTON_MAXSTATE);
  }

  @Override
  protected LangtonCell getSelf() {
    return this;
  }

  @Override
  public void setCurrentState(int state) {
    validateState(state, LANGTON_MAXSTATE);
    super.setCurrentState(state);
  }

  @Override
  public void setNextState(int state) {
    validateState(state, LANGTON_MAXSTATE);
    super.setNextState(state);
  }
}
