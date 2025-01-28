package cellsociety.model.interfaces;

import java.util.Map;

/**
 * Abstract class for representing a simulation rules.
 *
 * <p> Rules take in a map of parameters and apply them when calculating the next step of the
 * simulation
 *
 * @param <S> - the type of state this cell holds defined by the enum in the subclass
 * @param <C> - the type of cell, must be a subclass of Cell<S>
 *
 * Author: Jessica Chen
 */
public abstract class Rule<S extends Enum<S>, C extends Cell<S, C>> {

  private Map<String, Double> parameters;

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public Rule(Map<String, Double> parameters) {
    this.parameters = parameters;
  }

  /**
   * Apply the rules to determine the next state of a cell
   *
   * @param cell - cell to apply the rules to
   * @return next state of the cell
   */
  public abstract S apply(C cell);

  /**
   * Get the parameters for this rule set
   *
   * @return map of current parameters
   */
  public Map<String, Double> getParameters() {
    return parameters;
  }

  /**
   * Set the parameters for this rule set
   *
   * @param parameters - map of parameters to set it to
   */
  public void setParameters(Map<String, Double> parameters) {
    this.parameters = parameters;
  }
}
