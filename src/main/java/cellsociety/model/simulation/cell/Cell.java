package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.SimulationConstants.EXPECTED_POSITION_DIMENSION;
import static cellsociety.model.util.constants.SimulationConstants.MIN_STATE_HISTORY;

import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for representing a general cell.
 *
 * <p> Cells hold their state and neighbors.
 *
 * @param <C> - the type of cell, must be a subclass of Cell
 * @param <R> - the rule type of the cell, must be a subclass of Rule
 * @param <P> - the parameter type of the cell, must be a subclass of Parameter
 * @author Jessica Chen
 */
public abstract class Cell<C extends Cell<C, R, P>, R extends Rule<C, P>, P extends Parameters> {

  private List<C> neighbors;
  private Map<DirectionType, List<C>> directionalNeighbors;
  private int currentState;
  private int nextState;
  private int[] position;
  private R rule;

  private int stateLength;

  private List<Integer> stateHistory;


  /**
   * Constructs a cell with a specified initial state.
   *
   * @param state - the initial state of the cell
   * @param rule  - the rule used to calculate the next state
   */
  public Cell(int state, R rule) {
    initializeCell(state, rule);
  }

  private void initializeCell(int state, R rule) {
    stateLength = 1;

    this.currentState = state;
    this.nextState = state;
    this.rule = rule;
    neighbors = new ArrayList<>();
    directionalNeighbors = Map.of();
    stateHistory = new LinkedList<>();
    saveCurrentState();
  }

  /**
   * Saves the current state of the cell to the state history.
   */
  public void saveCurrentState() {
    int maxHistorySize;

    maxHistorySize = (int) rule.getParameters().getParameter("maxHistorySize");
    if (maxHistorySize < MIN_STATE_HISTORY) {
      throw new SimulationException("InvalidHistorySize");
    }

    stateHistory.addLast(currentState);
    if (stateHistory.size() > maxHistorySize) {
      stateHistory.removeFirst();
    }
  }

  /**
   * Moves the cell one step backward to the previous state.
   *
   * @throws SimulationException if no previous state is available
   * @returns true if succesfully stepBack, false if not enough history to rewind
   */
  public boolean stepBack() {
    boolean success = false;

    // this error should never reach with how stateHistory is set up in initialization
    if (stateHistory.isEmpty()) {
      throw new SimulationException("NoHistory");
    }

    int lastState = stateHistory.getLast();
    if (stateHistory.size() > MIN_STATE_HISTORY) {
      stateHistory.removeLast();
      success = true;
    }

    setCurrentState(lastState);
    setNextState(lastState);

    return success;
  }

  /**
   * Calculates the next state of the cell based on its rule.
   */
  public void calcNextState() {
    setNextState(rule.apply(getSelf()));
  }

  /**
   * Advances the cell's current state to the next state. Also updates the state length.
   *
   * <p>Assumption: calcNextState() has already been called.
   */
  public void step() {
    updateStateLength();
    setCurrentState(getNextState());
  }

  /**
   * Retrieves the current cell instance.
   */
  abstract C getSelf();

  /**
   * Resets parameters per step.
   *
   * <p>Assumption: calcNextState() has already been called.
   * <p>Override if needed.
   */
  public void resetParameters() {
    // Intentionally left blank; override in subclasses if needed.
  }

  // ==== Setters and Getters ====

  /**
   * Returns the current state of the cell.
   *
   * @return the current state of the cell
   */
  public int getCurrentState() {
    return currentState;
  }

  /**
   * Sets the current state of the cell.
   *
   * @param state - the state to set as the current state
   */
  public void setCurrentState(int state) {
    currentState = state;
  }

  /**
   * Returns the next state of the cell.
   *
   * @return the next state of the cell
   */
  public int getNextState() {
    return nextState;
  }

  /**
   * Sets the next state of the cell.
   *
   * @param state - the state to set as the next state
   */
  public void setNextState(int state) {
    nextState = state;
  }

  /**
   * Returns the position of the cell.
   *
   * @return an array [row, column] representing the cell's position
   * @throws SimulationException if the position has not been set
   */
  public int[] getPosition() {
    if (position == null) {
      throw new SimulationException("PositionNotSet");
    }
    return Arrays.copyOf(position, position.length);
  }


  /**
   * Sets the position of the cell.
   *
   * @param position - an array [row, column] representing the new position
   * @throws SimulationException if the position is null or invalid
   */
  public void setPosition(int[] position) {
    if (position == null) {
      throw new SimulationException("NoPosition");
    } else if (position.length != EXPECTED_POSITION_DIMENSION) {
      throw new SimulationException("InvalidPosition", Arrays.toString(position));
    }
    this.position = Arrays.copyOf(position, position.length);
  }

  /**
   * Returns the list of neighboring cells.
   *
   * @return a list of neighboring cells
   * @throws SimulationException if neighbors have not been initialized
   */
  public List<C> getNeighbors() {
    // should never be null because can't set to null
    if (neighbors == null) {
      throw new SimulationException("NeighborsNotInitialized");
    }
    return neighbors;
  }

  /**
   * Sets the neighbors of the cell.
   *
   * @param neighbors - a list of neighboring cells
   * @throws SimulationException if the list of neighbors is null
   */
  public void setNeighbors(List<C> neighbors) {
    if (neighbors == null) {
      throw new SimulationException("NullNeighborList");
    }
    this.neighbors = neighbors;
  }

  /**
   * Get rule for cell.
   *
   * @return the rule for the cell
   */
  R getRule() {
    return rule;
  }

  /**
   * Checks if the given state is valid within the allowed range.
   *
   * @param state    - the state to check
   * @param maxState - the maximum valid state value
   * @throws SimulationException if the state is out of range
   */
  void validateState(int state, int maxState) {
    if (state < 0 || state >= maxState) {
      throw new SimulationException("InvalidState", state, maxState);
    }
  }

  /**
   * Updates the state length of the cell.
   *
   * <p>If the current state is the same as the next state, the state length is incremented by one.
   * Otherwise, the state length is reset to zero.
   */
  void updateStateLength() {
    if (nextState == currentState) {
      stateLength++;
    } else {
      // 1 because first time in state
      stateLength = 1;
    }
  }

  /**
   * Returns the duration for which the cell has remained in the same state.
   *
   * @return the state length of the cell
   */
  public int getStateLength() {
    return stateLength;
  }

  /**
   * Sets the directional neighbors of the cell categorized by direction type.
   *
   * @param directionalNeighbors - a map where the key represents the direction type (e.g., N, NE,
   *                             E, etc.), and the value is a list of neighboring cells in that
   *                             direction
   */
  public void setDirectionalNeighbors(Map<DirectionType, List<C>> directionalNeighbors) {
    this.directionalNeighbors = directionalNeighbors;
  }

  /**
   * Retrieves the list of neighboring cells in a specified directional category.
   *
   * @param direction - the directional category from which to retrieve the neighbors
   * @return a list of neighboring cells in the specified direction; returns an empty list if no
   * neighbors are found for the given direction
   */
  public List<C> getDirectionalNeighbors(DirectionType direction) {
    return directionalNeighbors.getOrDefault(direction, new ArrayList<>());
  }

  /**
   * Clears all neighbors of the cell. This method removes both the primary neighbors and the
   * directional neighbors associated with the cell.
   * <p>
   * Primary neighbors are stored in the `neighbors` list, and directional neighbors are stored in
   * the `directionalNeighbors` map categorized by specific directions. After this method is called,
   * both collections will be empty.
   */
  public void clearNeighbors() {
    neighbors.clear();
    directionalNeighbors.values().forEach(List::clear);
  }
}
