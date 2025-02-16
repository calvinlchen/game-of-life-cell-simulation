package cellsociety.model.factories.statefactory;

import cellsociety.model.factories.statefactory.handler.*;
import cellsociety.model.util.SimulationTypes.SimType;
import java.util.HashMap;
import java.util.Map;

public class CellStateFactory {
  private static final Map<SimType, CellStateHandlerStatic> handlerMap = new HashMap<>();
  private static final Map<Integer, CellStateHandlerDynamic> dynamicHandlerMap = new HashMap<>();

  static {
    handlerMap.put(SimType.GameOfLife, new GameOfLifeStateHandler());
    handlerMap.put(SimType.Fire, new FireStateHandler());
    handlerMap.put(SimType.Percolation, new PercolationStateHandler());
    handlerMap.put(SimType.Segregation, new SegregationStateHandler());
    handlerMap.put(SimType.WaTor, new WaTorStateHandler());
    handlerMap.put(SimType.FallingSand, new FallingSandStateHandler());
  }

  // TODO: catch error if simulation type is not valid
  public static CellStateHandlerStatic getHandler(SimType simulationType) {
    return handlerMap.get(simulationType);
  }

  public static CellStateHandler getHandler(int simulationID, SimType simulationType, int numStates) {
    if (simulationType.isDynamic()) {
      return dynamicHandlerMap.computeIfAbsent(simulationID, k -> createNewDynamicStateHandler(numStates));
    }

    return getHandler(simulationType);
  }

  private static CellStateHandlerDynamic createNewDynamicStateHandler(int numStates) {
    CellStateHandlerDynamic dynamicHandler = new CellStateHandlerDynamic();

    for (int i = 0; i < numStates; i++) {
      dynamicHandler.addState(i, "State" + i);  // State0, State1....
    }

    return dynamicHandler;
  }

}
