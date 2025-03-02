package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.GAMEOFLIFE_MAXSTATE;

import cellsociety.model.simulation.rules.GameOfLifeRule;

/**
 * The {@code GameOfLifeCell} class represents a cell in the Conway's Game of Life simulation.
 *
 * <p>This class follows the {@link Cell} template and interacts with the {@link GameOfLifeRule}
 * to determine its next state.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Follows standard Game of Life state transitions (alive/dead).</li>
 *   <li>Utilizes the {@link GameOfLifeRule} for determining cell evolution.</li>
 *   <li>Inherits template-based state updates and history tracking from {@link Cell}.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * GameOfLifeRule rule = new GameOfLifeRule(parameters);
 * GameOfLifeCell cell = new GameOfLifeCell(GAMEOFLIFE_ALIVE, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class GameOfLifeCell extends Cell<GameOfLifeCell, GameOfLifeRule> {

  /**
   * Constructs a {@code GameOfLifeCell} with the specified initial state and rule.
   *
   * @param state the initial state of the cell (must be a valid Game of Life state).
   * @param rule  the {@code GameOfLifeRule} governing cell behavior.
   */
  public GameOfLifeCell(int state, GameOfLifeRule rule) {
    super(state, rule);
    validateState(state, GAMEOFLIFE_MAXSTATE);
  }

  @Override
  protected GameOfLifeCell getSelf() {
    return this;
  }

  @Override
  protected int getMaxState() {
    return GAMEOFLIFE_MAXSTATE;
  }
}
