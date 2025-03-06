package cellsociety.model.simulation;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.XmlData;

import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cellsociety.model.util.constants.GridTypes.EdgeType;
import cellsociety.model.util.constants.GridTypes.NeighborhoodType;
import cellsociety.model.util.constants.GridTypes.ShapeType;
import cellsociety.model.util.exceptions.SimulationException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The main entry point for interacting with simulation logic for both view and configuration. It
 * encapsulates the setup of the internal grid, rule, parameter, and cell management, and allows the
 * View to interact through well-defined methods.
 *
 * <p><b>API Mindset:</b>
 * This class follows an API mindset by providing methods to manage the simulation process rather
 * than exposing raw data. It uses position as an ID to manage cells without revealing underlying
 * structures. Simulation exceptions provide information to the View about errors and their causes.
 * It employs read-only methods (getters for states) and update-only methods for simulation
 * parameters to keep the internal state encapsulated.</p>
 *
 * <p><b>SOLID Principles:</b>
 * <ul>
 *   <li><b>S:</b> This class manages simulation initialization, stepping, and metadata access only,
 *       delegating cell logic and management to other subclasses.</li>
 *   <li><b>O:</b> Utilizes polymorphism and the <b>Factory Pattern</b> as well as <b>Reflection</b>
 *       to create Cell and Rule classes, allowing easy extension of simulation types without
 *       modifying logic.</li>
 *   <li><b>L:</b> Uses generics to allow any subclass of parameters, cell, or rule to be used in
 *       the simulation as long as they follow generic constraints.</li>
 *   <li><b>I:</b> External users do not interact directly with Grid, Rule, Cell, or Parameters.
 *   </li>
 *   <li><b>D:</b> Through the use of generics, the class depends on abstract versions of Rule,
 *      Cell, and Parameters.</li>
 * </ul></p>
 */
public class Simulation<T extends Cell<T, Rule<T>>> {

  private static final Logger logger = LogManager.getLogger(Simulation.class);

  private final XmlData myXmlData;
  private Grid<T> myGrid;
  private GenericParameters myParameters;

  private static final String CELL_PACKAGE = "cellsociety.model.simulation.cell.";


  private int totalIterations;


