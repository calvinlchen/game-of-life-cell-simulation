package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.CHOUREG2_MAXSTATE;

import cellsociety.model.simulation.parameters.ChouReg2Parameters;
import cellsociety.model.simulation.rules.ChouReg2Rule;

/**
 * Class for representing cell for ChouReg2 Langton's Loop simulation
 *
 * @author Jessica Chen
 */
public class ChouReg2Cell extends Cell<ChouReg2Cell, ChouReg2Rule, ChouReg2Parameters> {
  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the ChouReg2 Langton's Loop rule to calculate the next state
   */
  public ChouReg2Cell(int state, ChouReg2Rule rule) {
    super(state, rule);
    validateState(state, CHOUREG2_MAXSTATE);
  }

  @Override
  protected ChouReg2Cell getSelf() {
    return this;
  }

  @Override
  public void setCurrentState(int state) {
    validateState(state, CHOUREG2_MAXSTATE);
    super.setCurrentState(state);
  }

  @Override
  public void setNextState(int state) {
    validateState(state, CHOUREG2_MAXSTATE);
    super.setNextState(state);
  }

}
