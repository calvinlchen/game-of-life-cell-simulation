package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.rules.SegregationRule;


/**
 * Class for representing cell for Schelling's Model of Segregation simulation
 *
 * @author Jessica Chen
 */
public class SegregationCell extends Cell<SegregationCell, SegregationRule> {
  private final int SEGREGATION_EMPTY;

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - Schelling's Model of Segregation Rule to calculate next state
   */
  public SegregationCell(int state, SegregationRule rule) {
    super(state, rule);

    SEGREGATION_EMPTY = super.getStateProperty("SEGREGATION_EMPTY");
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
}
