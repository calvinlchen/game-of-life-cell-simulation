package cellsociety.model.util;

/**
 * Defines enums for representing the states of cells in various simulations, this should be where
 * the enums for cells are from.
 *
 * @author Jessica Chen
 */
public class CellStates {

  /**
   * Enum for representing the states of a cell in Conway's Game of Life
   */
  public enum GameOfLifeStates {
    ALIVE,
    DEAD
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

}
