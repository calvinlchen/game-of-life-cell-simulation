package cellsociety.view.components;

import javafx.scene.layout.Pane;

/**
 * Manages the position and display of the simulation's cells. (Pane suggested by ChatGPT.)
 */
public class SimulationView {
  private Pane myDisplay;

  public SimulationView() {
    myDisplay = new Pane();
    myDisplay.setPrefSize(1000, 1000);
  }

  public Pane getDisplay() {
    return myDisplay;
  }

  /**
   * Initializes given cells onto the grid view
   */
  public void initializeGridView() {
    // TODO: Implement actual simulation cells
  }

  /**
   * Updates the grid based on the current simulation state.
   */
  public void updateGrid() {
    // TODO: Implement actual simulation update logic
  }

  /**
   * Resets the grid to an empty state.
   */
  public void resetGrid() {
    myDisplay.getChildren().clear();
  }
}
