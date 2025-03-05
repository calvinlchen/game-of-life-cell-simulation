package cellsociety.model.statefactory.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class DarwinStateHandler extends CellStateHandlerStatic implements CellStateHandler  {
  // essentially each time you discover a new species you assign it a state
  private final List<String> cellStates;

  public DarwinStateHandler() {
    super(Map.of());
    this.cellStates = new LinkedList<>();
    cellStates.add("EMPTY");
  }

  @Override
  public void addState(String name) {
    if (!cellStates.contains(name)) {
      cellStates.addLast(name);
    }
  }
  
  @Override
  public List<Integer> getStateInt() {
    return List.copyOf(IntStream.range(0, cellStates.size()).boxed().toList());
  }

  @Override
  public List<String> getStateString() {
    return List.copyOf(cellStates);
  }

  @Override
  public int stateFromString(String state) {
    return cellStates.indexOf(state);
  }

  @Override
  public String statetoString(int state) {
    return cellStates.get(state);
  }

  @Override
  public boolean isValidState(int state) {
    return state >= 0 && state < cellStates.size();
  }
}
