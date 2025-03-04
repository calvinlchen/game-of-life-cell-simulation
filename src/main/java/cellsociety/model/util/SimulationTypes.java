package cellsociety.model.util;


/**
 * Utility class for defining simulation types.
 *
 * <p>This class contains an enumeration of the various types of simulations
 * that the program can run, along with metadata about their properties.
 *
 * @author Kyaira Boughton
 * @author Jessica Chen
 * @author ChatGPT, helped with some of the JavaDocs
 */
public class SimulationTypes {

  /**
   * Enum representing the types of simulations the program can run.
   *
   * <p>Each simulation type includes metadata regarding whether it supports
   * dynamic states and whether it defaults to a rectangular grid.
   */
  public enum SimType {
    GameOfLife(false, true),
    Percolation(false, false),
    Fire(false, false),
    Segregation(false, true),
    WaTor(false, false),
    FallingSand(false, true),
    RockPaperSciss(true, true),
    Langton(false, false),
    ChouReg2(false, false),
    Petelka(false, true),
    Darwin(false, true);

    private final boolean isDynamic;
    private final boolean defaultRectangularGrid;

    /**
     * Constructs a SimType enum with specified properties.
     *
     * @param isDynamic              - whether the simulation type supports dynamic states
     * @param defaultRectangularGrid - whether the simulation defaults to a rectangular grid
     */
    SimType(boolean isDynamic, boolean defaultRectangularGrid) {
      this.isDynamic = isDynamic;
      this.defaultRectangularGrid = defaultRectangularGrid;
    }

    /**
     * Checks if the simulation type supports dynamic states.
     *
     * @return true if the simulation type supports dynamic states, false otherwise
     */
    public boolean isDynamic() {
      return isDynamic;
    }

    /**
     * Checks if the simulation type defaults to a rectangular grid.
     *
     * @return true if the simulation type defaults to a rectangular grid, false otherwise
     */
    @Deprecated
    public boolean isDefaultRectangularGrid() {
      return defaultRectangularGrid;
    }
  }

}
