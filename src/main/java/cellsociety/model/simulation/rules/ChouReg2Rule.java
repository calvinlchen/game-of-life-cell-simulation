package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.ChouReg2Cell;
import cellsociety.model.simulation.parameters.ChouReg2Parameters;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for representing rules for Chou Reg 2 Loop simulation
 *
 * @author Jessica Chen
 */
public class ChouReg2Rule extends Rule<ChouReg2Cell, ChouReg2Parameters> {

  private static final Map<String, Integer> RULES_MAP_CHOUREG2 = new HashMap<>();

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
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public ChouReg2Rule(ChouReg2Parameters parameters) {
    super(parameters);
  }

  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   * @param language - name of language, for error message display
   */
  public ChouReg2Rule(ChouReg2Parameters parameters, String language) {
    super(parameters, language);
  }

  @Override
  public int apply(ChouReg2Cell cell) {
    String stateKey = getStateKey(cell, new String[]{"N", "E", "S", "W"});
    return RULES_MAP_CHOUREG2.getOrDefault(stateKey, cell.getCurrentState());

  }

}
