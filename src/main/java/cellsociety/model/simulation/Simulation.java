package cellsociety.model.simulation;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.cell.GameOfLifeCell;
import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.simulation.cell.PercolationCell;
import cellsociety.model.simulation.cell.SegregationCell;
import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.simulation.grid.AdjacentGrid;
import cellsociety.model.simulation.grid.RectangularGrid;
import cellsociety.model.simulation.rules.FireRule;
import cellsociety.model.simulation.rules.GameOfLifeRule;
import cellsociety.model.simulation.rules.PercolationRule;
import cellsociety.model.simulation.rules.SegregationRule;
import cellsociety.model.simulation.rules.WaTorRule;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.XMLData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simulation class, creates the grid from the given xml data, provides public methods for the
 * view/xml data to step through the simulation and get the current state of the cells
 *
 * @param <T> - the type of cell in the grid, must extend Cell
 * @author Jessica Chen
 */
public class Simulation<T extends Cell<T>> {

  private final XMLData xmlData;
  private Grid<T> myGrid;

  public Simulation(XMLData data) {
    xmlData = data;

    setUpGrid();
  }

  private void setUpGrid() {
    List<Cell> cellList = new ArrayList<>();
    Map<String, Double> params = xmlData.getParameters();
    for (Integer state : xmlData.getCellStateList()) {
      switch (xmlData.getType()) {
        case GAMEOFLIFE -> cellList.add(new GameOfLifeCell(state,
            new GameOfLifeRule(params)));
        case SEGREGATION -> cellList.add(new SegregationCell(state,
            new SegregationRule(params)));
        case FIRE -> cellList.add(new FireCell(state,
            new FireRule(params)));
        case PERCOLATION -> cellList.add(new PercolationCell(state,
            new PercolationRule(params)));
        case WATOR -> cellList.add(new WaTorCell(state,
            new WaTorRule(params)));
      }
    }

    switch (xmlData.getType()) {
      case GAMEOFLIFE, SEGREGATION -> setUpRectangularGrid(cellList);
      case FIRE, PERCOLATION, WATOR -> setUpAdjacentGrid(cellList);
    }
  }

  private void setUpAdjacentGrid(List<Cell> cellList) {
    myGrid = new AdjacentGrid(cellList, xmlData.getGridRowNum(),
        xmlData.getGridColNum());
  }

  private void setUpRectangularGrid(List<Cell> cellList) {
    myGrid = new RectangularGrid(cellList, xmlData.getGridRowNum(),
        xmlData.getGridColNum());
  }

  /**
   * Moves all cells in the simulation up by one step
   */
  public void step() {
    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<T> cell = myGrid.getCell(row, col);
        cell.calcNextState();
      }
    }

    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<T> cell = myGrid.getCell(row, col);
        cell.step();
      }
    }

    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<T> cell = myGrid.getCell(row, col);
        cell.resetParameters();
      }
    }
  }

  /**
   * Returns the state of the cell at location [row, col]
   *
   * @return the state of the cell at the location if valid
   * @throws IllegalArgumentException if the x and y are an invalid position on the grid
   */
  public int getCurrentState(int row, int col) {
    return myGrid.getCell(row, col).getCurrentState();
  }

  public XMLData getXMLData() {
    return xmlData;
  }

  public SimType getSimulationType() {
    return xmlData.getType();
  }
}
