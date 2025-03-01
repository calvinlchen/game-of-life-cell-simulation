package cellsociety.model.factories.edgefactory.handler;

import java.util.List;
import java.util.Optional;

public class MirrorEdgeHandler implements EdgeHandler {

  // TODO: once you know the rules work, can refactor to do cleaner math, this is just my thought
  //  process because my grid math is crite bad
  @Override
  public Optional<List<Integer>> handleEdgeNeighbor(int currRow, int curCol, int totalRow,
      int totalCol, int[] dir) {
    // this is the one that threw the error
    int newRow = currRow + dir[0];
    int newCol = curCol + dir[1];

    // okay do rows first
    if (newRow < 0) {
      // so in this case you going up when its illegal so you actually want to do the + - 1
      newRow = newRow * -1 - 1;   // so like -1 -> 0, -2 -> 1
    } else if (newRow >= totalRow) {
      // so in this case you have like m so it should be m -> m-1 and m+1 -> m-2
      // so if I was smart to math this its eaual to newRow - 1 - (new row - total row)
      newRow = totalRow - 1 - (totalRow - newRow);
      // so like no matter what m maps to m-1 bc m - 1 - (m-m) = m-1;
      // and then m+1 -> m -1 - (m - m+1) = m - 2 ? so yay ?????
    }

    // kinda similar for columns, columns need to be in a separate if statement
    if (newCol < 0) {
      newCol = newCol * -1 - 1;
    } else if (newCol >= totalCol) {
      newCol = totalCol - 1 - (totalCol - newCol);
    }

    return Optional.of(List.of(newRow, newCol));
  }
}
