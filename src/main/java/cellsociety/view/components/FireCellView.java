package cellsociety.view.components;

import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class FireCellView extends CellView {

  public FireCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState);
  }

  /**
   * Maps a Spreading of Fire state to its corresponding color.
   */
  @Override
  public Color getColorForState(int state) {
    return switch (state) {
      case 1 -> Color.GREEN;
      case 2 -> Color.RED;
      default -> DEFAULT_FILL;
    };
  }
}
