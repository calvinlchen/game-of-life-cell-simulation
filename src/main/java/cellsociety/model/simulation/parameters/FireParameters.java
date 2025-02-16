package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

public class FireParameters extends Parameters {
  public FireParameters() {
    super();

    Map<String, Double> parameters = new HashMap<>();
    parameters.put("ignitionLikelihood", 0.1);
    parameters.put("treeSpawnLikelihood", 0.01);

    setParameters(parameters);
  }
}
