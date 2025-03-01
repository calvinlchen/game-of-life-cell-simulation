package cellsociety.model.simulation;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.grid.Grid;
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

/**
 * Represents a simulation where cells interact based on rules, parameters, and configurations
 * provided through XML data.
 *
 * <p>This is the main API for interacting with simulations. The View should communicate only
 * through this class.
 *
 * <p><b>Contracts:</b>
 * <ul>
 *     <li>Encapsulates internal grid, rule, and parameter logic.</li>
 *     <li>Provides methods to step the simulation forward and backward.</li>
 *     <li>Provides methods to update the grids topology.</li>
 *     <li>Offers read-only access to cell states, and metadata.</li>
 *     <li>Offers read-write access to simulations current parameters.</li>
 *     <li>Throws {@link SimulationException} for invalid operations.</li>
 * </ul>
 * </p>
 *
 * @param <T> The type of cell used in the simulation, extending the base Cell class
 * @author Jessica Chen
 * @author ChatGPT, helped with some of the JavaDocs
 */
public class Simulation<T extends Cell<T, ?, ?>> {

  private final XmlData xmlData;
  private Grid<T> myGrid;
  private Parameters parameters;

  private static final String CELL_PACKAGE = "cellsociety.model.simulation.cell.";


  private int totalIterations;

  /**
   * Constructs a Simulation instance using the provided XML data.
   *
   * @param data the XML data containing the simulation configuration
   * @throws SimulationException if the XML data is null, or if simulation fails to setup
   */
  public Simulation(XmlData data) {
    xmlData = getXmlData(data);
    setUpSimulation();
  }

  /**
   * Retrieves and validates the provided XmlData object.
   *
   * @param data - the XmlData object to be validated and returned
   * @return the validated XmlData object
   * @throws SimulationException if the provided XmlData object is null
   */
  private XmlData getXmlData(XmlData data) {
    final XmlData xmlData;
    if (data == null) {
      throw new SimulationException("NullXMLData");
    }

    xmlData = data;
    return xmlData;
  }

  // Start of Simulation Setup ------

  /**
   * Sets up the initial conditions for the simulation. This method initializes the simulation by
   * configuring its type, parameters, and grid structure using the provided XML data.
   *
   * <p>The method performs the following steps:
   * <ul>
   *   <li>Retrieves the simulation type from the XML data.</li>
   *   <li>Initialize rules and parameters based on the simulation type.</li>
   *   <li>Creates a list of cells based on the simulation type with the rule.</li>
   *   <li>Connects the list of cells based on the grid toplogy.</li>
   * </ul>
   *
   * @throws SimulationException if the setup process fails, including issues with rule creation,
   *                             parameter retrieval, or grid initialization.
   */
  private void setUpSimulation() {
    try {
      SimType simType = xmlData.getType();

      Rule<T, ?> rule = setUpRules(simType);
      List<Cell<T, ?, ?>> cellList = createCells(simType, rule);
      // TODO: replace the current one with the commented out line
      setUpGridStructure(cellList, simType);
      // setUpGridStructure(cellList);
    } catch (Exception e) {
      throw new SimulationException("SimulationSetupFailed", e);
    }
  }

  /**
   * Sets up the rules for the simulation based on the provided simulation type. Additionally, it
   * stores the parameters associated with these rules in the simulation class.
   *
   * <p>This method retrieves parameters from the XML data, creates a rule based on the simulation
   * with the help of the rule factory and assigns the parameters to the rule. If any step fails, a
   * SimulationException is thrown.
   *
   * @param simType - the type of simulation for which the rules are being set up
   * @return the configured Rule object for the specified simulation type
   * @throws SimulationException if there is an error during rule creation or parameter retrieval
   */
  private Rule<T, ?> setUpRules(SimType simType) {
    Rule<T, ?> rule;

    try {
      Map<String, Double> params = xmlData.getParameters();

      rule = (Rule<T, ?>) RuleFactory.createRule(simType.name() + "Rule", params);
      parameters = rule.getParameters();

    } catch (Exception e) {
      throw new SimulationException("SimulationSetupFailed", e);
    }

    return rule;
  }

