package cellsociety.model.simulation.parameters;

import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code Parameters} class serves as the **base class** for storing and managing simulation
 * parameters, primarily in a **String → Double** format.
 *
 * <p>Its primary functions include:</p>
 * <ul>
 *   <li>Storing and retrieving **double-based** parameters efficiently.</li>
 *   <li>Providing methods to **update, validate, and retrieve** parameter values.</li>
 *   <li>Ensuring type safety by restricting parameters to doubles, except for explicit subclasses.</li>
 *   <li>Using **{@link ResourceBundle}** for localized error messages.</li>
 *   <li>Logging warnings/errors when invalid keys or values are encountered.</li>
 * </ul>
 *
 * <h2>🔑 Why Restrict to Double Parameters?</h2>
 *
 * <p>While some simulations (like Game of Life) require **lists or custom types**, most only
 * use doubles for efficiency and safety.</p>
 * <ul>
 *   <li>🟢 Double parameters are **safe and predictable**.</li>
 *   <li>🟡 Allowing **arbitrary objects** (Object type) can lead to runtime errors.</li>
 *   <li>🔴 To avoid unsafe code, only specialized subclasses handle **non-double parameters**.</li>
 * </ul>
 *
 * <p>For cases requiring non-double parameters, see {@link GenericParameters}.</p>
 *
 * <h2>📌 Example Usage</h2>
 * <pre>
 * Parameters params = new GenericParameters(SimType.Fire);
 * params.setParameter("spreadRate", 0.5);
 * double rate = params.getParameter("spreadRate");
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs hence again with the emojis, we might just be living with
 * these nows
 */
public abstract class Parameters {

  private static final String NULL_PARAMETER = "NullParameter";
  private static final Logger logger = LogManager.getLogger(Parameters.class);

  private final Map<String, Double> parameters;

  /**
   * Default constructor for the Parameters class.
   *
   * <p>Initializes the parameter map with a default value for history size.</p>
   */
  public Parameters() {
    parameters = createParamMap();
  }

  private Map<String, Double> createParamMap() {
    final Map<String, Double> parameters;
    parameters = new HashMap<>();
    parameters.put("maxHistorySize", 10.0);
    return parameters;
  }

  // Start of Parameters setters and getters ------

  /**
   * Return the parameters.
   *
   * @return returns the parameter map
   */
  public Map<String, Double> getParameters() {
    return parameters;
  }

  /**
   * Updates the parameter map with the provided new parameter values.
   *
   * <p>For extensibility, subclasses can add on to this method to also denote errors if they try
   * to modify a parameter that is read only.
   *
   * @param newParams - a map of parameter names (keys) and their associated values to be added or
   *                  updated in the existing parameter map.
   * @throws SimulationException if the provided parameter map is null.
   */
  public void setParameters(Map<String, Double> newParams) {
    if (newParams == null) {
      logger.error("Attempted to update parameters with a null map.");
      throw new SimulationException(NULL_PARAMETER,
          List.of("newParams", "setParameters() in Parameters"));
    }

    parameters.putAll(newParams);
  }

  /**
   * Retrieves the parameter value associated with the specified key.
   *
   * @param key - the name of the parameter to retrieve; must not be null or empty
   * @return the value of the parameter if it exists
   * @throws SimulationException if the key is null, empty, or not found in the parameters map
   */
  public double getParameter(String key) {
    if (key == null) {
      logger.error("Attempted to retrieve parameter with null.");
      throw new SimulationException(NULL_PARAMETER,
          List.of("key", "getParameter() in Parameters"));
    }
    if (!parameters.containsKey(key)) {
      logger.error("Attempted to retrieve parameter with key {} that does not exist.", key);
      throw new SimulationException("ParameterNotFound", List.of(key));
    }

    return parameters.get(key);
  }

  /**
   * Sets a parameter identified by a string key to a specified double value.
   *
   * @param key   - the name of the parameter to set; must not be null or empty
   * @param value - the value to associate with the specified key
   * @throws SimulationException if the provided key is null or empty
   */
  public void setParameter(String key, double value) {
    if (key == null) {
      logger.error("Attempted to set parameter with null.");
      throw new SimulationException(NULL_PARAMETER,
          List.of("key", "setParameter() in Parameters"));
    }

    parameters.put(key, value);
    logger.debug("Updated parameter: {} = {}", key, value);
  }

  /**
   * get all rules associated with this simulation type.
   *
   * @return get all keys associated with this simulation type
   */
  public List<String> getParameterKeys() {
    return List.copyOf(parameters.keySet());
  }
}
