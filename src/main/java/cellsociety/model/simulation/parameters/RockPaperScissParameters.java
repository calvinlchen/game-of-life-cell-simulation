package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameters for the Rock Paper Scissors simulation.
 *
 * <p>This class defines and initializes parameters for the Rock Paper Scissors simulation,
 * including: {@code numStates} (number of possible states) and {@code percentageToWin} (probability
 * threshold for winning).
 *
 * @author Jessica Chen
 * @author ChatGPT, helped with some of the JavaDocs
 */
public class RockPaperScissParameters extends Parameters {

  /**
   * Initializes the RockPaperScissParameters with default values.
   *
   * <p>Sets {@code numStates} to 3 and {@code percentageToWin} to 0.5.
   */
  public RockPaperScissParameters() {
    super();

    initializeParams();
  }

  /**
   * Initializes the RockPaperScissParameters with default values and a specified language for error
   * messages.
   *
   * <p>Sets {@code numStates} to 3 and {@code percentageToWin} to 0.5.
   *
   * @param language - name of the language for error message display
   */
  public RockPaperScissParameters(String language) {
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