  /**
   * Creates a list of cells for the simulation based on the provided simulation type and rule. The
   * method dynamically determines the appropriate cell class to use, initializes cells with given
   * states, and returns the resulting list of cells.
   *
   * @param simType - the type of simulation for which the cells are being created
   * @param rule    - the rule object associated with the simulation type
   * @return a list of cells created based on the provided simulation type and rule
   * @throws SimulationException if cell creation fails due to reflection issues or other errors
   */
  private List<Cell<T, ?, ?>> createCells(SimType simType, Rule<T, ?> rule) {
    List<Cell<T, ?, ?>> cellList = new ArrayList<>();

    try {
      // use reflections to find the type of cell to create
      Class<?> cellClass = Class.forName(CELL_PACKAGE + simType.name() + "Cell");
      Constructor<?> cellConstructor = cellClass.getConstructor(int.class, rule.getClass(),
          String.class);

      // for all the states, create cell with the correct state and rule of that specific typ
      for (Integer state : xmlData.getCellStateList()) {
        cellList.add((Cell<T, ?, ?>) cellConstructor.newInstance(state, rule));
      }
    } catch (Exception e) {
      throw new SimulationException("CellCreationFailed", simType, e);
    }

    return cellList;
  }

  @Deprecated
  private void setUpGridStructure(List<Cell<T, ?, ?>> cellList, SimType simType) {
    if (simType.isDefaultRectangularGrid()) {
      myGrid = new Grid(cellList, xmlData.getGridRowNum(), xmlData.getGridColNum(),
          ShapeType.RECTANGLE, NeighborhoodType.MOORE, EdgeType.NONE);
    } else {
      myGrid = new Grid(cellList, xmlData.getGridRowNum(), xmlData.getGridColNum(),
          ShapeType.RECTANGLE, NeighborhoodType.VON_NEUMANN, EdgeType.NONE);
    }
  }

  /**
   * Sets up the grid structure for the simulation using the provided list of cells. This method
   * initializes the grid's configuration, including its dimensions, cell shapes, neighborhood
   * relationships, and edge behavior based on the XML data.
   *
   * @param cellList a list of cells that will make up the grid structure
   */
  private void setUpGridStructure(List<Cell<T, ?, ?>> cellList) {
    // TODO: once XML is changed, delete above and uncomment out this line of code
    //    myGrid = new Grid(cellList, xmlData.getGridRowNum(), xmlData.getGridColNum(),
    //        xmlData.getShape(), xmlData.getNeighborhood(), xmlData.getEdge());
  }

  // Start of Public API calls for Simulation ------

  // API Calls for use in cell view ---

  // Simulation Related

  /**
   * Moves the simulation backward by one step, if possible.
   *
   * <p>If moving backward is not possible, the simulation remains in its current state,
   * and the total iterations count is not updated.
   *
   * <p><b>Intended Use:</b> This method is used when a user wants to revert the simulation state
   * to a previous step, as long as that previous step is still stored.
   *
   * <p><b>Example Usage:</b>
   * <pre>
   * Simulation sim = new Simulation(xmlData);
   * sim.stepBackward();
   * </pre>
   */
  public void stepBack() {
    if (myGrid.getCells().stream().allMatch(Cell::stepBack)) {
      totalIterations--;
    }
  }

  /**
   * Advances the simulation forward by one step.
   *
   * <p><b>Intended Use:</b> This method progresses the simulation to its next state based on
   * defined rules. It is the primary function for executing a simulation over time.
   *
   * <p><b>Example Usage:</b>
   * <pre>
   * Simulation sim = new Simulation(xmlData);
   * sim.stepForward();
   * </pre>
   */
  public void step() {
    totalIterations++;
    myGrid.getCells().forEach(Cell::saveCurrentState);
    myGrid.getCells().forEach(Cell::calcNextState);
    myGrid.getCells().forEach(Cell::step);
    myGrid.getCells().forEach(Cell::resetParameters);
  }

  // Metadata Related

