package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameters for Rock Paper Scissors
 *
 * @author Jessica Chen
 */
public class RPSParameters extends Parameters {

  /**
   * Initialize rockpaper scisors with numStates (3) and percentageToWin (0.5)
   */
  public RPSParameters() {
    super();

    initializeParams();
  }

  /**
   * Initialize rockpaper scisors with numStates (3) and percentageToWin (0.5)
   * @param language - name of language, for error message display
   */
  public RPSParameters(String language) {
    super(language);

    initializeParams();
  }

  private void initializeParams() {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("numStates", 3.);
    parameters.put("percentageToWin", 0.5);

    setParameters(parameters);
  }

}
