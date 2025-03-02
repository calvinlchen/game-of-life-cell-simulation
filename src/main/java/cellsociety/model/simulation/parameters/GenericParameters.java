package cellsociety.model.simulation.parameters;

import cellsociety.model.util.SimulationTypes.SimType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * The {@code GenericParameters} class dynamically manages simulation parameters, supporting both
 * standard double-based parameters and more complex data types when necessary.
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Stores most parameters as {@code String → Double} for type safety.</li>
 *   <li>Allows simulations like Game of Life to store additional parameters (e.g., lists).
 *   This extends upon the original parameter class by providing mirror calls for additional
 *   parameters as well as the default ones.</li>
 *   <li>Uses default parameter values for each simulation type.</li>
 *   <li>Provides error handling and logging for unsafe non-double parameters.</li>
 * </ul>
 *
 * <h2>📌 Why One Main Subclass?</h2>
 * <p>Previously, each simulation had its own parameter subclass (e.g., `FireParameters`,
 * `SegregationParameters`). This approach was:</p>
 * <ul>
 *   <li>❌ Redundant – Most simulations only stored doubles, leading to near-identical classes,
 *   or no additional parameters.</li>
 *   <li>❌ Rigid – Adding a new simulation required creating a new class manually.</li>
 *   <li>✅ Now Improved – By consolidating into one `GenericParameters`,
 *   we gain flexibility and simpler maintenance making it more extensible.</li>
 * </ul>
 *
 * <h2>⚠️ Handling Non-Double Parameters</h2>
 * <p>While most parameters are stored as doubles, some simulations require lists or other objects.
 * To minimize unsafe usage of objects:</p>
 * <ul>
 *   <li>🟢 Standard parameters remain strongly typed as `double`.</li>
 *   <li>🟡 Non-double parameters trigger a log warning but are still supported.</li>
 *   <li>🔴 Developers are encouraged to use `double` parameters unless absolutely necessary.</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 * <pre>
 * GenericParameters params = new GenericParameters(SimType.WaTor);
 * double reproductionTime = params.getParameter("fishReproductionTime");
 *
 * params.setAdditionalParameter("customSetting", List.of(1, 2, 3));
 * List<Integer> setting = params.getAdditionalParameter("customSetting", List.class);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs, and somehow it started adding emojis?
 */
public class GenericParameters extends Parameters {

  private static final Logger logger = LogManager.getLogger(GenericParameters.class);

  private static final Map<SimType, Map<String, Double>> DEFAULT_VALUES = new HashMap<>();
  private static final Map<SimType, Map<String, Object>> DEFAULT_ADDITIONAL_VALUES = new HashMap<>();

  static {
    DEFAULT_VALUES.put(SimType.RockPaperSciss, Map.of("numStates", 3.0, "percentageToWin", 0.5));
    DEFAULT_VALUES.put(SimType.Segregation, Map.of("toleranceThreshold", 0.5));
    DEFAULT_VALUES.put(SimType.Fire,
        Map.of("ignitionLikelihood", 0.1, "treeSpawnLikelihood", 0.01));
    DEFAULT_VALUES.put(SimType.WaTor,
        Map.of("fishReproductionTime", 3.0, "sharkEnergyGain", 2.0, "sharkReproductionTime", 3.0,
            "sharkInitialEnergy", 5.0));

    DEFAULT_ADDITIONAL_VALUES.put(SimType.GameOfLife,
        Map.of("S", new ArrayList<>(Arrays.asList(2, 3)), "B", new ArrayList<>(List.of(3))));
  }

  private final Map<String, Object> additionalParams;

  /**
   * Initializes {@code GenericParameters} with default values for the given simulation type.
   *
   * <p>If the simulation type has predefined parameter defaults, they are set automatically.</p>
   *
   * @param simType The simulation type whose parameters should be initialized.
   */
  public GenericParameters(SimType simType) {
    super();
    if (DEFAULT_VALUES.containsKey(simType)) {
      setParameters(DEFAULT_VALUES.get(simType));
    }
    if (DEFAULT_ADDITIONAL_VALUES.containsKey(simType)) {
      additionalParams = new HashMap<>(DEFAULT_ADDITIONAL_VALUES.get(simType));
    } else {
      additionalParams = new HashMap<>();
    }
  }

  /**
   * Stores an additional parameter that is not a double.
   *
   * <p><b>Warning:</b> Non-double parameters should only be used when necessary, as they
   * introduce type safety concerns.</p>
   *
   * @param key   The name of the parameter.
   * @param value The value of the parameter (can be Integer, Double, List, etc.).
   */
  public void setAdditionalParameter(String key, Object value) {
    additionalParams.put(key, value);
  }

  /**
   * Retrieves an additional parameter stored in the parameters map and casts it to the specified
   * type. If the parameter does not exist or is not an instance of the specified type, returns an
   * {@code Optional} containing the value, or an empty {@code Optional}.
   *
   * @param key  the name of the additional parameter to retrieve
   * @param type the expected class type of the parameter's value
   * @param <T>  the type of the parameter's value
   * @return an {@code Optional} containing the value of the additional parameter cast to the
   * specified type, or empty if not found or not of the expected type
   */
  public <T> Optional<T> getAdditionalParameter(String key, Class<T> type) {
    Object value = additionalParams.get(key);
    if (!type.isInstance(value)) {
      logger.warn("Additional parameter {} is not of type {}", key, type.getSimpleName());
      return Optional.empty();
    }
    return Optional.of(type.cast(value));
  }

  public List<String> getAdditionalParameterKeys() {
    return List.copyOf(additionalParams.keySet());
  }
}
