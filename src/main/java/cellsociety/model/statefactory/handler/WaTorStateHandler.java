package cellsociety.model.statefactory.handler;

import cellsociety.model.util.constants.CellStates;
import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the WaTorWorld simulation. This class extends
 * {@link CellStateHandlerStatic} and defines a fixed set of states for the WaTorWorld simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class WaTorStateHandler extends CellStateHandlerStatic {

  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(CellStates.WATOR_EMPTY, "empty");
    cellStates.put(CellStates.WATOR_FISH, "fish");
    cellStates.put(CellStates.WATOR_SHARK, "shark");
    cellStates.put(CellStates.WATOR_MAXSTATE, "max");
  }

  /**
   * Constructs a {@code WaTorStateHandler} with predefined states for WaTor World simulation.
   */
  public WaTorStateHandler() {
    super(cellStates);
  }
}
