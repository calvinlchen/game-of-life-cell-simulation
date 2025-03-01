package cellsociety.view.components.cell;

import cellsociety.view.interfaces.CellView;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 * FireCellView represents the visual representation of a Spreading Fire simulation cell.
 *
 * <p>This class extends CellView and defines the default colors for different states in the
 * Fire simulation, such as empty, blocked, and burning cells.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class FireCellView extends CellView {

  private static final Map<Integer, Color> DEFAULT_COLOR_MAP = Map.of(0, Color.TRANSPARENT,
      // Empty
      1, Color.GREEN,   // Blocked
      2, Color.RED      // Full
  );

  /**
   * Constructs a FireCellView with the specified position, size, and initial state.
   *
   * @param x         - the x-coordinate of the cell
   * @param y         - the y-coordinate of the cell
   * @param width     - the width of the cell
   * @param height    - the height of the cell
   * @param cellState - the initial state of the cell
   */
  public FireCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState, DEFAULT_COLOR_MAP);
  }
}
