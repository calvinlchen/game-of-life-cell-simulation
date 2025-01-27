package cellsociety.model.interfaces;

import java.util.Map;

/**
 * Abstract class for representing a simulation rules.
 *
 * <p> Rules take in a map of parameters and apply them when calculating the next step of the
 * simulation
 *
 * @param <S> - the type of state this cell holds defined by the enum in the subclass
 * @author Jessica Chen
 */
abstract class Rule<S extends Enum<S>> {

  private Map<String, Double> parameters;

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  protected Rule(Map<String, Double> parameters) {
    this.parameters = parameters;
  }

  /**
   * Apply the rules to determine the next state of a cell
   *
   * @param cell - cell to apply the rules to
   * @return next state of the cell
   */
  protected abstract S apply(Cell<S> cell);

  /**
   * Get the parameters for this rule set
   *
   * @return map of current parameters
   */
  protected Map<String, Double> getParameters() {
    return parameters;
  }

  /**
   * Set the parameters for this rule set
   *
   * @param parameters - map of parameters to set it too
   */
  protected void setParameters(Map<String, Double> parameters) {
    this.parameters = parameters;
  }
}
