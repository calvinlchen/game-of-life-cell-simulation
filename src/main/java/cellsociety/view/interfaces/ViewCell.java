package cellsociety.view.interfaces;

import cellsociety.model.interfaces.Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public abstract class ViewCell {
  public static Color DEFAULT_FILL = Color.BLACK;
  public static Color DEFAULT_OUTLINE = Color.WHITE;

  protected Cell myCell;
  protected Shape myShape;

  /**
   * Constructs a cell to be displayed to the user.
   * @param x x-position of the cell (relative to the grid)
   * @param y y-position of the cell (relative to the grid)
   * @param width width of the cell
   * @param height height of the cell
   * @param cell the Cell object which this cell represents
   * @param color fill color of the cell
   * @author Calvin Chen
   */
  ViewCell(double x, double y, double width, double height, Cell cell, Color color) {
    myCell = cell;
    myShape = createShape(x,y,width,height);
    myShape.setFill(color);
  }

  /**
   * Abstract method for creating the Shape view of the cell.
   * Subclasses can Override this method to use a different cell shape.
   */
  protected Shape createShape(double x, double y, double width, double height) {
    return new Rectangle(x, y, width, height);
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
   * Return the Cell object that this view represents
   * @return corresponding Cell object
   */
  public Cell getCell() {
    return myCell;
  }

  /**
   * Abstract method to be implemented by subclasses for updating the view
   * based on state changes in the simulation.
   */
  public abstract void updateView();
}
