package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.Map;

public class PercolationStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(0, "blocked");
    cellStates.put(1, "open");
    cellStates.put(2, "percolated");
  }

  public PercolationStateHandler() {
    super(cellStates);
  }
}
