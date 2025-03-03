package cellsociety.model.simulation;

import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.exceptions.SimulationException;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * The {@code RuleFactory} class is responsible for dynamically creating instances of {@link Rule}
 * based on the specified simulation type.
 *
 * <p>This factory follows the <b>Factory Method Pattern</b> and uses Java <b>Reflection</b> to
 * instantiate rules dynamically. It also initializes parameters using the {@link GenericParameters}
 * class, which provides flexibility for handling both numeric and non-numeric parameters.</p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Creates and initializes a {@link Rule} instance dynamically.</li>
 *   <li>Automatically maps parameters to either numeric values or additional object-based values.
 *   </li>
 *   <li>Uses reflection to instantiate the corresponding rule class.</li>
 * </ul>
 *
 * <h2>Design Rationale</h2>
 * <ul>
 *   <li>Encapsulates rule and parameter creation logic.</li>
 *   <li>Allows adding new rules without modifying factory code.</li>
 *   <li>Uses reflection to find matching rule classes at runtime.</li>
 * </ul>
 *
 * <h2>Expected Naming Conventions</h2>
 *
 * <p>This factory assumes the following naming conventions for dynamically locating classes:</p>
 * <ul>
 *   <li>Rule classes follow the format: <b>{@code SimTypeRule}}</b> (e.g., {@code FireRule}).</li>
 *   <li>All parameters are managed by a single <b>{@code GenericParameters}}</b> class.</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 * <pre>
 * Map&lt;String, Object> params = Map.of("ignitionLikelihood", 0.1, "treeSpawnLikelihood", 0.01);
 * Rule&lt;?> fireRule = RuleFactory.createRule(SimType.Fire, params);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
class RuleFactory {

  private static final Logger logger = LogManager.getLogger(RuleFactory.class);

  private static final String RULE_PACKAGE = "cellsociety.model.simulation.rules.";

  /**
   * Creates a {@link Rule} instance for the specified simulation type with the provided
   * parameters.
   *
   * <p>This method initializes the rule by:
   * <ul>
   *   <li>Creating a {@link GenericParameters} instance with default values for the given
   *       {@link SimType}.</li>
   *   <li>Updating the parameters based on user-specified values.</li>
   *   <li>Using reflection to instantiate the corresponding {@link Rule} class.</li>
   * </ul>
   *
   * <p><b>Example Usage:</b></p>
   * <pre>
   * Map&lt;String, Object> params = Map.of("fishReproductionTime", 3, "sharkEnergyGain", 2);
   * Rule<?> watorRule = RuleFactory.createRule(SimType.WaTor, params);
   * </pre>
   *
   * @param simType    The type of simulation (e.g., Fire, WaTor, Segregation).
   * @param parameters A map of parameter values, which may include numerical or object-based
   *                   parameters.
   * @return An initialized instance of {@link Rule} corresponding to the specified simulation type.
   * @throws SimulationException If the rule class cannot be found or instantiated.
   */
  static Rule<?> createRule(SimType simType, Map<String, Object> parameters) {
    try {
      GenericParameters paramInstance = createParameters(simType, parameters);
      return createRuleInstance(simType, paramInstance);
    } catch (Exception e) {
      logger.error("Error creating rule for simulation type {}: {}", simType, e);
      throw new SimulationException("CreationError", List.of(simType.name()), e);
    }
  }

  /**
   * Creates a {@link GenericParameters} instance and populates it with default and user-defined
   * values.
   *
   * <p>This method ensures that:
   * <ul>
   *   <li>Predefined default values are initialized for the given {@link SimType}.</li>
   *   <li>Numeric parameters are stored in {@code setParameter()}.</li>
   *   <li>Non-numeric parameters are stored in {@code setAdditionalParameter()}.</li>
   * </ul>
   *
   * <p>Example:</p>
   * <pre>
   * Map&lt;String, Object> params = Map.of("B", List.of(3), "S", List.of(2, 3));
   * GenericParameters gameOfLifeParams = createParameters(SimType.GameOfLife, params);
   * </pre>
   *
   * @param simType    The type of simulation (e.g., Fire, WaTor, GameOfLife).
   * @param parameters A map of parameter values provided by the user.
   * @return A {@link GenericParameters} instance with initialized values.
   */
  private static GenericParameters createParameters(SimType simType,
      Map<String, Object> parameters) {
    // Create parameters instance with defaults
    try {
      GenericParameters paramInstance = new GenericParameters(simType);

      // Iterate through all provided parameters
      for (Map.Entry<String, Object> entry : parameters.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();

        // Store numerical values in setParameter(), others in setAdditionalParameter()
        if (value instanceof Number) {
          paramInstance.setParameter(key, ((Number) value).doubleValue());
        } else {
          paramInstance.setAdditionalParameter(key, value);
          logger.warn("Stored additional parameter '{}' as non-double: {}", key, value);
        }
      }
      return paramInstance;
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Uses reflection to instantiate a {@link Rule} based on the given {@link SimType}.
   *
   * <p>This method dynamically resolves the class name of the corresponding rule using Java
   * reflection and invokes its constructor with a {@link GenericParameters} instance.</p>
   *
   * <p>Example:</p>
   * <pre>
   * Rule<?> rule = createRule(SimType.WaTor, genericParams);
   * </pre>
   *
   * @param simType       The simulation type (e.g., Fire, WaTor, Segregation).
   * @param paramInstance The {@link GenericParameters} instance containing initialized parameters.
   * @return A newly instantiated {@link Rule} object.
   * @throws SimulationException If the rule class is missing or cannot be instantiated.
   */
  private static Rule<?> createRuleInstance(SimType simType, GenericParameters paramInstance) {
    try {
      String ruleClassName = RULE_PACKAGE + simType.name() + "Rule";
      Class<?> ruleClass = Class.forName(ruleClassName);
      Constructor<?> ruleConstructor = ruleClass.getConstructor(GenericParameters.class);
      return (Rule<?>) ruleConstructor.newInstance(paramInstance);
    } catch (Exception e) {
      logger.error("Error creating rule: {}", simType, e);
      throw new SimulationException("CreationError", List.of(simType.name()), e);
    }
  }
}
