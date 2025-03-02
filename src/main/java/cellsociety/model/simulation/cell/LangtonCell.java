package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.LANGTON_MAXSTATE;

import cellsociety.model.simulation.rules.LangtonRule;

/**
 * The {@code LangtonCell} class represents a single cell in the Langton's Loop simulation.
 *
 * <p>This cell follows the {@link Cell} template and interacts with the {@link LangtonRule}
 * to determine its next state.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Ensures state validity within predefined Langton's Loop constraints.</li>
 *   <li>Utilizes the Template Method pattern for state calculations.</li>
 *   <li>Follows standard von Neumann neighborhood-based transitions.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * LangtonRule rule = new LangtonRule(parameters);
 * LangtonCell cell = new LangtonCell(0, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class LangtonCell extends Cell<LangtonCell, LangtonRule> {

  /**
   * Constructs a LangtonCell with a specified initial state and rule.
   *
   * @param state the initial state of this cell; must be within the valid range for Langton's Loop
   * @param rule  the LangtonRule that defines the state transition logic for this cell
   */
  public LangtonCell(int state, LangtonRule rule) {
    super(state, rule);
    validateState(state, LANGTON_MAXSTATE);
  }

  @Override
  protected LangtonCell getSelf() {
    return this;
  }

  @Override
  protected int getMaxState() {
    return LANGTON_MAXSTATE;
  }
}
