package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameters for WaTor World
 *
 * @author Jessica Chen
 */
public class WaTorParameters extends Parameters {

  /**
   * initializes fishReproductionTime (3), sharkEnergyGain (2), sharkReproductionTime (3),
   * sharkInitialEnergy (5)
   */
  public WaTorParameters() {
    super();

    initializeParams();
  }

  /**
   * initializes fishReproductionTime (3), sharkEnergyGain (2), sharkReproductionTime (3),
   * sharkInitialEnergy (5)
   * @param language - name of language, for error message display
   */
  public WaTorParameters(String language) {
    super(language);

    initializeParams();
  }

  private void initializeParams() {
    Map<String, Double> parameters = new HashMap<>();
    parameters.put("fishReproductionTime", 3.);
    parameters.put("sharkEnergyGain", 2.);
    parameters.put("sharkReproductionTime", 3.);
    parameters.put("sharkInitialEnergy", 5.);

    setParameters(parameters);
  }
}
