package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

public class FireStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "EMPTY");
    cellStates.put(1, "TREE");
    cellStates.put(2, "BURNING");
  }

  public FireStateHandler() {
    super(cellStates);
  }
}
