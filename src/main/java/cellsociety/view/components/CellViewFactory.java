package cellsociety.view.components;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.view.interfaces.CellView;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class CellViewFactory {
  private static final Map<SimType, BiFunction<Double[], Integer, CellView>> factoryMap = new HashMap<>();

  static {
    factoryMap.put(SimType.GAMEOFLIFE, (params, state) ->
        new GameOfLifeCellView(params[0], params[1], params[2], params[3], state));

    factoryMap.put(SimType.FIRE, (params, state) ->
        new FireCellView(params[0], params[1], params[2], params[3], state));

    factoryMap.put(SimType.PERCOLATION, (params, state) ->
        new PercolationCellView(params[0], params[1], params[2], params[3], state));

    factoryMap.put(SimType.SEGREGATION, (params, state) ->
        new SegregationCellView(params[0], params[1], params[2], params[3], state));

    factoryMap.put(SimType.WATOR, (params, state) ->
        new WaTorCellView(params[0], params[1], params[2], params[3], state));
  }

  public static CellView createCellView(SimType simType, double[] position, double width, double height, Integer cellState) {
    BiFunction<Double[], Integer, CellView> constructor = factoryMap.get(simType);
    if (constructor == null) {
      throw new IllegalArgumentException("Unsupported simulation type: " + simType);
    }
    return constructor.apply(new Double[]{position[0], position[1], width, height}, cellState);
  }
}
