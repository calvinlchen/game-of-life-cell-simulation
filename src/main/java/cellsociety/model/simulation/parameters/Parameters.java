package cellsociety.model.simulation.parameters;

import static cellsociety.model.util.constants.ResourcePckg.ERROR_SIMULATION_RESOURCE_PACKAGE;

import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Abstract class for representing a rules for a simulation
 *
 * @author Jessica Chen
 */
public abstract class Parameters {

  private final Map<String, Double> parameters;
  private ResourceBundle myResources;

  /**
   * Constructor for simulations
   *
   * <p> starts all simulations with a max history size with default of size 10
   */
  public Parameters() {
    parameters = new HashMap<>();
    parameters.put("maxHistorySize", 10.0);

    myResources = ResourceBundle.getBundle(ERROR_SIMULATION_RESOURCE_PACKAGE + "English");
  }

  /**
   * Constructor for simulations
   *
   * <p> starts all simulations with a max history size with default of size 10
   */
  public Parameters(String language) {
    parameters = new HashMap<>();
    parameters.put("maxHistorySize", 10.0);

    myResources = ResourceBundle.getBundle(ERROR_SIMULATION_RESOURCE_PACKAGE + language);
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
    if (newParams == null) {
      throw new SimulationException(String.format(myResources.getString("NullParameterMap")));
    }

    parameters.putAll(newParams);
  }

  /**
   * gets the parameter value of the passed in rule
   *
   * @param key - value to look up the rule for
   * @return value associated with the rule
   */
  public double getParameter(String key) {
    if (key == null || key.isEmpty()) {
      throw new SimulationException(String.format(myResources.getString("EmptyParameterKey")));
    }
    if (!parameters.containsKey(key)) {
      throw new SimulationException(String.format(myResources.getString("ParameterNotFound"), key));
    }

    return parameters.getOrDefault(key, 0.0);
  }

  /**
   * set the value for the given rule
   *
   * @param key   - rule to modify
   * @param value - value to update the rule too
   */
  public void setParameter(String key, double value) {
    if (key == null || key.isEmpty()) {
      throw new SimulationException(String.format(myResources.getString("EmptyParameterKey")));
    }
    if (!parameters.containsKey(key)) {
      throw new SimulationException(String.format(myResources.getString("ParameterNotFound"), key));
    }

    parameters.put(key, value);
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

  /**
   * return resource bundle associated for exceptions
   *
   * @return resource bundle associated for exception
   */
  public ResourceBundle getResources() {
    return myResources;
  }

}
