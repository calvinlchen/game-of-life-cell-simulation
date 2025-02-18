package cellsociety.model.simulation.parameters;

import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameOfLifeParameters extends Parameters {
  private List<Integer> survive;
  private List<Integer> born;

  public GameOfLifeParameters() {
    super();
    survive = new ArrayList<>(Arrays.asList(2, 3));
    born = new ArrayList<>(List.of(3));
  }

  public GameOfLifeParameters(String language) {
    super(language);
    survive = new ArrayList<>(Arrays.asList(2, 3));
    born = new ArrayList<>(List.of(3));
  }

  public GameOfLifeParameters(List<Integer> survive, List<Integer> born) {
    super();

    validateRules(survive, "InvalidSurviveRules");
    validateRules(born, "InvalidBornRules");

    this.survive = survive;
    this.born = born;
  }

  public GameOfLifeParameters(List<Integer> survive, List<Integer> born, String language) {
    super(language);

    validateRules(survive, "InvalidSurviveRules");
    validateRules(born, "InvalidBornRules");

    this.survive = survive;
    this.born = born;
  }

  public List<Integer> getSurviveRules() {
    return new ArrayList<>(survive);
  }

  public void setSurviveRules(List<Integer> survive) {
    validateRules(survive, "InvalidSurviveRules");

    this.survive = new ArrayList<>(survive);
  }

  public List<Integer> getBornRules() {
    return new ArrayList<>(born);
  }

  public void setBornRules(List<Integer> born) {
    validateRules(born, "InvalidBornRules");

    this.born = new ArrayList<>(born);
  }

  /**
   * Validates the rules list.
   *
   * @param rules - list to validate
   * @param errorKey - key for the error message in exceptions.properties
   */
  private void validateRules(List<Integer> rules, String errorKey) {
    if (rules == null) {
      throw new SimulationException(String.format(getResources().getString("NullRuleList"), errorKey));
    }
  }
}
