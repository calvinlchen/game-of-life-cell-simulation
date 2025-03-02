package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.FIRE_MAXSTATE;

import cellsociety.model.simulation.rules.FireRule;

/**
 * The {@code FireCell} class represents a cell in the Spreading Fire simulation.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Utilizes the {@link FireRule} to determine fire spread behavior.</li>
 *   <li>Overrides the template method to allow probability-based transitions.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * FireRule rule = new FireRule(parameters);
 * FireCell cell = new FireCell(FIRE_TREE, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class FireCell extends Cell<FireCell, FireRule> {

  /**
   * Constructs a {@code FireCell} with the specified initial state and simulation rule.
   *
   * @param state the initial state of the cell.
   * @param rule  the {@code FireRule} governing fire spread behavior.
   */
  public FireCell(int state, FireRule rule) {
    super(state, rule);
    validateState(state, FIRE_MAXSTATE);
  }

  @Override
  protected FireCell getSelf() {
    return this;
  }

  @Override
  protected int getMaxState() {
    return FIRE_MAXSTATE;
  }
}
