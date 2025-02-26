package cellsociety.view.interfaces;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Abstract class representing a Cell's visual representation.
 */
public abstract class CellView {

  public static final String DEFAULT_OUTLINE_CLASS = "cell-default-outline";
  public static final String DEFAULT_NO_OUTLINE_CLASS = "cell-no-outline";

  private final Map<Integer, Color> myColorMap;  // contains state-to-color mappings
  protected int myCellState;
  protected Shape myShape;
  private boolean myOutlinesEnabled;

  /**
   * Constructs a cell to be displayed to the user.
   *
   * @param x         x-position of the cell (relative to the Scene)
   * @param y         y-position of the cell (relative to the Scene)
   * @param width     width of the cell
   * @param height    height of the cell
   * @param cellState the current state represented by this cell
   * @author Calvin Chen
   */
  protected CellView(double x, double y, double width, double height, int cellState,
      Map<Integer, Color> colorMap) {
    myCellState = cellState;
    myColorMap = new HashMap<>(colorMap); // configure state colors according to colorMap
    myShape = createShape(x, y, width, height);
    enableOutlines();   // enable grid outlines by default
    updateViewColor();  // Set color based on cell state
  }

  /**
   * Default method for creating the Shape view of the cell. Subclasses can Override this method to
   * use a different cell Shape type or styling.
   */
  protected Shape createShape(double x, double y, double width, double height) {
    return new Rectangle(x, y, width, height);
  }

  /**
   * Return the cell's view as a Shape object.
   *
   * @return my Shape object
   */
  public Shape getShape() {
    return myShape;
  }

  /**
   * Set the State represented by this cell view.
   *
   * @param state corresponding State
   */
  public void setCellState(int state) {
    myCellState = state;
    updateViewColor();  // Update the color of the cell to match its new represented state
  }

  /**
   * Return the State represented by this cell view.
   *
   * @return corresponding State
   */
  public int getCellState() {
    return myCellState;
  }

  /**
   * Refreshes the cell view's fill color to match its currently represented state.
   */
  public void updateViewColor() {
    setColor(getColorForState(myCellState));
  }

  /**
   * Set the fill of the cell's Shape to the given Color.
   *
   * @param color Color to fill the cell
   */
  private void setColor(Color color) {
    myShape.setFill(color);
  }

  /**
   * Returns the color value of a given state.
   *
   * @param state int value of the state, such as 0 for EMPTY
   */
  public Color getColorForState(int state) {
    return myColorMap.getOrDefault(state, Color.TRANSPARENT);
  }

  /**
   * Updates a state's assigned color in the map, and updates the cell view accordingly.
   */
  public void setColorForState(int state, Color color) {
    myColorMap.put(state, color);
    updateViewColor();
  }

  /**
   * Get the number of displayable states.
   *
   * @return the total number of possible states for this cell
   */
  public int getNumStates() {
    return myColorMap.size();
  }

  /**
   * Add "grid" outlines based on CSS file format (resets cell CSS formatting).
   */
  private void enableOutlines() {
    if (!myOutlinesEnabled) {
      myShape.getStyleClass().remove(DEFAULT_NO_OUTLINE_CLASS);
      myShape.getStyleClass().addAll(DEFAULT_OUTLINE_CLASS);    // Apply CSS
      myOutlinesEnabled = true;
    }
  }

  /**
   * Remove "grid" outlines based on CSS file format (resets cell CSS formatting).
   */
  private void disableOutlines() {
    if (myOutlinesEnabled) {
      myShape.getStyleClass().remove(DEFAULT_OUTLINE_CLASS);
      myShape.getStyleClass().addAll(DEFAULT_NO_OUTLINE_CLASS); // Apply CSS
      myOutlinesEnabled = false;
    }
  }

  /**
   * Toggles the cell outline on/off for this CellView.
   *
   * @param enable TRUE if enabling gridlines, FALSE if disabling gridlines
   */
  public void toggleOutlines(boolean enable) {
    if (enable) {
      enableOutlines();
    } else {
      disableOutlines();
    }
  }
}
