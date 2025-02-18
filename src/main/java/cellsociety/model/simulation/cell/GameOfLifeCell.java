package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.GAMEOFLIFE_MAXSTATE;

import cellsociety.model.simulation.parameters.GameOfLifeParameters;
import cellsociety.model.simulation.rules.GameOfLifeRule;

/**
 * Class for representing cell for Game of Life simulation
 *
 * @author Jessica Chen
 */
public class GameOfLifeCell extends Cell<GameOfLifeCell, GameOfLifeRule, GameOfLifeParameters> {
  /**
   * Constructs a Game of Life cell with a specified initial state and rule.
   *
   * @param state - the initial state of the cell (must be a state from GameOfLifeStates)
   * @param rule  - the Game of Life rule to calculate the next state
   */
  public GameOfLifeCell(int state, GameOfLifeRule rule) {
    super(state, rule);
    validateState(state, GAMEOFLIFE_MAXSTATE);
  }

  /**
   * Constructs a Game of Life cell with a specified initial state and rule.
   *
   * @param state - the initial state of the cell (must be a state from GameOfLifeStates)
   * @param rule  - the Game of Life rule to calculate the next state
   * @param language - name of language, for error message display
   */
  public GameOfLifeCell(int state, GameOfLifeRule rule, String language) {
    super(state, rule, language);
    validateState(state, GAMEOFLIFE_MAXSTATE);
  }

  @Override
  protected GameOfLifeCell getSelf() {
    return this;
  }

  @Override
  public void setCurrentState(int state) {
    validateState(state, GAMEOFLIFE_MAXSTATE);
    super.setCurrentState(state);
  }

  @Override
  public void setNextState(int state) {
    validateState(state, GAMEOFLIFE_MAXSTATE);
    super.setNextState(state);
  }
}
