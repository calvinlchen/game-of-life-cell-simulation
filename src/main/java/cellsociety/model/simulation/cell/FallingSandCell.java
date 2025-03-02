package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.FALLINGSAND_MAXSTATE;

import cellsociety.model.simulation.rules.FallingSandRule;

/**
 * The {@code FallingSandCell} class represents a cell in the Falling Sand/Water simulation.
 *
 * <p>This cell follows gravity-like behavior, where sand falls downward and water spreads laterally
 * when obstructed.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Utilizes the {@link FallingSandRule} to determine movement.</li>
 *   <li>Overrides the template method to avoid redundant calculations.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * FallingSandRule rule = new FallingSandRule(parameters);
 * FallingSandCell cell = new FallingSandCell(1, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class FallingSandCell extends Cell<FallingSandCell, FallingSandRule> {

  /**
   * Constructs a {@code FallingSandCell} with the specified initial state and simulation rule.
   *
   * @param state the initial state of the cell.
   * @param rule  the {@code FallingSandRule} governing cell behavior.
   */
  public FallingSandCell(int state, FallingSandRule rule) {
    super(state, rule);
    validateState(state, FALLINGSAND_MAXSTATE);
  }

  /**
   * Determines whether the state calculation should be skipped.
   *
   * <p>Falling sand should only update if its current and next states are identical.</p>
   *
   * @return {@code true} if the state calculation should be skipped, {@code false} otherwise.
   */
  @Override
  protected boolean shouldSkipCalculation() {
    return getCurrentState() != getNextState();
  }

  @Override
  protected FallingSandCell getSelf() {
    return this;
  }

  @Override
  protected int getMaxState() {
    return FALLINGSAND_MAXSTATE;
  }
}
