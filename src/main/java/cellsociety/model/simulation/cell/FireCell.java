package cellsociety.model.simulation.cell;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.simulation.rules.FireRule;
import cellsociety.model.util.constants.CellStates.FireStates;

/**
 * Class for representing cell for Spreading of Fire simulation
 *
 * @author Jessica Chen
 */
public class FireCell extends Cell<FireStates, FireCell> {

  private final FireRule myRule;

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - Spreading of Fire Rule to calculate next state
   */
  public FireCell(FireStates state, FireRule rule) {
    super(state);
    myRule = rule;
  }

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state    - the initial state of the cell
   * @param position - the initial position of the cell
   * @param rule     - Spreading of Fire Rule to calculate next state
   */
  public FireCell(FireStates state, int[] position, FireRule rule) {
    super(state, position);
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
