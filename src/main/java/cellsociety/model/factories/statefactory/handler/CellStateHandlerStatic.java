package cellsociety.model.factories.statefactory.handler;

import cellsociety.model.factories.statefactory.exceptions.CellStateFactoryException;

import java.util.List;
import java.util.Map;

/**
 * Static implementation of {@link CellStateHandler} that defines the main handling for static
 * states.
 *
 * <p> Assumption: cell states are passed in through the constructor.
 *
 * @author Jessica Chen
 * @author ChatGPT for JavaDocs
 */
public class CellStateHandlerStatic implements CellStateHandler {

  private final Map<Integer, String> cellStates;

  /**
   * Constructs a {@code CellStateHandlerStatic} with a predefined set of states.
   *
   * @param cellStates A map containing state integer values and their corresponding names.
   */
  public CellStateHandlerStatic(Map<Integer, String> cellStates) {
    this.cellStates = cellStates;
  }

  /**
   * Retrieves a list of all state integer values.
   *
   * @return a list containing all integer state representations.
   */
  public List<Integer> getStateInt() {
    return List.copyOf(cellStates.keySet());
  }

  /**
   * Retrieves a list of all state string values.
   *
   * @return a list containing all string state representations.
   */
  public List<String> getStateString() {
    return List.copyOf(cellStates.values());
  }

  /**
   * Converts a string representation of a state to its corresponding integer value.
   *
   * @param state the string representation of the state.
   * @return the corresponding integer state value.
   * @throws CellStateFactoryException if the provided state string is invalid.
   */
  public int stateFromString(String state) {
    for (Map.Entry<Integer, String> entry : cellStates.entrySet()) {
      if (entry.getValue().equalsIgnoreCase(state)) {
        return entry.getKey();
      }
    }
    throw new CellStateFactoryException("Invalid state: " + state);
  }

  /**
   * Converts an integer state representation to its corresponding string representation.
   *
   * @param state the integer representation of the state.
   * @return the corresponding string state name, or "INVALID" if the state does not exist.
   */
  public String statetoString(int state) {
    return cellStates.getOrDefault(state, "INVALID");
  }

  /**
   * Checks if the given integer state exists in the handler.
   *
   * @param state the integer representation of the state.
   * @return {@code true} if the state exists, {@code false} otherwise.
   */
  public boolean isValidState(int state) {
    return cellStates.containsKey(state);
  }

}
