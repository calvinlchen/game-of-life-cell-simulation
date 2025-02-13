package cellsociety.model.util.constants;

/**
 * Defines enums for representing the states of cells in various simulations, this should be where
 * the enums for cells are from.
 *
 * @author Jessica Chen
 */
public class CellStates {
  public final int GAMEOFLIFE_DEAD = 0;
  public final int GAMEOFLIFE_ALIVE = 1;

  public final int PERCOLATION_BLOCKED = 0;
  public final int PERCOLATION_OPEN = 1;
  public final int PERCOLATION_PERCOLATED = 2;

  public final int FIRE_EMPTY = 0;
  public final int FIRE_TREE = 1;
  public final int FIRE_BURNING = 2;

  public final int SEGREGATION_EMPTY = 0;
  public final int SEGREGATION_A = 1;
  public final int SEGREGATION_B = 2;

  public final int WATOR_EMPTY = 0;
  public final int WATOR_FISH = 1;
  public final int WATOR_SHARK = 2;

  /**
   * Enum for represent the types of simulation
   */
  public enum SimulationTypes {
    GameOfLife,
    Percolation,
    Fire,
    Segregation,
    WaTor
  }

  /**
   * Enum for representing the states of a cell in Conway's Game of Life
   */
  public enum GameOfLifeStates {
    DEAD,
    ALIVE
  }

  /**
   * Enum for representing the states of a cell in Percolation
   */
  public enum PercolationStates {
    BLOCKED,
    OPEN,
    PERCOLATED
  }

  /**
   * Enum for representing the states of a cell in SpreadingOfFire
   */
  public enum FireStates {
    EMPTY,
    TREE,
    BURNING
  }

  /**
   * Enum for representing the states of a cell in Schelling's Model of Segregation
   */
  public enum SegregationStates {
    EMPTY,
    AGENT_A,
    AGENT_B
  }

  /**
   * Enum for representing the states of a cell in WaTor World
   */
  public enum WaTorStates {
    EMPTY,
    FISH,
    SHARK
  }

}
