package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the WaTorWorld simulation.
 * This class extends {@link CellStateHandlerStatic} and defines a fixed
 * set of states for the WaTorWorld simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class WaTorStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "empty");
    cellStates.put(1, "fish");
    cellStates.put(2, "shark");
  }

  public WaTorStateHandler() {
    super(cellStates);
  }
}