  /**
   * Returns the state of the cell at a specified location in the grid.
   *
   * <p><b>Intended Use:</b> This method is used to retrieve the current state of a specific cell
   * for visualization.
   *
   * <p><b>Example Usage:</b>
   * <pre>
   * int state = sim.getCurrentState(2, 3);
   * System.out.println("Cell state: " + state);
   * </pre>
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
      throw new SimulationException("InvalidGridPosition", row, col, e);
    }
  }

  /**
   * Retrieves the duration for which the cell at the specified position has remained in the same
   * state.
   *
   * <p><b>Intended Use:</b> This method helps track how long a cell has maintained a specific
   * state.
   *
   * <p><b>Example Usage:</b>
   * <pre>
   * int duration = sim.getStateLength(2, 3);
   * System.out.println("State duration: " + duration);
   * </pre>
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
      throw new SimulationException("InvalidGridPosition", row, col, e);
    }
  }

  /**
   * Retrieves the total number of iterations completed in the simulation.
   *
   * <p><b>Intended Use:</b> This method is useful for tracking how many steps the simulation has
   * progressed and the current step of the simulation you are on.
   *
   * <p><b>Example Usage:</b>
   * <pre>
   * int iterations = sim.getTotalIterations();
   * System.out.println("Total iterations: " + iterations);
   * </pre>
   *
   * @return the total number of iterations completed
   */
  public int getTotalIterations() {
    return totalIterations;
  }

  // Parameter Related

  /**
   * Retrieves all parameter keys in the simulation.
   *
   * <p><b>Intended Use:</b> This method provides access to all configurable parameters of the
   * simulation.
   *
   * <p><b>Example Usage:</b>
   * <pre>
   * List<String> keys = sim.getParameterKeys();
   * System.out.println("Available parameters: " + keys);
   * </pre>
   *
   * @return a list of parameter keys
   */
  public List<String> getParameterKeys() {
    return parameters.getParameterKeys();
  }

  /**
   * Updates a single simulation parameter dynamically.
   *
   * <p><b>Intended Use:</b> This method allows users to modify simulation behavior dynamically by
   * updating parameters.
   *
   * <p><b>Example Usage:</b>
   * <pre>
   * sim.updateParameter("growthRate", 1.5);
   * </pre>
   *
   * @param key   - the parameter name
   * @param value - the new value for the parameter
   * @throws SimulationException if the parameter key is not found
   */
  public void updateParameter(String key, double value) {
    try {
      parameters.setParameter(key, value);
    } catch (Exception e) {
      throw new SimulationException("ParameterNotFound", key, e);
    }
  }

  /**
   * Retrieves the current value of a parameter.
   *
   * <p><b>Intended Use:</b> This method is used to access the current value of a given parameter
   * for display or saving finally parameter value purposes.
   *
   * <p><b>Example Usage:</b>
   * <pre>
   * double value = sim.getParameter("growthRate");
   * System.out.println("Growth Rate: " + value);
   * </pre>
   *
   * @param key - the parameter name
   * @return the parameter's current value
   * @throws SimulationException if the parameter key is not found
   */
  public double getParameter(String key) {
    try {
      return parameters.getParameter(key);
    } catch (Exception e) {
      throw new SimulationException("ParameterNotFound", key, e);
    }
  }

  // Grid Topology Related

  /**
   * Sets the neighbors for the grid cells based on the specified shape, neighborhood type, and edge
   * type. This operation clears all previous neighbors for all cells before setting the new
   * configuration.
   *
   * <p><b>Intended Use:</b> This method is used when the user wants to change grid topology for an
   * already running simulation
   *
   * <p><b>Example Usage:</b>
   * <pre>
   * sim.configureNeighbors(ShapeType.RECTANGLE, NeighborhoodType.MOORE, EdgeType.TOROIDAL);
   * </pre>
   *
   * @param shape        The shape type of the cells (e.g., RECTANGLE, HEXAGON, TRIANGLE).
   * @param neighborhood The neighborhood type defining neighbor relationships (e.g., MOORE,
   *                     VON_NEUMANN, EXTENDED_MOORE).
   * @param edge         The edge type specifying boundary behavior (e.g., NONE, MIRROR, TOROIDAL).
   */
  public void changeTopology(ShapeType shape, NeighborhoodType neighborhood, EdgeType edge) {
    myGrid.setNeighbors(shape, neighborhood, edge);
  }

  // API Calls for use in saving simulation information ---

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
}
