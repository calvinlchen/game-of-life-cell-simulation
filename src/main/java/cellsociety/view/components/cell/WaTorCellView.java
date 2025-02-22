package cellsociety.view.components.cell;

import cellsociety.view.interfaces.CellView;
import java.util.Map;
import javafx.scene.paint.Color;

public class WaTorCellView extends CellView {

  private static final Map<Integer, Color> DEFAULT_COLOR_MAP = Map.of(
      0, Color.TRANSPARENT,   // Empty
      1, Color.ORANGE,   // Fish
      2, Color.BLUE      // Shark
  );

  public WaTorCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState, DEFAULT_COLOR_MAP);
  }
}
