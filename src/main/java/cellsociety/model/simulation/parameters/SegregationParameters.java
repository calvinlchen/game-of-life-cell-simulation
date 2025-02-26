package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameters for the Segregation simulation.
 *
 * <p>This class defines and initializes the parameter {@code toleranceThreshold},
 * which determines the threshold for segregation behavior in the simulation.
 *
 * @author Jessica Chen
 * @author ChatGPT, helped with some of the java docs
 */
public class SegregationParameters extends Parameters {

  /**
   * Initializes the SegregationParameters with default values.
   *
   * <p>Sets the {@code toleranceThreshold} parameter to 0.5.
   */
  public SegregationParameters() {
    super();

    initializeParams();
  }

  /**
   * Initializes the SegregationParameters with default values and a specified language for error
   * messages.
   *
   * <p>Sets the {@code toleranceThreshold} parameter to 0.5.
   *
   * @param language - name of the language for error message display
   */
  public SegregationParameters(String language) {
    super(language);

    initializeParams();
  }

  private void initializeParams() {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("toleranceThreshold", 0.5);

    setParameters(parameters);
  }
}
