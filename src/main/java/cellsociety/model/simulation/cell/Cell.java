package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.SimulationConstants.EXPECTED_POSITION_DIMENSION;
import static cellsociety.model.util.constants.SimulationConstants.MIN_STATE_HISTORY;

import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code Cell} class represents an abstract cell in a grid-based simulation.
 *
 * <p>Cells store their state, track their neighbors, and interact with a simulation rule to
 * determine their next state.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Maintains current and next states.</li>
 *   <li>Tracks simulation history for undo operations.</li>
 *   <li>Manages neighbor relationships (both general and directional).</li>
 *   <li>Uses the <b>Template Method Pattern</b> to standardize cell updates while allowing
 *   customization.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * Rule&lt;GameOfLifeCell&gt; rule = new GameOfLifeRule(parameters);
 * GameOfLifeCell cell = new GameOfLifeCell(GAMEOFLIFE_ALIVE, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @param <C> The type of cell, which must extend {@code Cell}.
 * @param <R> The type of rule governing the cell, which must extend {@code Rule}.
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public abstract class Cell<C extends Cell<C, R>, R extends Rule<C>> {

  // to avoid repeated literals
  private static final String NOT_SET = "NotSet";
  private static final String SET_NEIGHBORS = "setNeighbors()";

  private static final Logger logger = LogManager.getLogger(Cell.class);

  private List<C> neighbors;
  private Map<DirectionType, List<C>> directionalNeighbors;
  private int currentState;
  private int nextState;
  private int[] position;
  private final R rule;

  private int stateLength;

  private final List<Integer> stateHistory;

  /**
   * Constructs a cell with the specified initial state and a rule for determining its behavior.
   *
   * @param state - the initial state of the cell
   * @param rule  - the rule used to calculate the next state of the cell
   */
  public Cell(int state, R rule) {
    if (rule == null) {
      logger.error("Cell initialization failed: Rule cannot be null.");
      throw new SimulationException("NullParameter", List.of("rule", "Cell"));
    }

    stateLength = 1;
    this.currentState = state;
    this.nextState = state;
    this.rule = rule;
    neighbors = new ArrayList<>();
    directionalNeighbors = Map.of();
    stateHistory = new LinkedList<>();

    try {
      saveCurrentState();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  // Start of API for Calculating and Stepping Through Simulatiojn ------

  /**
   * Saves the current state of the cell into a history for potential future retrievals such as
   * state rollback or analysis. Ensures that the history maintains a maximum size defined by the
   * rule's configuration.
   *
   * <p>The maximum allowed size for the state history is retrieved from the associated rule's
   * parameters under the key "maxHistorySize". If the retrieved value is less than the predefined
   * minimum size for history, a {@link SimulationException} is thrown indicating an invalid history
   * size.
   *
   * <p>The method appends the current state to the history. If the history exceeds the configured
   * maximum size, the oldest state is removed to preserve memory efficiency.
   *
   * @throws SimulationException if the
   */
  public void saveCurrentState() {
    try {
      int maxHistorySize;
      maxHistorySize = (int) rule.getParameters().getParameter("maxHistorySize");
      if (maxHistorySize < MIN_STATE_HISTORY) {
        logger.error("Invalid maxHistorySize parameter: {}", maxHistorySize);
        throw new SimulationException("InvalidHistorySize",
            List.of(String.valueOf(maxHistorySize), String.valueOf(MIN_STATE_HISTORY)));
      }

      stateHistory.addLast(currentState);
      if (stateHistory.size() > maxHistorySize) {
        stateHistory.removeFirst();
      }

    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

  }

  /**
   * Moves the cell one step backward to its previous state.
   *
   * @return {@code true} if the step back was successful, {@code false} if history is insufficient.
   * @throws SimulationException if no history is available.
   */
  public boolean stepBack() {
    if (stateHistory.isEmpty()) {
      logger.error("Step back failed: No history available.");
      throw new SimulationException("NoHistory");
    }

    try {
      if (stateHistory.size() > MIN_STATE_HISTORY) {
        stateHistory.removeLast();
        setCurrentState(stateHistory.getLast());
        setNextState(stateHistory.getLast());
        return true;
      }
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    return false;
  }

  /**
   * Calculates the next state of the cell based on its rule.
   *
   * <p>Uses the <b>Template Method Pattern</b> to allow subclass modifications.
   * <ul>
   *   <li>shouldSkipCalculations(), return if true</li>
   *   <li>otherwise calc next state with rule</li>
   *   <li>postProcessNextState(newState from step2)</li>
   * </ul>
   */
  public void calcNextState() {
    try {
      if (shouldSkipCalculation()) {
        return;
      }
      int newState = rule.apply(getSelf());
      postProcessNextState(newState);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Determines whether the state calculation should be skipped.
   *
   * <p>Override in subclasses if specific conditions require skipping the update. Default
   * Hook method returns false</p>
   *
   * @return {@code true} if the calculation should be skipped, {@code false} otherwise.
   */
  protected boolean shouldSkipCalculation() {
    return false;
  }

  /**
   * Processes the next computed state before applying it.
   *
   * <p>Override in subclasses if special handling is required. Default Hook method sets the
   * nextState of the cell to the new state</p>
   *
   * @param newState The newly computed state.
   */
  protected void postProcessNextState(int newState) {
    try {
      setNextState(newState);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Advances the cell's state to the next state and updates state length tracking.
   */
  public void step() {
    try {
      updateStateLength();
      setCurrentState(getNextState());
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Updates the duration the cell has remained in its current state.
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
   * Resets parameters after a simulation step.
   *
   * <p>Override if needed, part of template for stepping through a simulation in Simulation class
   */
  public void resetParameters() {
    // Intentionally left blank; override in subclasses if needed.
  }

  // Start of Abstract methods ------

  /**
   * Returns the current instance of the cell.
   *
   * <p>This method is useful in contexts where the specific type of the
   * subclass instance is required, enabling fluent interfaces or dynamic type-specific behaviors.
   *
   * @return the current instance of the cell as the specific subclass type
   */
  protected abstract C getSelf();

  /**
   * Returns the maximum allowable state value for the cell.
   *
   * @return the maximum state value that the cell can have
   */
  protected abstract int getMaxState();


  /**
   * Checks if the given state is valid within the allowed range.
   *
   * @param state    - the state to check
   * @param maxState - the maximum valid state value
   * @throws SimulationException if the state is out of range
   */
  protected void validateState(int state, int maxState) {
    if (state < 0 || state >= maxState) {
      logger.error("Invalid state assigned: {} (Max: {})", state, maxState);
      throw new SimulationException("InvalidState",
          List.of(String.valueOf(state), String.valueOf(maxState)));
    }
  }

  // Start of Setters and Getters ------

  /**
   * Returns the current state of the cell.
   *
   * @return the current state of the cell
   */
  public int getCurrentState() {
    return currentState;
  }

  /**
   * Updates the current state of the cell to the specified value. Ensures that the new state is
   * within the valid range defined by the cell's maximum allowable state.
   *
   * @param state the new state to set for the cell. It must be within the range of 0 (inclusive) to
   *              the maximum state value (exclusive), as determined by the getMaxState() method.
   * @throws SimulationException if the specified state is out of the valid range.
   */
  public void setCurrentState(int state) {
    try {
      validateState(state, getMaxState());
      currentState = state;
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
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
   * Updates the cell's next state to the specified value. Ensures that the provided state is valid
   * within the allowable range as defined by the cell's maximum state value.
   *
   * @param state the new state to set as the next state of the cell
   * @throws SimulationException if the provided state is out of the valid range
   */
  public void setNextState(int state) {
    try {
      validateState(state, getMaxState());
      nextState = state;
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Retrieves a copy of the current position of the cell.
   *
   * @return an array representing the position of the cell. The array is a copy of the cell's
   * internal position to ensure immutability.
   * @throws SimulationException if the position has not been set.
   */
  public int[] getPosition() {
    if (position == null) {
      logger.error("Attempted to retrieve position before setting it.");
      throw new SimulationException(NOT_SET, List.of("position", "setPosition()"));
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
    if (position == null || position.length != EXPECTED_POSITION_DIMENSION) {
      String positionString = Arrays.toString(position); // Convert array to a single string
      logger.error("Invalid position attempted to be set: {}", positionString);
      throw new SimulationException("InvalidPosition",
          List.of(positionString, String.valueOf(EXPECTED_POSITION_DIMENSION)));
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
      logger.error(
          "Neighbors list has not yet been initialized. Call " + SET_NEIGHBORS + " first.");
      throw new SimulationException(NOT_SET, List.of("neighbors", SET_NEIGHBORS));
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
      logger.error("Attempted to set null neighbor list.");
      throw new SimulationException("NullParameters", List.of("neighbors", SET_NEIGHBORS));
    }
    this.neighbors = neighbors;
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
   * Retrieves the list of neighboring cells associated with a specific direction. This method
   * returns the neighbors that are categorized under the provided direction type. If no neighbors
   * exist for the specified direction, an empty list is returned.
   *
   * @param direction the direction for which the neighbors are to be retrieved. This parameter
   *                  should be one of the predefined values in the {@link DirectionType} enum.
   * @return a list of neighboring cells in the specified direction. If no neighbors exist in the
   * direction, an empty list is returned.
   */
  public List<C> getDirectionalNeighbors(DirectionType direction) {
    if (directionalNeighbors == null) {
      logger.error(
          "Directional neighbors have not yet been initialized. Call " + SET_NEIGHBORS + " first.");
      throw new SimulationException(NOT_SET, List.of("directionalNeighbors", SET_NEIGHBORS));
    }
    return directionalNeighbors.getOrDefault(direction, new ArrayList<>());
  }


  /**
   * Sets the directional neighbors of the cell. Directional neighbors are organized in a map where
   * the keys represent specific directions and the values are lists of cells corresponding to those
   * directions.
   *
   * @param directionalNeighbors a map associating {@link DirectionType} with lists of neighboring
   *                             cells. Cannot be null.
   * @throws SimulationException if the directionalNeighbors parameter is null.
   */
  public void setDirectionalNeighbors(Map<DirectionType, List<C>> directionalNeighbors) {
    if (directionalNeighbors == null) {
      logger.error("Attempted to set directional neighbors to null, if want to clear, "
          + "use clearNeighbors() instead.");
      throw new SimulationException("NullParameters",
          List.of("directionalNeighbors", "setDirectionalNeighbors()"));
    }
    this.directionalNeighbors = directionalNeighbors;
  }

  /**
   * Clears the neighboring cells of the current cell.
   *
   * <p>This method removes all entries from the list of neighbors and clears all lists of
   * directional neighbors, resetting the cell's neighborhood relationships to an empty state.
   *
   * <p>It is intended for scenarios where the neighbor configuration needs to be reset, such as
   * reinitializing simulations or modifying the structure of the grid.
   */
  public void clearNeighbors() {
    neighbors.clear();
    directionalNeighbors.values().forEach(List::clear);
  }

  // Start of Helper methods for cell subclasses ------

  /**
   * Get rule for cell.
   *
   * @return the rule for the cell
   */
  protected R getRule() {
    return rule;
  }

}
