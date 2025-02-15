package cellsociety.model.factories;

import cellsociety.model.factories.statefactory.handler.*;
import cellsociety.model.util.SimulationTypes.SimType;
import java.util.HashMap;
import java.util.Map;

// NOTE: I actually don't think this works for RPS, so I think we just make a catch just for RPS
// its because RPS needs to be able to support multiple simulations so its not a final number
// of states
public class CellStateFactory {
  private static final Map<SimType, CellStateHandlerStatic> handlerMap = new HashMap<>();

  static {
    handlerMap.put(SimType.GAMEOFLIFE, new GameOfLifeStateHandler());
    handlerMap.put(SimType.FIRE, new FireStateHandler());
    handlerMap.put(SimType.PERCOLATION, new PercolationStateHandler());
    handlerMap.put(SimType.SEGREGATION, new SegregationStateHandler());
    handlerMap.put(SimType.WATOR, new WaTorStateHandler());
  }

  // TODO: catch error if simulation type is not valid
  public static CellStateHandlerStatic getHandler(SimType simulationType) {
    return handlerMap.get(simulationType);
  }

}
