package cellsociety.model.simulation;

import cellsociety.model.interfaces.Cell;
import cellsociety.model.interfaces.Grid;
import cellsociety.model.simulation.grid.AdjacentGrid;
import cellsociety.model.simulation.grid.RectangularGrid;
import cellsociety.model.util.XMLData;
import java.util.ArrayList;

public class Simulation<S extends Enum<S>, T extends Cell<S, T>> {
  private final XMLData xmlData;
  private Grid<S,T> myGrid;

  public Simulation(XMLData data) {
    xmlData = data;

    setUpGrid();
  }

  private void setUpGrid() {
    switch (xmlData.getType()) {
      case GAMEOFLIFE, SEGREGATION -> setUpRectangularGrid();
      case FIRE, PERCOLATION, WATOR -> setUpAdjacentGrid();
    }
  }

  // replace with xmlData.getCellStateList()
  private void setUpAdjacentGrid() {
    myGrid = new AdjacentGrid<>(new ArrayList<>(), xmlData.getGridRowNum(),
        xmlData.getGridColNum());
  }

  private void setUpRectangularGrid() {
    myGrid = new RectangularGrid<>(new ArrayList<>(), xmlData.getGridRowNum(),
        xmlData.getGridColNum());
  }

  /**
   * moves all cells in the simulation up by one step
   */
  public void step() {
    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<S,T> cell = myGrid.getCell(row, col);
        cell.calcNextState();
      }
    }

    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<S,T> cell = myGrid.getCell(row, col);
        cell.step();
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
    return myGrid.getCell(row,col).getCurrentState();
  }
}
