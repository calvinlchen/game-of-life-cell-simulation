package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.WATOR_EMPTY;
import static cellsociety.model.util.constants.CellStates.WATOR_FISH;
import static cellsociety.model.util.constants.CellStates.WATOR_SHARK;

import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.simulation.parameters.WaTorParameters;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   * @param language - name of language, for error message display
   */
  public WaTorRule(WaTorParameters parameters, String language) {
    super(parameters, language);

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
    int stepsSurvived = cell.getStepsSurvived() + 1;

    Optional<WaTorCell> emptyNeighbor = findEmptyCell(cell);
    if (emptyNeighbor.isPresent()) {
      WaTorCell target = emptyNeighbor.get();

      target.setNextState(WATOR_FISH, stepsSurvived, 0, cell);

      if (stepsSurvived >= fishReproductionTime) {
        target.setNextState(WATOR_FISH, 0, 0, cell);
        return WATOR_FISH;
      }
      return WATOR_EMPTY;
    }

    cell.setNextState(WATOR_FISH, stepsSurvived, 0);
    return -1;
  }

  private int handleShark(WaTorCell cell) {
    int stepsSurvived = cell.getStepsSurvived() + 1;
    int energy = cell.getEnergy() - 1;

    if (energy <= 0) {
      return WATOR_EMPTY; // Shark dies
    }

    Optional<WaTorCell> fishNeighbor = findFishCell(cell);
    Optional<WaTorCell> emptyNeighbor = findEmptyCell(cell);

    if (fishNeighbor.isPresent()) {
      WaTorCell target = fishNeighbor.get();
      energy += sharkEnergyGain;

      cell.setCurrentState(WATOR_EMPTY);
      target.setNextState(WATOR_SHARK, stepsSurvived, energy);
      target.setConsumed(true);

      if (stepsSurvived >= sharkReproductionTime) {
        target.setNextState(WATOR_SHARK, 0, energy);
        return WATOR_SHARK;
      }
      return WATOR_EMPTY;

    } else if (emptyNeighbor.isPresent()) {
      WaTorCell target = emptyNeighbor.get();
      target.setNextState(WATOR_SHARK, stepsSurvived, energy);

      if (stepsSurvived >= sharkReproductionTime) {
        target.setNextState(WATOR_SHARK, 0, energy);
        return WATOR_SHARK;
      }
      return WATOR_EMPTY;
    }

    cell.setNextState(WATOR_SHARK, stepsSurvived, energy);
    return -1;
  }

  private Optional<WaTorCell> findEmptyCell(WaTorCell cell) {
    List<WaTorCell> emptyNeighbors = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == WATOR_EMPTY
            && neighbor.getNextState() == WATOR_EMPTY)
        .toList();

    if (emptyNeighbors.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(emptyNeighbors.get(random.nextInt(emptyNeighbors.size())));
  }

  private Optional<WaTorCell> findFishCell(WaTorCell cell) {
    List<WaTorCell> fishNeighbors = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == WATOR_FISH)
        .toList();

    if (fishNeighbors.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(fishNeighbors.get(random.nextInt(fishNeighbors.size())));
  }
}
