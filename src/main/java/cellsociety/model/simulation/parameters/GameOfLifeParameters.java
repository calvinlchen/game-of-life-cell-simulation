package cellsociety.model.simulation.parameters;

import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parameters for the Game of Life simulation.
 *
 * <p>This class defines and manages the survival and birth rules for the Game of Life simulation.
 * These rules determine under what conditions a cell survives or is born in the next generation.
 *
 * @author Jessica Chen
 * @author ChatGPT, helped with some of the JavaDocs
 */
public class GameOfLifeParameters extends Parameters {

  private List<Integer> survive;
  private List<Integer> born;

  /**
   * Initializes the GameOfLifeParameters with default survival and birth rules.
   *
   * <p>Sets {@code survive} to [2, 3] and {@code born} to [3].
   */
  public GameOfLifeParameters() {
    super();
    initializeDefaultParams();
  }

  /**
   * Initializes the GameOfLifeParameters with default survival and birth rules and a specified
   * language for error messages.
   *
   * <p>Sets {@code survive} to [2, 3] and {@code born} to [3].
   *
   * @param language - name of the language for error message display
   */
  public GameOfLifeParameters(String language) {
    super(language);
    initializeDefaultParams();
  }

  /**
   * Initializes the GameOfLifeParameters with custom survival and birth rules.
   *
   * @param survive - list of neighbor counts for a cell to survive
   * @param born    - list of neighbor counts for a new cell to be born
   */
  public GameOfLifeParameters(List<Integer> survive, List<Integer> born) {
    super();
    initializeParams(survive, born);
  }

  /**
   * Initializes the GameOfLifeParameters with custom survival and birth rules and a specified
   * language for error messages.
   *
   * @param survive  - list of neighbor counts for a cell to survive
   * @param born     - list of neighbor counts for a new cell to be born
   * @param language - name of the language for error message display
   */
  public GameOfLifeParameters(List<Integer> survive, List<Integer> born, String language) {
    super(language);
    initializeParams(survive, born);
  }

  private void initializeDefaultParams() {
    survive = new ArrayList<>(Arrays.asList(2, 3));
    born = new ArrayList<>(List.of(3));
  }

  private void initializeParams(List<Integer> survive, List<Integer> born) {
    validateRules(survive, "InvalidSurviveRules");
    validateRules(born, "InvalidBornRules");

    this.survive = survive;
    this.born = born;
  }

  /**
   * Retrieves the survival rules.
   *
   * @return a list of integers representing the neighbor counts required for a cell to survive
   */
  public List<Integer> getSurviveRules() {
    return new ArrayList<>(survive);
  }

  /**
   * Sets the survival rules.
   *
   * @param survive - a list of neighbor counts required for a cell to survive
   */
  public void setSurviveRules(List<Integer> survive) {
    validateRules(survive, "InvalidSurviveRules");

    this.survive = new ArrayList<>(survive);
  }

  /**
   * Retrieves the birth rules.
   *
   * @return a list of integers representing the neighbor counts required for a new cell to be born
   */
  public List<Integer> getBornRules() {
    return new ArrayList<>(born);
  }

  /**
   * Sets the birth rules.
   *
   * @param born - a list of neighbor counts required for a new cell to be born
   */
  public void setBornRules(List<Integer> born) {
    validateRules(born, "InvalidBornRules");

    this.born = new ArrayList<>(born);
  }

  /**
   * Validates the rules list.
   *
   * @param rules    - list to validate
   * @param errorKey - key for the error message in exceptions.properties
   */
  private void validateRules(List<Integer> rules, String errorKey) {
    if (rules == null) {
      throw new SimulationException(
          String.format(getResources().getString("NullRuleList"), errorKey));
    }
  }
}
