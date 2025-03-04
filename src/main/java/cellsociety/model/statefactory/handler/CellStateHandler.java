package cellsociety.model.statefactory.handler;

import java.util.List;

/**
 * Interface defining the behavior for handling retrieving cell states in different simulations.
 *
 * @author Jessica Chen
 * @author javadoc authored with help of chatgpt
 */
public interface CellStateHandler {

  /**
   * Retrieves a list of available state values as integers.
   *
   * @return List of integer values representing possible states.
   */
  List<Integer> getStateInt();

  /**
   * Retrieves a list of available state values as string representations.
   *
   * @return List of string values representing possible states.
   */
  List<String> getStateString();

  /**
   * Converts a string representation of a state into its corresponding integer value.
   *
   * @param state The string representation of the state.
   * @return The integer representation of the state.
   */
  int stateFromString(String state);

  /**
   * Converts an integer representation of a state into its corresponding string value.
   *
   * @param state The integer representation of the state.
   * @return The string representation of the state.
   */
  String statetoString(int state);

  /**
   * Checks if a given integer state is a valid state in the current simulation.
   *
   * @param state The integer representation of the state.
   * @return True if the state is valid, false otherwise.
   */
  boolean isValidState(int state);

  /**
   * Adds a new state to the state handler.
   *
   * @param state The string representation of the new state to be added.
   */
  void addState(String state);

}
