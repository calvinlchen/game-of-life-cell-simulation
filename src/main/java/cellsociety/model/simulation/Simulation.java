package cellsociety.model.simulation;

import static cellsociety.model.util.constants.ResourcePckg.getErrorSimulationResourceBundle;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.grid.AdjacentGrid;
import cellsociety.model.simulation.grid.RectangularGrid;
import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.XmlData;

import cellsociety.model.util.constants.GridTypes.EdgeType;
import cellsociety.model.util.constants.GridTypes.NeighborhoodType;
import cellsociety.model.util.constants.GridTypes.ShapeType;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Simulation class, creates the grid from the given XML data, provides public methods for the
 * view/XML data to step through the simulation and get the current state of the cells.
 *
 * @param <T> - the type of cell in the grid, must extend Cell
 * @author Jessica Chen
 * @author ChatGPT, helped with some of the JavaDocs
 */
public class Simulation<T extends Cell<T, ?, ?>> {

  private final XmlData xmlData;
  private Grid<T> myGrid;
  private Parameters parameters;

  private static final String CELL_PACKAGE = "cellsociety.model.simulation.cell.";

  private final ResourceBundle myResources;
  private final String myLanguage;

  private int totalIterations;

  /**
   * Initializes a Simulation instance using the provided XML data.
   *
   * @param data - XML data containing simulation configuration
   * @throws SimulationException if the XML data is null
   */
  public Simulation(XmlData data) {
    myResources = getErrorSimulationResourceBundle("English");
    myLanguage = "English";
    xmlData = getXmlData(data);
    setUpSimulation();
  }

  /**
   * Initializes a Simulation instance using the provided XML data and a specified language for
   * error messages.
   *
   * @param data     - XML data containing simulation configuration
   * @param language - name of the language for error message display
   * @throws SimulationException if the XML data is null
   */
  public Simulation(XmlData data, String language) {
    myResources = getErrorSimulationResourceBundle(language);
    myLanguage = language;
    xmlData = getXmlData(data);
    setUpSimulation();
  }

  private XmlData getXmlData(XmlData data) {
    final XmlData xmlData;
    if (data == null) {
      throw new SimulationException(myResources.getString("NullXMLData"));
    }

    xmlData = data;
    return xmlData;
  }

  private void setUpSimulation() {
    try {
      SimType simType = xmlData.getType();
      Map<String, Double> params = xmlData.getParameters();

      Rule<T, ?> rule = (Rule<T, ?>) RuleFactory.createRule(simType.name() + "Rule", params,
          myLanguage);
      parameters = rule.getParameters();

      List<Cell<T, ?, ?>> cellList = createCells(simType, rule, myLanguage);

      setUpGridStructure(cellList, simType);
    } catch (Exception e) {
      throw new SimulationException(myResources.getString("SimulationSetupFailed"), e);
    }
  }

  private List<Cell<T, ?, ?>> createCells(SimType simType, Rule<T, ?> rule, String language) {
    List<Cell<T, ?, ?>> cellList = new ArrayList<>();

    try {
      Class<?> cellClass = Class.forName(CELL_PACKAGE + simType.name() + "Cell");
      Constructor<?> cellConstructor = cellClass.getConstructor(int.class, rule.getClass(),
          String.class);

      for (Integer state : xmlData.getCellStateList()) {
        cellList.add((Cell<T, ?, ?>) cellConstructor.newInstance(state, rule, language));
      }
    } catch (Exception e) {
      throw new SimulationException(
          String.format(myResources.getString("CellCreationFailed"), simType), e);
    }

    return cellList;
  }

  private void setUpGridStructure(List<Cell<T, ?, ?>> cellList, SimType simType) {
    if (simType.isDefaultRectangularGrid()) {
      myGrid = new RectangularGrid(cellList, xmlData.getGridRowNum(), xmlData.getGridColNum(),
          myLanguage);
    } else {
      myGrid = new AdjacentGrid(cellList, xmlData.getGridRowNum(), xmlData.getGridColNum(),
          myLanguage);
    }
  }

  /**
   * Moves all cells in the simulation backward by one step.
   */
  public void stepBack() {
    boolean success = false;
    for (Cell cell : myGrid.getCells()) {
      // all cells have the same success since they should ideally all have the same step history
      success = cell.stepBack();
    }

    if (success) {
      totalIterations--;
    }
  }

