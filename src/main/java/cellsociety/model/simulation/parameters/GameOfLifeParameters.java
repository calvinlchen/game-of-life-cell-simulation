package cellsociety.model.simulation.parameters;

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

  public GameOfLifeParameters(List<Integer> survive, List<Integer> born) {
    super();

    this.survive = survive;
    this.born = born;
  }

  public List<Integer> getSurviveRules() {
    return new ArrayList<>(survive);
  }

  public void setSurviveRules(List<Integer> survive) {
    this.survive = new ArrayList<>(survive);
  }

  public List<Integer> getBornRules() {
    return new ArrayList<>(born);
  }

  public void setBornRules(List<Integer> born) {
    this.born = new ArrayList<>(born);
  }
}