  /**
   * Constructs a new simulation instance using the provided XML configuration data. This method
   * initializes the simulation by validating the input data and setting up the simulation
   * environment (grid, rules, parameters, and cells).
   *
   * @param data The XMLData object containing configuration details such as simulation type, grid
   *             dimensions, initial cell states, and parameter settings.
   * @throws SimulationException If the provided XML data is null or if issues occur during
   *                             simulation setup (e.g., rule creation or grid construction).
   */
  public Simulation(XmlData data) {
    try {
      myXmlData = getXmlData(data);
      setUpSimulation();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }


  /**
   * Helper method: validates XmlData and stores it.
   *
   * <p>Throws error if XML is null.</p>
   */
  private XmlData getXmlData(XmlData data) {
    final XmlData xmlData;
    if (data == null) {
      logger.error("Simulation initialization failed: Null XML data");
      throw new SimulationException("NullParameter", List.of("XmlData", "Simulation()"));
    }

    xmlData = data;
    return xmlData;
  }

  // Start of Simulation Setup ------

  /**
   * Helper method: outlines the order in which to setup the simulation.
   *
   * <ul>
   *   <li>Create a grid instance so rule can get a reference of it if needed.</li>
   *   <li>Call rule factory to set up Parameter and Rule based on simType.</li>
   *   <li>Based on states from XML as well as new rule just created create cells.</li>
   *   <li>Using cells, initialize them into the grid with the correct neighbors.</li>
   * </ul>
   */
  private void setUpSimulation() {
    try {
      SimType simType = myXmlData.getType();
      myGrid = new Grid();

      Rule<T> rule = setUpRules(simType);
      List<T> cellList = createCells(simType, rule);
      setUpGridStructure(cellList);
    } catch (SimulationException e) {
      logger.error("Failed to set up simulation: ", e);
      throw new SimulationException(e);
    }
  }


  private Rule<T> setUpRules(SimType simType) {
    Rule<T> rule;

    try {
      Map<String, Object> params = myXmlData.getParameters();

      rule = (Rule<T>) RuleFactory.createRule(simType, params, myGrid);
      myParameters = rule.getParameters();

    } catch (SimulationException e) {
      logger.error("Failed to set up rules for simulation type: {}", simType, e);
      throw new SimulationException("SimulationSetupFailed", e);
    }

    return rule;
  }


  private List<T> createCells(SimType simType, Rule<T> rule) {
    List<T> cellList = new ArrayList<>();

    try {
      // use reflections to find the type of cell to create
      Class<?> cellClass = Class.forName(CELL_PACKAGE + simType.name() + "Cell");
      Constructor<?> cellConstructor = cellClass.getConstructor(int.class, rule.getClass());

      // for all the states, create cell with the correct state and rule of that specific typ
      for (Integer state : myXmlData.getCellStateList()) {
        cellList.add((T) cellConstructor.newInstance(state, rule));
      }
    } catch (Exception e) {
      logger.error("Failed to create cells for simulation type: {} with states: {}", simType,
          myXmlData.getCellStateList().stream().map(Object::toString)
              .collect(Collectors.joining(", ")), e);
      throw new SimulationException("CellCreationFailed", List.of(simType.name()), e);
    }

    return cellList;
  }

  private void setUpGridStructure(List<T> cellList) {
    try {
      myGrid.constructGrid(cellList, myXmlData.getGridRowNum(), myXmlData.getGridColNum(),
          myXmlData.getShape(), myXmlData.getNeighborhood(), myXmlData.getEdge());
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  // Start of Public API calls for Simulation ------

  // API Calls for use in cell view ---

  // Simulation Related

  /**
   * Moves the simulation backward by one step if all cells can revert to a previous state. If not,
   * the simulation remains unchanged and a warning is logged. If successful, it also decrements the
   * cell state length metadata and the total iteration count.
   *
   * @throws SimulationException If an error occurs during step back. This should never be thrown
   *                             due to precautions in Cell (should not be possible to have no
   *                             history).
   */
  public void stepBack() {
    if (myGrid.getCells().stream().allMatch(Cell::stepBack)) {
      totalIterations--;
    } else {
      logger.warn("Step back not possible, simulation remains at the current state");
    }
  }

  /**
   * Advances the simulation forward by one step. This method calculates the next state for all
   * cells, applies the state changes, resets parameters, and saves the current state for potential
   * rollback.
   *
   * @throws SimulationException If an error occurs in stepping forward. This should never be thrown
   *                             due to precautions in Cell and Rule.
   */
  public void step() {
    try {
      totalIterations++;
      myGrid.getCells().forEach(Cell::calcNextState);
      myGrid.getCells().forEach(Cell::step);
      myGrid.getCells().forEach(Cell::resetParameters);
      myGrid.getCells().forEach(Cell::saveCurrentState);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  // Metadata Related

  /**
   * Retrieves the current state of the cell at the specified row and column within the grid.
   *
   * @param row The row index of the target cell.
   * @param col The column index of the target cell.
   * @return The current state of the specified cell.
   * @throws SimulationException If the specified row and column are out of bounds.
   */
  public int getCurrentState(int row, int col) {
    try {
      return myGrid.getCell(row, col).getCurrentState();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Retrieves the duration (number of steps) that the cell at the given position has remained in
   * its current state.
   *
   * @param row The row index of the cell.
   * @param col The column index of the cell.
   * @return The number of consecutive steps the cell has maintained its current state.
   * @throws SimulationException If the cell position is invalid.
   */
  public int getStateLength(int row, int col) {
    try {
      return myGrid.getCell(row, col).getStateLength();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Returns the current iteration number of the simulation.
   *
   * @return The total iteration count.
   */
  public int getTotalIterations() {
    return totalIterations;
  }

  // Parameter Related

  /**
   * Retrieves all double parameter key values for the current simulation.
   *
   * @return A list of all valid keys with double values for the current simulation.
   */
  public List<String> getParameterKeys() {
    return myParameters.getParameterKeys();
  }

  /**
   * Updates the simulation parameter identified by {@code key} with a new double value.
   *
   * @param key   The name of the parameter to update.
   * @param value The new value for the parameter.
   * @throws SimulationException If the parameter key is not found.
   */
  public void updateParameter(String key, double value) {
    try {
      myParameters.setParameter(key, value);
      myXmlData.getParameters().put(key, value);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Retrieves the current value of the simulation parameter associated with the provided key.
   *
   * @param key The name of the parameter to retrieve.
   * @return The value of the parameter.
   * @throws SimulationException If the parameter key is not found.
   */
  public double getParameter(String key) {
    try {
      return myParameters.getParameter(key);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Retrieves additional parameter keys that may be used in the simulation.
   *
   * @return A list of additional parameter keys.
   */
  public List<String> getAdditionalParameterKeys() {
    return myParameters.getAdditionalParameterKeys();
  }

  /**
   * Updates an additional simulation parameter identified by {@code key} with a new value.
   *
   * @param key   The name of the additional parameter.
   * @param value The new value for the parameter.
   * @throws SimulationException If the key is not a current key in additional parameters.
   */
  public void updateAdditionalParameter(String key, Object value) {
    myParameters.setAdditionalParameter(key, value);
    myXmlData.getParameters().put(key, value);
  }

  /**
   * Retrieves an additional parameter by key and casts it to the specified type, returning an
   * Optional<T>.
   *
   * @param key  The name of the parameter.
   * @param type The expected type of the parameter value.
   * @param <T>  The generic type of the parameter.
   * @return An optional containing the parameter value if found and correctly typed, or an empty
   * optional otherwise.
   */
  public <T> Optional<T> getAdditionalParameter(String key, Class<T> type) {
    return myParameters.getAdditionalParameter(key, type);
  }

  /**
   * Returns the number of editable parameters for a simulation
   *
   * @return number of editable parameters for the simulation
   */
  public int getNumEditableParameters() {
    List<String> params = getParameterKeys();
    List<String> additionalParameterKeys = getAdditionalParameterKeys();
    return params.size() + additionalParameterKeys.size() - myParameters.getUnmodifiableParameters()
        .size();
  }

  /**
   * Returns list of all unmodifiable keys
   *
   * @return list of all unmodifiable keys
   */
  public List<String> getUnmodifiableParameterKeys() {
    return myParameters.getUnmodifiableParameters();
  }

  // Grid Topology Related

  /**
   * Updates the grid topology by reconfiguring the neighbor relationships based on the provided
   * shape, neighborhood, and edge type parameters.
   *
   * @param shape        The desired cell shape (e.g., RECTANGLE, HEXAGON).
   * @param neighborhood The desired neighborhood type (e.g., MOORE, VON_NEUMANN).
   * @param edge         The desired edge behavior (e.g., NONE, TOROIDAL).
   * @throws SimulationException If an invalid topology configuration is provided (should not be
   *                             possible due to enum restrictions).
   */
  public void changeTopology(ShapeType shape, NeighborhoodType neighborhood, EdgeType edge) {
    myGrid.setNeighborsAllCells(shape, neighborhood, edge);
  }

  // API Calls for use in saving simulation information ---
  // these are related to saving simulation info, they just return info simulation currently
  // knows

  /**
   * Returns the XML data that created the simulation.
   *
   * @return the XMLData object containing the simulation's initial configuration
   */
  public XmlData getXmlDataObject() {
    return myXmlData;
  }

  /**
   * Retrieves the simulation type.
   *
   * <p>Assumption: Each dynamic type is initialized with a unique XMLData instance.
   *
   * @return the simulation type as a SimType enum
   */
  public SimType getSimulationType() {
    return myXmlData.getType();
  }

  /**
   * Retrieves the simulation ID from the XML data.
   *
   * <p>Assumption: Each dynamic type is initialized with a unique XMLData instance.
   *
   * @return the simulation ID from the XML data
   */
  public int getSimulationId() {
    return myXmlData.getId();
  }

  /**
   * Retrieves the number of states from the XML data.
   *
   * <p>This value is only accurate for simulations with dynamically assigned states.
   *
   * @return the number of states from the XML data parameters
   */
  public int getNumStates() {
    return myXmlData.getNumStates();
  }


  /**
   * Retrieves all the cells contained within the grid.
   *
   * <p>Only used in testing really to get all the cells, if you want all the cells otherwise
   * please use getting each cell with chosen position
   *
   * <p>Maybe could have made an iterator but we live and we learn about better design patterns,
   * and that was a timely one with less positive value
   *
   * @return a list of all cells in the grid
   */
  List<T> getAllCells() {
    return myGrid.getCells();
  }
}
