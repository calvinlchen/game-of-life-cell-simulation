package cellsociety.model.simulation.grid;

import cellsociety.model.util.constants.GridTypes.DirectionType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The {@code DirectionFactory} class provides a utility for mapping string representations of
 * directions, based on Cartesian coordinate format, to their corresponding {@code DirectionType}
 * enumerations.
 * <p>
 * This class maintains a predefined mapping of direction strings (e.g., "0,1") to
 * {@code DirectionType} values. It allows for retrieval of these mappings to facilitate the
 * interpretation of directional data.
 *
 * @author Jessica Chen
 * @author ChatGPT helped with javadocs
 */
public class DirectionFactory {

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
   * Retrieves the corresponding DirectionType for the given direction string.
   *
   * @param direction - the string representing a direction, in a specific format such as "0,1"
   *                  where the format corresponds to Cartesian coordinates.
   * @return an {@code Optional} containing the corresponding {@code DirectionType} if the direction
   * exists in the predefined mappings, or an empty {@code Optional} if no match is found.
   */
  public static Optional<DirectionType> getDirection(String direction) {
    return Optional.ofNullable(directionMap.get(direction));
  }

}
