package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.parameters.Parameters;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Abstract class for representing a simulation rules.
 *
 * <p> Rules take in a map of parameters and apply them when calculating the next step of the
 * simulation
 *
 * @param <C> - the type of cell, must be a subclass of Cell
 * @param <P> - the type of the parameter, must be a subclass of Rules
 * @author Jessica Chen
 */
public abstract class Rule<C extends Cell<C, ?, ?>, P extends Parameters> {

  private P parameters;

  private final ResourceBundle myResources;
  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.constants.CellStates";

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public Rule(P parameters) {
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
   * @return parameters
   */
  public P getParameters() {
    return parameters;
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
