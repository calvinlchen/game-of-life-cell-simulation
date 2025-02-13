package cellsociety.view.components;

import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class SegregationCellView extends CellView {

  public SegregationCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState);
  }

  /**
   * Maps a Percolation state to its corresponding color.
   */
  @Override
  protected Color getColorForState(int state) {
    return switch (state) {
      case 1 -> Color.RED;
      case 2 -> Color.LIGHTBLUE;
      default -> DEFAULT_FILL;
    };
  }
}
