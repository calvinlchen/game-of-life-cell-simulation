package cellsociety.view.components;

import cellsociety.model.util.constants.CellStates.PercolationStates;
import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class PercolationCellView extends CellView<PercolationStates> {

  public PercolationCellView(double x, double y, double width, double height, PercolationStates cellState) {
    super(x, y, width, height, cellState);
  }

  /**
   * Maps a Percolation state to its corresponding color.
   */
  @Override
  protected Color getColorForState(PercolationStates state) {
    return switch (state) {
      case BLOCKED -> DEFAULT_FILL;
      case OPEN -> DEFAULT_FILL.invert();
      case PERCOLATED -> Color.BLUE;
    };
  }
}
