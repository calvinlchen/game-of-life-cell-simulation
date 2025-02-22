package cellsociety.model.factories.statefactory.handler;

import cellsociety.model.util.constants.CellStates;
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
    cellStates.put(CellStates.SEGREGATION_EMPTY, "empty");
    cellStates.put(CellStates.SEGREGATION_A, "agentA");
    cellStates.put(CellStates.SEGREGATION_B, "agentB");
  }

  public SegregationStateHandler() {
    super(cellStates);
  }
}
