package cellsociety.view.components;

import cellsociety.model.util.constants.CellStates.FireStates;
import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class FireCellView extends CellView<FireStates> {

  public FireCellView(double x, double y, double width, double height, FireStates cellState) {
    super(x, y, width, height, cellState);
  }

  /**
   * Maps a Spreading of Fire state to its corresponding color.
   */
  @Override
  protected Color getColorForState(FireStates state) {
    return switch (state) {
      case EMPTY -> DEFAULT_FILL;
      case TREE -> Color.GREEN;
      case BURNING -> Color.RED;
    };
  }
}
