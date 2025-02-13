package cellsociety.model.simulation.rules;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.interfaces.Rule;
import cellsociety.model.simulation.cell.WaTorCell;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class for representing rules for WaTor World simulation
 *
 * @author Jessica Chen
 */
public class WaTorRule extends Rule<WaTorCell> {

  private final Random random = new Random();
  private int fishReproductionTime;
  private int sharkEnergyGain;
  private int sharkReproductionTime;

  private final int WATOR_EMPTY;
  private final int WATOR_FISH;
  private final int WATOR_SHARK;


  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public WaTorRule(Map<String, Double> parameters) {
    super(parameters);

    fishReproductionTime = getParameters().getOrDefault("fishReproductionTime", 3.0).intValue();
    sharkEnergyGain = getParameters().getOrDefault("sharkEnergyGain", 2.0).intValue();
    sharkReproductionTime = getParameters().getOrDefault("sharkReproductionTime", 3.0)
        .intValue();

    WATOR_EMPTY = super.getStateProperty("WATOR_EMPTY");
    WATOR_FISH = super.getStateProperty("WATOR_FISH");
    WATOR_SHARK = super.getStateProperty("WATOR_SHARK");
  }

  @Override
  public int apply(WaTorCell cell) {
    int currentState = cell.getCurrentState();
    return switch (currentState) {
      case 1 -> handleFish(cell);
      case 2 -> handleShark(cell);
      default -> WATOR_EMPTY;
    };
  }

  private int handleFish(WaTorCell cell) {
    int stepsSurvived = cell.getStepsSurvived() + 1;    // increment steps surived

    WaTorCell emptyNeighbor = findEmptyCell(cell);
    if (emptyNeighbor != null) {                        // if empty space available
      // move current fish to the new square
      emptyNeighbor.setNextState(WATOR_FISH, stepsSurvived, 0, cell);

      if (stepsSurvived >= fishReproductionTime) {
        stepsSurvived = 0;
        emptyNeighbor.setNextState(WATOR_FISH, stepsSurvived, 0, cell);

        return WATOR_FISH;
      }
      return WATOR_EMPTY;
    }

    cell.setNextState(WATOR_FISH, stepsSurvived, 0);
    return -1;
  }

  // TODO: refactor to be shorter -- specially movement no mater if reproduce or not
  private int handleShark(WaTorCell cell) {

    WaTorCell fishNeighbor = findFishCell(cell);
    WaTorCell emptyNeighbor = findEmptyCell(cell);

    int stepsSurvived = cell.getStepsSurvived() + 1;
    int energy = cell.getEnergy() - 1;

    // shark dies before it can eat fish
    if (energy <= 0) {
      return WATOR_EMPTY;
    }

    if (fishNeighbor != null) {
      energy += sharkEnergyGain;

      cell.setCurrentState(WATOR_EMPTY);
      fishNeighbor.setNextState(WATOR_SHARK, stepsSurvived, energy);
      fishNeighbor.setConsumed(true);

      if (stepsSurvived >= sharkReproductionTime) {
        stepsSurvived = 0;
        fishNeighbor.setNextState(WATOR_SHARK, stepsSurvived, energy);
        return WATOR_SHARK;
      }
      return WATOR_EMPTY;

    } else if (emptyNeighbor != null) {
      emptyNeighbor.setNextState(WATOR_SHARK, stepsSurvived, energy);

      if (stepsSurvived >= sharkReproductionTime) {
        stepsSurvived = 0;
        emptyNeighbor.setNextState(WATOR_SHARK, stepsSurvived, energy);
        return WATOR_SHARK;
      }
      return WATOR_EMPTY;
    }

    cell.setNextState(WATOR_SHARK, stepsSurvived, energy);
    return -1;
  }

  private WaTorCell findEmptyCell(WaTorCell cell) {
    List<WaTorCell> neighbors = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == WATOR_EMPTY
            && neighbor.getNextState() == WATOR_EMPTY)
        .toList();

    if (neighbors.isEmpty()) {
      return null;
    }
    return neighbors.get(random.nextInt(neighbors.size()));
  }

  private WaTorCell findFishCell(WaTorCell cell) {
    List<WaTorCell> neighbors = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == WATOR_FISH)
        .toList();

    if (neighbors.isEmpty()) {
      return null;
    }
    return neighbors.get(random.nextInt(neighbors.size()));
  }

}
