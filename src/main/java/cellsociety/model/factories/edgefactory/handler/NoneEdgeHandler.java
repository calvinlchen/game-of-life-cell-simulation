package cellsociety.model.factories.edgefactory.handler;

import java.util.List;
import java.util.Optional;

public class NoneEdgeHandler implements EdgeHandler {

  @Override
  public Optional<List<Integer>> handleEdgeNeighbor(int currRow, int curCol, int totalRow,
      int totalCol, int[] dir) {
    // don't care if it invalid its just invalid
    return Optional.empty();
  }

}
