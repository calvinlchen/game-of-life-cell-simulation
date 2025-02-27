package cellsociety.model.util.constants;

/**
 * Utility class for defining the different types of configurations for grid topology.
 *
 * @author Jessica Chen
 */
public class GridTypes {

  /**
   * Enum representing the types of shapes the program can run.
   */
  public enum ShapeType {
    RECTANGLE,
    HEXAGON,
    TRIANGLE
  }

  /**
   * Enum representing the types of neighborhood the program can run.
   */
  public enum NeighborhoodType {
    MOORE,
    VON_NEUMANN,
    EXTENDED_MOORE
  }

  /**
   * Enum representing the types of edges the program can run.
   */
  public enum EdgeType {
    NONE,
    MIRROR,
    TOROIDAL
  }
}
