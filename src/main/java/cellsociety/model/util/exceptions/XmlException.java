package cellsociety.model.util.exceptions;

import cellsociety.view.utils.ResourceManager;

/**
 * Custom exception class for handling XML-related errors.
 *
 * <p>This exception is thrown when an error occurs while parsing or processing XML data,
 * such as missing or malformed elements in the simulation configuration.
 *
 * @author Kyaira Boughton
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT for javadoc
 */
public class XmlException extends IllegalArgumentException {
  /**
   * Constructs an XMLException with the specified error message.
   *
   * @param key - the key used to retrieve the corresponding error message from the resource bundle
   */
  public XmlException(String key) {
    super(String.format(ResourceManager.getCurrentErrorBundle().getString(key)));
  }

  /**
   * Constructs an XMLException with the specified error message and a given object argument.
   *
   * @param key - the key used to retrieve the corresponding error message from the resource bundle
   * @param argument - additional object displayed in the provided error message, such as a given invalid simulation type
   */
  public XmlException(String key, Object argument) {
    super(String.format(ResourceManager.getCurrentErrorBundle().getString(key), argument));
  }
}
