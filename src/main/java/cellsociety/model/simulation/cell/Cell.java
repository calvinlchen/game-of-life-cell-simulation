package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.rules.Rule;
import java.util.ArrayList;
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
 * @author Jessica Chen
 */
public abstract class Cell<C extends Cell<C, R>, R extends Rule<C>> {

  private List<C> neighbors;
  private int currentState;
  private int nextState;
  private int[] position;
  private R rule;

  private LinkedList<Integer> stateHistory;

  private ResourceBundle myResources;
  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.constants.CellStates";

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule     - the rule used to calculate the next state
   */
  public Cell(int state, R rule) {
    this.currentState = state;
    this.nextState = state;
    this.rule = rule;
    neighbors = new ArrayList<>();
    stateHistory = new LinkedList<>();
    saveCurrentState();

    myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE);
  }

  /**
   * Save current state
   */
  public void saveCurrentState() {
    stateHistory.addLast(currentState);
    if (stateHistory.size() > rule.getParameters().getOrDefault("maxHistorySize", 10.0)) {
      stateHistory.removeFirst();
    }
  }

  /**
   * Step one step backbward
   */
  public void stepBack() {

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
    return position;
  }

  /**
   * Sets the position of the cell
   *
   * @param position - the position to set, represented as an array [row, column]
   * @throws IllegalArgumentException if position is null or has invalid length
   */
  public void setPosition(int[] position) {
    if (position == null || position.length != 2) {
      throw new IllegalArgumentException("Position must be a non-null array of length 2");
    }
    this.position = position;
  }

  /**
   * Returns the list of neighbors of the cell
   *
   * @return a list of neighboring cells
   */
  public List<C> getNeighbors() {
    return neighbors;
  }

  /**
   * Sets the neighbors of the cell
   *
   * @param neighbors - a list of neighboring cells to set
   */
  public void setNeighbors(List<C> neighbors) {
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
      throw new IllegalArgumentException("Neighbor cannot be null");
    } else if (neighbors.contains(neighbor)) {
      throw new IllegalArgumentException("Neighbor is already in the list");
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
      throw new IllegalArgumentException("Neighbor to remove cannot be null");
    } else if (!neighbors.contains(neighbor)) {
      throw new IllegalArgumentException("Neighbor is not in the list");
    }
    return neighbors.remove(neighbor);
  }

  /**
   * Returns the int associated with the state from the resource property
   *
   * @param key - the String key associated with the state
   * @return the int associated with the property's key
   */
  public int getStateProperty(String key) {
    return Integer.parseInt(myResources.getString(key));
  }

  R getRule() {
    return rule;
  }
}
