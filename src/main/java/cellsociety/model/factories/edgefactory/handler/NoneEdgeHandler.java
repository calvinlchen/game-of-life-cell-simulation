package cellsociety.model.factories.edgefactory.handler;

import java.util.List;
import java.util.Optional;

public class NoneEdgeHandler  implements EdgeHandler {

  @Override
  public Optional<List<Integer>> handleEdgeNeighbor(int row, int col, int[] dir) {
    return Optional.empty();
  }
}
