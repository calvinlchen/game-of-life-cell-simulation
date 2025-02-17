package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the Segregation simulation.
 * This class extends {@link CellStateHandlerStatic} and defines a fixed
 * set of states for the Segregation simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class SegregationStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "empty");
    cellStates.put(1, "agentA");
    cellStates.put(2, "agentB");
  }

  public SegregationStateHandler() {
    super(cellStates);
  }
}