  /**
   * Moves all cells in the simulation forward by one step.
   */
  public void step() {
    totalIterations++;
    myGrid.getCells().forEach(Cell::saveCurrentState);
    myGrid.getCells().forEach(Cell::calcNextState);
    myGrid.getCells().forEach(Cell::step);
    myGrid.getCells().forEach(Cell::resetParameters);
  }

  /**
   * Returns the state of the cell at a specified location in the grid.
   *
   * @param row - row index of the cell
   * @param col - column index of the cell
   * @return the current state of the cell at the specified location
   * @throws SimulationException if the row and column are out of bounds
   */
  public int getCurrentState(int row, int col) {
    try {
      return myGrid.getCell(row, col).getCurrentState();
    } catch (Exception e) {
      throw new SimulationException(
          String.format(myResources.getString("InvalidGridPosition"), row, col), e);
    }
  }

  /**
   * Retrieves the duration for which the cell at the specified position has remained in the same
   * state.
   *
   * @param row - the row index of the cell
   * @param col - the column index of the cell
   * @return the state length of the cell at the specified location
   * @throws SimulationException if the specified position is invalid or an error occurs
   */
  public int getStateLength(int row, int col) {
    try {
      return myGrid.getCell(row, col).getStateLength();
    } catch (Exception e) {
      throw new SimulationException(
          String.format(myResources.getString("InvalidGridPosition"), row, col), e);
    }
  }

  /**
   * Updates a single simulation parameter dynamically.
   *
   * @param key   - the parameter name
   * @param value - the new value for the parameter
   * @throws SimulationException if the parameter key is not found
   */
  public void updateParameter(String key, double value) {
    try {
      parameters.setParameter(key, value);
    } catch (Exception e) {
      throw new SimulationException(String.format(myResources.getString("ParameterNotFound"), key),
          e);
    }
  }

  /**
   * Retrieves the current value of a parameter.
   *
   * @param key - the parameter name
   * @return the parameter's current value
   * @throws SimulationException if the parameter key is not found
   */
  public double getParameter(String key) {
    try {
      return parameters.getParameter(key);
    } catch (Exception e) {
      throw new SimulationException(String.format(myResources.getString("ParameterNotFound"), key),
          e);
    }
  }

  /**
   * Retrieves all parameter keys in the simulation.
   *
   * @return a list of parameter keys
   */
  public List<String> getParameterKeys() {
    return parameters.getParameterKeys();
  }

  /**
   * Returns the XML data that created the simulation.
   *
   * @return the XMLData object containing the simulation's initial configuration
   */
  public XmlData getXmlDataObject() {
    return xmlData;
  }

  /**
   * Retrieves the simulation type.
   *
   * <p>Assumption: Each dynamic type is initialized with a unique XMLData instance.
   *
   * @return the simulation type as a SimType enum
   */
  public SimType getSimulationType() {
    return xmlData.getType();
  }

  /**
   * Retrieves the simulation ID from the XML data.
   *
   * <p>Assumption: Each dynamic type is initialized with a unique XMLData instance.
   *
   * @return the simulation ID from the XML data
   */
  public int getSimulationId() {
    return xmlData.getId();
  }

  /**
   * Retrieves the number of states from the XML data.
   *
   * <p>This value is only accurate for simulations with dynamically assigned states.
   *
   * @return the number of states from the XML data parameters
   */
  public int getNumStates() {
    return xmlData.getNumStates();
  }

  /**
   * Retrieves the total number of iterations completed in the simulation.
   *
   * @return the total number of iterations completed
   */
  public int getTotalIterations() {
    return totalIterations;
  }

  /**
   * Sets the neighbors for the grid cells based on the specified shape, neighborhood type, and edge
   * type. This operation clears all previous neighbors for all cells before setting the new
   * configuration.
   *
   * @param shape        The shape type of the cells (e.g., RECTANGLE, HEXAGON, TRIANGLE).
   * @param neighborhood The neighborhood type defining neighbor relationships (e.g., MOORE,
   *                     VON_NEUMANN, EXTENDED_MOORE).
   * @param edge         The edge type specifying boundary behavior (e.g., NONE, MIRROR, TOROIDAL).
   */
  public void setNeighbors(ShapeType shape, NeighborhoodType neighborhood, EdgeType edge) {
    myGrid.setNeighbors(shape, neighborhood, edge);
  }
}
