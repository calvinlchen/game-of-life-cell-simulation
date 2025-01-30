package cellsociety.model.simulation.cell;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.simulation.rules.GameOfLifeRule;
import cellsociety.model.util.CellStates.GameOfLifeStates;

/**
 * Class for representing cell for Game of Life simulation
 *
 * @author Jessica Chen
 */
public class GameOfLifeCell extends Cell<GameOfLifeStates, GameOfLifeCell> {

  private final GameOfLifeRule myRule;

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

  // NOTE: I thought about making these non abstract, but with the generics it becomes hard to pass
  // the correct cell into
  @Override
  public void calcNextState() {
    setNextState(myRule.apply(this));
  }

  @Override
  public void step() {
    setCurrentState(getNextState());
  }
}
