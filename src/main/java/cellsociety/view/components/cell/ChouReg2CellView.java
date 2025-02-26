package cellsociety.view.components.cell;

/**
 * ChouReg2CellView represents the visual representation of a Chou-Reg 2 Langton's Loop cell.
 *
 * <p>This class extends LangtonCellView, inheriting its behavior while allowing for specific
 * customization if needed for Chou-Reg 2 simulations.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class ChouReg2CellView extends LangtonCellView {

  /**
   * Constructs a ChouReg2CellView with the specified position, size, and initial state.
   *
   * @param x         - the x-coordinate of the cell
   * @param y         - the y-coordinate of the cell
   * @param width     - the width of the cell
   * @param height    - the height of the cell
   * @param cellState - the initial state of the cell
   */
  public ChouReg2CellView(double x, double y, double width, double height, int cellState) {
    super(x, y, width, height, cellState);
  }
}
