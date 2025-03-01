package cellsociety.model.factories.edgefactory.handler;

import java.util.List;
import java.util.Optional;

public class ToroidalEdgeHandler implements EdgeHandler {


  @Override
  public Optional<List<Integer>> handleEdgeNeighbor(int currRow, int curCol, int totalRow,
      int totalCol, int[] dir) {
    return Optional.empty();
  }
}