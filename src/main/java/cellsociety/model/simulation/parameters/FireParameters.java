package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameters for Fire simulation
 *
 * @author Jessica Chen
 */
public class FireParameters extends Parameters {

  /**
   * initials fire with parameters ignition likelihood ratio (0.1) and tree spawn likelihood ratio
   * (0.01)
   */
  public FireParameters() {
    super();

    Map<String, Double> parameters = new HashMap<>();
    parameters.put("ignitionLikelihood", 0.1);
    parameters.put("treeSpawnLikelihood", 0.01);

    setParameters(parameters);
  }

  /**
   * initials fire with parameters ignition likelihood ratio (0.1) and tree spawn likelihood ratio
   * (0.01)
   * @param language - name of language, for error message display
   */
  public FireParameters(String language) {
    super(language);

    Map<String, Double> parameters = new HashMap<>();
    parameters.put("ignitionLikelihood", 0.1);
    parameters.put("treeSpawnLikelihood", 0.01);

    setParameters(parameters);
  }
}
