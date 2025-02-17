package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.CHOUREG2_MAXSTATE;
import static cellsociety.model.util.constants.ResourcePckg.ERROR_SIMULATION_RESOURCE_PACKAGE;

import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Abstract class for representing a general cell.
 *
 * <p> Cells hold their state and neighbors
 *
 * @param <C> - the type of cell, must be a subclass of Cell
 * @param <R> - the rule type of the cell, must be a subclass of Rule
 * @param <P> - the parameter type of the cell, must be a subclass of Parameter
 * @author Jessica Chen
 */
public abstract class Cell<C extends Cell<C, R, P>, R extends Rule<C, P>, P extends Parameters> {

  private List<C> neighbors;
  private int currentState;
  private int nextState;
  private int[] position;
  private R rule;

  private LinkedList<Integer> stateHistory;

  private ResourceBundle myResources;

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the rule used to calculate the next state
   */
  public Cell(int state, R rule) {
    this.currentState = state;
    this.nextState = state;
    this.rule = rule;
    neighbors = new ArrayList<>();
    stateHistory = new LinkedList<>();
    saveCurrentState();

    myResources = ResourceBundle.getBundle(ERROR_SIMULATION_RESOURCE_PACKAGE + "English");
  }

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the rule used to calculate the next state
   */
  public Cell(int state, R rule, String language) {
    this.currentState = state;
    this.nextState = state;
    this.rule = rule;
    neighbors = new ArrayList<>();
    stateHistory = new LinkedList<>();
    saveCurrentState();

    myResources = ResourceBundle.getBundle(ERROR_SIMULATION_RESOURCE_PACKAGE + language);
  }

  /**
   * Save current state
   */
  public void saveCurrentState() {
    int maxHistorySize;
    try {
      maxHistorySize = (int) rule.getParameters().getParameter("maxHistorySize");
    } catch (Exception e) {
      throw new SimulationException(String.format(myResources.getString("InvalidHistorySize")));
    }

    stateHistory.addLast(currentState);
    if (stateHistory.size() > maxHistorySize) {
      stateHistory.removeFirst();
    }
  }


  /**
   * Step one step backbward
   */
  public void stepBack() {
    if (stateHistory.isEmpty()) {
      throw new SimulationException(String.format(myResources.getString("NoHistory")));
    }

    int lastState = stateHistory.getLast();
    if (stateHistory.size() > 1) {
      stateHistory.removeLast();
    }

    setCurrentState(lastState);
    setNextState(lastState);
  }

  /**
   * Based on the rules of the simulation, calculate the next state of the cell and set next state
   * to the calculated value
   */
  public void calcNextState() {
    setNextState(rule.apply(getSelf()));
  }

  /**
   * Advances current state to next state
   *
   * <p> Assumption: assumes already called calcNextState
   */
  public void step() {
    setCurrentState(getNextState());
  }

  /**
   * Retrieves the current cell instance.
   */
  protected abstract C getSelf();

  /**
   * Resets parameters per step
   *
   * <p> Assumption: assumes already called calcNextState
   * <p> Override if needed
   */
  public void resetParameters() {
  }

  // ==== Setters and Getters ====

  /**
   * Return the current state of the cell
   *
   * @return the current state of the cell
   */
  public int getCurrentState() {
    return currentState;
  }

  /**
   * Sets the value of current state to the passed in state
   *
   * @param state - state to set the current state of the cell
   */
  public void setCurrentState(int state) {
    currentState = state;
  }

  /**
   * Return the next state of the cell
   *
   * @return the next state of the cell
   */
  public int getNextState() {
    return nextState;
  }

  /**
   * Sets the value of next state (the next state the cell takes on in the simulation) to the passed
   * in state
   *
   * @param state - state the cell will be on the next step of the simulation
   */
  public void setNextState(int state) {
    nextState = state;
  }

  /**
   * Returns the position of the cell
   *
   * @return the position of the cell as an array [row, column]
   */
  public int[] getPosition() {
    if (position == null) {
      throw new SimulationException(String.format(myResources.getString("PositionNotSet")));
    }
    return position;
  }


  /**
   * Sets the position of the cell
   *
   * @param position - the position to set, represented as an array [row, column]
   */
  public void setPosition(int[] position) {
    if (position == null) {
      throw new SimulationException(String.format(myResources.getString("NoPosition")));
    } else if (position.length != 2) {
      throw new SimulationException(String.format(myResources.getString("InvalidPosition"),
          Arrays.toString(position)));
    }
    this.position = position;
  }

  /**
   * Returns the list of neighbors of the cell
   *
   * @return a list of neighboring cells
   */
  public List<C> getNeighbors() {
    if (neighbors == null) {
      throw new SimulationException(
          String.format(myResources.getString("NeighborsNotInitialized")));
    }
    return neighbors;
  }


  /**
   * Sets the neighbors of the cell
   *
   * @param neighbors - a list of neighboring cells to set
   */
  public void setNeighbors(List<C> neighbors) {
    if (neighbors == null) {
      throw new SimulationException(String.format(myResources.getString("NullNeighborList")));
    }
    this.neighbors = neighbors;
  }


  /**
   * Adds a specific cell to the list of neighbors
   *
   * @param neighbor - the cell to be added as a neighbor
   * @return true if successfully added false otherwise (list adding)
   */
  public boolean addNeighbor(C neighbor) {
    if (neighbor == null) {
      throw new SimulationException(String.format(myResources.getString("NullNeighbor")));
    } else if (neighbors.contains(neighbor)) {
      throw new SimulationException(String.format(myResources.getString("DuplicateNeighbor")));
    }
    return neighbors.add(neighbor);
  }

  /**
   * Removes a specific cell from the list of neighbors
   *
   * @param neighbor - the cell to be removed from neighbors
   * @return true if the neighbor was successfully removed; false otherwise
   */
  public boolean removeNeighbor(C neighbor) {
    if (neighbor == null) {
      throw new SimulationException(String.format(myResources.getString("NullNeighbor")));
    } else if (!neighbors.contains(neighbor)) {
      throw new SimulationException(String.format(myResources.getString("NeighborNotFound")));
    }
    return neighbors.remove(neighbor);
  }

  /**
   * Get rule for cell
   *
   * @return the rule for the cell
   */
  R getRule() {
    return rule;
  }

  /**
   * Return resource bundle for error handling
   *
   * @return resource bundle for error handling
   */
  ResourceBundle getMyResources() {
    return myResources;
  }

  /**
   * check if state is a valid state
   *
   * @param state    - state to check
   * @param maxState - max state for a simulation
   */
  public void validateState(int state, int maxState) {
    if (state < 0 || state > maxState) {
      throw new SimulationException(
          String.format(getMyResources().getString("InvalidState"), state, maxState));
    }
  }
}
