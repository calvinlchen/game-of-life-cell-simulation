package cellsociety.model.simulation;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.interfaces.Grid;
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
import cellsociety.model.util.XMLData;
import cellsociety.model.util.constants.CellStates.FireStates;
import cellsociety.model.util.constants.CellStates.GameOfLifeStates;
import cellsociety.model.util.constants.CellStates.PercolationStates;
import cellsociety.model.util.constants.CellStates.SegregationStates;
import cellsociety.model.util.constants.CellStates.WaTorStates;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Simulation<S extends Enum<S>, T extends Cell<S, T>> {

  private final XMLData xmlData;
  private Grid<S, T> myGrid;

  public Simulation(XMLData data) {
    xmlData = data;

    setUpGrid();
  }

  private void setUpGrid() {
    List<Cell> cellList = new ArrayList<>();
    Map<String, Double> params = xmlData.getParameters();
    for (Enum state : xmlData.getCellStateList()) {
      switch (xmlData.getType()) {
        case GAMEOFLIFE -> cellList.add(new GameOfLifeCell((GameOfLifeStates) state,
            new GameOfLifeRule(params)));
        case SEGREGATION -> cellList.add(new SegregationCell((SegregationStates) state,
            new SegregationRule(params)));
        case FIRE -> cellList.add(new FireCell((FireStates) state,
            new FireRule(params)));
        case PERCOLATION -> cellList.add(new PercolationCell((PercolationStates) state,
            new PercolationRule(params)));
        case WATOR -> cellList.add(new WaTorCell((WaTorStates) state,
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
   * moves all cells in the simulation up by one step
   */
  public void step() {
    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<S, T> cell = myGrid.getCell(row, col);
        cell.calcNextState();
      }
    }

    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<S, T> cell = myGrid.getCell(row, col);
        cell.step();
      }
    }

    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<S, T> cell = myGrid.getCell(row, col);
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
  public S getCurrentState(int row, int col) {
    return myGrid.getCell(row, col).getCurrentState();
  }

  public XMLData getXMLData() {
    return xmlData;
  }
}
