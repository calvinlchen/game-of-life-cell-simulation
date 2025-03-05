package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// helper methods for handlers
class DarwinCommandHandlerHelperMethods {
  private static final Logger logger = LogManager.getLogger(DarwinCommandHandlerHelperMethods.class);
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
    List<DarwinCell> enemyNeighbor = cell.getNeighbors().stream().filter(
        neighbor -> neighbor.getCurrentState() != cell.getCurrentState()).toList();

    return enemyNeighbor.isEmpty();
  }

  static boolean nearbyState(DarwinCell cell, int state) {
    List<DarwinCell> matchingStateNeighbor = cell.getNeighbors().stream().filter(
        neighbor -> neighbor.getCurrentState() == state).toList();

    return !matchingStateNeighbor.isEmpty();
  }

  // so you can mock random outcome
  public static void setRandom(Random newRandom) {
    random = newRandom;
  }
}
