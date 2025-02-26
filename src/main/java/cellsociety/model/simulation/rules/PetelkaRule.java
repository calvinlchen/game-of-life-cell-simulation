package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.PetelkaCell;
import cellsociety.model.simulation.parameters.PetelkaParameters;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for representing rules for Petelka Loop simulation.
 *
 * @author Jessica Chen
 */
public class PetelkaRule extends Rule<PetelkaCell, PetelkaParameters> {

  private static final Map<String, Integer> RULES_MAP_PETELKA = new HashMap<>();

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
   * Constructor for the Rule class.
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public PetelkaRule(PetelkaParameters parameters) {
    super(parameters);
  }

  /**
   * Constructor for the Rule class.
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   * @param language   - name of language, for error message display
   */
  public PetelkaRule(PetelkaParameters parameters, String language) {
    super(parameters, language);
  }

  @Override
  public int apply(PetelkaCell cell) {
    String[] baseDirections = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

    // Generate state key for the original direction order
    String stateKey = getStateKey(cell, baseDirections);
    if (stateKey.length() != 9) {
      return cell.getCurrentState();
    }

    // Check all rotations of the directions
    int rotationsState = checkRotations(cell, baseDirections);
    if (rotationsState != -1) {
      return rotationsState;
    }

    int reflectionsState = checkReflections(cell);
    if (reflectionsState != -1) {
      return reflectionsState;
    }

    // If no match is found, return 0
    return 0;
  }

  private int checkRotations(PetelkaCell cell, String[] directions) {
    String stateKey = getStateKey(cell, directions);
    for (int i = 0; i < 4; i++) {
      if (RULES_MAP_PETELKA.containsKey(stateKey)) {
        return RULES_MAP_PETELKA.get(stateKey);
      }

      directions = rotateArray(directions);
      stateKey = getStateKey(cell, directions);
    }

    return -1;
  }

  private int checkReflections(PetelkaCell cell) {
    String[][] reflectionSet = {
        {"S", "SE", "E", "NE", "N", "NW", "W", "SW"},
        {"N", "NW", "W", "SW", "S", "SE", "E", "NE"},
        {"W", "SW", "S", "SE", "E", "NE", "N", "NW"},
        {"E", "NE", "N", "NW", "W", "SW", "S", "SE"}
    };
    for (String[] directions : reflectionSet) {
      String stateKey = getStateKey(cell, directions);
      if (RULES_MAP_PETELKA.containsKey(stateKey)) {
        return RULES_MAP_PETELKA.get(stateKey);
      }
    }

    return -1;
  }

  private String[] rotateArray(String[] array) {
    String[] rotated = new String[array.length];
    System.arraycopy(array, 2, rotated, 0, array.length - 2);
    rotated[array.length - 1] = array[1];
    rotated[array.length - 2] = array[0];
    return rotated;
  }

}
