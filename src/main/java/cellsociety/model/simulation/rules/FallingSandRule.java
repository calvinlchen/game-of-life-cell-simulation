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
import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 * Class for representing rules for Falling Sand simulation.
 *
 * @author Jessica Chen
 */
public class FallingSandRule extends Rule<FallingSandCell> {

  private final Random random = new Random();

  /**
   * Constructor for the Rule class.
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public FallingSandRule(GenericParameters parameters) {
    super(parameters);
  }

  /**
   * Constructor for the Rule class.
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   * @param language   - name of language, for error message display
   */
  public FallingSandRule(GenericParameters parameters, String language) {
    super(parameters, language);
  }

  /**
   * ASSUMPTION: traverse calc from left to right and top to bottom.
   * <p> this is because of sand and water gravity rules.
   *
   * @param cell - cell to apply the rules to
   * @return int next state of the current cell
   */
  @Override
  public int apply(FallingSandCell cell) {
    return switch (cell.getCurrentState()) {
      case FALLINGSAND_SAND -> moveDown(cell, List.of(SW, SE), FALLINGSAND_SAND,
          List.of(FALLINGSAND_EMPTY, FALLINGSAND_WATER));
      case FALLINGSAND_WATER ->
          moveDown(cell, List.of(W, E), FALLINGSAND_WATER, List.of(FALLINGSAND_EMPTY));
      default -> cell.getCurrentState();  // steel and empty
    };
  }

  private int moveDown(FallingSandCell cell, List<DirectionType> secondaryDirections, int newState,
      List<Integer> replaceableNeighbors) {

    for (int replaceableNeighbor : replaceableNeighbors) {
      Optional<FallingSandCell> neighbor = findNeighborInDirection(cell, S, replaceableNeighbor);
      if (neighbor.isPresent()) {
        neighbor.get().setNextState(newState);
        return replaceableNeighbor;
      }

      List<FallingSandCell> possibleMoves = secondaryDirections.stream()
          .map(dir -> findNeighborInDirection(cell, dir, replaceableNeighbor))
          .flatMap(Optional::stream).toList();
      // chose random secondary
      if (!possibleMoves.isEmpty()) {
        FallingSandCell chosenMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
        chosenMove.setNextState(newState);
        return replaceableNeighbor;
      }
    }
    return cell.getCurrentState();
  }


  private Optional<FallingSandCell> findNeighborInDirection(FallingSandCell cell,
      DirectionType direction,
      int state) {
    return cell.getNeighbors().stream()
        .filter(neighbor -> matchesDirection(cell, neighbor, direction))
        .filter(neighbor -> neighbor.getCurrentState() == state && neighbor.getNextState() == state)
        .findFirst();
  }


}
