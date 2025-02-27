package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.ResourcePckg.getErrorSimulationResourceBundle;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Abstract class for representing a simulation rules.
 *
 * <p> Rules take in a map of parameters and apply them when calculating the next step of the
 * simulation
 *
 * @param <C> - the type of cell, must be a subclass of Cell
 * @param <P> - the type of the parameter, must be a subclass of Rules
 * @author Jessica Chen
 */
public abstract class Rule<C extends Cell<C, ?, ?>, P extends Parameters> {

  private P parameters;
  private final ResourceBundle myResources;
  private static final Map<String, int[]> DIRECTION_MAP = Map.of("S", new int[]{0, 1}, "N",
      new int[]{0, -1}, "W", new int[]{-1, 0}, "E", new int[]{1, 0}, "NE", new int[]{1, -1}, "NW",
      new int[]{-1, -1}, "SE", new int[]{1, 1}, "SW", new int[]{-1, 1});

  /**
   * Constructor for the Rule class.
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public Rule(P parameters) {
    myResources = getErrorSimulationResourceBundle("English");

    checkAndSetParams(parameters);
  }

  /**
   * Constructor for the Rule class.
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public Rule(P parameters, String language) {
    myResources = getErrorSimulationResourceBundle(language);

    checkAndSetParams(parameters);
  }

  private void checkAndSetParams(P parameters) {
    if (parameters == null) {
      throw new SimulationException(String.format(myResources.getString("NullRuleParameters")));
    }
    this.parameters = parameters;
  }

  /**
   * Apply the rules to determine the next state of a cell.
   *
   * @param cell - cell to apply the rules to
   * @return next state of the cell
   */
  public abstract int apply(C cell);

  /**
   * Get the parameters for this rule set.
   *
   * @return parameters
   */
  public P getParameters() {
    return parameters;
  }

  /**
   * returns true if the cell matches a direction.
   *
   * @param cell      - the cell
   * @param neighbor  - neighbor of a cell
   * @param direction - direction to test (N, S, E, W, NE, NW, SE, SW)
   * @return true if the neighbor is the direction neighbor
   */
  protected boolean matchesDirection(C cell, C neighbor, String direction) {
    validateMatchDirectionInputs(cell, neighbor, direction);

    int[] pos = neighbor.getPosition();
    int[] posCell = cell.getPosition();

    int dx = pos[0] - posCell[0];
    int dy = pos[1] - posCell[1];

    return DIRECTION_MAP.getOrDefault(direction, new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE})[0]
        == dx && DIRECTION_MAP.get(direction)[1] == dy;
  }

  private void validateMatchDirectionInputs(C cell, C neighbor, String direction) {
    if (cell == null || neighbor == null) {
      throw new SimulationException(String.format(myResources.getString("NullCellOrNeighbor")));
    }
    if (cell.getPosition() == null || neighbor.getPosition() == null) {
      throw new SimulationException(String.format(myResources.getString("NullPosition")));
    }
    if (!isValidDirection(direction)) {
      throw new SimulationException(
          String.format(myResources.getString("InvalidDirection"), direction));
    }
  }

  /**
   * Constructs a state key based on the current state of the cell and the states of its neighbors
   * in the specified directions.
   *
   * @param cell       - the reference cell for which the state key is generated
   * @param directions - an array of direction strings (e.g., "N", "S", "E", "W", "NE", "NW", "SE",
   *                   "SW")
   * @return a string representing the state key, which includes the cell's current state followed
   * by the states of its neighbors in the given directions
   */
  protected String getStateKey(C cell, String[] directions) {
    StringBuilder stateBuilder = new StringBuilder();

    stateBuilder.append(cell.getCurrentState());
    for (String dir : directions) {
      cell.getNeighbors().stream().filter(neighbor -> matchesDirection(cell, neighbor, dir))
          .findFirst().ifPresentOrElse(neighbor -> stateBuilder.append(neighbor.getCurrentState()),
              () -> stateBuilder.append(""));
    }

    return stateBuilder.toString();
  }

  private boolean isValidDirection(String direction) {
    return direction.equals("N") || direction.equals("S") || direction.equals("E")
        || direction.equals("W") || direction.equals("NE") || direction.equals("NW")
        || direction.equals("SE") || direction.equals("SW");
  }

  /**
   * return resource bundle associated for exceptions.
   *
   * @return resource bundle associated for exception
   */
  public ResourceBundle getResources() {
    return myResources;
  }
}
