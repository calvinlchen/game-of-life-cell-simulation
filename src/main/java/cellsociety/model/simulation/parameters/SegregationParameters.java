package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

public class SegregationParameters extends Parameters {
  public SegregationParameters() {
    super();

    Map<String, Double> parameters = new HashMap<>();
    parameters.put("toleranceThreshold", 0.5);

    setParameters(parameters);
  }
}
