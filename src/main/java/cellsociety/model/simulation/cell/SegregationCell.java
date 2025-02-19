package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.SEGREGATION_MAXSTATE;
import static cellsociety.model.util.constants.CellStates.SEGREGATION_EMPTY;

import cellsociety.model.simulation.parameters.SegregationParameters;
import cellsociety.model.simulation.rules.SegregationRule;


/**
 * Class for representing cell for Schelling's Model of Segregation simulation
 *
 * @author Jessica Chen
 */
public class SegregationCell extends Cell<SegregationCell, SegregationRule, SegregationParameters> {

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - Schelling's Model of Segregation Rule to calculate next state
   */
  public SegregationCell(int state, SegregationRule rule) {
    super(state, rule);
    validateState(state, SEGREGATION_MAXSTATE);
  }

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - Schelling's Model of Segregation Rule to calculate next state
   * @param language - name of language, for error message display
   */
  public SegregationCell(int state, SegregationRule rule, String language) {
    super(state, rule, language);
    validateState(state, SEGREGATION_MAXSTATE);
  }

  @Override
  public void calcNextState() {
    // check to make sure you aren't overriding already calculated stuff
    // namely in a prior thing you were empty and then someone moved into you because
    // they weren't satisfied
    if (!(getCurrentState() == SEGREGATION_EMPTY && getNextState() != SEGREGATION_EMPTY)) {
      super.calcNextState();
    }
  }

  @Override
  protected SegregationCell getSelf() {
    return this;
  }

  @Override
  public void setCurrentState(int state) {
    validateState(state, SEGREGATION_MAXSTATE);
    super.setCurrentState(state);
  }

  @Override
  public void setNextState(int state) {
    validateState(state, SEGREGATION_MAXSTATE);
    super.setNextState(state);
  }
}
