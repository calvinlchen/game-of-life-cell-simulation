package cellsociety.model.util.constants.exceptions;

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
   * Constructs a SimulationException with the specified error message.
   *
   * @param message - the detail message explaining the cause of the exception
   */
  public SimulationException(String message) {
    super(message);
  }

  /**
   * Constructs a SimulationException with the specified error message and cause.
   *
   * @param message - the detail message explaining the cause of the exception
   * @param cause - the underlying cause of the exception
   */
  public SimulationException(String message, Throwable cause) {
    super(message, cause);
  }
}
