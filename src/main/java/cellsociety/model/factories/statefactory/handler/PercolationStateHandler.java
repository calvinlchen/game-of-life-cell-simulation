package cellsociety.model.factories.statefactory.handler;

import cellsociety.model.util.constants.CellStates;
import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the Percolation simulation.
 * This class extends {@link CellStateHandlerStatic} and defines a fixed
 * set of states for the Percolation simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class PercolationStateHandler extends CellStateHandlerStatic {
  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(CellStates.PERCOLATION_BLOCKED, "blocked");
    cellStates.put(CellStates.PERCOLATION_OPEN, "open");
    cellStates.put(CellStates.PERCOLATION_PERCOLATED, "percolated");
  }

  public PercolationStateHandler() {
    super(cellStates);
  }
}
