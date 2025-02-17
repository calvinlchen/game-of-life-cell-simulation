package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.FALLINGSAND_EMPTY;
import static cellsociety.model.util.constants.CellStates.FALLINGSAND_SAND;
import static cellsociety.model.util.constants.CellStates.FALLINGSAND_WATER;

import cellsociety.model.simulation.cell.FallingSandCell;
import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.simulation.parameters.FallingSandParameters;
import cellsociety.model.simulation.parameters.FireParameters;
import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 * Class for representing rules for Falling Sand simulation
 *
 * @author Jessica Chen
 */
public class FallingSandRule extends Rule<FallingSandCell, FallingSandParameters> {

  private final Random random = new Random();

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public FallingSandRule(FallingSandParameters parameters) {
    super(parameters);
  }

  @Override
  public int apply(FallingSandCell cell) {
    return switch (cell.getCurrentState()) {
      case FALLINGSAND_SAND -> handleSand(cell);
      case FALLINGSAND_WATER -> handleWater(cell);
      default -> cell.getCurrentState();  // steel
    };
  }

  private int handleSand(FallingSandCell cell) {
    return moveToEmptyNeighbor(cell, "S", List.of("SW", "SE"), FALLINGSAND_SAND);
  }

  private int handleWater(FallingSandCell cell) {
    return moveToEmptyNeighbor(cell, "S", List.of("W", "E"), FALLINGSAND_WATER);
  }

  private int moveToEmptyNeighbor(FallingSandCell cell, String primaryDirection,
      List<String> secondaryDirections, int newState) {

    // first try primary direction
    Optional<FallingSandCell> neighbor = findEmptyNeighborInDirection(cell, primaryDirection);
    if (neighbor.isPresent()) {
      neighbor.get().setNextState(newState);
      return FALLINGSAND_EMPTY;
    }

    // otherwise try secondary
    List<FallingSandCell> possibleMoves = secondaryDirections.stream()
        .map(dir -> findEmptyNeighborInDirection(cell, dir))
        .flatMap(Optional::stream)
        .toList();
    // chose random secondary
    if (!possibleMoves.isEmpty()) {
      FallingSandCell chosenMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
      chosenMove.setNextState(newState);
      return FALLINGSAND_EMPTY;
    }

    // if no moves its the current state
    return cell.getCurrentState();
  }


  public Optional<FallingSandCell> findEmptyNeighborInDirection(FallingSandCell cell,
      String direction) {
    return cell.getNeighbors().stream()
        .filter(neighbor -> matchesDirection(cell, neighbor, direction))
        .filter(neighbor -> neighbor.getCurrentState() == FALLINGSAND_EMPTY
            && neighbor.getNextState() == FALLINGSAND_EMPTY)
        .findFirst();
  }



}
