package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.FALLINGSAND_EMPTY;
import static cellsociety.model.util.constants.CellStates.FALLINGSAND_SAND;
import static cellsociety.model.util.constants.CellStates.FALLINGSAND_WATER;
import static cellsociety.model.util.constants.GridTypes.DirectionType.E;
import static cellsociety.model.util.constants.GridTypes.DirectionType.S;
import static cellsociety.model.util.constants.GridTypes.DirectionType.SE;
import static cellsociety.model.util.constants.GridTypes.DirectionType.W;
import static cellsociety.model.util.constants.GridTypes.DirectionType.SW;

import cellsociety.model.simulation.cell.FallingSandCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 * The {@code FallingSandRule} class defines the behavior of sand and water particles in a Falling
 * Sand simulation.
 *
 * <p>This rule determines how sand and water move based on their current state and the
 * states of their surrounding neighbors.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Sand falls straight down if possible; otherwise, it tries to fall diagonally.</li>
 *   <li>Water flows sideways if it cannot fall directly downward.</li>
 *   <li>Movement prioritizes randomness when multiple choices are available.</li>
 *   <li>Prevents movement if a neighboring cell is occupied.</li>
 * </ul>
 *
 * <h2>Assumptions:</h2>
 * <ul>
 *   <li>Rules are applied from left to right and top to bottom for correct
 *   sand-water interactions.</li>
 *   <li>Only empty or water cells are valid movement targets for sand.</li>
 *   <li>Only empty cells are valid movement targets for water.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * FallingSandRule rule = new FallingSandRule(parameters);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class FallingSandRule extends Rule<FallingSandCell> {

  //private static final Logger logger = LogManager.getLogger(FallingSandRule.class);
  private static final Random random = new Random();

  /**
   * Constructs a {@code FallingSandRule} object, initializing it with the specified simulation
   * parameters. This rule is designed to simulate the behavior of falling sand in a grid-based
   * environment, such as handling interactions between sand, water, and empty spaces.
   *
   * @param parameters the {@code GenericParameters} containing the configuration and settings
   *                   required for the falling sand simulation. Must not be {@code null}.
   */
  public FallingSandRule(GenericParameters parameters) {
    super(parameters);
  }

  /**
   * Applies the Falling Sand rule to determine the next state of a given cell based on its current
   * state and the states of its surrounding neighbors.
   *
   * <p>ASSUMPTION: this rule assumes that the apply method is traversed from left to right and top
   * to bottom so that the interaction between sand and water rules are done correctly
   *
   * <p>Behavior:</p>
   * <ul>
   *   <li>Sand attempts to fall straight down, then diagonally if obstructed.</li>
   *   <li>Water flows sideways if it cannot fall straight down.</li>
   *   <li>Movements are applied only if the destination is empty (or water in sand's case).</li>
   * </ul>
   *
   * @param cell The {@code FallingSandCell} to which the rule is being applied.
   * @return The next state of the cell after applying the rule.
   */
  @Override
  public int apply(FallingSandCell cell) {
    try {
      return switch (cell.getCurrentState()) {
        case FALLINGSAND_SAND -> attemptMovement(cell, List.of(SW, SE), FALLINGSAND_SAND,
            List.of(FALLINGSAND_EMPTY, FALLINGSAND_WATER));
        case FALLINGSAND_WATER ->
            attemptMovement(cell, List.of(W, E), FALLINGSAND_WATER, List.of(FALLINGSAND_EMPTY));
        default -> cell.getCurrentState();  // steel and empty
      };
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  int attemptMovement(FallingSandCell cell, List<DirectionType> secondaryDirections,
      int newState, List<Integer> replaceableNeighbors) {
    try {
      for (int replaceableNeighbor : replaceableNeighbors) {
        //logger.debug("Looking for primary neighbor with state {}", replaceableNeighbor);
        Optional<FallingSandCell> neighbor = findValidNeighbor(cell, S, replaceableNeighbor);
        if (neighbor.isPresent()) {
          //logger.debug("Primary neighbor found with state {}", replaceableNeighbor);
          neighbor.get().setNextState(newState);
          return replaceableNeighbor;
        }
      }

      for (int replaceableNeighbor : replaceableNeighbors) {
        List<FallingSandCell> possibleMoves = secondaryDirections.stream()
            .map(dir -> findValidNeighbor(cell, dir, replaceableNeighbor)).flatMap(Optional::stream)
            .toList();
        // chose random secondary
        if (!possibleMoves.isEmpty()) {
          FallingSandCell chosenMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
          chosenMove.setNextState(newState);
          return replaceableNeighbor;
        }
      }
      return cell.getCurrentState();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  Optional<FallingSandCell> findValidNeighbor(FallingSandCell cell, DirectionType direction,
      int state) {
    try {
      return cell.getDirectionalNeighbors(direction).stream()
          .filter(
              neighbor -> neighbor.getCurrentState() == state && neighbor.getNextState() == state)
          .findFirst();
    } catch (SimulationException e) {
      // should not happen because matches direction should not be able to hit its exception cases
      throw new SimulationException(e);
    }
  }

}
