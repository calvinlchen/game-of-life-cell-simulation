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
    RECTANGLE, HEXAGON, TRIANGLE
  }

  /**
   * Enum representing the types of neighborhood the program can run.
   */
  public enum NeighborhoodType {
    MOORE, VON_NEUMANN, EXTENDED_MOORE
  }

  /**
   * Enum representing the types of edges the program can run.
   */
  public enum EdgeType {
    NONE, MIRROR, TOROIDAL
  }

  /**
   * Enum representing the types of directional of the neighbors.
   *
   * <p>This is relevant here, because with the new edge cases, you can no longer solve neighbors
   * direction with purely math
   * <p>While this should allow falling sand to function as normal, the langtons loop and their
   * variations will remain broken as they were not made to handle these grid topologies
   */
  public enum DirectionType {
    N, NE, E, SE, S, SW, W, NW
  }
}
