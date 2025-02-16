package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

public class PetelkaStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    for (int i = 0; i < 5; i++) {
      cellStates.put(i, "state" + i);
    }
  }

  public PetelkaStateHandler() {
    super(cellStates);
  }
}
