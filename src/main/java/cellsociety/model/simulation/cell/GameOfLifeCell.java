package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.rules.GameOfLifeRule;

/**
 * Class for representing cell for Game of Life simulation
 *
 * @author Jessica Chen
 */
public class GameOfLifeCell extends Cell<GameOfLifeCell, GameOfLifeRule> {
  /**
   * Constructs a Game of Life cell with a specified initial state and rule.
   *
   * @param state - the initial state of the cell (must be a state from GameOfLifeStates)
   * @param rule  - the Game of Life rule to calculate the next state
   */
  public GameOfLifeCell(int state, GameOfLifeRule rule) {
    super(state, rule);
  }

  @Override
  protected GameOfLifeCell getSelf() {
    return this;
  }
}
