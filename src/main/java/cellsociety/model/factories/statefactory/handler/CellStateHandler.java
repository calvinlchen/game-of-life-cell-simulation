package cellsociety.model.factories.statefactory.handler;

import java.util.List;

public interface CellStateHandler {

  List<Integer> getStateInt();

  List<String> getStateString();

  int stateFromString(String state);

  String statetoString(int state);

  boolean isValidState(int state);

}
