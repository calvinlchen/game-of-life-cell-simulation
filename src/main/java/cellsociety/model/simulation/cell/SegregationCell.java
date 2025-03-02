package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.SEGREGATION_MAXSTATE;
import static cellsociety.model.util.constants.CellStates.SEGREGATION_EMPTY;

import cellsociety.model.simulation.rules.SegregationRule;
import cellsociety.model.util.constants.exceptions.SimulationException;


/**
 * The {@code SegregationCell} class represents an agent in Schelling's Model of Segregation.
 *
 * <p>In this simulation, agents move if they are not satisfied with their neighborhood.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Uses {@link SegregationRule} to determine movement based on satisfaction.</li>
 *   <li>Prevents overriding prior moves to avoid conflicts.</li>
 *   <li>Implements Template Method hooks to control state updates.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * SegregationRule rule = new SegregationRule(parameters);
 * SegregationCell cell = new SegregationCell(1, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class SegregationCell extends Cell<SegregationCell, SegregationRule> {

  /**
   * Constructs a {@code SegregationCell} object with the specified initial state and behavior
   * rule.
   *
   * <p>This constructor initializes the cell with its current and next states, validates the state
   * against the maximum allowable state for the segregation simulation, and assigns the specified
   * segregation rule.
   *
   * @param state the initial state of the cell, which represents the type of agent or an empty
   *              space. Must be within the valid range of states.
   * @param rule  the {@code SegregationRule} that governs the behavior of the cell, determining
   *              transitions and satisfaction in the simulation. Must not be null.
   * @throws SimulationException if the state is out of the valid range.
   */
  public SegregationCell(int state, SegregationRule rule) {
    super(state, rule);
    validateState(state, SEGREGATION_MAXSTATE);
  }

  /**
   * Determines whether the state calculation should be skipped.
   *
   * <p>Prevents reassigning state if an agent has already moved into an empty space.</p>
   *
   * @return {@code true} if the calculation should be skipped, {@code false} otherwise.
   */
  @Override
  protected boolean shouldSkipCalculation() {
    return getCurrentState() == SEGREGATION_EMPTY && getNextState() != SEGREGATION_EMPTY;
  }

  @Override
  protected SegregationCell getSelf() {
    return this;
  }

  @Override
  protected int getMaxState() {
    return SEGREGATION_MAXSTATE;
  }
}
