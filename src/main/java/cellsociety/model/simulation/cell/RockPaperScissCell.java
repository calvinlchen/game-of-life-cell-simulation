package cellsociety.model.simulation.cell;


import cellsociety.model.simulation.rules.RockPaperScissRule;
import cellsociety.model.util.constants.exceptions.SimulationException;

/**
 * The {@code RockPaperScissCell} class represents a single cell in the Rock-Paper-Scissors
 * simulation.
 *
 * <p>This simulation models **cyclic dominance**, where each state (Rock, Paper, Scissors)
 * can be "defeated" by another, creating a competitive evolutionary system.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Interacts with the {@link RockPaperScissRule} to determine the next state.</li>
 *   <li>Validates state transitions based on the maximum state count.</li>
 *   <li>Utilizes the **Template Method Pattern** for modularity and extension.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * RockPaperScissRule rule = new RockPaperScissRule(parameters);
 * RockPaperScissCell cell = new RockPaperScissCell(0, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class RockPaperScissCell extends
    Cell<RockPaperScissCell, RockPaperScissRule> {

  /**
   * Constructs a {@code RockPaperScissCell} with the specified initial state and simulation rule.
   * This constructor initializes the cell by validating its initial state against the maximum
   * allowed state defined in the provided rule.
   *
   * @param state the initial state of the cell; must be within the range [0, maxState - 1], where
   *              {@code maxState} is defined by the provided rule.
   * @param rule  the {@code RockPaperScissRule} that governs the behavior and rules of this cell;
   *              must not be null.
   * @throws SimulationException if the initial state is invalid or out of range.
   */
  public RockPaperScissCell(int state, RockPaperScissRule rule) {
    super(state, rule);
    validateState(state, rule.getMaxState());
  }

  @Override
  protected RockPaperScissCell getSelf() {
    return this;
  }

  @Override
  protected int getMaxState() {
    return getRule().getMaxState();
  }
}
