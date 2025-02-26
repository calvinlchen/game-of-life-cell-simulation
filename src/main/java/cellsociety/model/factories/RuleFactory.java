package cellsociety.model.factories;

import static cellsociety.model.util.constants.ResourcePckg.getErrorSimulationResourceBundle;

import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.util.constants.exceptions.SimulationException;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Creates Rule and Parameters instances dynamically for a simulation.
 *
 * <p> Assumption: For this to work, all the rules and parameters should be named SimTypeRule and
 * SimTypeParameters. Implements robust exception handling for reflection-related issues.
 *
 * @author Jessica Chen
 */
public class RuleFactory {

  private static final String RULE_PACKAGE = "cellsociety.model.simulation.rules.";
  private static final String PARAMETER_PACKAGE = "cellsociety.model.simulation.parameters.";
  private static ResourceBundle myResources = getErrorSimulationResourceBundle("English");

  /**
   * Update the language of the error messages
   *
   * @param language - update the language of the error messages
   */
  public static void updateLanguage(String language) {
    myResources = getErrorSimulationResourceBundle(language);
  }

  /**
   * Creates a Rule and its corresponding Parameters instance dynamically.
   *
   * @param ruleType   - The name of the simulation type
   * @param parameters - Map of parameters to initialize the rule
   * @param language   - Name of intended display language, used for error-reporting
   * @return an instance of the Rule with its parameters initialized
   * @throws SimulationException if any reflection error occurs during rule creation
   */
  public static Rule<?, ?> createRule(String ruleType, Map<String, Double> parameters,
      String language) {
    updateLanguage(
        language); // errors will display using the given language's resource properties file

    try {
      // create the Parameters class
      String paramClassName = PARAMETER_PACKAGE + ruleType.replace("Rule", "Parameters");
      Class<?> parameterClass = Class.forName(paramClassName);
      Constructor<?> paramConstructor = parameterClass.getConstructor(String.class);
      Parameters paramInstance = (Parameters) paramConstructor.newInstance(language);
      paramInstance.setParameters(parameters);

      // create the Rule class hopefully
      String ruleClassName = RULE_PACKAGE + ruleType;
      Class<?> ruleClass = Class.forName(ruleClassName);
      Constructor<?> ruleConstructor = ruleClass.getConstructor(parameterClass, String.class);
      return (Rule<?, ?>) ruleConstructor.newInstance(paramInstance, language);

    } catch (ClassNotFoundException e) {
      throw new SimulationException(
          String.format(myResources.getString("RuleClassNotFound"), ruleType), e);
    } catch (NoSuchMethodException e) {
      throw new SimulationException(
          String.format(myResources.getString("RuleConstructorNotFound"), ruleType), e);
    } catch (ReflectiveOperationException e) {
      throw new SimulationException(
          String.format(myResources.getString("RuleInstantiationFailed"), ruleType), e);
    } catch (Exception e) {
      throw new SimulationException(myResources.getString("UnknownRuleCreationError"), e);
    }
  }
}
