package cellsociety.model.simulation.cell;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.simulation.rules.GameOfLifeRule;
import cellsociety.model.util.constants.CellStates.GameOfLifeStates;

/**
 * Class for representing cell for Game of Life simulation
 *
 * @author Jessica Chen
 */
public class GameOfLifeCell extends Cell<GameOfLifeCell> {

  private final GameOfLifeRule myRule;

  /**
   * Constructs a Game of Life cell with a specified initial state and rule.
   *
   * @param state - the initial state of the cell (must be a state from GameOfLifeStates)
   * @param rule  - the Game of Life rule to calculate the next state
   */
  public GameOfLifeCell(int state, GameOfLifeRule rule) {
    super(state);
    myRule = rule;
  }

  /**
   * Constructs a Game of Life cell with a specified initial state and rule.
   *
   * @param state    - the initial state of the cell (must be a state from int)
   * @param position - the initial position of the cell
   * @param rule     - the Game of Life rule to calculate the next state
   */
  public GameOfLifeCell(int state, int[] position, GameOfLifeRule rule) {
    super(state, position);
    myRule = rule;
  }

  // NOTE: I thought about making these non abstract, but with the generics it becomes hard to pass
  // the correct cell into, also segragation and wator world become interesting
  @Override
  public void calcNextState() {
    setNextState(myRule.apply(this));
  }

  @Override
  public void step() {
    setCurrentState(getNextState());
  }
}
