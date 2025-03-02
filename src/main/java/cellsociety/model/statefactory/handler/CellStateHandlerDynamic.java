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

  private Map<Integer, String> cellStates;

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

  @Override
  public List<Integer> getStateInt() {
    return List.copyOf(cellStates.keySet());
  }

  @Override
  public List<String> getStateString() {
    return List.copyOf(cellStates.values());
  }

  @Override
  public int stateFromString(String state) {
    for (Map.Entry<Integer, String> entry : cellStates.entrySet()) {
      if (entry.getValue().equalsIgnoreCase(state)) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException("Invalid state: " + state);
  }

  @Override
  public String statetoString(int state) {
    return cellStates.getOrDefault(state, "INVALID");
  }

  @Override
  public boolean isValidState(int state) {
    return cellStates.containsKey(state);
  }
}
