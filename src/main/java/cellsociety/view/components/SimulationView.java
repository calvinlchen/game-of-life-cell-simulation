package cellsociety.view.components;

import cellsociety.model.util.XMLData;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.view.interfaces.CellView;
import cellsociety.view.components.cell.FireCellView;
import cellsociety.view.components.cell.GameOfLifeCellView;
import cellsociety.view.components.cell.PercolationCellView;
import cellsociety.view.components.cell.SegregationCellView;
import cellsociety.view.components.cell.WaTorCellView;
import javafx.scene.layout.Pane;

/**
 * Manages the position and display of the simulation's cells. (Pane suggested by ChatGPT.)
 */
public class SimulationView {

  private Pane myDisplay;
  private double myGridWidth;
  private double myGridHeight;
  private CellView[][] myCellViews;
  private double myCellWidth;
  private double myCellHeight;

  private XMLData myXML;
  private Simulation mySimulation;

  public SimulationView(double width, double height) {
    myDisplay = new Pane();
    myGridWidth = width;
    myGridHeight = height;
    myDisplay.setPrefSize(width, height);

    myCellViews = new CellView[0][0];
    myCellWidth = 0;
    myCellHeight = 0;
  }

  /**
   * Returns the grid representing the simulation cells, if mySimulation exists
   *
   * @return Pane containing the cell grid visual
   */
  public Pane getDisplay() {
    return myDisplay;
  }

  /**
   * Prepares a Simulation model and the grid's cell configuration based on an XMLData object
   *
   * @param xmlData XMLData object representing the XML file for a simulation
   */
  public void configureFromXML(XMLData xmlData) {
    myXML = xmlData;

    int numRows = xmlData.getGridRowNum();
    int numCols = xmlData.getGridColNum();
    myCellWidth = myGridWidth / numCols;
    myCellHeight = myGridHeight / numRows;

    myCellViews = new CellView[numRows][numCols];

    mySimulation = new Simulation(xmlData);
  }

  /**
   * Initializes the grid view of the currently-stored Simulation model
   */
  public void initializeGridView() {
    // If there is no simulation stored, then a grid cannot be generated
    if (mySimulation == null) {
      return;
    }

    for (int row = 0; row < myCellViews.length; row++) {
      for (int col = 0; col < myCellViews[0].length; col++) {
        createCellView(row, col, mySimulation.getCurrentState(row, col), myXML.getType());
      }
    }
  }

  private void createCellView(int cellRow, int cellCol, int cellState, SimType simType) {
    double[] position = getCellPosition(cellRow, cellCol);
    double x = position[0];
    double y = position[1];

    CellView cellView = null;

    switch (simType) {
      case GameOfLife ->
          cellView = new GameOfLifeCellView(x, y, myCellWidth, myCellHeight, cellState);
      case Fire -> cellView = new FireCellView(x, y, myCellWidth, myCellHeight, cellState);
      case Percolation ->
          cellView = new PercolationCellView(x, y, myCellWidth, myCellHeight, cellState);
      case Segregation ->
          cellView = new SegregationCellView(x, y, myCellWidth, myCellHeight, cellState);
      case WaTor -> cellView = new WaTorCellView(x, y, myCellWidth, myCellHeight, cellState);
      default -> throw new IllegalArgumentException("Unsupported simulation type: " + simType);
    }

    if (cellView != null) {
      myCellViews[cellRow][cellCol] = cellView;
      myDisplay.getChildren().add(cellView.getShape());
    }
  }

  /**
   * Returns a coordinate position for a cell based on its row and column
   *
   * @param row    A cell's row in the grid (index starting from 0)
   * @param column A cell's column in the grid (index starting from 0)
   * @return double array: [0] = x-coordinate, [1] = y-coordinate
   */
  private double[] getCellPosition(int row, int column) {
    double[] cellPositions = new double[2];
    // x-position
    cellPositions[0] = myCellWidth * column;
    // y-position
    cellPositions[1] = myCellHeight * row;
    return cellPositions;
  }

  /**
   * Updates all cell states based on simulation rules, and updates cell colors accordingly.
   */
  public void stepGridSimulation() {
    if (mySimulation == null) {
      return;
    }

    // update Cell object states based on simulation type
    mySimulation.step();

    // update cell views so that their colors match the new cell states
    for (int row = 0; row < myCellViews.length; row++) {
      for (int col = 0; col < myCellViews[0].length; col++) {
        myCellViews[row][col].setCellState(mySimulation.getCurrentState(row, col));
      }
    }
  }

  /**
   * Toggles grid lines (AKA cell outlines) for all CellViews in the simulation grid.
   * @param enable TRUE if enabling gridlines, FALSE if disabling gridlines
   */
  public void toggleGridlines(boolean enable) {
    for (CellView[] row : myCellViews) {
      for (CellView cellView : row) {
        cellView.toggleOutlines(enable);
      }
    }
  }

  /**
   * Resets the grid to an empty state.
   */
  public void resetGrid() {
    myDisplay.getChildren().clear();
    mySimulation = null;
  }

  /**
   * Get the Simulation model object represented by this view
   *
   * @return null or Simulation object
   */
  public Simulation getSimulation() {
    return mySimulation;
  }
}
