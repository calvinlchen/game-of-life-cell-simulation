package cellsociety.model.simulation.cell;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.simulation.rules.FireRule;
import cellsociety.model.util.CellStates.FireStates;

public class FireCell extends Cell<FireStates> {
  private final FireRule myRule;

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   */
  public FireCell(FireStates state, FireRule rule) {
    super(state);
    myRule = rule;
  }

  @Override
  public void calcNextState() {
    setNextState(myRule.apply(this));
  }

  @Override
  public void step() {
    setCurrentState(getNextState());
  }
}
