package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.PETELKA_MAXSTATE;

import cellsociety.model.simulation.rules.PetelkaRule;

/**
 * The {@code PetelkaCell} class represents a single cell in the Petelka's Loop simulation.
 *
 * <p>This class follows the {@link Cell} template and interacts with the {@link PetelkaRule}
 * to determine its next state.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Ensures state validity within predefined simulation constraints.</li>
 *   <li>Utilizes the Template Method pattern for state calculations.</li>
 *   <li>Simulates self-replicating structures similar to Langton's Loops.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * PetelkaRule rule = new PetelkaRule(parameters);
 * PetelkaCell cell = new PetelkaCell(0, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class PetelkaCell extends Cell<PetelkaCell, PetelkaRule> {

  /**
   * Constructs a {@code PetelkaCell} object with the specified initial state and transition rule.
   *
   * @param state the initial state of the cell. Must be a valid state within the predefined range
   *              of the Petelka system.
   * @param rule  the {@code PetelkaRule} object that defines the state transition behavior for the
   *              cell. Must not be {@code null}.
   */
  public PetelkaCell(int state, PetelkaRule rule) {
    super(state, rule);
    validateState(state, PETELKA_MAXSTATE);
  }

  @Override
  protected PetelkaCell getSelf() {
    return this;
  }

  @Override
  protected int getMaxState() {
    return PETELKA_MAXSTATE;
  }
}
