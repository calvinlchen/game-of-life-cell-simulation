package cellsociety.view.components;

import cellsociety.model.util.constants.CellStates.SegregationStates;
import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class SegregationCellView extends CellView<SegregationStates> {

  public SegregationCellView(double x, double y, double width, double height, SegregationStates cellState) {
    super(x, y, width, height, cellState);
  }

  /**
   * Maps a Percolation state to its corresponding color.
   */
  @Override
  protected Color getColorForState(SegregationStates state) {
    return switch (state) {
      case EMPTY -> DEFAULT_FILL;
      case AGENT_A -> Color.RED;
      case AGENT_B -> Color.LIGHTBLUE;
    };
  }
}
