package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the Falling Sand simulation.
 * This class extends {@link CellStateHandlerStatic} and defines a fixed
 * set of states for the Falling Sand simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class FallingSandStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "empty");
    cellStates.put(1, "steel");
    cellStates.put(2, "sand");
    cellStates.put(3, "water");
  }

  public FallingSandStateHandler() {
    super(cellStates);
  }
}
