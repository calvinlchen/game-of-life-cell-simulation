package cellsociety.view.components.cell;

import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class GameOfLifeCellView extends CellView {

  public GameOfLifeCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState);
  }

  /**
   * Maps a Game of Life state to its corresponding color.
   */
  @Override
  public Color getColorForState(int state) {
    return switch (state) {
      case 1 -> Color.GREEN;
      default -> DEFAULT_FILL;
    };
  }
}
