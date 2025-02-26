package cellsociety.view.components.cell;

import cellsociety.model.util.constants.CellStates;
import cellsociety.view.interfaces.CellView;
import java.util.Map;
import javafx.scene.paint.Color;

public class FallingSandCellView extends CellView {

  private static final Map<Integer, Color> DEFAULT_COLOR_MAP = Map.of(0, Color.TRANSPARENT, // Empty
      1, Color.SILVER,      // Steel
      2, Color.YELLOW,      // Sand
      3, Color.BLUE         // Water
  );

  public FallingSandCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState, DEFAULT_COLOR_MAP);
  }
}
