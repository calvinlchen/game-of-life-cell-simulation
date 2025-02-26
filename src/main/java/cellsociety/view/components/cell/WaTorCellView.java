package cellsociety.view.components.cell;

import cellsociety.view.interfaces.CellView;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 * WaTorCellView represents the visual representation of a Wa-Tor World simulation cell.
 *
 * <p>This class extends CellView and defines the default colors for different states in the
 * Wa-Tor simulation, such as empty, fish, and shark cells.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class WaTorCellView extends CellView {

  private static final Map<Integer, Color> DEFAULT_COLOR_MAP = Map.of(0, Color.TRANSPARENT,
      // Empty
      1, Color.ORANGE,   // Fish
      2, Color.BLUE      // Shark
  );

  /**
   * Constructs a WaTorCellView with the specified position, size, and initial state.
   *
   * @param x         - the x-coordinate of the cell
   * @param y         - the y-coordinate of the cell
   * @param width     - the width of the cell
   * @param height    - the height of the cell
   * @param cellState - the initial state of the cell
   */
  public WaTorCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState, DEFAULT_COLOR_MAP);
  }
}
