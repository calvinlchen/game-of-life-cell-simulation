package cellsociety.model.factories.statefactory.handler;

import cellsociety.model.factories.statefactory.exceptions.CellStateFactoryException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellStateHandlerStatic {
  private Map<Integer, String> cellStates = new HashMap<>();

  public CellStateHandlerStatic(Map<Integer, String> cellStates) {
    this.cellStates = cellStates;
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
    throw new CellStateFactoryException("Invalid state: " + state);
  }

  public String statetoString(int state) {
    return cellStates.getOrDefault(state, "INVALID");
  }

  public boolean isValidState(int state) {
    return cellStates.containsKey(state);
  }

}
