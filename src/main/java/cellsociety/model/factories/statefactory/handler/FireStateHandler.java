package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

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
