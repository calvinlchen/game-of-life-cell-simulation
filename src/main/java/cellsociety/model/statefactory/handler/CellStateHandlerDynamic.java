package cellsociety.model.statefactory.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A dynamic implementation of {@link CellStateHandler} that allows flexible state management.
 *
 * <p> Assumption: States are added dynamically and must have unique integer values.
 *
 * @author Jessica Chen
 */
public class CellStateHandlerDynamic implements CellStateHandler {

  private final Map<Integer, String> cellStates;

  /**
   * Constructs an empty CellStateHandlerDynamic with no predefined states.
   */
  public CellStateHandlerDynamic() {
    this.cellStates = new HashMap<>();
  }

  /**
   * Adds a new state to the handler.
   *
   * @param state - integer representation of the state.
   * @param name  - string representation of the state.
   */
  public void addState(int state, String name) {
    cellStates.put(state, name);
  }

  public List<Integer> getStateInt() {
    return List.copyOf(cellStates.keySet());
  }

  public List<String> getStateString() {
    return List.copyOf(cellStates.values());
  }

  public int stateFromString(String state) {
    for (Map.Entry<Integer, String> entry : cellStates.entrySet()) {
      if (entry.getValue().equalsIgnoreCase(state)) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException("Invalid state: " + state);
  }

  public String statetoString(int state) {
    return cellStates.getOrDefault(state, "INVALID");
  }

  public boolean isValidState(int state) {
    return cellStates.containsKey(state);
  }

  public void addState(String state) {
    // doesnt do anything
  }


}
