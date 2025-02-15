package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

public class GameOfLifeStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "dead");
    cellStates.put(1, "alive");
  }

  public GameOfLifeStateHandler() {
    super(cellStates);
  }
}
