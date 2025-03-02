package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.FIRE_BURNING;
import static cellsociety.model.util.constants.CellStates.FIRE_EMPTY;
import static cellsociety.model.util.constants.CellStates.FIRE_TREE;

import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.simulation.parameters.GenericParameters;


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
   *   <li><b>Tree → Burning:</b> A tree ignites if a neighbor is burning or via spontaneous ignition.</li>
   *   <li><b>Empty → Tree:</b> An empty cell regrows into a tree based on probability.</li>
   * </ul>
   *
   * @param cell The {@code FireCell} to evaluate.
   * @return The next state of the cell.
   */
  @Override
  public int apply(FireCell cell) {
    return switch (cell.getCurrentState()) {
      case FIRE_BURNING -> FIRE_EMPTY;
      case FIRE_TREE -> evaluateTreeState(cell);
      case FIRE_EMPTY -> evaluateEmptyState(cell);
      default -> cell.getCurrentState();
    };
  }

  private int evaluateTreeState(FireCell cell) {
    if (hasBurningNeighbor(cell)) {
      return FIRE_BURNING;
    }

    double ignitionLikelihood = getParameters().getParameter("ignitionLikelihood");
    if (Math.random() < ignitionLikelihood) {
      return FIRE_BURNING;
    }

    return cell.getCurrentState();
  }

  private int evaluateEmptyState(FireCell cell) {
    double treeSpawnLikelihood = getParameters().getParameter("treeSpawnLikelihood");
    if (Math.random() < treeSpawnLikelihood) {
      return FIRE_TREE;
    }
    return cell.getCurrentState();
  }

  private boolean hasBurningNeighbor(FireCell cell) {
    return cell.getNeighbors().stream()
        .anyMatch(neighbor -> neighbor.getCurrentState() == FIRE_BURNING);
  }
}
