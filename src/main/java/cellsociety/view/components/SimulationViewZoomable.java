package cellsociety.view.components;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;

public class SimulationViewZoomable extends SimulationView {
  public static final double MAX_ZOOM_SCALE = 10.0;
  public static final double MIN_ZOOM_SCALE = 0.1;

  private ScrollPane myScrollPane;

  public SimulationViewZoomable(double width, double height) {
    super(width, height);
  }

  /**
   * Return and/or initialize a zoom-able grid representing the simulation cells.
   *
   * @return ScrollPane containing grid view. Returns existing ScrollPane if one already exists.
   */
  public ScrollPane getZoomableDisplay() {
    // return existing ScrollPane if one exists
    if (myScrollPane != null) {
      return myScrollPane;
    }

    // else, create ScrollPane
    Pane gridPane = super.myDisplay;
    if (gridPane == null) {
      return null;
    }
    // Wrap gridPane in a StackPane which recalculates its layout bounds based on its children transforms
    StackPane zoomContainer = new StackPane(gridPane);
    initializeMyScrollPane(zoomContainer, gridPane);

    return myScrollPane;
  }

  private void initializeMyScrollPane(StackPane zoomContainer, Pane gridPane) {
    ScrollPane scrollPane = new ScrollPane(zoomContainer);
    // Disable the scrollbars so that scrolling won’t trigger panning.
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setPannable(true);  // Instead, enable drag-to-move via mouse
    scrollPane.setFitToWidth(false);
    scrollPane.setFitToHeight(false);

    scrollPane.setOnScroll(event -> {
      // Determine zoom factor based on scroll direction.
      double zoomFactor = (event.getDeltaY() > 0) ? 1.1 : 0.9;
      double currentScale = gridPane.getScaleX();
      double newScale = currentScale * zoomFactor;
      if (newScale < MIN_ZOOM_SCALE || newScale > MAX_ZOOM_SCALE) {
        return;  // prevent zooming beyond limits
      }

      // Get the mouse coordinates relative to gridPane before scaling.
      javafx.geometry.Point2D mouseCoords = gridPane.sceneToLocal(event.getSceneX(), event.getSceneY());

      // Apply the zoom.
      gridPane.setScaleX(newScale);
      gridPane.setScaleY(newScale);

      // After scaling, compute the new mouse coordinates.
      javafx.geometry.Point2D newMouseCoords = gridPane.localToScene(mouseCoords);

      // Calculate the translation adjustment needed so that the point under the mouse remains fixed.
      double dx = event.getSceneX() - newMouseCoords.getX();
      double dy = event.getSceneY() - newMouseCoords.getY();

      gridPane.setTranslateX(gridPane.getTranslateX() + dx);
      gridPane.setTranslateY(gridPane.getTranslateY() + dy);

      event.consume();  // Prevent the event from triggering other scroll behaviors.
    });

    myScrollPane = scrollPane;
  }

  /**
   * Reset the positioning and zoom of this grid view
   */
  public void resetGridZoom() {
    Pane gridPane = super.myDisplay;
    if (gridPane != null) {
      gridPane.setScaleX(1.0);
      gridPane.setScaleY(1.0);
      gridPane.setTranslateX(0);
      gridPane.setTranslateY(0);
    }
  }
}
