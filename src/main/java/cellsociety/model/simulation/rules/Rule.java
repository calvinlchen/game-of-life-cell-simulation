package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.cell.ChouReg2Cell;
import cellsociety.model.simulation.parameters.Parameters;
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

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public Rule(P parameters) {
    this.parameters = parameters;
  }

  /**
   * Apply the rules to determine the next state of a cell
   *
   * @param cell - cell to apply the rules to
   * @return next state of the cell
   */
  public abstract int apply(C cell);

  /**
   * Get the parameters for this rule set
   *
   * @return parameters
   */
  public P getParameters() {
    return parameters;
  }

  /**
   * returns true if the cell matches a direction
   *
   * @param cell      - the cell
   * @param neighbor  - neighbor of a cell
   * @param direction - direction to test (N, S, E, W, NE, NW, SE, SW)
   * @return true if the neighbor is the direction neighbor
   */
  public boolean matchesDirection(C cell, C neighbor, String direction) {
    int[] pos = neighbor.getPosition();
    int[] posCell = cell.getPosition();

    int dx = pos[0] - posCell[0];
    int dy = pos[1] - posCell[1];

    return switch (direction) {
      case "S" -> dx == 0 && dy == 1;
      case "N" -> dx == 0 && dy == -1;
      case "W" -> dx == -1 && dy == 0;
      case "E" -> dx == 1 && dy == 0;
      case "NE" -> dx == 1 && dy == -1;
      case "NW" -> dx == -1 && dy == -1;
      case "SE" -> dx == 1 && dy == 1;
      case "SW" -> dx == -1 && dy == 1;
      default -> false;
    };
  }
}
