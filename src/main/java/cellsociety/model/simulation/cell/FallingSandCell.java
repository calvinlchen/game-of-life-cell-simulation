package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.FALLINGSAND_MAXSTATE;
import static cellsociety.model.util.constants.CellStates.FALLINGSAND_EMPTY;

import cellsociety.model.simulation.parameters.FallingSandParameters;
import cellsociety.model.simulation.rules.FallingSandRule;

/**
 * Class for representing cell for Falling Sand/Water simulation
 *
 * @author Jessica Chen
 */
public class FallingSandCell extends Cell<FallingSandCell, FallingSandRule, FallingSandParameters> {

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - Falling Sand Rule to calculate next state
   */
  public FallingSandCell(int state, FallingSandRule rule) {
    super(state, rule);
    validateState(state, FALLINGSAND_MAXSTATE);
  }

  @Override
  public void calcNextState() {
    if (getCurrentState() == getNextState()) {
      super.calcNextState();
    }
  }

  @Override
  protected FallingSandCell getSelf() {
    return this;
  }

  @Override
  public void setCurrentState(int state) {
    validateState(state, FALLINGSAND_MAXSTATE);
    super.setCurrentState(state);
  }

  @Override
  public void setNextState(int state) {
    validateState(state, FALLINGSAND_MAXSTATE);
    super.setNextState(state);
  }
}
