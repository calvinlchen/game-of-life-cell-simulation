package cellsociety.view.utils.exceptions;

import cellsociety.view.utils.ResourceManager;

/**
 * Custom exception class for handling View-related errors.
 *
 * <p>This exception is thrown when an error occurs while interacting with elements in the UI that
 * is unrelated to the backend XML and simulation functionality.
 *
 * @author Kyaira Boughton
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT for javadoc
 */
public class ViewException extends IllegalStateException {
  /**
   * Constructs a ViewException with the specified error message.
   *
   * @param key - the key used to retrieve the corresponding error message from the resource bundle
   */
  public ViewException(String key) {
    super(ResourceManager.getCurrentErrorBundle().getString(key));
  }

  /**
   * Constructs a ViewException with the specified error message and a given object argument.
   *
   * @param key - the key used to retrieve the corresponding error message from the resource bundle
   * @param argument - additional object displayed in the provided error message, such as a given invalid simulation type
   */
  public ViewException(String key, Object argument) {
    super(ResourceManager.getCurrentErrorBundle().getString(key) + argument);
  }
}
