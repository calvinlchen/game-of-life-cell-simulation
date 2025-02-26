package cellsociety.view.components.cell;

import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.view.interfaces.CellView;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * CellViewFactory is responsible for creating CellView objects based on the simulation type.
 *
 * <p>This factory uses a mapping of simulation types to corresponding CellView constructors,
 * allowing for dynamic instantiation of different cell views.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class CellViewFactory {

  private static final Map<SimType, BiFunction<Double[], Integer, CellView>> factoryMap =
      new HashMap<>();

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

  /**
   * Creates a CellView instance based on the given simulation type.
   *
   * @param simType   - the type of simulation
   * @param position  - an array containing the x and y coordinates of the cell
   * @param width     - the width of the cell
   * @param height    - the height of the cell
   * @param cellState - the initial state of the cell
   * @return a CellView object corresponding to the simulation type
   * @throws IllegalArgumentException if the simulation type is unsupported
   */
  public static CellView createCellView(SimType simType, double[] position, double width,
      double height, Integer cellState) {
    BiFunction<Double[], Integer, CellView> constructor = factoryMap.get(simType);
    if (constructor == null) {
      throw new IllegalArgumentException("Unsupported simulation type: " + simType);
    }
    return constructor.apply(new Double[]{position[0], position[1], width, height}, cellState);
  }
}
