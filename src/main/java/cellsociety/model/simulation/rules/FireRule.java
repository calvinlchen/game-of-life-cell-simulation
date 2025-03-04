package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.FIRE_BURNING;
import static cellsociety.model.util.constants.CellStates.FIRE_EMPTY;
import static cellsociety.model.util.constants.CellStates.FIRE_TREE;

import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.Random;


/**
 * The {@code FireRule} class defines the state transition logic for the Spreading of Fire
 * simulation.
 *
 * <p>This rule models fire spreading through trees, including probabilities for spontaneous
 * ignition and tree regrowth over time.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Burning trees turn into empty cells.</li>
 *   <li>Trees ignite if adjacent to a burning tree or by random ignition chance.</li>
 *   <li>Empty spaces can grow trees with a small probability.</li>
 * </ul>
 *
 * <h2>Simulation Parameters:</h2>
 * <ul>
 *   <li>{@code ignitionLikelihood} → Probability of a tree catching fire randomly.</li>
 *   <li>{@code treeSpawnLikelihood} → Probability of an empty space growing a new tree.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * FireRule rule = new FireRule(parameters);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class FireRule extends Rule<FireCell> {

  private Random random = new Random();

  /**
   * Constructs a {@code FireRule} object, initializing fire simulation parameters required for the
   * behavior of fire spread rules.
   *
   * @param parameters the {@code GenericParameters} object containing simulation configuration,
   *                   such as ignition likelihood and tree spawn likelihood. Must not be
   *                   {@code null}.
   */
  public FireRule(GenericParameters parameters) {
    super(parameters);
  }

  /**
   * Determines the next state of a given fire cell based on its current state and its
   * surroundings.
   *
   * <h2>State Transition Logic:</h2>
   * <ul>
   *   <li><b>Burning Tree → Empty:</b> Fire consumes the tree, leaving an empty cell.</li>
   *   <li><b>Tree → Burning:</b> A tree ignites if a neighbor is burning or via spontaneous
   *   ignition.</li>
   *   <li><b>Empty → Tree:</b> An empty cell regrows into a tree based on probability.</li>
   * </ul>
   *
   * @param cell The {@code FireCell} to evaluate.
   * @return The next state of the cell.
   */
  @Override
  public int apply(FireCell cell) {
    try {
      return switch (cell.getCurrentState()) {
        case FIRE_BURNING -> FIRE_EMPTY;
        case FIRE_TREE -> evaluateTreeState(cell);
        case FIRE_EMPTY -> evaluateEmptyState(cell);
        default -> cell.getCurrentState();
      };
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  int evaluateTreeState(FireCell cell) {
    try {
      if (hasBurningNeighbor(cell)) {
        return FIRE_BURNING;
      }

      double ignitionLikelihood = getParameters().getParameter("ignitionLikelihood");
      if (random.nextDouble() < ignitionLikelihood) {
        return FIRE_BURNING;
      }

      return cell.getCurrentState();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Sets the random instance to be used for stochastic operations within the {@code FireRule}.
   *
   * <p>Used for mocking because I couldn't figure out how to make it work any other way
   *
   * @param random the {@code Random} object to use for generating random values. Must not be null.
   */
  void setRandom(Random random) {
    this.random = random;
  }

  int evaluateEmptyState(FireCell cell) {
    try {
      double treeSpawnLikelihood = getParameters().getParameter("treeSpawnLikelihood");
      if (random.nextDouble() < treeSpawnLikelihood) {
        return FIRE_TREE;
      }
      return cell.getCurrentState();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  boolean hasBurningNeighbor(FireCell cell) {
    return cell.getNeighbors().stream()
        .anyMatch(neighbor -> neighbor.getCurrentState() == FIRE_BURNING);
  }
}
