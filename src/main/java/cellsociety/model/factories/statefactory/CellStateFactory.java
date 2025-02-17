package cellsociety.model.factories.statefactory;

import cellsociety.model.factories.statefactory.handler.*;
import cellsociety.model.util.SimulationTypes.SimType;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for creating and managing cell state handlers for different simulation types. It
 * maintains a static map of predefined handlers for known simulation types and dynamically creates
 * new handlers for simulations with dynamic states.
 *
 * <p> Assumption: Each simulation type must have a corresponding state handler defined.
 * If the simulation type is dynamic, a new state handler is created per simulation instance, for
 * dynamic need unique ID and the number of states for that simulation.
 *
 * <p> If simulation type is static, still need to put in ID and number of states, but these values
 * do not matter
 *
 * @author Jessica Chen
 */
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
    handlerMap.put(SimType.Langton, new LangtonStateHandler());
    handlerMap.put(SimType.ChouReg2, new LangtonStateHandler());
    handlerMap.put(SimType.Petelka, new PetelkaStateHandler());
  }

  /**
   * Retrieves the cell state handler for a simulation, dynamically creating a new handler if
   * necessary
   *
   * <p> If simulation type is static, still need to put in ID and number of states, but these
   * values do not matter
   *
   * @param simulationID   - unique identifier for the simulation instance (does not matter if
   *                       static)
   * @param simulationType - the type of simulation
   * @param numStates      - number of states in the simulation (foes not matter if static
   * @return CellStateHandler or the simulation
   * @throws IllegalArgumentException if the simulation type is not recognized
   */
  public static CellStateHandler getHandler(int simulationID, SimType simulationType,
      int numStates) {
    if (simulationType.isDynamic()) {
      return dynamicHandlerMap.computeIfAbsent(simulationID,
          k -> createNewDynamicStateHandler(numStates));
    }

    return getHandler(simulationType);
  }

  // TODO: catch error if simulation type is not valid
  private static CellStateHandlerStatic getHandler(SimType simulationType) {
    return handlerMap.get(simulationType);
  }

  private static CellStateHandlerDynamic createNewDynamicStateHandler(int numStates) {
    CellStateHandlerDynamic dynamicHandler = new CellStateHandlerDynamic();

    for (int i = 0; i < numStates; i++) {
      dynamicHandler.addState(i, "state" + i);  // state0, state1....
    }

    return dynamicHandler;
  }

}
