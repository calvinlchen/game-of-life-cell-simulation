package cellsociety.model.util.exceptions;

import cellsociety.view.utils.ResourceManager;
import java.util.List;

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

  /**
   * Constructs a SimulationException with a specified error message key.
   *
   * @param key - the key used to retrieve the corresponding error message from the resource bundle
   */
  public SimulationException(String key) {
    super(String.format(ResourceManager.getCurrentErrorBundle().getString(key)));
  }


  /**
   * Constructs a SimulationException with the specified error message and a given object argument.
   *
   * @param key - the key used to retrieve the corresponding error message from the resource bundle
   * @param argument - additional object displayed in the provided error message, such as a given invalid simulation type
   */
  public SimulationException(String key, Object argument) {
    super(ResourceManager.getCurrentErrorBundle().getString(key) + argument);
  }

  /**
   * Constructs a SimulationException with a specified error message key and additional
   * information.
   *
   * @param key            - the key used to retrieve the corresponding error message from the
   *                       resource bundle
   * @param additionalInfo - a list of additional information strings related to the error context
   */
  public SimulationException(String key, List<String> additionalInfo) {
    super(String.format(
        ResourceManager.getCurrentErrorBundle().getString(key),
        additionalInfo.toArray())
    );
  }

  /**
   * Constructs a SimulationException with a specified error message key and a cause.
   *
   * @param key   - the key used to retrieve the corresponding error message from the resource
   *              bundle
   * @param cause - the underlying cause of the exception
   */
  public SimulationException(String key, Throwable cause) {
    super(String.format(ResourceManager.getCurrentErrorBundle().getString(key)), cause);
  }

  /**
   * Constructs a SimulationException with a specified error message key, additional information,
   * and an underlying cause.
   *
   * @param key            - the key used to retrieve the corresponding error message from the
   *                       resource bundle
   * @param additionalInfo - a list of additional information strings related to the error context
   * @param cause          - the underlying cause of the exception
   */
  public SimulationException(String key, List<String> additionalInfo, Throwable cause) {
    super(String.format(
        ResourceManager.getCurrentErrorBundle().getString(key),
        additionalInfo.toArray()), cause
    );
  }

  /**
   * Constructs a SimulationException with the specified underlying exception as its cause.
   *
   * <p>The intended use for this is just to propagate a custom exception thrown from
   * another class</p>
   *
   * @param e the underlying exception that caused this SimulationException
   */
  public SimulationException(Exception e) {
    super(e);
  }
}
