package cellsociety.model.interfaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for representing a general cell.
 *
 * <p> Cells hold their state and neighbors
 *
 * @param <S> - the type of state this cell holds defined by the enum in the subclass
 * @author Jessica Chen
 */
public abstract class Cell<S extends Enum<S>> {

  private List<Cell<S>> neighbors;
  private S currentState;
  private S nextState;
  private int[] position;

  /**
   * Constructs a cell with specified initial state.
   *
   * @param state - the initial state of the cell
   */
  public Cell(S state) {
    this.currentState = state;
    neighbors = new ArrayList<>();
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
   */
  public void setCurrentState(S state) {
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
   */
  public void setNextState(S state) {
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
   */
  public void setPosition(int[] position) {
    this.position = position;
  }

  /**
   * Returns the list of neighbors of the cell
   *
   * @return a list of neighboring cells
   */
  public List<Cell<S>> getNeighbors() {
    return neighbors;
  }

  /**
   * Sets the neighbors of the cell
   *
   * @param neighbors - a list of neighboring cells to set
   */
  public void setNeighbors(List<Cell<S>> neighbors) {
    this.neighbors = neighbors;
  }

  /**
   * Adds a specific cell to the list of neighbors
   *
   * @param neighbor - the cell to be added as a neighbor
   */
  public void addNeighbor(Cell<S> neighbor) {
    if (!neighbors.contains(neighbor)) {
      neighbors.add(neighbor);
    }
  }

  /**
   * Removes a specific cell from the list of neighbors
   *
   * @param neighbor - the cell to be removed from neighbors
   * @return true if the neighbor was successfully removed; false otherwise
   */
  public boolean removeNeighbor(Cell<S> neighbor) {
    return neighbors.remove(neighbor);
  }
}
