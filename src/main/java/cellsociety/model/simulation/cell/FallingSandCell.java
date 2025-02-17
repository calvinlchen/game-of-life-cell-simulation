package cellsociety.model.simulation.cell;


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
  }

  @Override
  public void calcNextState() {
    // don't need to calculate next state if empty
    if (getCurrentState() != super.getStateProperty("FALLINGSAND_EMPTY")) {
      super.calcNextState();
    }
  }

  @Override
  protected FallingSandCell getSelf() {
    return this;
  }
}
