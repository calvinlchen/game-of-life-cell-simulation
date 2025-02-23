package cellsociety.view.components.cell;

import cellsociety.view.interfaces.CellView;
import java.util.Map;
import javafx.scene.paint.Color;

public class LangtonCellView extends CellView {

  private static final Map<Integer, Color> DEFAULT_COLOR_MAP = Map.of(
      0, Color.TRANSPARENT,
      1, Color.RED,
      2, Color.ORANGE,
      3, Color.YELLOW,
      4, Color.GREEN,
      5, Color.LIGHTBLUE,
      6, Color.NAVY,
      7, Color.VIOLET
  );

  public LangtonCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState, DEFAULT_COLOR_MAP);
  }
}
