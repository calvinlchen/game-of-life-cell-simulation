package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameters for the WaTor World simulation.
 *
 * <p>This class defines and initializes parameters for the WaTor World simulation, including:
 * {@code fishReproductionTime} (time required for fish to reproduce), {@code sharkEnergyGain}
 * (energy gained by sharks when eating fish), {@code sharkReproductionTime} (time required for
 * sharks to reproduce), and {@code sharkInitialEnergy} (starting energy level of sharks).
 *
 * @author Jessica Chen
 * @author ChatGPT, helped with some of the JavaDocs
 */
public class WaTorParameters extends Parameters {

  /**
   * Initializes the WaTorParameters with default values.
   *
   * <p>Sets {@code fishReproductionTime} to 3, {@code sharkEnergyGain} to 2,
   * {@code sharkReproductionTime} to 3, and {@code sharkInitialEnergy} to 5.
   */
  public WaTorParameters() {
    super();

    initializeParams();
  }

  /**
   * Initializes the WaTorParameters with default values and a specified language for error
   * messages.
   *
   * <p>Sets {@code fishReproductionTime} to 3, {@code sharkEnergyGain} to 2,
   * {@code sharkReproductionTime} to 3, and {@code sharkInitialEnergy} to 5.
   *
   * @param language - name of the language for error message display
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
