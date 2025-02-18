package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.RPSCell;
import cellsociety.model.simulation.parameters.RPSParameters;

/**
 * Class for representing rules for Rock Paper Scissor simulation
 *
 * @author Jessica Chen
 */
public class RPSRule extends Rule<RPSCell, RPSParameters> {

  private final int totalNumStates; // this is the one parameter that should not change

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public RPSRule(RPSParameters parameters) {
    super(parameters);

    totalNumStates = getTotalNumStates(parameters);
  }

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   * @param language - name of language, for error message display
   */
  public RPSRule(RPSParameters parameters, String language) {
    super(parameters, language);

    totalNumStates = getTotalNumStates(parameters);
  }

  private int getTotalNumStates(RPSParameters parameters) {
    final int totalNumStates;
    totalNumStates = (int) parameters.getParameter("numStates");
    return totalNumStates;
  }

  @Override
  public int apply(RPSCell cell) {
    // so like with 3 0 -> 1 -> 2 -> 0 (bc 2 + 1 = 3 % 3 = 0)
    int winningState = (cell.getCurrentState() + 1) % totalNumStates;

    // count how many neighbors have winning state
    long winningNeighborsCount = cell.getNeighbors().stream()
        .filter(neighbor -> neighbor.getCurrentState() == winningState)
        .count();

    if ((double) winningNeighborsCount / cell.getNeighbors().size() > getParameters().getParameter(
        "percentageToWin")) {
      return winningState;
    }

    // update current cell only, current cell only updates based on neighbors current state
    return cell.getCurrentState();
  }

  /**
   * return max state for this RPS rule
   *
   * @return max state for this RPS rule
   */
  public int getMaxState() {
    return totalNumStates;
  }

}
