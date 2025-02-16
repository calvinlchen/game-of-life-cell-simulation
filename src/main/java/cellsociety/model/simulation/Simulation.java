package cellsociety.model.simulation;

import cellsociety.model.factories.RuleFactory;
import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.grid.AdjacentGrid;
import cellsociety.model.simulation.grid.RectangularGrid;
import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.XMLData;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simulation class, creates the grid from the given XML data, provides public methods for the
 * view/XML data to step through the simulation and get the current state of the cells.
 *
 * @param <T> - the type of cell in the grid, must extend Cell
 * @author Jessica Chen
 */
public class Simulation<T extends Cell<T, ?, ?>> {
  private final XMLData xmlData;
  private Grid<T> myGrid;
  private Parameters parameters; // Holds the parameters for the rule

  private static final String CELL_PACKAGE = "cellsociety.model.simulation.cell.";

  public Simulation(XMLData data) {
    xmlData = data;

    setUpSimulation();
  }

  /**
   * Sets up the simulation, initializing the rule, parameters, and grid dynamically.
   */
  private void setUpSimulation() {
    SimType simType = xmlData.getType();
    Map<String, Double> params = xmlData.getParameters();

    Rule<T, ?> rule = (Rule<T, ?>) RuleFactory.createRule(simType.name() + "Rule", params);
    parameters = rule.getParameters();

    List<Cell<T, ?, ?>> cellList = createCells(simType, rule);

    setUpGridStructure(cellList, simType);
  }

  /**
   * Dynamically creates cells based on the simulation type.
   *
   * @param simType - Type of simulation
   * @param rule    - The rule instance to associate with each cell
   * @return List of dynamically created cells
   */
  private List<Cell<T, ?, ?>> createCells(SimType simType, Rule<T, ?> rule) {
    List<Cell<T, ?, ?>> cellList = new ArrayList<>();

    try {
      Class<?> cellClass = Class.forName(CELL_PACKAGE + simType.name() + "Cell");
      Constructor<?> cellConstructor = cellClass.getConstructor(int.class, rule.getClass());

      for (Integer state : xmlData.getCellStateList()) {
        cellList.add((Cell<T, ?, ?>) cellConstructor.newInstance(state, rule));
      }
    } catch (Exception e) {
      throw new RuntimeException("Error creating cells for simulation: " + simType, e);
    }

    return cellList;
  }

  /**
   * Configures the grid structure based on the simulation type.
   *
   * @param cellList - The list of cells for the simulation
   * @param simType  - The type of simulation
   */
  private void setUpGridStructure(List<Cell<T, ?, ?>> cellList, SimType simType) {
    if (simType == SimType.GameOfLife || simType == SimType.Segregation) {
      myGrid = new RectangularGrid(cellList, xmlData.getGridRowNum(), xmlData.getGridColNum());
    } else {
      myGrid = new AdjacentGrid(cellList, xmlData.getGridRowNum(), xmlData.getGridColNum());
    }
  }

  /**
   * Moves all cells in the simulation backward by one step.
   */
  public void stepBack() {
    myGrid.getCells().forEach(Cell::stepBack);
  }

  /**
   * Moves all cells in the simulation forward by one step.
   */
  public void step() {
    // add get cells to simplify this
    // TODO: jessica once you done refactoring need to refactor your random comments
    // TOOD: really adding so much refactoring for myself *facepalm*
    myGrid.getCells().forEach(Cell::saveCurrentState);
    myGrid.getCells().forEach(Cell::calcNextState);
    myGrid.getCells().forEach(Cell::step);
    myGrid.getCells().forEach(Cell::resetParameters);
  }

  /**
   * Returns the state of the cell at location [row, col].
   *
   * @return the state of the cell at the location if valid
   * @throws IllegalArgumentException if the row and col are an invalid position on the grid
   */
  public int getCurrentState(int row, int col) {
    return myGrid.getCell(row, col).getCurrentState();
  }

  /**
   * Update a single simulation parameter dynamically.
   *
   * @param key   - The parameter name
   * @param value - The new value for the parameter
   */
  public void updateParameter(String key, double value) {
    parameters.setParameter(key, value);
  }

  /**
   * Retrieve the current value of a parameter.
   *
   * @param key - The parameter name
   * @return The parameter's current value
   */
  public double getParameter(String key) {
    return parameters.getParameter(key);
  }

  /**
   * Retrieve all parameter keys.
   *
   * @return A set of parameter keys
   */
  public List<String> getParameterKeys() {
    return parameters.getParameterKeys();
  }

  public XMLData getXMLData() {
    return xmlData;
  }

  public SimType getSimulationType() {
    return xmlData.getType();
  }

  public int getSimulationID() {
    return xmlData.getId();
  }

  public int getNumStates() {
    return xmlData.getNumStates();
  }
}
