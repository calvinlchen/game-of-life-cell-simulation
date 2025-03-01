package cellsociety.model.simulation;

import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.util.constants.exceptions.SimulationException;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Creates Rule and Parameters instances dynamically for a simulation.
 *
 * <p>Assumption: For this to work, all the rules and parameters should be named SimTypeRule and
 * SimTypeParameters. Implements robust exception handling for reflection-related issues.
 *
 * @author Jessica Chen
 */
class RuleFactory {

  private static final String RULE_PACKAGE = "cellsociety.model.simulation.rules.";
  private static final String PARAMETER_PACKAGE = "cellsociety.model.simulation.parameters.";

  /**
   * Creates a Rule and its corresponding Parameters instance dynamically.
   *
   * @param ruleType   - The name of the simulation type
   * @param parameters - Map of parameters to initialize the rule
   * @return an instance of the Rule with its parameters initialized
   * @throws SimulationException if any reflection error occurs during rule creation
   */
  static Rule<?, ?> createRule(String ruleType, Map<String, Double> parameters) {
    try {
      // create the Parameters class
      String paramClassName = PARAMETER_PACKAGE + ruleType.replace("Rule", "Parameters");
      Class<?> parameterClass = Class.forName(paramClassName);
      Constructor<?> paramConstructor = parameterClass.getConstructor();
      Parameters paramInstance = (Parameters) paramConstructor.newInstance();
      paramInstance.setParameters(parameters);

      // create the Rule class hopefully
      String ruleClassName = RULE_PACKAGE + ruleType;
      Class<?> ruleClass = Class.forName(ruleClassName);
      Constructor<?> ruleConstructor = ruleClass.getConstructor(parameterClass);
      return (Rule<?, ?>) ruleConstructor.newInstance(paramInstance);

    } catch (Exception e) {
      throw new SimulationException("UnknownRuleCreationError", e);
    }
  }
}
