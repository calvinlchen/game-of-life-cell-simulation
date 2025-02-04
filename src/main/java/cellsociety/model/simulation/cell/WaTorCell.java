package cellsociety.model.simulation.cell;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.simulation.rules.WaTorRule;
import cellsociety.model.util.constants.CellStates.WaTorStates;

/**
 * Class for representing cell for WaTor simulation
 *
 * @author Jessica Chen
 */
public class WaTorCell extends Cell<WaTorStates, WaTorCell> {

  private final WaTorRule myRule;

  private int myStepsSurvived;
  private int myEnergy;

  private int myNextStepsSurvived;
  private int myNextEnergy;

  private boolean consumed;
  private WaTorCell movedFrom;

  /**
   * Constructs a WaTorCell with a specified initial state and rule.
   *
   * @param state - the initial state of the cell (must be a state from WaTorStates)
   * @param rule  - the WaTorRule to calculate the next state
   */
  public WaTorCell(WaTorStates state, WaTorRule rule) {
    super(state);
    myRule = rule;

    initializeDefaultVariables(state);
  }

  /**
   * Constructs a WaTorCell with a specified initial state and rule.
   *
   * @param state    - the initial state of the cell (must be a state from WaTorStates)
   * @param position - the inital position of the cell
   * @param rule     - the WaTorRule to calculate the next state
   */
  public WaTorCell(WaTorStates state, int[] position, WaTorRule rule) {
    super(state, position);
    myRule = rule;

    initializeDefaultVariables(state);
  }

  private void initializeDefaultVariables(WaTorStates state) {
    myStepsSurvived = 0;
    myEnergy =
        state == WaTorStates.SHARK ? myRule.getParameters().getOrDefault("sharkInitialEnergy", 5.0)
            .intValue() : 0;

    myNextStepsSurvived = 0;
    myNextEnergy = 0;

    consumed = false;
    movedFrom = null;
  }

  /**
   * Get the current steps survived
   *
   * @return the current steps survived
   */
  public int getStepsSurvived() {
    return myStepsSurvived;
  }

  /**
   * Set the current steps survived
   *
   * <p> This should only be used on tests
   *
   * @param stepsSurvived - the current steps survived
   */
  public void setStepsSurvived(int stepsSurvived) {
    myStepsSurvived = stepsSurvived;
  }

  /**
   * Set the energy to a specific amount
   *
   * <p> This should only be used on tests
   *
   * @param energy - the energy left
   */
  public void setEnergy(int energy) {
    myEnergy = energy;
  }

  /**
   * Get the current energy level of the cell
   *
   * @return the energy level of the cell
   */
  public int getEnergy() {
    return myEnergy;
  }

  /**
   * Return if this cell has been consumed
   *
   * @return true if the cell in the current state has been consumed
   */
  public boolean isConsumed() {
    return consumed;
  }

  /**
   * Set the state of if the cell has been consumed
   *
   * @param consumed - true if the cell is consumed
   */
  public void setConsumed(boolean consumed) {
    this.consumed = consumed;
  }

  /**
   * Sets the next stage based on the calculated rules, based on the state return set the next steps
   * survived and energy to the default ones for that state
   */
  @Override
  public void calcNextState() {
    // only not equal if was empty and then a fish / shark swam there
    // or if there was a fish there and it got eaten
    if (getCurrentState() == getNextState()) {
      WaTorStates nextState = myRule.apply(this);
      if (nextState != null) {
        setNextState(nextState);

        myNextStepsSurvived = 0;
        myNextEnergy =
            nextState == WaTorStates.SHARK ? myRule.getParameters()
                .getOrDefault("sharkInitialEnergy", 5.0)
                .intValue() : 0;
      }
    }
  }

  /**
   * Sets the next state of the cell to the given cell with the given cell values
   *
   * @param state         - the state the next state of the cell should be
   * @param stepsSurvived - the number of steps the next state of the cell should have
   * @param energy        - the amount of energy the next state of the cell should have
   */
  public void setNextState(WaTorStates state, int stepsSurvived, int energy) {
    setNextState(state);

    myNextStepsSurvived = stepsSurvived;
    myNextEnergy = energy;
  }

  public void setNextState(WaTorStates state, int stepsSurvived, int energy, WaTorCell movedFrom) {
    this.movedFrom = movedFrom;
    setNextState(state, stepsSurvived, energy);
  }

  /**
   * Steps the current state to the next state as well as the cell values
   */
  @Override
  public void step() {
    if (movedFrom != null && movedFrom.isConsumed()) {
      setNextState(WaTorStates.EMPTY);
      setCurrentState(WaTorStates.EMPTY);
    } else {
      setCurrentState(getNextState());
    }
  }

  @Override
  public void resetParameters() {
    myStepsSurvived = myNextStepsSurvived;
    myEnergy = myNextEnergy;
    consumed = false;
    movedFrom = null;
  }
}
