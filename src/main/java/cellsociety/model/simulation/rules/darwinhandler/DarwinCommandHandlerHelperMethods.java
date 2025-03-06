package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.constants.GridTypes.EdgeType;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// helper methods for handlers
class DarwinCommandHandlerHelperMethods {

  private static final Logger logger = LogManager.getLogger(
      DarwinCommandHandlerHelperMethods.class);
  private static Random random = new Random();


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

  static boolean nearbyEnemy(DarwinCell cell) {
    // enemy is any different species
    List<DarwinCell> enemyNeighbor = cell.getDirectionalNeighbors(cell.getDirection()).stream()
        .filter(
            neighbor -> neighbor.getCurrentState() != cell.getCurrentState()).toList();

    return enemyNeighbor.isEmpty();
  }

  static boolean nearbyState(DarwinCell cell, int state) {
    List<DarwinCell> matchingStateNeighbor = cell.getDirectionalNeighbors(cell.getDirection())
        .stream().filter(
            neighbor -> neighbor.getCurrentState() == state).toList();

    return !matchingStateNeighbor.isEmpty();
  }

  // so you can mock random outcome
  public static void setRandom(Random newRandom) {
    random = newRandom;
  }

  public static boolean nearbyBoundary(DarwinCell cell, Grid grid) {
    // unless you none you can't be a boundary
    if (grid.getEdgeType() != EdgeType.NONE) {
      return false;
    }

    List<DarwinCell> enemyNeighbor = cell.getDirectionalNeighbors(cell.getDirection()).stream()
        .filter(
            neighbor -> neighbor.getPosition()[0] < 0 || neighbor.getPosition()[0] >= grid.getRows()
                ||
                neighbor.getPosition()[1] < 0 || neighbor.getPosition()[1] >= grid.getCols())
        .toList();

    return enemyNeighbor.isEmpty();
  }

  public static void rotate(DarwinCell cell, int degrees, boolean isRight) {
    // basically determine what is the closest degrees mod 45 move it across the list circular
    // list based on left or right, return the new one
    cell.setDirection(DirectionType.E);
  }

  public static void move(DarwinCell cell, int move, Grid grid) {
    int[] position = cell.getPosition();
    position[0] += move;
    position[1] += move;
    // based on the direction
    // how to do it for north west and stuff
    if (grid.isValidPosition(position[0], position[1])) {
      cell.setPosition(position);
    }
  }
}
