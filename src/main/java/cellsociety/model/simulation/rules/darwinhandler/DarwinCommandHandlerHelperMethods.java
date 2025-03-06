package cellsociety.model.simulation.rules.darwinhandler;

import static cellsociety.model.util.constants.CellStates.DARWIN_EMPTY;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.constants.GridTypes.EdgeType;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiPredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// whooo this is all package protected so no java doc

class DarwinCommandHandlerHelperMethods {

  private static final Logger logger = LogManager.getLogger(
      DarwinCommandHandlerHelperMethods.class);
  private static Random random = new Random();

  private static Map<DirectionType, int[]> movementMap = Map.of(
      DirectionType.N, new int[]{-1, 0},
      DirectionType.NE, new int[]{-(1 / 2), 1 / 2},   // should normalize it? righhhhtt?
      DirectionType.E, new int[]{0, 1},
      DirectionType.SE, new int[]{1 / 2, 1 / 2},
      DirectionType.S, new int[]{1, 0},
      DirectionType.SW, new int[]{1 / 2, -1 / 2},
      DirectionType.W, new int[]{0, -1},
      DirectionType.NW, new int[]{-(1 / 2), -1 / 2}
  );


  static int getIntegerArgument(DarwinCommand command) {
    try {
      return Integer.parseInt(command.getArgument());
    } catch (NumberFormatException e) {
      logger.error("Invalid number format for go command, expected a integer, recieved {}",
          command.getArgument());
      throw new SimulationException("InvalidDarwinInstruction",
          List.of(command.getType().name(), command.getArgument()));
    }
  }

  static Random getRandom() {
    return random;
  }

  // so you can mock random outcome
  public static void setRandom(Random newRandom) {
    random = newRandom;
  }

  // recursion structure I had written
  // CHATGPT: helped me refactor these since I notcied they were similar wiht the BiPredicate
  // - prompt: I have 3 methods that use the same recursion struction but wiht a different cheeck
  //   for the truth condition <Insert one of them and the truth pattern> how do I refactor this
  //   so they can use the same recurison struction
  static boolean nearbyCondition(DarwinCell cell, int nearbyAhead, DirectionType direction,
      BiPredicate<DarwinCell, Grid> condition, Grid grid) {
    if (nearbyAhead <= 0) {
      return false; // Base case: stop searching when depth reaches 0
    }

    List<DarwinCell> neighbors = cell.getDirectionalNeighbors(direction);

    for (DarwinCell neighbor : neighbors) {
      if (condition.test(neighbor, grid)) {
        return true; // If condition is met, return true
      }

      if (nearbyCondition(neighbor, nearbyAhead - 1, direction, condition, grid)) {
        return true; // Recursive call to check deeper
      }
    }

    return false; // If no match found within the given depth
  }

  static boolean nearbyEnemy(DarwinCell cell, int nearbyAhead, DirectionType direction) {
    return nearbyCondition(cell, nearbyAhead, direction,
        (neighbor, grid) -> neighbor.getCurrentState() != cell.getCurrentState(),
        null // Grid is not needed in this case
    );
  }

  static boolean nearbyState(DarwinCell cell, int state, int nearbyAhead, DirectionType direction) {
    return nearbyCondition(cell, nearbyAhead, direction,
        (neighbor, grid) -> neighbor.getCurrentState() == state,
        null // Grid is not needed in this case
    );
  }

  static boolean nearbyBoundary(DarwinCell cell, Grid grid, int nearbyAhead,
      DirectionType direction) {
    if (grid.getEdgeType() != EdgeType.NONE) {
      return false; // If the grid has boundaries, there's no out-of-bounds area
    }

    return nearbyCondition(cell, nearbyAhead, direction,
        (neighbor, g) -> {
          int[] pos = neighbor.getPosition();
          return pos[0] < 0 || pos[0] >= g.getRows() || pos[1] < 0 || pos[1] >= g.getCols();
        },
        grid
    );
  }

  static void rotate(DarwinCell cell, int degrees, boolean isRight) {
    // direction types clockwise
    List<DirectionType> directions = List.of(
        DirectionType.N, DirectionType.NE, DirectionType.E, DirectionType.SE,
        DirectionType.S, DirectionType.SW, DirectionType.W, DirectionType.NW
    );

    DirectionType currentDirection = cell.getDirection();
    int currentIndex = directions.indexOf(currentDirection);
    // should never be -1 bc we love enums -- sorry for my code comments
    // this is how I think when I can't talk outloud, I'll clean them up if I have time

    // round to the nearest 45th degree and then do that fun remainder thing to determine
    // how many offsets of 8 directions there is (this should work I hope), but my recent math
    // has been questionable
    int steps = (degrees / 45) % 8;

    if (!isRight) {
      steps = -steps;     // right is clockwise, so if its left counter clockwise
    }

    // get the new position, and also make sure valid
    int newIndex = (currentIndex + steps + 8) % 8;
    cell.setDirection(directions.get(newIndex));
  }

  static void move(DarwinCell cell, int move, Grid grid) {
    int[] position = cell.getPosition();

    int[] proportionalOffset = movementMap.get(cell.getDirection());
    int newX = position[0] + move * proportionalOffset[0];
    int newY = position[1] + move * proportionalOffset[1];

    // if it valid and not occpied or to be occupied
    if (grid.isValidPosition(position[0], position[1])
        || grid.getCell(position[0], position[1]).getCurrentState() != DARWIN_EMPTY
        || grid.getCell(position[0], position[1]).getCurrentState() != DARWIN_EMPTY) {
      cell.setPosition(position);
    }
  }
}
