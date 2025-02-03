package cellsociety.view.components;

import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.util.constants.CellStates.FireStates;
import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class FireCellView extends CellView<FireStates, FireCell> {

  public FireCellView(double x, double y, double width, double height, FireCell cell) {
    super(x, y, width, height, cell);
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
