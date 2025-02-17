package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.parameters.LangtonParameters;
import cellsociety.model.simulation.rules.LangtonRule;

/**
 * Class for representing cell for Langton's Loop simulation
 *
 * @author Jessica Chen
 */
public class LangtonCell extends Cell<LangtonCell, LangtonRule, LangtonParameters> {
  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Langton's Loop rule to calculate the next state
   */
  public LangtonCell(int state, LangtonRule rule) {
    super(state, rule);
  }

  @Override
  protected LangtonCell getSelf() {
    return this;
  }
}
