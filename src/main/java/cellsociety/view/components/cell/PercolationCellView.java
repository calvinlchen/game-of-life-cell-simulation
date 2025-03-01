package cellsociety.view.components.cell;

import cellsociety.view.interfaces.CellView;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 * PercolationCellView represents the visual representation of a Percolation simulation cell.
 *
 * <p>This class extends CellView and defines the default colors for different states in the
 * Percolation simulation, such as blocked, empty, and full cells.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class PercolationCellView extends CellView {

  private static final Map<Integer, Color> DEFAULT_COLOR_MAP = Map.of(0, Color.BLACK,     // Blocked
      1, Color.TRANSPARENT,   // Empty
      2, Color.BLUE           // Full
  );

  /**
   * Constructs a PercolationCellView with the specified position, size, and initial state.
   *
   * @param x         - the x-coordinate of the cell
   * @param y         - the y-coordinate of the cell
   * @param width     - the width of the cell
   * @param height    - the height of the cell
   * @param cellState - the initial state of the cell
   */
  public PercolationCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState, DEFAULT_COLOR_MAP);
  }
}
