package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameters for Chou Reg 2 Langton's Loop
 *
 * @author Jessica Chen
 */
public class SegregationParameters extends Parameters {

  /**
   * initializes parameter toleranceThreshold (0.5)
   */
  public SegregationParameters() {
    super();

    Map<String, Double> parameters = new HashMap<>();
    parameters.put("toleranceThreshold", 0.5);

    setParameters(parameters);
  }

  /**
   * initializes parameter toleranceThreshold (0.5)
   * @param language - name of language, for error message display
   */
  public SegregationParameters(String language) {
    super(language);

    Map<String, Double> parameters = new HashMap<>();
    parameters.put("toleranceThreshold", 0.5);

    setParameters(parameters);
  }
}
