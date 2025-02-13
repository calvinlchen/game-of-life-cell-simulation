package cellsociety.view.components;

import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class WaTorCellView extends CellView {

  public WaTorCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState);
  }

  /**
   * Maps a Percolation state to its corresponding color.
   */
  @Override
  protected Color getColorForState(int state) {
    return switch (state) {
      case 1 -> Color.ORANGE;
      case 2 -> Color.BLUE;
      default -> DEFAULT_FILL;
    };
  }
}
