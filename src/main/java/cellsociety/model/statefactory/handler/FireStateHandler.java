package cellsociety.model.statefactory.handler;

import cellsociety.model.util.constants.CellStates;
import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the Spreading of Fire simulation. This class extends
 * {@link CellStateHandlerStatic} and defines a fixed set of states for the Spreading of Fire
 * simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class FireStateHandler extends CellStateHandlerStatic {

  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(CellStates.FIRE_EMPTY, "empty");
    cellStates.put(CellStates.FIRE_TREE, "tree");
    cellStates.put(CellStates.FIRE_BURNING, "burning");
  }

  /**
   * Constructs a {@code FireStateHandler} with predefined states for the Spreading of Fire
   * simulation.
   */
  public FireStateHandler() {
    super(cellStates);
  }
}
