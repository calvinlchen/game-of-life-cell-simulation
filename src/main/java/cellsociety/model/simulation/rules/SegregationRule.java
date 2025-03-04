package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.SEGREGATION_A;
import static cellsociety.model.util.constants.CellStates.SEGREGATION_B;
import static cellsociety.model.util.constants.CellStates.SEGREGATION_EMPTY;

import cellsociety.model.simulation.cell.SegregationCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code SegregationRule} class defines the state transition logic for the Segregation
 * simulation.
 *
 * <p>This rule models social segregation by determining whether a cell (representing an
 * individual) is "satisfied" with its surroundings. If not satisfied, the cell moves to an empty
 * adjacent location.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Each individual (cell) prefers to be surrounded by at least a certain fraction of
 *       similar neighbors.</li>
 *   <li>If a cell is unsatisfied, it moves to a nearby empty space.</li>
 *   <li>Empty spaces are chosen randomly when multiple options exist.</li>
 * </ul>
 *
 * <h2>Simulation Parameters:</h2>
 * <ul>
 *   <li>{@code toleranceThreshold} → Minimum fraction of similar neighbors required for
 *   satisfaction.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * SegregationRule rule = new SegregationRule(parameters);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class SegregationRule extends Rule<SegregationCell> {

  private static final Logger logger = LogManager.getLogger(SegregationRule.class);

  private final Random random = new Random();

  /**
   * Constructs a {@code SegregationRule} object, initializing the segregation simulation parameters
   * required for determining individual satisfaction and movement rules.
   *
   * @param parameters the {@code GenericParameters} object containing simulation configuration,
   *                   such as tolerance thresholds. Must not be {@code null}.
   */
  public SegregationRule(GenericParameters parameters) {
    super(parameters);
  }

  /**
   * Applies the segregation rule to determine the next state of a given cell.
   *
   * <h2>State Transition Logic:</h2>
   * <ul>
   *   <li><b>Empty cells remain empty.</b></li>
   *   <li><b>Satisfied individuals remain in place.</b></li>
   *   <li><b>Unsatisfied individuals attempt to move to a adjacent empty space.</b></li>
   *   <li><b>If no empty space is available, the individual stays in place.</b></li>
   * </ul>
   *
   * @param cell The {@code SegregationCell} being evaluated.
   * @return The next state of the cell.
   */
  @Override
  public int apply(SegregationCell cell) {
    try {
      if (cell.getCurrentState() == SEGREGATION_EMPTY) {
        return SEGREGATION_EMPTY;
      }

      double satisfactionThreshold = getParameters().getParameter("toleranceThreshold");
      if (isSatisfied(cell, satisfactionThreshold)) {
        return cell.getCurrentState();
      }

      Optional<SegregationCell> emptyCell = findAdjacentEmptyCell(cell);

      if (emptyCell.isPresent()) {
        emptyCell.get().setNextState(cell.getCurrentState());
        return SEGREGATION_EMPTY;
      }

      logger.debug("[SegregationRule] No empty cells available for cell at {} to move.",
          cell.getPosition());
      return cell.getCurrentState();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  boolean isSatisfied(SegregationCell cell, double threshold) {
    try {
      List<SegregationCell> neighbors = cell.getNeighbors();
      if (neighbors.isEmpty()) {
        return true; // Cells with no neighbors are always satisfied.
      }

      double similarityRatio = calculateSimilarityRatio(cell, neighbors);

      return similarityRatio >= threshold;
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  double calculateSimilarityRatio(SegregationCell cell, List<SegregationCell> neighbors) {
    try {
      long similarNeighbors = neighbors.stream()
          .filter(neighbor -> neighbor.getCurrentState() == cell.getCurrentState())
          .count();

      int oppositeState = (cell.getCurrentState() == SEGREGATION_A) ? SEGREGATION_B : SEGREGATION_A;

      long oppositeNeighbors = neighbors.stream()
          .filter(neighbor -> neighbor.getCurrentState() == oppositeState)
          .count();

      if (oppositeNeighbors == 0) {
        return 1.0;
      }

      return (double) similarNeighbors / (similarNeighbors + oppositeNeighbors);
    } catch (SimulationException e) {
      // should never be able to hit due to other places safe guards
      throw new SimulationException(e);
    }
  }

  Optional<SegregationCell> findAdjacentEmptyCell(SegregationCell cell) {
    List<SegregationCell> emptyNeighbors = cell.getNeighbors().stream().filter(
        neighbor -> neighbor.getCurrentState() == SEGREGATION_EMPTY
            && neighbor.getNextState() == SEGREGATION_EMPTY).toList();

    if (emptyNeighbors.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(emptyNeighbors.get(random.nextInt(emptyNeighbors.size())));
  }
}
