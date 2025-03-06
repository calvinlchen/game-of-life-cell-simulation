package cellsociety.model.statefactory.handler;

import cellsociety.model.util.constants.CellStates;
import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the Conway's game of life simulation. This class extends
 * {@link CellStateHandlerStatic} and defines a fixed set of states for the Conway's game of life
 * simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class GameOfLifeStateHandler extends CellStateHandlerStatic {

  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(CellStates.GAMEOFLIFE_DEAD, "dead");
    cellStates.put(CellStates.GAMEOFLIFE_ALIVE, "alive");
    cellStates.put(CellStates.GAMEOFLIFE_MAXSTATE, "max");
  }

  /**
   * Constructs a {@code GameOfLifeStateHandler} with predefined states for the Conway's Game of
   * Life simulation.
   */
  public GameOfLifeStateHandler() {
    super(cellStates);
  }
}
