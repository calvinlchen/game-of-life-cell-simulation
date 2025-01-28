package cellsociety.model.simulation.cell;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.simulation.rules.GameOfLifeRule;
import cellsociety.model.util.CellStates.GameOfLifeStates;

/**
 * Class for representing cell for Game of Life simulation
 *
 * @author Jessica Chen
 */
public class GameOfLifeCell extends Cell<GameOfLifeStates> {

  private GameOfLifeRule myRule;

  /**
   * Constructs a Game of Life cell with a specified initial state and rule.
   *
   * @param state - the initial state of the cell (must be a state from GameOfLifeStates)
   * @param rule  - the Game of Life rule to calculate the next state
   */
  public GameOfLifeCell(GameOfLifeStates state, GameOfLifeRule rule) {
    super(state);
    myRule = rule;
  }

  @Override
  public void calcNextState() {
    setNextState(myRule.apply(this));
  }

  @Override
  public void step() {
    setCurrentState(getNextState());
  }
}
