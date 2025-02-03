package cellsociety.view.components;

import cellsociety.model.util.constants.CellStates.SimulationTypes;
import cellsociety.model.util.constants.CellStates.FireStates;
import cellsociety.model.util.constants.CellStates.GameOfLifeStates;
import cellsociety.view.interfaces.CellView;
import javafx.scene.layout.Pane;

/**
 * Manages the position and display of the simulation's cells. (Pane suggested by ChatGPT.)
 */
public class SimulationView {
  private Pane myDisplay;
  private int myGridWidth;
  private int myGridHeight;
  private int myNumRows;
  private int myNumCols;

  public SimulationView(int width, int height) {
    myDisplay = new Pane();
    myGridWidth = width;
    myGridHeight = height;
    myDisplay.setPrefSize(width, height);

    myNumRows = 0;
    myNumCols = 0;
  }

  public Pane getDisplay() {
    return myDisplay;
  }

  /**
   * Initializes given cells onto the grid view
   */
  public void initializeGridView() {
    // TODO: Implement CellView creation
  }

  private void createCellView(int cellRow, int cellCol, Enum<?> cellState, SimulationTypes simType) {
    double[] position = getCellPosition(cellRow, cellCol);
    double x = position[0];
    double y = position[1];
    double cellWidth = (double) myGridWidth / myNumCols;
    double cellHeight = (double) myGridHeight / myNumRows;

    CellView<?> cellView = null;

    switch (simType) {
      case GameOfLife -> {
        if (cellState instanceof GameOfLifeStates) {
          cellView = new GameOfLifeCellView(x, y, cellWidth, cellHeight, (GameOfLifeStates) cellState);
        }
      }
      case Fire -> {
        if (cellState instanceof FireStates) {
          cellView = new FireCellView(x, y, cellWidth, cellHeight, (FireStates) cellState);
        }
      }
      case Percolation -> {
        // TODO: Percolation cell creation implementation
      }
      case Segregation -> {
        // TODO: Segregation cell creation implementation
      }
      case WaTor -> {
        // TODO: WaTor cell creation implementation
      }
      default -> throw new IllegalArgumentException("Unsupported simulation type: " + simType);
    }

    if (cellView != null) {
      myDisplay.getChildren().add(cellView.getShape());
    }
  }

  /**
   * Returns a coordinate position for a cell based on its row and column
   * @param row A cell's row in the grid (index starting from 0)
   * @param column A cell's column in the grid (index starting from 0)
   * @return double array: [0] = x-coordinate, [1] = y-coordinate
   */
  private double[] getCellPosition(int row, int column) {
    double[] cellPositions = new double[2];
    // x-position
    cellPositions[0] = (double) (myGridWidth / myNumCols)  * column;
    // y-position
    cellPositions[1] = (double) (myGridHeight / myNumRows) * row;
    return cellPositions;
  }

  /**
   * Calls the Simulation class to update all cells in the simulation
   */
  public void stepGrid() {
    // TODO: Implement actual simulation update logic
  }

  /**
   * Resets the grid to an empty state.
   */
  public void resetGrid() {
    myDisplay.getChildren().clear();
  }

  /**
   * Set the number of grid rows
   * @param numRows total number of rows of cells to display in the grid
   */
  public void setNumRows(int numRows) {
    myNumRows = numRows;
  }

  /**
   * Set the number of grid columns
   * @param numCols total number of columns of cells to display in the grid
   */
  public void setNumCols(int numCols) {
    myNumCols = numCols;
  }
}
