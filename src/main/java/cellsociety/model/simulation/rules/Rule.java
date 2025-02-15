package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.Cell;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Abstract class for representing a simulation rules.
 *
 * <p> Rules take in a map of parameters and apply them when calculating the next step of the
 * simulation
 *
 * @param <C> - the type of cell, must be a subclass of Cell
 * @author Jessica Chen
 */
public abstract class Rule<C extends Cell<C, ?>> {

  private Map<String, Double> parameters;

  private final ResourceBundle myResources;
  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.constants.CellStates";

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public Rule(Map<String, Double> parameters) {
    this.parameters = parameters;

    myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE);
  }

  /**
   * Apply the rules to determine the next state of a cell
   *
   * @param cell - cell to apply the rules to
   * @return next state of the cell
   */
  public abstract int apply(C cell);

  /**
   * Get the parameters for this rule set
   *
   * @return map of current parameters
   */
  public Map<String, Double> getParameters() {
    return parameters;
  }

  /**
   * Set new parameters for this rule set.
   *
   * @param parameters - new parameters to set..
   */
  public void setParameters(Map<String, Double> parameters) {
    this.parameters = parameters;
  }

  /**
   * Returns the int associated with the state from the resource property
   *
   * @param key - the String key associated with the state
   * @return the int associated with the property's key
   */
  public int getStateProperty(String key) {
    return Integer.parseInt(myResources.getString(key));
  }
}
