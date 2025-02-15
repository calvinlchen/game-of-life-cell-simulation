package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

public class WaTorStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "EMPTY");
    cellStates.put(1, "Fish");
    cellStates.put(2, "Shark");
  }

  public WaTorStateHandler() {
    super(cellStates);
  }
}
