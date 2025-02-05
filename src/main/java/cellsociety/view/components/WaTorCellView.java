package cellsociety.view.components;

import cellsociety.model.util.constants.CellStates.WaTorStates;
import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class WaTorCellView extends CellView<WaTorStates> {

  public WaTorCellView(double x, double y, double width, double height, WaTorStates cellState) {
    super(x, y, width, height, cellState);
  }

  /**
   * Maps a Percolation state to its corresponding color.
   */
  @Override
  protected Color getColorForState(WaTorStates state) {
    return switch (state) {
      case EMPTY -> DEFAULT_FILL;
      case FISH -> Color.ORANGE;
      case SHARK -> Color.BLUE;
    };
  }
}
