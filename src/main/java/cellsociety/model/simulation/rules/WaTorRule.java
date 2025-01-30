package cellsociety.model.simulation.rules;

import cellsociety.model.interfaces.Rule;
import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.util.CellStates.WaTorStates;
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

  // TODO: note to self need to finish dependencies
  // sharks have higher priority over fish, fish have higher priority over empty
  // if shark eats a fish their proposed movemnt is canceled

  @Override
  public WaTorStates apply(WaTorCell cell) {
    WaTorStates currentState = cell.getCurrentState();
    return switch (currentState) {
      case FISH -> handleFish(cell);
      case SHARK -> handleShark(cell);
      default -> WaTorStates.EMPTY;
    };
  }

  // TODO: need to make sure the states get passed around correctly
  private WaTorStates handleFish(WaTorCell cell) {
    int stepsSurvived = cell.getStepsSurvived() + 1;    // increment steps surived

    WaTorCell emptyNeighbor = findEmptyNeighbor(cell);
    if (emptyNeighbor != null) {                        // if empty space available
      // move current fish to the new square
      emptyNeighbor.setNextState(WaTorStates.FISH, stepsSurvived, 0);

      if (stepsSurvived >= fishReproductionTime) {
        stepsSurvived = 0;
        emptyNeighbor.setNextState(WaTorStates.FISH, stepsSurvived, 0);

        return WaTorStates.FISH;
      }
      return WaTorStates.EMPTY;
    }

    // otherwise fish stays in the same spot and you somehow have to update it?????
    cell.setNextState(WaTorStates.FISH, stepsSurvived, 0);
    return null;
  }

  private WaTorStates handleShark(WaTorCell cell) {
    WaTorCell fishNeighbor = findFishNeighbor(cell);
    WaTorCell emptyNeighbor = findEmptyNeighbor(cell);

    int stepsSurvived = cell.getStepsSurvived() + 1;
    int energy = cell.getEnergy() - 1;

    if (cell.getEnergy() <= 0) {
      return WaTorStates.EMPTY;
    }

    if (fishNeighbor != null) {
      energy += sharkEnergyGain;

      // somehow get rid of the fish and make sure it doesn't move?
      // TODO: make sure this actually gets rid of the fish and the fish doesn't continue to move
      cell.setCurrentState(WaTorStates.EMPTY);
      fishNeighbor.setNextState(WaTorStates.SHARK, stepsSurvived, energy);

      // TODO: refactor this and the if statement below, just pull out the neighbor
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

    // otherwise fish stays in the same spot and you somehow have to update it?????
    cell.setNextState(WaTorStates.SHARK, stepsSurvived, energy);
    return null;
  }

  // TODO: need to fix the moving logic of this one
  private WaTorCell findEmptyNeighbor(WaTorCell cell) {
    List<WaTorCell> emptyNeighbors = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == WaTorStates.EMPTY
            && neighbor.getNextState() == WaTorStates.EMPTY)
        .toList();

    if (emptyNeighbors.isEmpty()) {
      return null;
    }
    return emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
  }

  private WaTorCell findFishNeighbor(WaTorCell cell) {
    List<WaTorCell> fishNeighbors = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == WaTorStates.FISH)
        .toList();

    if (fishNeighbors.isEmpty()) {
      return null;
    }
    return fishNeighbors.get(random.nextInt(fishNeighbors.size()));
  }
}
