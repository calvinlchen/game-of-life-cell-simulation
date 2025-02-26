package cellsociety.model.factories.statefactory.exceptions;

/**
 * Exception class for errors related to the CellStateFactory.
 * This exception is thrown when an invalid or unexpected state is encountered
 * during the creation of cell states.
 *
 * @author Kyaira Boughton
 * @author Jessica Chen and ChatGPT for javadoc
 */
public class CellStateFactoryException extends IllegalArgumentException {

  /**
   * Constructs a new CellStateFactoryException with the specified detail message.
   *
   * @param message the detail message explaining the cause of the exception
   */
  public CellStateFactoryException(String message) {
    super(message);
  }
}