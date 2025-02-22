package cellsociety.view.components.cell;

import cellsociety.view.interfaces.CellView;
import java.util.Map;
import javafx.scene.paint.Color;

public class PetelkaCellView extends CellView {

  private static final Map<Integer, Color> DEFAULT_COLOR_MAP = Map.of(
      0, Color.RED,
      1, Color.YELLOW,
      2, Color.GREEN,
      3, Color.BLUE,
      4, Color.VIOLET
  );

  public PetelkaCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState, DEFAULT_COLOR_MAP);
  }
}
