package cellsociety.model.simulation;

import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.util.constants.exceptions.SimulationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Factory class for creating instances of {@link Rule} and {@link Parameters} dynamically. This
 * class is only to be used by simulation hence package private.
 *
 * <p>This class follows the <b>Factory Method Pattern</b> to abstract the creation process of
 * different simulation rules while leveraging Java <b>Reflection</b> to instantiate rule-related
 * objects at runtime based on naming conventions.</p>
 *
 * <p><b>Design Rationale:</b>
 * <ul>
 *   <li>Encapsulates the creation logic of rules and parameters.</li>
 *   <li>Provides flexibility for adding new rule types without modifying existing code.</li>
 *   <li>Utilizes reflection to determine the correct class dynamically, reducing manual
 *   instantiation logic.</li>
 * </ul>
 *
 * <p><b>Assumptions:</b>
 * <ul>
 *   <li>Each rule class follows the naming pattern: {@code SimTypeRule}.</li>
 *   <li>Each parameters class follows the naming pattern: {@code SimTypeParameters}.</li>
 *   <li>Classes exist within predefined packages.</li>
 * </ul>
 * </p>
 *
 * @author Jessica Chen
 */
class RuleFactory {

  private static final Logger logger = LogManager.getLogger(RuleFactory.class);

  private static final String RULE_PACKAGE = "cellsociety.model.simulation.rules.";
  private static final String PARAMETER_PACKAGE = "cellsociety.model.simulation.parameters.";

  /**
   * Creates an instance of a {@link Rule} dynamically based on the given rule type.
   *
   * <p>This method follows the <b>Factory Method Pattern</b>, dynamically instantiating the
   * appropriate rule and its corresponding parameters using Java <b>Reflection</b>. It retrieves
   * the correct class names at runtime based on predefined naming conventions.
   *
   * <p><b>Intended Use:</b> This method allows new rule types to be added without modifying
   * the factory logic, promoting extensibility.
   *
   * <p><b>Example Usage:</b>
   * <pre>
   * Rule<?, ?> rule = RuleFactory.createRule("FireRule", parameters);
   * </pre>
   *
   * @param ruleType   The name of the simulation type (e.g., "FireRule").
   * @param parameters A map of parameters used to initialize the rule.
   * @return An instance of the corresponding {@link Rule} with its parameters initialized.
   * @throws SimulationException If reflection fails due to missing classes or incorrect constructor
   *                             signatures.
   */
  static Rule<?, ?> createRule(String ruleType, Map<String, Double> parameters) {
    try {
      String paramClassName = PARAMETER_PACKAGE + ruleType.replace("Rule", "Parameters");
      Class<?> parameterClass = Class.forName(paramClassName);
      Constructor<?> paramConstructor = parameterClass.getConstructor();
      Parameters paramInstance = (Parameters) paramConstructor.newInstance();
      paramInstance.setParameters(parameters);

      // create the Rule class hopefully
      String ruleClassName = RULE_PACKAGE + ruleType;
      Class<?> ruleClass = Class.forName(ruleClassName);
      Constructor<?> ruleConstructor = ruleClass.getConstructor(parameterClass);
      Rule<?, ?> ruleInstance = (Rule<?, ?>) ruleConstructor.newInstance(paramInstance);
      return ruleInstance;
    } catch (Exception e) {
      logger.error("Error creating rule: {}", ruleType, e);
      throw new SimulationException("UnknownRuleCreationError", e);
    }
  }
}
