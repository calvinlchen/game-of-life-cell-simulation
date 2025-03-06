package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.GridTypes.DirectionType.E;
import static cellsociety.model.util.constants.GridTypes.DirectionType.N;
import static cellsociety.model.util.constants.GridTypes.DirectionType.NE;
import static cellsociety.model.util.constants.GridTypes.DirectionType.NW;
import static cellsociety.model.util.constants.GridTypes.DirectionType.S;
import static cellsociety.model.util.constants.GridTypes.DirectionType.SE;
import static cellsociety.model.util.constants.GridTypes.DirectionType.SW;
import static cellsociety.model.util.constants.GridTypes.DirectionType.W;
import static cellsociety.model.util.constants.SimulationConstants.KEYLENGTH_MOORE_LOOPS;
import static cellsociety.model.util.constants.SimulationConstants.NULL_STATE;
import static cellsociety.model.util.constants.SimulationConstants.NUM_UNIQUE_90_DEG_ROTATIONS;

import cellsociety.model.simulation.cell.PetelkaCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code PetelkaRule} class defines the state transition rules for the Petelka Langton's Loop
 * simulation.
 *
 * <p>This rule determines the next state of a {@link PetelkaCell} based on a predefined lookup
 * table that maps state keys to output states. The Petelka system follows Moore neighborhood-based
 * transitions with full rotational and reflectional symmetry.</p>
 *
 * <p><b>Warning:</b> This rule is only for rectangular grids with Moore neighborhoods.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Uses an 8-direction Moore neighborhood for state lookups.</li>
 *   <li>Applies both rotational and reflectional symmetry for rule matching.</li>
 *   <li>If no valid rule is found, returns 0 (Petelka's default behavior).</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * PetelkaRule rule = new PetelkaRule(parameters);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class PetelkaRule extends Rule<PetelkaCell> {

  private static final Map<String, Integer> RULES_MAP_PETELKA = new HashMap<>();
  private static final Logger logger = LogManager.getLogger(PetelkaRule.class);

  static {
    RULES_MAP_PETELKA.put("014000000", 1);
    RULES_MAP_PETELKA.put("123400000", 2);
    RULES_MAP_PETELKA.put("234100000", 2);
    RULES_MAP_PETELKA.put("041000000", 4);
    RULES_MAP_PETELKA.put("432100000", 2);
    RULES_MAP_PETELKA.put("341200000", 3);
    RULES_MAP_PETELKA.put("122400000", 3);
    RULES_MAP_PETELKA.put("223241000", 3);
    RULES_MAP_PETELKA.put("232200000", 2);
    RULES_MAP_PETELKA.put("422100000", 3);
    RULES_MAP_PETELKA.put("232214000", 3);
    RULES_MAP_PETELKA.put("322200000", 3);
    RULES_MAP_PETELKA.put("133400000", 2);
    RULES_MAP_PETELKA.put("333341000", 0);
    RULES_MAP_PETELKA.put("323333000", 2);
    RULES_MAP_PETELKA.put("233300000", 3);
    RULES_MAP_PETELKA.put("433100000", 3);
    RULES_MAP_PETELKA.put("333314000", 0);
    RULES_MAP_PETELKA.put("332333000", 1);
    RULES_MAP_PETELKA.put("333200000", 4);

  }

  /**
   * Constructs a {@code PetelkaRule} object and initializes it with the provided parameters. This
   * rule is used to define behavior specific to the Petelka simulation.
   *
   * @param parameters the {@code GenericParameters} object containing the configuration and
   *                   settings required for the rule. Must not be {@code null}.
   */
  public PetelkaRule(GenericParameters parameters) {
    super(parameters);
  }

  /**
   * Applies the Petelka Langton’s Loop transition rules to determine the next state of a cell.
   *
   * <p>The method:</p>
   * <ol>
   *   <li>Generates a state key based on the cell’s current state and its neighbors.</li>
   *   <li>Checks all four 90-degree rotational symmetries for a match.</li>
   *   <li>If no rotation matches, it checks all four mirror-reflected configurations.</li>
   *   <li>If no valid rule is found, returns 0 (default Petelka behavior).</li>
   * </ol>
   *
   * @param cell the cell whose state transition is computed.
   * @return the next state of the cell.
   */
  @Override
  public int apply(PetelkaCell cell) {
    try {
      DirectionType[] baseDirections = {N, NE, E, SE, S, SW, W, NW};

      // Generate state key for the original direction order
      String stateKey = getStateKey(cell, baseDirections);
      if (stateKey.length() != KEYLENGTH_MOORE_LOOPS) {
        return cell.getCurrentState();
      }

      // Check all rotations of the directions
      int rotationsState = checkRotations(cell, baseDirections);
      if (rotationsState != NULL_STATE) {
        return rotationsState;
      }

      int reflectionsState = checkReflections(cell);
      if (reflectionsState != NULL_STATE) {
        return reflectionsState;
      }

      // If no match is found, return 0 because that how petelka's work
      return 0;
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  private int checkRotations(PetelkaCell cell, DirectionType[] directions) {
    String stateKey = getStateKey(cell, directions);
    for (int i = 0; i < NUM_UNIQUE_90_DEG_ROTATIONS; i++) {
      if (RULES_MAP_PETELKA.containsKey(stateKey)) {
        return RULES_MAP_PETELKA.get(stateKey);
      }

      directions = rotateArray(directions);
      stateKey = getStateKey(cell, directions);
    }

    return -1;
  }

  private int checkReflections(PetelkaCell cell) {
    DirectionType[][] reflectionSet = {{S, SE, E, NE, N, NW, W, SW}, {N, NW, W, SW, S, SE, E, NE},
        {W, SW, S, SE, E, NE, N, NW}, {E, NE, N, NW, W, SW, S, SE}};
    for (DirectionType[] directions : reflectionSet) {
      String stateKey = getStateKey(cell, directions);
      if (RULES_MAP_PETELKA.containsKey(stateKey)) {
        return RULES_MAP_PETELKA.get(stateKey);
      }
    }

    return -1;
  }

  private DirectionType[] rotateArray(DirectionType[] array) {
    DirectionType[] rotated = new DirectionType[array.length];
    System.arraycopy(array, 2, rotated, 0, array.length - 2);
    rotated[array.length - 1] = array[1];
    rotated[array.length - 2] = array[0];
    return rotated;
  }

}
