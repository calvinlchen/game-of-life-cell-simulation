package cellsociety.model.simulation.rules;

import static cellsociety.model.util.constants.SimulationConstants.KEYLENGTH_VON_NEUMANN_LOOPS;
import static cellsociety.model.util.constants.SimulationConstants.NUM_UNIQUE_90_DEG_ROTATIONS;

import cellsociety.model.simulation.cell.ChouReg2Cell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code ChouReg2Rule} class defines the state transition rules for the Chou Reg 2 Langton's
 * Loop simulation.
 *
 * <p>This rule determines the next state of a {@link ChouReg2Cell} based on a predefined
 * lookup table that maps state keys to output states. These states also additionally follow 4
 * rotational symmetry.</p>
 *
 * <p>Warning: This will only work properly with rectangular grids with von neumann neighborhoods.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Uses a predefined state transition table {@code RULES_MAP_CHOUREG2}.</li>
 *   <li>Supports Von Neumann neighborhood-based lookups.</li>
 *   <li>Rotates the neighbor configuration up to 4 times for 90-degree symmetry.</li>
 *   <li>Applies the first valid rule it finds; otherwise, keeps the current state.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * ChouReg2Rule rule = new ChouReg2Rule(parameters);
 * int nextState = rule.apply(cell);
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class ChouReg2Rule extends Rule<ChouReg2Cell> {

  private static final Map<String, Integer> RULES_MAP_CHOUREG2 = new HashMap<>();
  private static final Logger logger = LogManager.getLogger(ChouReg2Rule.class);

  static {
    RULES_MAP_CHOUREG2.put("00000", 0);
    RULES_MAP_CHOUREG2.put("00044", 0);
    RULES_MAP_CHOUREG2.put("00054", 7);
    RULES_MAP_CHOUREG2.put("00010", 0);
    RULES_MAP_CHOUREG2.put("00011", 0);
    RULES_MAP_CHOUREG2.put("00033", 0);
    RULES_MAP_CHOUREG2.put("00404", 0);
    RULES_MAP_CHOUREG2.put("00444", 5);
    RULES_MAP_CHOUREG2.put("00410", 0);
    RULES_MAP_CHOUREG2.put("00104", 0);
    RULES_MAP_CHOUREG2.put("00101", 0);
    RULES_MAP_CHOUREG2.put("00174", 0);
    RULES_MAP_CHOUREG2.put("00300", 0);
    RULES_MAP_CHOUREG2.put("00301", 0);
    RULES_MAP_CHOUREG2.put("00303", 0);
    RULES_MAP_CHOUREG2.put("00704", 0);
    RULES_MAP_CHOUREG2.put("00703", 0);
    RULES_MAP_CHOUREG2.put("00710", 4);
    RULES_MAP_CHOUREG2.put("00711", 0);
    RULES_MAP_CHOUREG2.put("04000", 0);
    RULES_MAP_CHOUREG2.put("04007", 0);
    RULES_MAP_CHOUREG2.put("04710", 0);
    RULES_MAP_CHOUREG2.put("05000", 7);
    RULES_MAP_CHOUREG2.put("01700", 0);
    RULES_MAP_CHOUREG2.put("07000", 0);
    RULES_MAP_CHOUREG2.put("07007", 7);
    RULES_MAP_CHOUREG2.put("07101", 0);
    RULES_MAP_CHOUREG2.put("40010", 1);
    RULES_MAP_CHOUREG2.put("40031", 3);
    RULES_MAP_CHOUREG2.put("40103", 3);
    RULES_MAP_CHOUREG2.put("40710", 3);
    RULES_MAP_CHOUREG2.put("41103", 3);
    RULES_MAP_CHOUREG2.put("43103", 3);
    RULES_MAP_CHOUREG2.put("50003", 3);
    RULES_MAP_CHOUREG2.put("50333", 0);
    RULES_MAP_CHOUREG2.put("10004", 5);
    RULES_MAP_CHOUREG2.put("10001", 1);
    RULES_MAP_CHOUREG2.put("10041", 4);
    RULES_MAP_CHOUREG2.put("10104", 4);
    RULES_MAP_CHOUREG2.put("10130", 1);
    RULES_MAP_CHOUREG2.put("10301", 1);
    RULES_MAP_CHOUREG2.put("10713", 1);
    RULES_MAP_CHOUREG2.put("14041", 4);
    RULES_MAP_CHOUREG2.put("14104", 4);
    RULES_MAP_CHOUREG2.put("11104", 4);
    RULES_MAP_CHOUREG2.put("13301", 1);
    RULES_MAP_CHOUREG2.put("17771", 1);
    RULES_MAP_CHOUREG2.put("30401", 1);
    RULES_MAP_CHOUREG2.put("30501", 1);
    RULES_MAP_CHOUREG2.put("30514", 1);
    RULES_MAP_CHOUREG2.put("30714", 1);
    RULES_MAP_CHOUREG2.put("34401", 1);
    RULES_MAP_CHOUREG2.put("34501", 1);
    RULES_MAP_CHOUREG2.put("35401", 0);
    RULES_MAP_CHOUREG2.put("31400", 1);
    RULES_MAP_CHOUREG2.put("37771", 1);
    RULES_MAP_CHOUREG2.put("70000", 0);
    RULES_MAP_CHOUREG2.put("70033", 7);
    RULES_MAP_CHOUREG2.put("70710", 0);
    RULES_MAP_CHOUREG2.put("70714", 1);
    RULES_MAP_CHOUREG2.put("70711", 0);
    RULES_MAP_CHOUREG2.put("71700", 0);
    RULES_MAP_CHOUREG2.put("73000", 7);
    RULES_MAP_CHOUREG2.put("77007", 0);
    RULES_MAP_CHOUREG2.put("77071", 1);
  }

  /**
   * Constructs a {@code ChouReg2Rule} object with the specified parameters. This class extends the
   * {@code Rule} class and inherits its functionality. The provided {@code GenericParameters}
   * object is used to configure the rule's behavior in the simulation.
   *
   * @param parameters the {@code GenericParameters} object containing settings for the rule. Must
   *                   not be {@code null}.
   */
  public ChouReg2Rule(GenericParameters parameters) {
    super(parameters);
  }

  /**
   * Applies the Chou Reg 2 Langton’s Loop transition rules to determine the next state of a cell.
   *
   * <p>The method:
   * <ol>
   *   <li>Generates a state key based on the cell’s current state and its neighbors.</li>
   *   <li>Rotates the neighbor configuration up to four times to check for symmetry.</li>
   *   <li>Returns the new state based on the first matching key in {@code RULES_MAP_CHOUREG2}.</li>
   *   <li>If no match is found, the cell retains its current state.</li>
   * </ol>
   * </p>
   *
   * @param cell the cell whose state transition is computed.
   * @return the next state of the cell.
   */
  @Override
  public int apply(ChouReg2Cell cell) {
    try {
      DirectionType[] directions = {DirectionType.N, DirectionType.E, DirectionType.S,
          DirectionType.W};

      for (int rotations = 0; rotations < NUM_UNIQUE_90_DEG_ROTATIONS; rotations++) {
        String stateKey = getStateKey(cell, directions);

        if (stateKey.length() != KEYLENGTH_VON_NEUMANN_LOOPS) {
          logger.warn("[ChouReg2Rule] Invalid state key length for cell at {}: {}",
              cell.getPosition(), stateKey);
          return cell.getCurrentState();
        }

        if (RULES_MAP_CHOUREG2.containsKey(stateKey)) {
          return RULES_MAP_CHOUREG2.get(stateKey);
        }

        directions = rotateClockwise(directions);
      }

      logger.warn("[ChouReg2Rule] No valid rule found, retaining current state for cell at {}",
          cell.getPosition());
      return cell.getCurrentState();
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

}
