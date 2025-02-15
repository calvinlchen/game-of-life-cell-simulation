package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

public class SegregationStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "EMPTY");
    cellStates.put(1, "A");
    cellStates.put(2, "B");
  }

  public SegregationStateHandler() {
    super(cellStates);
  }
}
