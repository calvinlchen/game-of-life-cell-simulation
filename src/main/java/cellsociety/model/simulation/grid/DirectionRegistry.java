package cellsociety.model.simulation.grid;

import cellsociety.model.util.constants.GridTypes.DirectionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The {@code DirectionRegistry} class provides a mapping between Cartesian coordinate-based
 * direction representations (e.g., "0,1") and their corresponding {@code DirectionType}
 * enumerations.
 *
 * <p><b>Design:</b></p>
 * <ul>
 *   <li>Maintains a predefined mapping of direction strings to {@link DirectionType}.</li>
 *   <li>Allows efficient lookup of directional mappings without object creation.</li>
 *   <li>Uses {@link Optional} to handle cases where no mapping is found.</li>
 * </ul>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * Optional&lt;DirectionType&lt; direction = DirectionRegistry.getDirection("0,1");
 * direction.ifPresent(d -> System.out.println("Mapped direction: " + d));
 * // Output: Mapped direction: E
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
class DirectionRegistry {

  private static final Logger logger = LogManager.getLogger(DirectionRegistry.class);
  private static final Map<String, DirectionType> directionMap = new HashMap<>();

  static {
    directionMap.put("0,1", DirectionType.E);
    directionMap.put("0,-1", DirectionType.W);
    directionMap.put("1,0", DirectionType.S);
    directionMap.put("-1,0", DirectionType.N);
    directionMap.put("1,1", DirectionType.SE);
    directionMap.put("1,-1", DirectionType.SW);
    directionMap.put("-1,1", DirectionType.NE);
    directionMap.put("-1,-1", DirectionType.NW);
  }

  /**
   * Retrieves the corresponding {@link DirectionType} for the given direction string.
   *
   * <p>The format of the input string should be "x,y" where:
   * <ul>
   *   <li>x = 0 means no horizontal movement, -1 means left, +1 means right.</li>
   *   <li>y = 0 means no vertical movement, -1 means up, +1 means down.</li>
   * </ul>
   *
   * <p>Example inputs and outputs:
   * <ul>
   *   <li>"0,1" → {@code DirectionType.E} (East)</li>
   *   <li>"-1,-1" → {@code DirectionType.NW} (Northwest)</li>
   *   <li>"2,2" → {@code Optional.empty()} (Invalid input)</li>
   * </ul>
   *
   * @param direction The string representing a direction, in the format "x,y".
   * @return an {@link Optional} containing the corresponding {@code DirectionType}, or empty if the
   * direction is not found in the registry.
   */
  static Optional<DirectionType> getDirection(String direction) {
    Optional<DirectionType> result = Optional.ofNullable(directionMap.get(direction));

    if (result.isEmpty()) {
      logger.warn("Direction {} not found in mapping", direction);
    }

    return result;
  }
}
