package cellsociety.model.factories.statefactory.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellStateHandlerDynamic implements CellStateHandler {
  private Map<Integer, String> cellStates;

  public CellStateHandlerDynamic() {
    this.cellStates = new HashMap<>();
  }

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
