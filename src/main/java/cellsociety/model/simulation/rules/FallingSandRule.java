package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.FallingSandCell;
import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.simulation.parameters.FallingSandParameters;
import cellsociety.model.simulation.parameters.FireParameters;
import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 * Class for representing rules for Spreading of Fire simulation
 *
 * @author Jessica Chen
 */
public class FallingSandRule extends Rule<FallingSandCell, FallingSandParameters> {

  private final int FALLINGSAND_EMPTY;
  private final int FALLINGSAND_SAND;
  private final int FALLINGSAND_WATER;

  private final Random random = new Random();

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public FallingSandRule(FallingSandParameters parameters) {
    super(parameters);

    FALLINGSAND_EMPTY = super.getStateProperty("FALLINGSAND_EMPTY");
    FALLINGSAND_SAND = super.getStateProperty("FALLINGSAND_SAND");
    FALLINGSAND_WATER = super.getStateProperty("FALLINGSAND_WATER");
  }

  @Override
  public int apply(FallingSandCell cell) {
    return switch (cell.getCurrentState()) {
      case 2 -> handleSand(cell);
      case 3 -> handleWater(cell);
      default -> cell.getCurrentState();  // steel
    };
  }

  private int handleSand(FallingSandCell cell) {
    return moveToEmptyNeighbor(cell, "Down", List.of("DOWN_LEFT", "DOWN_RIGHT"), FALLINGSAND_SAND);
  }

  private int handleWater(FallingSandCell cell) {
    return moveToEmptyNeighbor(cell, "DOWN", List.of("LEFT", "RIGHT"), FALLINGSAND_WATER);
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


  private boolean matchesDirection(FallingSandCell cell, FallingSandCell neighbor,
      String direction) {
    int[] pos = neighbor.getPosition();
    int[] posCell = cell.getPosition();

    int dx = pos[0] - posCell[0];
    int dy = pos[1] - posCell[1];

    return switch (direction) {
      case "DOWN" -> dx == 0 && dy == 1;
      case "DOWN_LEFT" -> dx == -1 && dy == 1;
      case "DOWN_RIGHT" -> dx == 1 && dy == 1;
      case "LEFT" -> dx == -1 && dy == 0;
      case "RIGHT" -> dx == 1 && dy == 0;
      default -> false;
    };
  }


}
