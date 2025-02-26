package cellsociety.model.factories.statefactory.handler;

import cellsociety.model.util.constants.CellStates;
import java.util.HashMap;
import java.util.Map;

/**
 * A predefined state handler for the Falling Sand simulation. This class extends
 * {@link CellStateHandlerStatic} and defines a fixed set of states for the Falling Sand
 * simulation.
 *
 * @author Jessica Chen
 * @author javadoc by ChatGPT, edited by Jessica Chen
 */
public class FallingSandStateHandler extends CellStateHandlerStatic {

  private static final Map<Integer, String> cellStates = new HashMap<>();

  static {
    cellStates.put(CellStates.FALLINGSAND_EMPTY, "empty");
    cellStates.put(CellStates.FALLINGSAND_STEEL, "steel");
    cellStates.put(CellStates.FALLINGSAND_SAND, "sand");
    cellStates.put(CellStates.FALLINGSAND_WATER, "water");
  }

  public FallingSandStateHandler() {
    super(cellStates);
  }
}
