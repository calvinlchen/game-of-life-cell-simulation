package cellsociety.view.components.cell;

import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.view.interfaces.CellView;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class CellViewFactory {

  private static final Map<SimType, BiFunction<Double[], Integer, CellView>> factoryMap = new HashMap<>();

  static {
    factoryMap.put(SimType.GameOfLife,
        (params, state) -> new GameOfLifeCellView(params[0], params[1], params[2], params[3],
            state));

    factoryMap.put(SimType.Fire,
        (params, state) -> new FireCellView(params[0], params[1], params[2], params[3], state));

    factoryMap.put(SimType.Percolation,
        (params, state) -> new PercolationCellView(params[0], params[1], params[2], params[3],
            state));

    factoryMap.put(SimType.Segregation,
        (params, state) -> new SegregationCellView(params[0], params[1], params[2], params[3],
            state));

    factoryMap.put(SimType.WaTor,
        (params, state) -> new WaTorCellView(params[0], params[1], params[2], params[3], state));

    factoryMap.put(SimType.FallingSand,
        (params, state) -> new FallingSandCellView(params[0], params[1], params[2], params[3],
            state));

    factoryMap.put(SimType.Langton,
        (params, state) -> new LangtonCellView(params[0], params[1], params[2], params[3], state));

    factoryMap.put(SimType.ChouReg2,
        (params, state) -> new ChouReg2CellView(params[0], params[1], params[2], params[3], state));

    factoryMap.put(SimType.Petelka,
        (params, state) -> new PetelkaCellView(params[0], params[1], params[2], params[3], state));
  }

  public static CellView createCellView(SimType simType, double[] position, double width,
      double height, Integer cellState) {
    BiFunction<Double[], Integer, CellView> constructor = factoryMap.get(simType);
    if (constructor == null) {
      throw new IllegalArgumentException("Unsupported simulation type: " + simType);
    }
    return constructor.apply(new Double[]{position[0], position[1], width, height}, cellState);
  }
}
