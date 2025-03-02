package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.CHOUREG2_MAXSTATE;

import cellsociety.model.simulation.rules.ChouReg2Rule;

/**
 * The {@code ChouReg2Cell} class represents a single cell in the ChouReg2 Langton's Loop
 * simulation.
 *
 * <p>This class follows the {@link Cell} template and interacts with the {@link ChouReg2Rule}
 * to determine its next state.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Ensures state validity within predefined simulation constraints.</li>
 *   <li>Utilizes the Template Method pattern for state calculations.</li>
 *   <li>Logs state transitions and validation errors.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * ChouReg2Rule rule = new ChouReg2Rule(parameters);
 * ChouReg2Cell cell = new ChouReg2Cell(0, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class ChouReg2Cell extends Cell<ChouReg2Cell, ChouReg2Rule> {

  /**
   * Constructs a new {@code ChouReg2Cell} instance with the specified initial state and transition
   * rule.
   *
   * @param state the initial state of the cell; must be within the valid range for the ChouReg2
   *              simulation.
   * @param rule  the {@code ChouReg2Rule} instance that governs transitions for this cell.
   * @throws IllegalArgumentException if the provided state is outside the valid range.
   */
  public ChouReg2Cell(int state, ChouReg2Rule rule) {
    super(state, rule);
    validateState(state, CHOUREG2_MAXSTATE);
  }


  @Override
  protected ChouReg2Cell getSelf() {
    return this;
  }

  @Override
  protected int getMaxState() {
    return CHOUREG2_MAXSTATE;
  }

}
