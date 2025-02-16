package cellsociety.model.factories;

import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.simulation.parameters.Parameters;

import java.lang.reflect.Constructor;
import java.util.Map;

public class RuleFactory {

  private static final String RULE_PACKAGE = "cellsociety.model.simulation.rules.";
  private static final String PARAMETER_PACKAGE = "cellsociety.model.simulation.parameters.";

  /**
   * Create a Rule and its corresponding Parameters instance dynamically
   *
   * @param ruleType   - The name of the simulation type
   * @param parameters - Map of parameters to initialize the rule
   * @return an instance of the Rule with its parameters initialized
   */
  public static Rule createRule(String ruleType, Map<String, Double> parameters) {
    try {
      // create the parameter class
      Class<?> parameterClass = Class.forName(PARAMETER_PACKAGE + ruleType.replace("Rule", "Parameters"));
      Constructor<?> paramConstructor = parameterClass.getConstructor();
      Parameters paramInstance = (Parameters) paramConstructor.newInstance();
      paramInstance.setParameters(parameters);

      // create the rule class hopefully
      Class<?> ruleClass = Class.forName(RULE_PACKAGE + ruleType);
      Constructor<?> ruleConstructor = ruleClass.getConstructor(parameterClass);
      return (Rule<?, ?>) ruleConstructor.newInstance(paramInstance);
    } catch (Exception e) {
      throw new RuntimeException("Error creating rule: " + ruleType, e);
    }
  }
}