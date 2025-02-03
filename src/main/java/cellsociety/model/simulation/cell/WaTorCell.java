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

  /**
   * Constructs a WaTorCell with a specified initial state and rule.
   *
   * @param state - the initial state of the cell (must be a state from WaTorStates)
   * @param rule  - the WaTorRule to calculate the next state
   */
  public WaTorCell(WaTorStates state, WaTorRule rule) {
    super(state);
    myRule = rule;

    myStepsSurvived = 0;
    myEnergy =
        state == WaTorStates.SHARK ? myRule.getParameters().getOrDefault("sharkInitialEnergy", 5.0)
            .intValue() : 0;

    myNextStepsSurvived = 0;
    myNextEnergy = 0;
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

    myStepsSurvived = 0;
    myEnergy =
        state == WaTorStates.SHARK ? myRule.getParameters().getOrDefault("sharkInitialEnergy", 5.0)
            .intValue() : 0;

    myNextStepsSurvived = 0;
    myNextEnergy = 0;
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
   * Get the current energy level of the cell
   *
   * @return the energy level of the cell
   */
  public int getEnergy() {
    return myEnergy;
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

  /**
   * Steps the current state to the next state as well as the cell values
   */
  @Override
  public void step() {
    setCurrentState(getNextState());

    myStepsSurvived = myNextStepsSurvived;
    myEnergy = myNextEnergy;
  }
}
