package cellsociety.model.simulation.cell;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.simulation.rules.SegregationRule;
import cellsociety.model.util.constants.CellStates.SegregationStates;

/**
 * Class for representing cell for Schelling's Model of Segregation simulation
 *
 * @author Jessica Chen
 */
public class SegregationCell extends Cell<SegregationStates, SegregationCell> {

  private final SegregationRule myRule;

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - Schelling's Model of Segregation Rule to calculate next state
   */
  public SegregationCell(SegregationStates state, SegregationRule rule) {
    super(state);
    myRule = rule;
  }

  @Override
  public void calcNextState() {
    // TODO: make sure this doesn't override calculated stuff
    if (getCurrentState() == SegregationStates.EMPTY && getNextState() != SegregationStates.EMPTY) {
      setNextState(myRule.apply(this));
    }
  }

  @Override
  public void step() {
    setCurrentState(getNextState());
  }
}
