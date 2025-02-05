package cellsociety.model.simulation.rules;

import cellsociety.model.interfaces.Rule;
import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.util.constants.CellStates.WaTorStates;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class for representing rules for WaTor World simulation
 *
 * @author Jessica Chen
 */
public class WaTorRule extends Rule<WaTorStates, WaTorCell> {

  private final Random random = new Random();
  int fishReproductionTime;
  int sharkEnergyGain;
  int sharkReproductionTime;

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
  }

  @Override
  public WaTorStates apply(WaTorCell cell) {
    WaTorStates currentState = cell.getCurrentState();
    return switch (currentState) {
      case FISH -> handleFish(cell);
      case SHARK -> handleShark(cell);
      default -> WaTorStates.EMPTY;
    };
  }

  private WaTorStates handleFish(WaTorCell cell) {
    int stepsSurvived = cell.getStepsSurvived() + 1;    // increment steps surived

    WaTorCell emptyNeighbor = findEmptyCell(cell);
    if (emptyNeighbor != null) {                        // if empty space available
      // move current fish to the new square
      emptyNeighbor.setNextState(WaTorStates.FISH, stepsSurvived, 0, cell);

      if (stepsSurvived >= fishReproductionTime) {
        stepsSurvived = 0;
        emptyNeighbor.setNextState(WaTorStates.FISH, stepsSurvived, 0, cell);

        return WaTorStates.FISH;
      }
      return WaTorStates.EMPTY;
    }

    cell.setNextState(WaTorStates.FISH, stepsSurvived, 0);
    return null;
  }

  // TODO: refactor to be shorter -- specially movement no mater if reproduce or not
  private WaTorStates handleShark(WaTorCell cell) {
    WaTorCell fishNeighbor = findFishCell(cell);
    WaTorCell emptyNeighbor = findEmptyCell(cell);

    int stepsSurvived = cell.getStepsSurvived() + 1;
    int energy = cell.getEnergy() - 1;

    // shark dies before it can eat fish
    if (energy <= 0) {
      return WaTorStates.EMPTY;
    }

    if (fishNeighbor != null) {
      energy += sharkEnergyGain;

      cell.setCurrentState(WaTorStates.EMPTY);
      fishNeighbor.setNextState(WaTorStates.SHARK, stepsSurvived, energy);
      fishNeighbor.setConsumed(true);

      if (stepsSurvived >= sharkReproductionTime) {
        stepsSurvived = 0;
        fishNeighbor.setNextState(WaTorStates.SHARK, stepsSurvived, energy);
        return WaTorStates.SHARK;
      }
      return WaTorStates.EMPTY;

    } else if (emptyNeighbor != null) {
      emptyNeighbor.setNextState(WaTorStates.SHARK, stepsSurvived, energy);

      if (stepsSurvived >= sharkReproductionTime) {
        stepsSurvived = 0;
        emptyNeighbor.setNextState(WaTorStates.SHARK, stepsSurvived, energy);
        return WaTorStates.SHARK;
      }
      return WaTorStates.EMPTY;
    }

    cell.setNextState(WaTorStates.SHARK, stepsSurvived, energy);
    return null;
  }

  private WaTorCell findEmptyCell(WaTorCell cell) {
    List<WaTorCell> neighbors = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == WaTorStates.EMPTY
            && neighbor.getNextState() == WaTorStates.EMPTY)
        .toList();

    if (neighbors.isEmpty()) {
      return null;
    }
    return neighbors.get(random.nextInt(neighbors.size()));
  }

  private WaTorCell findFishCell(WaTorCell cell) {
    List<WaTorCell> neighbors = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == WaTorStates.FISH)
        .toList();

    if (neighbors.isEmpty()) {
      return null;
    }
    return neighbors.get(random.nextInt(neighbors.size()));
  }

}
