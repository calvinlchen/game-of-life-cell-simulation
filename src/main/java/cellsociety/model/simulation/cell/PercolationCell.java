package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.PERCOLATION_MAXSTATE;

import cellsociety.model.simulation.rules.PercolationRule;
import cellsociety.model.util.exceptions.SimulationException;

/**
 * The {@code PercolationCell} class represents a single cell in the Percolation simulation.
 *
 * <p>This class follows the {@link Cell} template and interacts with the {@link PercolationRule}
 * to determine its next state.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Ensures state validity within predefined percolation constraints.</li>
 *   <li>Utilizes the Template Method pattern for state calculations.</li>
 *   <li>Simulates fluid percolation through a grid of porous and blocked cells.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * PercolationRule rule = new PercolationRule(parameters);
 * PercolationCell cell = new PercolationCell(0, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class PercolationCell extends Cell<PercolationCell, PercolationRule> {

  /**
   * Constructs a {@code PercolationCell} with the specified initial state and rule. This cell is
   * used in a percolation simulation to represent the state and behavior of a single grid element.
   *
   * @param state the initial state of the cell. Must be within the range of valid states as per
   *              {@code PERCOLATION_MAXSTATE}.
   * @param rule  the {@code PercolationRule} defining the behavior of the cell in the simulation.
   *              Cannot be {@code null}.
   * @throws SimulationException if the state is out of the valid range or the rule is
   *                             {@code null}.
   */
  public PercolationCell(int state, PercolationRule rule) {
    super(state, rule);
    validateState(state, PERCOLATION_MAXSTATE);
  }

  @Override
  protected PercolationCell getSelf() {
    return this;
  }

  @Override
  protected int getMaxState() {
    return PERCOLATION_MAXSTATE;
  }
}
