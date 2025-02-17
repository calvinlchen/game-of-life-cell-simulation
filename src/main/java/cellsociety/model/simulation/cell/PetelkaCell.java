package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.parameters.PetelkaParameters;
import cellsociety.model.simulation.rules.PetelkaRule;

/**
 * Class for representing cell for Petelka's Loop simulation
 *
 * @author Jessica Chen
 */
public class PetelkaCell extends Cell<PetelkaCell, PetelkaRule, PetelkaParameters> {
  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the Petelka's Loop rule to calculate the next state
   */
  public PetelkaCell(int state, PetelkaRule rule) {
    super(state, rule);
  }

  @Override
  protected PetelkaCell getSelf() {
    return this;
  }
}
