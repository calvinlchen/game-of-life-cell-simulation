package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.CellStates.PERCOLATION_OPEN;
import static cellsociety.model.util.constants.CellStates.PERCOLATION_PERCOLATED;

import cellsociety.model.simulation.cell.PercolationCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.constants.exceptions.SimulationException;

/**
 * The {@code PercolationRule} class defines the behavior of percolation in a grid-based
 * simulation.
 *
 * <p>Percolation models how fluid spreads through porous material, where:</p>
 * <ul>
 *   <li>Open cells ({@code PERCOLATION_OPEN}) become percolated if at least one neighbor is
 *   percolated.</li>
 *   <li>Percolated cells ({@code PERCOLATION_PERCOLATED}) remain percolated indefinitely.</li>
 *   <li>Blocked cells ({@code PERCOLATION_BLOCKED}) remain blocked indefinitely.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * PercolationRule rule = new PercolationRule(parameters);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class PercolationRule extends Rule<PercolationCell> {

  /**
   * Constructs a {@code PercolationRule} object, which defines the behavior of the percolation
   * simulation based on the given parameters. The parameters are passed to the superclass
   * {@code Rule} for initialization and validation.
   *
   * @param parameters the {@code GenericParameters} object containing the configuration and
   *                   settings required for the percolation simulation. Must not be {@code null}.
   */
  public PercolationRule(GenericParameters parameters) {
    super(parameters);
  }

  /**
   * Applies the percolation rule to determine the next state of a given cell.
   *
   * <h2>Percolation Logic:</h2>
   * <ul>
   *   <li>If a cell is {@code OPEN} and has a percolated neighbor, it becomes
   *   {@code PERCOLATED}.</li>
   *   <li>Percolated cells remain in their state permanently.</li>
   *   <li>Otherwise, the cell retains its current state.</li>
   * </ul>
   *
   * @param cell The {@code PercolationCell} being evaluated.
   * @return The next state of the cell.
   */
  @Override
  public int apply(PercolationCell cell) {
    try {
      if (cell.getCurrentState() == PERCOLATION_OPEN && neighborIsPercolated(cell)) {
        return PERCOLATION_PERCOLATED;
      }

      return cell.getCurrentState();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  private boolean neighborIsPercolated(PercolationCell cell) {
    try {
      return cell.getNeighbors().stream()
          .anyMatch(neighbor -> neighbor.getCurrentState() == PERCOLATION_PERCOLATED);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }
}
