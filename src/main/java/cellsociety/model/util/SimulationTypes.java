package cellsociety.model.util;

public class SimulationTypes {

  /**
   * Enum for representing the types of simulations the program can run
   */
  public enum SimType {
    GameOfLife(false, true), Percolation(false, false), Fire(false, false), Segregation(false,
        true), WaTor(false, false), FallingSand(false, true), RockPaperSciss(true, true), Langton(
        false, false), ChouReg2(false, false), Petelka(false, true);

    private final boolean isDynamic;
    private final boolean defaultRectangularGrid;

    SimType(boolean isDynamic, boolean defaultRectangularGrid) {
      this.isDynamic = isDynamic;
      this.defaultRectangularGrid = defaultRectangularGrid;
    }

    public boolean isDynamic() {
      return isDynamic;
    }

    public boolean isDefaultRectangularGrid() {
      return defaultRectangularGrid;
    }
  }

}
