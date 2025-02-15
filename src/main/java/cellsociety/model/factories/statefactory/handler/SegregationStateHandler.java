package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

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
