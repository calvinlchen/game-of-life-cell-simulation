package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameters for the Fire simulation.
 *
 * <p>This class defines and initializes parameters for fire spread, including:
 * {@code ignitionLikelihood} (probability of a tree catching fire) and {@code treeSpawnLikelihood}
 * (probability of a new tree growing).
 *
 * @author Jessica Chen
 * @author ChatGPT, helped with some of the JavaDocs
 */
public class FireParameters extends Parameters {

  /**
   * Initializes the FireParameters with default values.
   *
   * <p>Sets {@code ignitionLikelihood} to 0.1 and {@code treeSpawnLikelihood} to 0.01.
   */
  public FireParameters() {
    super();

    initializeParams();
  }

  /**
   * Initializes the FireParameters with default values and a specified language for error
   * messages.
   *
   * <p>Sets {@code ignitionLikelihood} to 0.1 and {@code treeSpawnLikelihood} to 0.01.
   *
   * @param language - name of the language for error message display
   */
  public FireParameters(String language) {
    super(language);

    initializeParams();
  }

  private void initializeParams() {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("ignitionLikelihood", 0.1);
    parameters.put("treeSpawnLikelihood", 0.01);

    setParameters(parameters);
  }
}
