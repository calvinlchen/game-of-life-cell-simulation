package cellsociety.model.factories;

import cellsociety.model.util.constants.GridTypes.NeighborhoodType;
import cellsociety.model.util.constants.GridTypes.ShapeType;
import java.util.HashMap;
import java.util.Map;

/**
 * Returns the Grid Direction based on the Shape and Topology of the passed in parameters.
 *
 * @author Jessica Chen
 */
public class GridFactory {

  private static final Map<String, Map<String, int[][]>> directionMap = new HashMap<>();

  static {
    Map<String, int[][]> rectangleMoore = new HashMap<>();
    rectangleMoore.put("even",
        new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}});
    rectangleMoore.put("odd",
        new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}});
    directionMap.put("RECTANGLE_MOORE", rectangleMoore);

    Map<String, int[][]> rectangleVonNeumann = new HashMap<>();
    rectangleVonNeumann.put("even", new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}});
    rectangleVonNeumann.put("odd", new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}});
    directionMap.put("RECTANGLE_VON_NEUMANN", rectangleVonNeumann);

    Map<String, int[][]> rectangleExtendedMoore = new HashMap<>();
    rectangleExtendedMoore.put("even",
        new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {-2, -2},
            {-1, -2}, {0, -2}, {1, -2}, {2, -2}, {2, 2}, {1, 2}, {0, 2}, {-1, 2}, {-2, 2}, {-2, -1},
            {-2, 0}, {-2, 1}, {2, -1}, {2, 0}, {2, 1}});
    rectangleExtendedMoore.put("odd",
        new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {-2, -2},
            {-1, -2}, {0, -2}, {1, -2}, {2, -2}, {2, 2}, {1, 2}, {0, 2}, {-1, 2}, {-2, 2}, {-2, -1},
            {-2, 0}, {-2, 1}, {2, -1}, {2, 0}, {2, 1}});
    directionMap.put("RECTANGLE_EXTENDED_MOORE", rectangleExtendedMoore);

    // we index rows from 0 so 0 is even
    // even rows have the 0th column slightly more left
    Map<String, int[][]> hexagonMoore = new HashMap<>();
    hexagonMoore.put("even", new int[][]{{-1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 0}});
    hexagonMoore.put("odd", new int[][]{{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, 1}});
    directionMap.put("HEXAGON_MOORE", hexagonMoore);

    Map<String, int[][]> hexagonVonNeumann = new HashMap<>();
    hexagonVonNeumann.put("even", new int[][]{{0, -1}, {0, 1}});
    hexagonVonNeumann.put("odd", new int[][]{{0, -1}, {0, 1}});
    directionMap.put("HEXAGON_VON_NEUMANN", hexagonVonNeumann);

    Map<String, int[][]> hexagonExtendedMoore = new HashMap<>();
    hexagonExtendedMoore.put("even",
        new int[][]{{-2, -1}, {-2, 0}, {-2, 1}, {-1, -2}, {-1, -1}, {-1, 0}, {-1, 1}, {0, -2},
            {0, -1}, {0, 1}, {0, 2}, {1, -2}, {1, -1}, {1, 0}, {1, 1}, {2, -1}, {2, 0}, {2, 1},});
    hexagonExtendedMoore.put("odd",
        new int[][]{{-2, -1}, {-2, 0}, {-2, 1}, {-1, -1}, {-1, 0}, {-1, 1}, {-1, 2}, {0, -2},
            {0, -1}, {0, 1}, {0, 2}, {1, -1}, {1, 0}, {1, 1}, {1, 2}, {2, -1}, {2, 0}, {2, 1},});
    directionMap.put("HEXAGON_EXTENDED_MOORE", hexagonExtendedMoore);

    // we index rows from 0 so 0 is technically even
    // even rows have bottom flat
    Map<String, int[][]> triangleMoore = new HashMap<>();
    triangleMoore.put("even",
        new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -2}, {0, -1}, {0, 1}, {0, 2}, {1, -2}, {1, -1},
            {1, 0}, {1, 1}, {1, 2}});
    triangleMoore.put("odd",
        new int[][]{{-1, -2}, {-1, -1}, {-1, 0}, {-1, 1}, {-1, 2}, {0, -2}, {0, -1}, {0, 1}, {0, 2},
            {1, -1}, {1, 0}, {1, 1}});
    directionMap.put("TRIANGLE_MOORE", triangleMoore);

    Map<String, int[][]> triangleVonNeumann = new HashMap<>();
    triangleVonNeumann.put("even", new int[][]{{-1, 0}, {0, -1}, {0, 1}});
    triangleVonNeumann.put("odd", new int[][]{{0, -1}, {0, 1}, {1, 0}});
    directionMap.put("TRIANGLE_VON_NEUMANN", triangleVonNeumann);

    Map<String, int[][]> triangleExtendedMoore = new HashMap<>();
    triangleExtendedMoore.put("even",
        new int[][]{{-2, -2}, {-2, -1}, {-2, 0}, {-2, 1}, {-2, 2}, {-1, -3}, {-1, -2}, {-1, -1},
            {-1, 0}, {-1, 1}, {-1, 2}, {-1, 3}, {0, -4}, {0, -3}, {0, -2}, {0, -1}, {0, 1}, {0, 2},
            {0, 3}, {0, 4}, {1, -4}, {1, -3}, {1, -2}, {1, -1}, {1, 0}, {1, 1}, {1, 2}, {1, 3},
            {1, 4}, {2, -3}, {2, -2}, {2, -1}, {2, 0}, {2, 1}, {2, 2}, {2, 3},});
    triangleExtendedMoore.put("odd",
        new int[][]{{-2, -3}, {-2, -2}, {-2, -1}, {-2, 0}, {-2, 1}, {-2, 2}, {-2, 3}, {-1, -4},
            {-1, -3}, {-1, -2}, {-1, -1}, {-1, 0}, {-1, 1}, {-1, 2}, {-1, 3}, {-1, 4}, {0, -4},
            {0, -3}, {0, -2}, {0, -1}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, -3}, {1, -2}, {1, -1},
            {1, 0}, {1, 1}, {1, 2}, {1, 3}, {2, -2}, {2, -1}, {2, 0}, {2, 1}, {2, 2}});
    directionMap.put("TRIANGLE_EXTENDED_MOORE", triangleExtendedMoore);
  }

  /**
   * Retrieves the grid directions based on the specified shape type and neighborhood type.
   *
   * @param shape        - the type of grid shape (e.g., RECTANGLE, HEXAGON, TRIANGLE)
   * @param neighborhood - the type of neighborhood pattern (e.g., MOORE, VON_NEUMANN,
   *                     EXTENDED_MOORE)
   * @return a 2D integer array representing the possible directional offsets for the given
   * combination of shape and neighborhood
   */
  public static Map<String, int[][]> getDirections(ShapeType shape, NeighborhoodType neighborhood) {
    return directionMap.get(shape.name() + "_" + neighborhood.name());
  }
}
