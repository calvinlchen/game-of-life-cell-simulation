package cellsociety.view.interfaces;

import cellsociety.model.interfaces.Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Abstract class representing a Cell's visual representation.
 *
 * @param <S> The state type of the cell (must be an Enum).
 */
public abstract class CellView<S extends Enum<S>> {
  public static Color DEFAULT_FILL = Color.BLACK;
  public static Color DEFAULT_OUTLINE = Color.WHITE;

  protected S myCellState;
  protected Shape myShape;

  /**
   * Constructs a cell to be displayed to the user.
   * @param x x-position of the cell (relative to the Scene)
   * @param y y-position of the cell (relative to the Scene)
   * @param width width of the cell
   * @param height height of the cell
   * @param cellState the cell's current state from the simulation type's enum
   * @author Calvin Chen
   */
  protected CellView(double x, double y, double width, double height, S cellState) {
    myCellState = cellState;
    myShape = createShape(x,y,width,height);
    updateViewColor(); // Set color based on cell state
  }

  /**
   * Default method for creating the Shape view of the cell.
   * Subclasses can Override this method to use a different cell Shape type or styling.
   */
  protected Shape createShape(double x, double y, double width, double height) {
    Shape shape = new Rectangle(x, y, width, height);
    shape.setFill(DEFAULT_FILL);
    shape.setStroke(DEFAULT_OUTLINE);
    shape.setStrokeWidth(1);
    return shape;
  }

  /**
   * Set the fill of the cell's Shape to the given Color
   * @param color Color to fill the cell
   */
  public void setColor(Color color) {
    myShape.setFill(color);
  }

  /**
   * Return the cell's view as a Shape object
   * @return my Shape object
   */
  public Shape getShape() {
    return myShape;
  }

  /**
   * Return the State of the cell that this view represents
   * @return corresponding State
   */
  public S getCellState() {
    return myCellState;
  }

  /**
   * Updates the cell's visual representation based on its state.
   */
  public void updateViewColor() {
    setColor(getColorForState(myCellState));
  }

  /**
   * Abstract method to be implemented by subclasses to provide state-to-color mapping. (ChatGPT)
   */
  protected abstract Color getColorForState(S state);
}
