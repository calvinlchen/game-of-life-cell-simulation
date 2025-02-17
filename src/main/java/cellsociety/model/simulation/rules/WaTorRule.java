package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.WATOR_EMPTY;
import static cellsociety.model.util.constants.CellStates.WATOR_FISH;
import static cellsociety.model.util.constants.CellStates.WATOR_SHARK;

import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.simulation.parameters.WaTorParameters;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class for representing rules for WaTor World simulation
 *
 * @author Jessica Chen
 */
public class WaTorRule extends Rule<WaTorCell, WaTorParameters> {

  private final Random random = new Random();
  private int fishReproductionTime;
  private int sharkEnergyGain;
  private int sharkReproductionTime;

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public WaTorRule(WaTorParameters parameters) {
    super(parameters);

    fishReproductionTime = (int) getParameters().getParameter("fishReproductionTime");
    sharkEnergyGain = (int) getParameters().getParameter("sharkEnergyGain");
    sharkReproductionTime = (int) getParameters().getParameter("sharkReproductionTime");
  }

  @Override
  public int apply(WaTorCell cell) {
    int currentState = cell.getCurrentState();
    return switch (currentState) {
      case WATOR_FISH -> handleFish(cell);
      case WATOR_SHARK -> handleShark(cell);
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
