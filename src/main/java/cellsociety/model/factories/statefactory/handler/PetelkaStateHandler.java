package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the Petelka's Loop simulation. This class extends
 * {@link CellStateHandlerStatic} and defines a fixed set of states for the Petelka's Loop
 * simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class PetelkaStateHandler extends CellStateHandlerStatic {

  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    for (int i = 0; i < 5; i++) {
      cellStates.put(i, "state" + i);
    }
  }

  public PetelkaStateHandler() {
    super(cellStates);
  }
}
