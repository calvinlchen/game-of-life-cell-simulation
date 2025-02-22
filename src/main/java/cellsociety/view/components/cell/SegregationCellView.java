package cellsociety.view.components.cell;

import cellsociety.view.interfaces.CellView;
import java.util.Map;
import javafx.scene.paint.Color;

public class SegregationCellView extends CellView {

  private static final Map<Integer, Color> DEFAULT_COLOR_MAP = Map.of(
      0, Color.TRANSPARENT,   // Empty
      1, Color.RED,           // Group A
      2, Color.LIGHTBLUE      // Group B
  );

  public SegregationCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState, DEFAULT_COLOR_MAP);
  }
}
