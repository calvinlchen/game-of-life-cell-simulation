package cellsociety.model.interfaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for representing a general cell.
 *
 * <p> Cells hold their state and neighbors
 *
 * @param <S> - the type of state this cell holds defined by the enum in the subclass
 * @param <U> - the type of neighbors, must be a subclass of Cell<S>
 * @author Jessica Chen
 */
public abstract class Cell<S extends Enum<S>, U extends Cell<S, U>> {

  private List<U> neighbors;
  private S currentState;
  private S nextState;
  private int[] position;

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   * @throws IllegalArgumentException if state is null
   */
  public Cell(S state) {
    if (state == null) {
      throw new IllegalArgumentException("State cannot be null");
    }
    this.currentState = state;
    this.nextState = state;
    neighbors = new ArrayList<>();
  }

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state    - the initial state of the cell
   * @param position - the position of a the cell
   * @throws IllegalArgumentException if state or position is null
   */
  public Cell(S state, int[] position) {
    this(state);
    setPosition(position);
  }

  /**
   * Based on the rules of the simulation, calculate the next state of the cell and set next state
   * to the calculated value
   */
  public abstract void calcNextState();

  /**
   * Advances current state to next state
   *
   * <p> Assumption: assumes already called calcNextState
   */
  public abstract void step();

  // ==== Setters and Getters ====

  /**
   * Return the current state of the cell
   *
   * @return the current state of the cell
   */
  public S getCurrentState() {
    return currentState;
  }

  /**
   * Sets the value of current state to the passed in state
   *
   * @param state - state to set the current state of the cell
   * @throws IllegalArgumentException if state is null
   */
  public void setCurrentState(S state) {
    if (state == null) {
      throw new IllegalArgumentException("Current state cannot be null");
    }
    currentState = state;
  }

  /**
   * Return the next state of the cell
   *
   * @return the next state of the cell
   */
  public S getNextState() {
    return nextState;
  }

  /**
   * Sets the value of next state (the next state the cell takes on in the simulation) to the passed
   * in state
   *
   * @param state - state the cell will be on the next step of the simulation
   * @throws IllegalArgumentException if state is null
   */
  public void setNextState(S state) {
    if (state == null) {
      throw new IllegalArgumentException("Next state cannot be null");
    }
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
  public List<U> getNeighbors() {
    return neighbors;
  }

  /**
   * Sets the neighbors of the cell
   *
   * @param neighbors - a list of neighboring cells to set
   * @throws IllegalArgumentException if neighbors is null
   */
  public void setNeighbors(List<U> neighbors) {
    if (neighbors == null) {
      throw new IllegalArgumentException("Neighbors list cannot be null");
    }
    this.neighbors = neighbors;
  }

  /**
   * Adds a specific cell to the list of neighbors
   *
   * @param neighbor - the cell to be added as a neighbor
   * @return true if successfully added false otherwise (list adding)
   * @throws IllegalArgumentException if neighbor is null or already contained
   */
  public boolean addNeighbor(U neighbor) {
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
   * @throws IllegalArgumentException if neighbor is null or not in the list
   */
  public boolean removeNeighbor(U neighbor) {
    if (neighbor == null) {
      throw new IllegalArgumentException("Neighbor to remove cannot be null");
    } else if (!neighbors.contains(neighbor)) {
      throw new IllegalArgumentException("Neighbor is not in the list");
    }
    return neighbors.remove(neighbor);
  }
}
