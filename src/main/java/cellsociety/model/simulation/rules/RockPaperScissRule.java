package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.RockPaperScissCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.constants.exceptions.SimulationException;

/**
 * The {@code RockPaperScissRule} class implements the Rock-Paper-Scissors simulation rule.
 *
 * <p>Each cell represents one of the possible states in an extended Rock-Paper-Scissors model.
 * Cells compete with their neighbors, and if a sufficient proportion of neighbors have the
 * "winning" state, the cell transitions to that state.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>A cell's state is determined by the majority rule of neighboring "winning" states.</li>
 *   <li>The "winning" state is calculated as {@code (currentState + 1) % numStates}.</li>
 *   <li>If enough neighbors have the winning state (determined by {@code percentageToWin}),
 *   the cell adopts the new state.</li>
 * </ul>
 *
 * <h2>Simulation Parameters:</h2>
 * <ul>
 *   <li>{@code numStates} → Total number of possible states. This state is not changeable
 *   after rule creation</li>
 *   <li>{@code percentageToWin} → Minimum fraction of neighbors needed to change state.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * RockPaperScissRule rule = new RockPaperScissRule(parameters);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class RockPaperScissRule extends Rule<RockPaperScissCell> {

  private final int totalNumStates; // this is the one parameter that should not change

  /**
   * Initializes a new instance of the {@code RockPaperScissRule} with the specified parameters.
   * This constructor extracts the total number of states from the parameters and ensures it remains
   * constant throughout the simulation.
   *
   * <p><b>Note:</b> The number of states ({@code numStates}) is set once and cannot be
   * changed.</p>
   *
   * @param parameters a {@code GenericParameters} object containing the configuration and settings
   *                   for the simulation, including the total number of states ("numStates").
   */
  public RockPaperScissRule(GenericParameters parameters) {
    super(parameters);
    try {
      totalNumStates = (int) parameters.getParameter("numStates");
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Applies the Rock-Paper-Scissors rule to determine the next state of a given cell.
   *
   * <h2>State Transition Logic:</h2>
   * <ul>
   *   <li>A cell competes with its neighbors.</li>
   *   <li>The "winning" state is calculated as {@code (currentState + 1) % numStates}.</li>
   *   <li>If enough neighbors have the winning state (determined by {@code percentageToWin}),
   *   the cell adopts the new state.</li>
   *   <li>Otherwise, the cell retains its current state.</li>
   * </ul>
   *
   * @param cell The {@code RockPaperScissCell} being evaluated.
   * @return The next state of the cell.
   */
  @Override
  public int apply(RockPaperScissCell cell) {
    try {
      // so like with 3 0 -> 1 -> 2 -> 0 (bc 2 + 1 = 3 % 3 = 0)
      int winningState = (cell.getCurrentState() + 1) % totalNumStates;

      // count how many neighbors have winning state
      long winningNeighborsCount = cell.getNeighbors().stream()
          .filter(neighbor -> neighbor.getCurrentState() == winningState).count();

      if ((double) winningNeighborsCount / cell.getNeighbors().size()
          > getParameters().getParameter("percentageToWin")) {
        return winningState;
      }

      // update current cell only, current cell only updates based on neighbors current state
      return cell.getCurrentState();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Returns the total number of possible states in the Rock-Paper-Scissors simulation.
   *
   * @return The total number of states.
   */
  public int getMaxState() {
    return totalNumStates;
  }

}
