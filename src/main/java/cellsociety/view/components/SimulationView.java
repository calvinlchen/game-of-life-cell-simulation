package cellsociety.view.components;

import cellsociety.model.util.XMLData;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.util.SimulationTypes.SimType;
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
  private CellView[][] myCellViews;
  private double myCellWidth;
  private double myCellHeight;

  private XMLData myXML;
  private Simulation mySimulation;

  public SimulationView(int width, int height) {
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
   * @return Pane containing the cell grid visual
   */
  public Pane getDisplay() {
    return myDisplay;
  }

  /**
   * Prepares a Simulation model and the grid's cell configuration based on an XMLData object
   * @param xmlData XMLData object representing the XML file for a simulation
   */
  public void configureFromXML(XMLData xmlData) {
    myXML = xmlData;

    int numRows = xmlData.getGridRowNum();
    int numCols = xmlData.getGridColNum();
    myCellWidth = (double) myGridWidth / numRows;
    myCellHeight = (double) myGridHeight / numCols;

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

  private void createCellView(int cellRow, int cellCol, Enum<?> cellState, SimType simType) {
    double[] position = getCellPosition(cellRow, cellCol);
    double x = position[0];
    double y = position[1];

    CellView<?> cellView = null;

    switch (simType) {
      case GAMEOFLIFE -> {
        if (cellState instanceof GameOfLifeStates) {
          cellView = new GameOfLifeCellView(x, y, myCellWidth, myCellHeight, (GameOfLifeStates) cellState);
        }
      }
      case FIRE -> {
        if (cellState instanceof FireStates) {
          cellView = new FireCellView(x, y, myCellWidth, myCellHeight, (FireStates) cellState);
        }
      }
      case PERCOLATION -> {
        // TODO: Percolation cell creation implementation
      }
      case SEGREGATION -> {
        // TODO: Segregation cell creation implementation
      }
      case WATOR -> {
        // TODO: WaTor cell creation implementation
      }
      default -> throw new IllegalArgumentException("Unsupported simulation type: " + simType);
    }

    if (cellView != null) {
      myCellViews[cellRow][cellCol] = cellView;
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
    cellPositions[0] = (double) (myGridWidth / myCellViews.length)  * column;
    // y-position
    cellPositions[1] = (double) (myGridHeight / myCellViews[0].length) * row;
    return cellPositions;
  }

  /**
   * Updates all cell states based on simulation rules, and updates cell colors accordingly.
   */
  public void stepGridSimulation() {
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
   * Resets the grid to an empty state.
   */
  public void resetGrid() {
    myDisplay.getChildren().clear();
  }
}
