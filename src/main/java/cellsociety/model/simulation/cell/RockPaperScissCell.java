package cellsociety.model.simulation.cell;


import cellsociety.model.simulation.parameters.RockPaperScissParameters;
import cellsociety.model.simulation.rules.RockPaperScissRule;

/**
 * Class for representing cell for Rock Paper Scissors simulation.
 *
 * @author Jessica Chen
 */
public class RockPaperScissCell extends Cell<RockPaperScissCell, RockPaperScissRule, RockPaperScissParameters> {

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Rock Paper Scissors rule to calculate the next state
   */
  public RockPaperScissCell(int state, RockPaperScissRule rule) {
    super(state, rule);
    validateState(state, rule.getMaxState());
  }

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state    - the initial state of the cell
   * @param rule     - the Rock Paper Scissors rule to calculate the next state
   * @param language - name of language, for error message display
   */
  public RockPaperScissCell(int state, RockPaperScissRule rule, String language) {
    super(state, rule, language);
    validateState(state, rule.getMaxState());
  }

  @Override
  protected RockPaperScissCell getSelf() {
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
