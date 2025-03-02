package cellsociety.model.simulation.grid;

import cellsociety.model.simulation.grid.griddirectionstrategy.EvenOddParityGridDirectionStrategy;
import cellsociety.model.simulation.grid.griddirectionstrategy.GridDirectionStrategy;
import cellsociety.model.simulation.grid.griddirectionstrategy.NoEvenOddParityGridDirectionStrategy;
import cellsociety.model.util.constants.GridTypes.NeighborhoodType;
import cellsociety.model.util.constants.GridTypes.ShapeType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code GridDirectionRegistry} class is responsible for storing and retrieving movement
 * direction offsets based on grid shape types and neighborhood types.
 *
 * <p><b>Overview:</b></p>
 * <ul>
 *   <li>Implements the Strategy Pattern via {@link GridDirectionStrategy}, allowing different movement
 *       rules for different grid structures.</li>
 *   <li>Supports even-odd row parity handling for hexagonal and triangular grids, where movement
 *       patterns differ based on row index.</li>
 *   <li>Provides fast lookup of movement directions via a static registry.</li>
 * </ul>
 *
 * <p><b>Implemented Strategies:</b></p>
 * <ul>
 *   <li>{@link NoEvenOddParityGridDirectionStrategy} - Used for rectangular grids where all rows
 *       follow the same movement rules.</li>
 *   <li>{@link EvenOddParityGridDirectionStrategy} - Used for hexagonal and triangular grids,
 *       where even and odd rows have different movement rules.</li>
 * </ul>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * Optional&lt;int[][]&gt; directions = GridDirectionRegistry.getDirections(ShapeType.HEXAGON, NeighborhoodType.MOORE, 3);
 * directions.ifPresent(dir -> System.out.println(Arrays.deepToString(dir)));
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
class GridDirectionRegistry {

  private static final Logger logger = LogManager.getLogger(GridDirectionRegistry.class);
  private static final Map<String, GridDirectionStrategy> strategyMap = new HashMap<>();

  static {
    strategyMap.put("RECTANGLE_MOORE", new NoEvenOddParityGridDirectionStrategy(
        new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}}));
    strategyMap.put("RECTANGLE_VON_NEUMANN",
        new NoEvenOddParityGridDirectionStrategy(new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}));
    strategyMap.put("RECTANGLE_EXTENDED_MOORE", new NoEvenOddParityGridDirectionStrategy(
        new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {-2, -2},
            {-1, -2}, {0, -2}, {1, -2}, {2, -2}, {2, 2}, {1, 2}, {0, 2}, {-1, 2}, {-2, 2}, {-2, -1},
            {-2, 0}, {-2, 1}, {2, -1}, {2, 0}, {2, 1}}));

    strategyMap.put("HEXAGON_MOORE", new EvenOddParityGridDirectionStrategy(
        Map.of(true, new int[][]{{-1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 0}}, // Even row
            false, new int[][]{{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, 1}}  // Odd row
        )));
    strategyMap.put("HEXAGON_VON_NEUMANN",
        new NoEvenOddParityGridDirectionStrategy(new int[][]{{0, -1}, {0, 1}}));
    strategyMap.put("HEXAGON_EXTENDED_MOORE", new EvenOddParityGridDirectionStrategy(Map.of(true,
        new int[][]{{-2, -1}, {-2, 0}, {-2, 1}, {-1, -2}, {-1, -1}, {-1, 0}, {-1, 1}, {0, -2},
            {0, -1}, {0, 1}, {0, 2}, {1, -2}, {1, -1}, {1, 0}, {1, 1}, {2, -1}, {2, 0}, {2, 1}},
        false, new int[][]{{-2, -1}, {-2, 0}, {-2, 1}, {-1, -1}, {-1, 0}, {-1, 1}, {-1, 2}, {0, -2},
            {0, -1}, {0, 1}, {0, 2}, {1, -1}, {1, 0}, {1, 1}, {1, 2}, {2, -1}, {2, 0}, {2, 1}})));

    strategyMap.put("TRIANGLE_MOORE", new EvenOddParityGridDirectionStrategy(Map.of(true,
        new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -2}, {0, -1}, {0, 1}, {0, 2}, {1, -2}, {1, -1},
            {1, 0}, {1, 1}, {1, 2}}, false,
        new int[][]{{-1, -2}, {-1, -1}, {-1, 0}, {-1, 1}, {-1, 2}, {0, -2}, {0, -1}, {0, 1}, {0, 2},
            {1, -1}, {1, 0}, {1, 1}})));
    strategyMap.put("TRIANGLE_VON_NEUMANN", new EvenOddParityGridDirectionStrategy(
        Map.of(true, new int[][]{{-1, 0}, {0, -1}, {0, 1}}, false,
            new int[][]{{0, -1}, {0, 1}, {1, 0}})));
    strategyMap.put("TRIANGLE_EXTENDED_MOORE", new EvenOddParityGridDirectionStrategy(Map.of(true,
        new int[][]{{-2, -2}, {-2, -1}, {-2, 0}, {-2, 1}, {-2, 2}, {-1, -3}, {-1, -2}, {-1, -1},
            {-1, 0}, {-1, 1}, {-1, 2}, {-1, 3}, {0, -4}, {0, -3}, {0, -2}, {0, -1}, {0, 1}, {0, 2},
            {0, 3}, {0, 4}, {1, -4}, {1, -3}, {1, -2}, {1, -1}, {1, 0}, {1, 1}, {1, 2}, {1, 3},
            {1, 4}, {2, -3}, {2, -2}, {2, -1}, {2, 0}, {2, 1}, {2, 2}, {2, 3}}, false,
        new int[][]{{-2, -3}, {-2, -2}, {-2, -1}, {-2, 0}, {-2, 1}, {-2, 2}, {-2, 3}, {-1, -4},
            {-1, -3}, {-1, -2}, {-1, -1}, {-1, 0}, {-1, 1}, {-1, 2}, {-1, 3}, {-1, 4}, {0, -4},
            {0, -3}, {0, -2}, {0, -1}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, -3}, {1, -2}, {1, -1},
            {1, 0}, {1, 1}, {1, 2}, {1, 3}, {2, -2}, {2, -1}, {2, 0}, {2, 1}, {2, 2}})));
  }

  /**
   * Retrieves the movement directions for a given grid shape, neighborhood type, and row
   * index.
   *
   * <p>The method first looks up the corresponding strategy for the given shape and
   * neighborhood type. If found, it returns the movement directions based on the row index.</p>
   *
   * @param shape        The type of grid shape (e.g., RECTANGLE, HEXAGON, TRIANGLE).
   * @param neighborhood The neighborhood pattern defining movement rules (e.g., MOORE,
   *                     VON_NEUMANN).
   * @param row          The row index (only relevant for hexagonal and triangular
   *                     grids).
   * @return An {@code Optional<int[][]>} containing the movement offsets, or
   * {@code Optional.empty()} if no matching strategy exists.
   */
  static Optional<int[][]> getDirections(ShapeType shape, NeighborhoodType neighborhood, int row) {
    String key = shape.name() + "_" + neighborhood.name();

    return Optional.ofNullable(strategyMap.get(key))
        .map(strategy -> Optional.ofNullable(strategy.getDirections(row))).orElseGet(() -> {
          logger.warn("No strategy found for: {}", key);
          return Optional.empty();
        });

  }
}

