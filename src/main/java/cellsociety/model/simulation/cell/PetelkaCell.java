package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.PETELKA_MAXSTATE;

import cellsociety.model.simulation.parameters.PetelkaParameters;
import cellsociety.model.simulation.rules.PetelkaRule;

/**
 * Class for representing cell for Petelka's Loop simulation.
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
    validateState(state, PETELKA_MAXSTATE);
  }

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state    - the initial state of the cell
   * @param rule     - the Petelka's Loop rule to calculate the next state
   * @param language - name of language, for error message display
   */
  public PetelkaCell(int state, PetelkaRule rule, String language) {
    super(state, rule, language);
    validateState(state, PETELKA_MAXSTATE);
  }

  @Override
  protected PetelkaCell getSelf() {
    return this;
  }

  @Override
  public void setCurrentState(int state) {
    validateState(state, PETELKA_MAXSTATE);
    super.setCurrentState(state);
  }

  @Override
  public void setNextState(int state) {
    validateState(state, PETELKA_MAXSTATE);
    super.setNextState(state);
  }
}
