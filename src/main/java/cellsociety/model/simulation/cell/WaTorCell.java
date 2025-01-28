package cellsociety.model.simulation.cell;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.simulation.rules.WaTorRule;
import cellsociety.model.util.CellStates.WaTorStates;

public class WaTorCell extends Cell<WaTorStates, WaTorCell> {

  private WaTorStates proposedState;
  private WaTorCell proposedBy;
  private final WaTorRule myRule;

  private int myStepsSurvived;
  private int myEnergy;

  /**
   * Constructs a WaTorCell with a specified initial state and rule.
   *
   * @param state - the initial state of the cell (must be a state from WaTorStates)
   * @param rule  - the WaTorRule to calculate the next state
   */
  public WaTorCell(WaTorStates state, WaTorRule rule) {
    super(state);
    myRule = rule;

    this.proposedState = null;
    this.proposedBy = null;

    myStepsSurvived = 0;
    myEnergy =
        state == WaTorStates.SHARK ? myRule.getParameters().getOrDefault("sharkInitialEnergy", 5.0)
            .intValue() : 0;
  }

  public void proposeState(WaTorStates state, WaTorCell proposer) {
    if (proposedBy == null || resolveConflict(state)) {
      proposedState = state;
      proposedBy = proposer;
    }
  }

  public void incrementStepsSurvived() {
    myStepsSurvived++;
  }

  public void resetStepsSurvived() {
    myStepsSurvived = 0;
  }

  public int getStepsSurvived() {
    return myStepsSurvived;
  }

  public void addEnergy(int amount) {
    myEnergy += amount;
  }

  public void reduceEnergy() {
    myEnergy--;
  }

  public int getEnergy() {
    return myEnergy;
  }

  private boolean resolveConflict(WaTorStates state) {
    // priority: Shark, Fish, Empty
    return (proposedState == WaTorStates.EMPTY && state != WaTorStates.EMPTY) ||
        proposedState == WaTorStates.FISH && state == WaTorStates.SHARK;
  }

  public WaTorStates getProposedState() {
    return proposedState;
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
