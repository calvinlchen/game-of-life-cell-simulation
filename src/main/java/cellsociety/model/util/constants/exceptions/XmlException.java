package cellsociety.model.util.constants.exceptions;

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
   * @param message - the detail message explaining the cause of the exception
   */
  public XmlException(String message) {
    super(message);
  }
}
