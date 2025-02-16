package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

public class LangtonStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "state0");
    cellStates.put(1, "state1");
    cellStates.put(2, "state2");
    cellStates.put(3, "state3");
    cellStates.put(4, "state4");
    cellStates.put(5, "state5");
    cellStates.put(6, "state6");
    cellStates.put(7, "state7");
  }

  public LangtonStateHandler() {
    super(cellStates);
  }
}
