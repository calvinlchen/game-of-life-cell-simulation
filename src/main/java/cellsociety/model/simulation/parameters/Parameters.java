package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for representing a rules for a simulation
 *
 * @author Jessica Chen
 */
public abstract class Parameters {

  private Map<String, Double> parameters;

  /**
   * Constructor for simulations
   *
   * <p> starts all simulations with a max history size with default of size 10
   */
  public Parameters() {
    parameters = new HashMap<>();
    parameters.put("maxHistorySize", 10.0);
  }

  /**
   * Return the parameters
   *
   * @return returns the parameter map
   */
  public Map<String, Double> getParameters() {
    return parameters;
  }

  /**
   * updates all parameters with the new parameters, while maintaining original parameters if
   * unchanged
   *
   * @param newParams - new parameters to add to rules / update with
   */
  public void setParameters(Map<String, Double> newParams) {
    parameters.putAll(newParams);
  }

  /**
   * gets the parameter value of the passed in rule
   *
   * @param key - value to look up the rule for
   * @return value associated with the rule
   */
  public double getParameter(String key) {
    return parameters.getOrDefault(key, 0.0);
  }

  /**
   * set the value for the given rule
   *
   * @param key   - rule to modify
   * @param value - value to update the rule too
   */
  public void setParameter(String key, double value) {
    if (parameters.containsKey(key)) {
      parameters.put(key, value);
    } else {
      throw new IllegalArgumentException("Invalid parameter: " + key);
    }
  }

  /**
   * get all rules associated with this simulation type
   *
   * @return get all keys associated with this simulation type
   */
  public List<String> getParameterKeys() {
    return List.copyOf(parameters.keySet());
  }

  /**
   * returns whether key is a valid key in the state
   *
   * @param key - key to check if it is in rules
   * @return true if it is valid, false otherwise
   */
  public boolean isValidKey(String key) {
    return parameters.containsKey(key);
  }

}
