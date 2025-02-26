package cellsociety.view.components.cell;

import cellsociety.view.interfaces.CellView;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 * LangtonCellView represents the visual representation of a Langton's Loop simulation cell.
 *
 * <p>This class extends CellView and defines the default colors for different states in the
 * Langton's Loop simulation, which consists of multiple cell states representing the automaton's
 * behavior.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class LangtonCellView extends CellView {

  private static final Map<Integer, Color> DEFAULT_COLOR_MAP = Map.of(0, Color.TRANSPARENT, 1,
      Color.RED, 2, Color.ORANGE, 3, Color.YELLOW, 4, Color.GREEN, 5, Color.LIGHTBLUE, 6,
      Color.NAVY, 7, Color.VIOLET);

  /**
   * Constructs a LangtonCellView with the specified position, size, and initial state.
   *
   * @param x         - the x-coordinate of the cell
   * @param y         - the y-coordinate of the cell
   * @param width     - the width of the cell
   * @param height    - the height of the cell
   * @param cellState - the initial state of the cell
   */
  public LangtonCellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState, DEFAULT_COLOR_MAP);
  }
}
