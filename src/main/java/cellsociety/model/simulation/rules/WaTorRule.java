package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.WATOR_EMPTY;
import static cellsociety.model.util.constants.CellStates.WATOR_FISH;
import static cellsociety.model.util.constants.CellStates.WATOR_SHARK;

import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * The {@code WaTorRule} class defines the behavior of fish and sharks in the Wa-Tor world
 * simulation.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><b>Fish:</b> Move randomly to an adjacent empty cell.</li>
 *   <li><b>Sharks:</b> Move towards fish if available, otherwise move randomly.</li>
 *   <li><b>Reproduction:</b> Both fish and sharks reproduce after surviving a set number of
 *   steps.</li>
 *   <li><b>Energy Depletion:</b> Sharks lose energy each step and die if it reaches zero.</li>
 * </ul>
 *
 * <h2>Simulation Parameters:</h2>
 * <ul>
 *   <li>{@code fishReproductionTime} → Steps before a fish reproduces.</li>
 *   <li>{@code sharkEnergyGain} → Energy gained when a shark eats a fish.</li>
 *   <li>{@code sharkReproductionTime} → Steps before a shark reproduces.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * WaTorRule rule = new WaTorRule(parameters);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class WaTorRule extends Rule<WaTorCell> {

  private final Random random = new Random();
  private final int fishReproductionTime;
  private final int sharkEnergyGain;
  private final int sharkReproductionTime;

  /**
   * Constructs a {@code WaTorRule} object and initializes it with the provided parameters. The
   * parameters are used to configure the behavior of the Wa-Tor simulation, including reproduction
   * and energy gain characteristics for fish and sharks.
   *
   * @param parameters the {@code GenericParameters} object containing the configuration and
   *                   settings required for the Wa-Tor simulation. Must not be {@code null}.
   */
  public WaTorRule(GenericParameters parameters) {
    super(parameters);

    fishReproductionTime = (int) getParameters().getParameter("fishReproductionTime");
    sharkEnergyGain = (int) getParameters().getParameter("sharkEnergyGain");
    sharkReproductionTime = (int) getParameters().getParameter("sharkReproductionTime");
  }

  /**
   * Applies the Wa-Tor world simulation rules to determine the next state of a cell.
   *
   * <h2>State Transition Logic:</h2>
   * <ul>
   *   <li><b>Fish:</b> Move to an empty cell, reproducing if their age meets the threshold.</li>
   *   <li><b>Sharks:</b> Prioritize moving towards fish, gaining energy if they eat one.</li>
   *   <li><b>Energy Loss:</b> Sharks lose energy each step and die if energy reaches zero.</li>
   * </ul>
   *
   * @param cell The {@code WaTorCell} being evaluated.
   * @return The next state of the cell.
   */
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
    List<WaTorCell> emptyNeighbors = cell.getNeighbors().stream().filter(
        neighbor -> neighbor.getCurrentState() == WATOR_EMPTY
            && neighbor.getNextState() == WATOR_EMPTY).toList();

    if (emptyNeighbors.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(emptyNeighbors.get(random.nextInt(emptyNeighbors.size())));
  }

  private Optional<WaTorCell> findFishCell(WaTorCell cell) {
    List<WaTorCell> fishNeighbors = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == WATOR_FISH).toList();

    if (fishNeighbors.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(fishNeighbors.get(random.nextInt(fishNeighbors.size())));
  }
}
