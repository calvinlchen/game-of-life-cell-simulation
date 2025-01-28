package cellsociety.model.simulation.rules;

import cellsociety.model.interfaces.Rule;
import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.util.CellStates.WaTorStates;
import java.util.Map;
import java.util.Random;

public class WaTorRule extends Rule<WaTorStates, WaTorCell> {
  private final Random random = new Random();

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public WaTorRule(Map<String, Double> parameters) {
    super(parameters);
  }

  // TODO: note to self need to finish dependencies
  // sharks have higher priority over fish, fish have higher priority over empty
  // if shark eats a fish their proposed movemnt is canceled
  @Override
  public WaTorStates apply(WaTorCell cell) {
    WaTorStates currentState = cell.getCurrentState();
    switch (currentState) {
      case FISH:
        return handleFish(cell);
      case SHARK:
        return handleShark(cell);
      case EMPTY:
      default:
        return WaTorStates.EMPTY;
    }
  }

  private WaTorStates handleFish(WaTorCell cell) {
    int fishReproductionTime = getParameters().getOrDefault("fishReproductionTime", 3.0).intValue();
    WaTorCell emptyNeighbor = findRandomNeighbor(cell, WaTorStates.EMPTY);
    if (emptyNeighbor != null) {
      cell.proposeState(WaTorStates.EMPTY, null);
      emptyNeighbor.proposeState(WaTorStates.FISH, cell);

      if (cell.getStepsSurvived() >= fishReproductionTime) {
        cell.resetStepsSurvived();
        cell.proposeState(WaTorStates.FISH, null);
      }
    }
    cell.incrementStepsSurvived();
    return cell.getCurrentState();
  }

  private WaTorStates handleShark(WaTorCell cell) {
    WaTorCell fishNeighbor = findRandomNeighbor(cell, WaTorStates.FISH);
    WaTorCell emptyNeighbor = findRandomNeighbor(cell, WaTorStates.EMPTY);

    int sharkEnergyGain = getParameters().getOrDefault("sharkEnergyGain", 2.0).intValue();
    int sharkReproductionTime = getParameters().getOrDefault("sharkReproductionTime", 3.0)
        .intValue();

    if (fishNeighbor != null) {
      fishNeighbor.proposeState(WaTorStates.EMPTY, cell);
      cell.addEnergy(sharkEnergyGain);
    } else if (emptyNeighbor != null) {
      cell.proposeState(WaTorStates.EMPTY, null);
      emptyNeighbor.proposeState(WaTorStates.SHARK, cell);
    }

    cell.reduceEnergy();
    if (cell.getEnergy() <= 0) {
      return WaTorStates.EMPTY;
    }
    
    if (cell.getStepsSurvived() >= sharkReproductionTime) {
      cell.resetStepsSurvived();
      cell.proposeState(WaTorStates.SHARK, null);
    }
    cell.incrementStepsSurvived();
    return cell.getCurrentState();
  }

  private WaTorCell findRandomNeighbor(WaTorCell cell, WaTorStates targetState) {
    return cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == targetState)
        .skip(random.nextInt((int) cell.getNeighbors().stream()
            .filter(neighbor -> neighbor.getCurrentState() == targetState)
            .count()))
        .findFirst()
        .orElse(null);
  }
}
