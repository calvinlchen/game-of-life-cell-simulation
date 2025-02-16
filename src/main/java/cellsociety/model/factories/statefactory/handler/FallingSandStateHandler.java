package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

public class FallingSandStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "empty");
    cellStates.put(1, "steel");
    cellStates.put(2, "sand");
    cellStates.put(3, "water");
  }

  public FallingSandStateHandler() {
    super(cellStates);
  }
}
