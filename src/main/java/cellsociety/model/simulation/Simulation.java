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
import java.lang.reflect.Constructor;
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
public class Simulation<T extends Cell<T, ?>> {

  private final XMLData xmlData;
  private Grid<T> myGrid;

  private static final Map<SimType, String> CELL_CLASSES = Map.of(
      SimType.GAMEOFLIFE, "cellsociety.model.simulation.cell.GameOfLifeCell",
      SimType.SEGREGATION, "cellsociety.model.simulation.cell.SegregationCell",
      SimType.FIRE, "cellsociety.model.simulation.cell.FireCell",
      SimType.PERCOLATION, "cellsociety.model.simulation.cell.PercolationCell",
      SimType.WATOR, "cellsociety.model.simulation.cell.WaTorCell"
  );

  private static final Map<SimType, String> RULE_CLASSES = Map.of(
      SimType.GAMEOFLIFE, "cellsociety.model.simulation.rules.GameOfLifeRule",
      SimType.SEGREGATION, "cellsociety.model.simulation.rules.SegregationRule",
      SimType.FIRE, "cellsociety.model.simulation.rules.FireRule",
      SimType.PERCOLATION, "cellsociety.model.simulation.rules.PercolationRule",
      SimType.WATOR, "cellsociety.model.simulation.rules.WaTorRule"
  );

  public Simulation(XMLData data) {
    xmlData = data;

    setUpGrid();
  }

  private void setUpGrid() {
    List<Cell> cellList = new ArrayList<>();
    SimType simType = xmlData.getType();
    Map<String, Double> params = xmlData.getParameters();

    try {
      Class<?> ruleClass = Class.forName(RULE_CLASSES.get(simType));
      Constructor<?> ruleConstructor = ruleClass.getConstructor(Map.class);
      Object ruleInstance = ruleConstructor.newInstance(params);

      Class<?> cellClass = Class.forName(CELL_CLASSES.get(simType));
      Constructor<?> cellConstructor = cellClass.getConstructor(int.class, ruleClass);

      for (Integer state : xmlData.getCellStateList()) {
        cellList.add((Cell) cellConstructor.newInstance(state, ruleInstance));
      }

      setUpGridStructure(cellList, simType);

    } catch (Exception e) {
      // TODO: make this error better
      throw new RuntimeException("Error initializing simulation: " + e.getMessage(), e);
    }
  }

  private void setUpGridStructure(List<Cell> cellList, SimType simType) {
    if (simType == SimType.GAMEOFLIFE || simType == SimType.SEGREGATION) {
      myGrid = new RectangularGrid(cellList, xmlData.getGridRowNum(), xmlData.getGridColNum());
    } else {
      myGrid = new AdjacentGrid(cellList, xmlData.getGridRowNum(), xmlData.getGridColNum());
    }
  }

  /**
   * Moves all cells in the simulation backward by one step
   */
  public void stepBack() {
    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<T, ?> cell = myGrid.getCell(row, col);
        cell.stepBack();
      }
    }
  }

  /**
   * Moves all cells in the simulation up by one step
   */
  public void step() {
    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<T, ?> cell = myGrid.getCell(row, col);
        cell.saveCurrentState();
        cell.calcNextState();
      }
    }

    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<T, ?> cell = myGrid.getCell(row, col);
        cell.step();
      }
    }

    for (int row = 0; row < xmlData.getGridRowNum(); row++) {
      for (int col = 0; col < xmlData.getGridColNum(); col++) {
        Cell<T, ?> cell = myGrid.getCell(row, col);
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
