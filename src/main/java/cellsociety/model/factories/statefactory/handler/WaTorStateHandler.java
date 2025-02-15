package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

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
