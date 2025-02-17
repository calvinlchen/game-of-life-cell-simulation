package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the Spreading of Fire simulation.
 * This class extends {@link CellStateHandlerStatic} and defines a fixed
 * set of states for the Spreading of Fire simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class FireStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "empty");
    cellStates.put(1, "tree");
    cellStates.put(2, "burning");
  }

  public FireStateHandler() {
    super(cellStates);
  }
}
