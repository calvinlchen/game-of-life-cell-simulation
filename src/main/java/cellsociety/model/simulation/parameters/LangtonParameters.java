package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

public class LangtonParameters extends Parameters {
  public LangtonParameters() {
    super();

    Map<String, Double> parameters = new HashMap<>();
    parameters.put("numStates", 3.);
    parameters.put("percentageToWin", 0.5);

    setParameters(parameters);
  }

}
