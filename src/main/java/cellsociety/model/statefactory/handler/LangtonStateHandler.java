package cellsociety.model.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the Langton's Loop and ChouReg2 simulation. This class extends
 * {@link CellStateHandlerStatic} and defines a fixed set of states for the Langton's Loop and
 * ChouReg2 simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class LangtonStateHandler extends CellStateHandlerStatic {

  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    for (int i = 0; i < 8; i++) {
      cellStates.put(i, "state" + i);
    }
  }

  /**
   * Constructs a {@code LangtonStateHandler} with predefined states for the Langton's Loop
   * simulation.
   */
  public LangtonStateHandler() {
    super(cellStates);
  }
}
