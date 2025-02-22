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
    validateState(state, rule.getMaxState());
  }

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Rock Paper Scissors rule to calculate the next state
   * @param language - name of language, for error message display
   */
  public RPSCell(int state, RPSRule rule, String language) {
    super(state, rule, language);
    validateState(state, rule.getMaxState());
  }

  @Override
  protected RPSCell getSelf() {
    return this;
  }

  @Override
  public void setCurrentState(int state) {
    validateState(state, getRule().getMaxState());
    super.setCurrentState(state);
  }

  @Override
  public void setNextState(int state) {
    validateState(state, getRule().getMaxState());
    super.setNextState(state);
  }
}
