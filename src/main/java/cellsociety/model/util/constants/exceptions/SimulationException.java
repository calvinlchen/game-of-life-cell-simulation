package cellsociety.model.util.constants.exceptions;

import static cellsociety.model.util.constants.ResourcePckg.getErrorSimulationResourceBundle;

import cellsociety.model.util.SimulationTypes.SimType;
import java.util.ResourceBundle;

/**
 * Custom exception class for handling simulation-related errors.
 *
 * <p>This exception is thrown when an error occurs during the execution of a simulation,
 * such as invalid parameters, missing configurations, or unexpected failures.
 *
 * @author Jessica Chen
 * @author Calvin Chen
 * @author ChatGPT, helped with some of the JavaDocs
 */
public class SimulationException extends RuntimeException {

  // TODO: this should be replaced to something that can be switched later
  private static final ResourceBundle myResources = getErrorSimulationResourceBundle("English");

  /**
   * Constructs a SimulationException with a specified error message key.
   *
   * @param key - the key used to retrieve the corresponding error message from the resource bundle
   */
  public SimulationException(String key) {
    super(String.format(myResources.getString(key)));
  }

  /**
   * Constructs a SimulationException with a specified error message key and an additional string
   * parameter.
   *
   * @param key - the key used to retrieve the corresponding error message from the resource bundle
   * @param str - the additional string parameter related to the error context
   */
  public SimulationException(String key, String str) {
    super(String.format(myResources.getString(key), str));
  }

  /**
   * Constructs a SimulationException with a specified error message key, two integer parameters.
   *
   * @param key   - the key used to retrieve the corresponding error message from the resource
   *              bundle
   * @param i     - the first integer parameter related to the error context
   * @param j     - the second integer parameter related to the error context
   */
  public SimulationException(String key, int i, int j) {
    super(String.format(myResources.getString(key), i, j));
  }

  /**
   * Constructs a SimulationException with a specified error message key and a cause.
   *
   * @param key   - the key used to retrieve the corresponding error message from the resource
   *              bundle
   * @param cause - the underlying cause of the exception
   */
  public SimulationException(String key, Throwable cause) {
    super(String.format(myResources.getString(key)), cause);
  }

  /**
   * Constructs a SimulationException with a specified error message key, simulation type, and an
   * underlying cause.
   *
   * @param key     - the key used to retrieve the corresponding error message from the resource
   *                bundle
   * @param simType - the type of simulation where the exception occurred
   * @param cause   - the underlying cause of the exception
   */
  public SimulationException(String key, SimType simType, Throwable cause) {
    super(String.format(myResources.getString(key), simType), cause);
  }

  /**
   * Constructs a SimulationException with a specified error message key, additional string
   * parameter, and an underlying cause.
   *
   * @param key   - the key used to retrieve the corresponding error message from the resource
   *              bundle
   * @param str   - the additional string parameter related to the error context
   * @param cause - the underlying cause of the exception
   */
  public SimulationException(String key, String str, Throwable cause) {
    super(String.format(myResources.getString(key), str), cause);
  }

  /**
   * Constructs a SimulationException with a specified error message key, two integer parameters,
   * and an underlying cause.
   *
   * @param key   - the key used to retrieve the corresponding error message from the resource
   *              bundle
   * @param i     - the first integer parameter related to the error context
   * @param j     - the second integer parameter related to the error context
   * @param cause - the underlying cause of the exception
   */
  public SimulationException(String key, int i, int j, Throwable cause) {
    super(String.format(myResources.getString(key), i, j), cause);
  }
}
