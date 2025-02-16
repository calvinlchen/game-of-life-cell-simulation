package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.LangtonCell;
import cellsociety.model.simulation.parameters.LangtonParameters;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for representing rules for Percolation simulation
 *
 * @author Jessica Chen
 */
public class LangtonRule extends Rule<LangtonCell, LangtonParameters> {

  private static final Map<String, Integer> RULES_MAP_LANGTON = new HashMap<>();

  static {
    RULES_MAP_LANGTON.put("00000", 0);
    RULES_MAP_LANGTON.put("00001", 2);
    RULES_MAP_LANGTON.put("00002", 0);
    RULES_MAP_LANGTON.put("00003", 0);
    RULES_MAP_LANGTON.put("00005", 0);
    RULES_MAP_LANGTON.put("00006", 3);
    RULES_MAP_LANGTON.put("00007", 1);
    RULES_MAP_LANGTON.put("00011", 2);
    RULES_MAP_LANGTON.put("00012", 2);
    RULES_MAP_LANGTON.put("00013", 2);
    RULES_MAP_LANGTON.put("00021", 2);
    RULES_MAP_LANGTON.put("00022", 0);
    RULES_MAP_LANGTON.put("00023", 0);
    RULES_MAP_LANGTON.put("00026", 2);
    RULES_MAP_LANGTON.put("00027", 2);
    RULES_MAP_LANGTON.put("00032", 0);
    RULES_MAP_LANGTON.put("00052", 5);
    RULES_MAP_LANGTON.put("00062", 2);
    RULES_MAP_LANGTON.put("00072", 2);
    RULES_MAP_LANGTON.put("00102", 2);
    RULES_MAP_LANGTON.put("00112", 0);
    RULES_MAP_LANGTON.put("00202", 0);
    RULES_MAP_LANGTON.put("00203", 0);
    RULES_MAP_LANGTON.put("00205", 0);
    RULES_MAP_LANGTON.put("00212", 5);
    RULES_MAP_LANGTON.put("00222", 0);
    RULES_MAP_LANGTON.put("00232", 2);
    RULES_MAP_LANGTON.put("00522", 2);
    RULES_MAP_LANGTON.put("01232", 1);
    RULES_MAP_LANGTON.put("01242", 1);
    RULES_MAP_LANGTON.put("01252", 5);
    RULES_MAP_LANGTON.put("01262", 1);
    RULES_MAP_LANGTON.put("01272", 1);
    RULES_MAP_LANGTON.put("01275", 1);
    RULES_MAP_LANGTON.put("01422", 1);
    RULES_MAP_LANGTON.put("01432", 1);
    RULES_MAP_LANGTON.put("01442", 1);
    RULES_MAP_LANGTON.put("01472", 1);
    RULES_MAP_LANGTON.put("01625", 1);
    RULES_MAP_LANGTON.put("01722", 1);
    RULES_MAP_LANGTON.put("01725", 5);
    RULES_MAP_LANGTON.put("01752", 1);
    RULES_MAP_LANGTON.put("01762", 1);
    RULES_MAP_LANGTON.put("01772", 1);
    RULES_MAP_LANGTON.put("02527", 1);
    RULES_MAP_LANGTON.put("10001", 1);
    RULES_MAP_LANGTON.put("10006", 1);
    RULES_MAP_LANGTON.put("10007", 7);
    RULES_MAP_LANGTON.put("10011", 1);
    RULES_MAP_LANGTON.put("10012", 1);
    RULES_MAP_LANGTON.put("10021", 1);
    RULES_MAP_LANGTON.put("10024", 4);
    RULES_MAP_LANGTON.put("10027", 7);
    RULES_MAP_LANGTON.put("10051", 1);
    RULES_MAP_LANGTON.put("10101", 1);
    RULES_MAP_LANGTON.put("10111", 1);
    RULES_MAP_LANGTON.put("10124", 4);
    RULES_MAP_LANGTON.put("10127", 7);
    RULES_MAP_LANGTON.put("10202", 6);
    RULES_MAP_LANGTON.put("10212", 1);
    RULES_MAP_LANGTON.put("10221", 1);
    RULES_MAP_LANGTON.put("10224", 4);
    RULES_MAP_LANGTON.put("10226", 3);
    RULES_MAP_LANGTON.put("10227", 7);
    RULES_MAP_LANGTON.put("10232", 7);
    RULES_MAP_LANGTON.put("10242", 4);
    RULES_MAP_LANGTON.put("10262", 6);
    RULES_MAP_LANGTON.put("10264", 4);
    RULES_MAP_LANGTON.put("10267", 7);
    RULES_MAP_LANGTON.put("10271", 0);
    RULES_MAP_LANGTON.put("10272", 7);
    RULES_MAP_LANGTON.put("10542", 7);
    RULES_MAP_LANGTON.put("11112", 1);
    RULES_MAP_LANGTON.put("11122", 1);
    RULES_MAP_LANGTON.put("11124", 4);
    RULES_MAP_LANGTON.put("11125", 1);
    RULES_MAP_LANGTON.put("11126", 1);
    RULES_MAP_LANGTON.put("11127", 7);
    RULES_MAP_LANGTON.put("11152", 2);
    RULES_MAP_LANGTON.put("11212", 1);
    RULES_MAP_LANGTON.put("11222", 1);
    RULES_MAP_LANGTON.put("11224", 4);
    RULES_MAP_LANGTON.put("11225", 1);
    RULES_MAP_LANGTON.put("11227", 7);
    RULES_MAP_LANGTON.put("11232", 1);
    RULES_MAP_LANGTON.put("11242", 4);
    RULES_MAP_LANGTON.put("11262", 1);
    RULES_MAP_LANGTON.put("11272", 7);
    RULES_MAP_LANGTON.put("11322", 1);
    RULES_MAP_LANGTON.put("12224", 4);
    RULES_MAP_LANGTON.put("12227", 7);
    RULES_MAP_LANGTON.put("12243", 4);
    RULES_MAP_LANGTON.put("12254", 7);
    RULES_MAP_LANGTON.put("12324", 4);
    RULES_MAP_LANGTON.put("12327", 7);
    RULES_MAP_LANGTON.put("12425", 5);
    RULES_MAP_LANGTON.put("12426", 7);
    RULES_MAP_LANGTON.put("12527", 5);
    RULES_MAP_LANGTON.put("20001", 2);
    RULES_MAP_LANGTON.put("20002", 2);
    RULES_MAP_LANGTON.put("20004", 2);
    RULES_MAP_LANGTON.put("20007", 1);
    RULES_MAP_LANGTON.put("20012", 2);
    RULES_MAP_LANGTON.put("20015", 2);
    RULES_MAP_LANGTON.put("20021", 2);
    RULES_MAP_LANGTON.put("20022", 2);
    RULES_MAP_LANGTON.put("20023", 2);
    RULES_MAP_LANGTON.put("20024", 2);
    RULES_MAP_LANGTON.put("20025", 0);
    RULES_MAP_LANGTON.put("20026", 2);
    RULES_MAP_LANGTON.put("20027", 2);
    RULES_MAP_LANGTON.put("20032", 6);
    RULES_MAP_LANGTON.put("20042", 3);
    RULES_MAP_LANGTON.put("20051", 7);
    RULES_MAP_LANGTON.put("20052", 2);
    RULES_MAP_LANGTON.put("20057", 5);
    RULES_MAP_LANGTON.put("20072", 2);
    RULES_MAP_LANGTON.put("20102", 2);
    RULES_MAP_LANGTON.put("20112", 2);
    RULES_MAP_LANGTON.put("20122", 2);
    RULES_MAP_LANGTON.put("20142", 2);
    RULES_MAP_LANGTON.put("20172", 2);
    RULES_MAP_LANGTON.put("20202", 2);
    RULES_MAP_LANGTON.put("20203", 2);
    RULES_MAP_LANGTON.put("20205", 2);
    RULES_MAP_LANGTON.put("20207", 3);
    RULES_MAP_LANGTON.put("20212", 2);
    RULES_MAP_LANGTON.put("20215", 2);
    RULES_MAP_LANGTON.put("20221", 2);
    RULES_MAP_LANGTON.put("20222", 2);
    RULES_MAP_LANGTON.put("20227", 2);
    RULES_MAP_LANGTON.put("20232", 1);
    RULES_MAP_LANGTON.put("20242", 2);
    RULES_MAP_LANGTON.put("20245", 2);
    RULES_MAP_LANGTON.put("20252", 0);
    RULES_MAP_LANGTON.put("20255", 2);
    RULES_MAP_LANGTON.put("20262", 2);
    RULES_MAP_LANGTON.put("20272", 2);
    RULES_MAP_LANGTON.put("20312", 2);
    RULES_MAP_LANGTON.put("20321", 6);
    RULES_MAP_LANGTON.put("20322", 6);
    RULES_MAP_LANGTON.put("20342", 2);
    RULES_MAP_LANGTON.put("20422", 2);
    RULES_MAP_LANGTON.put("20512", 2);
    RULES_MAP_LANGTON.put("20521", 2);
    RULES_MAP_LANGTON.put("20522", 2);
    RULES_MAP_LANGTON.put("20552", 1);
    RULES_MAP_LANGTON.put("20572", 5);
    RULES_MAP_LANGTON.put("20622", 2);
    RULES_MAP_LANGTON.put("20672", 2);
    RULES_MAP_LANGTON.put("20712", 2);
    RULES_MAP_LANGTON.put("20722", 2);
    RULES_MAP_LANGTON.put("20742", 2);
    RULES_MAP_LANGTON.put("20772", 2);
    RULES_MAP_LANGTON.put("21122", 2);
    RULES_MAP_LANGTON.put("21126", 1);
    RULES_MAP_LANGTON.put("21222", 2);
    RULES_MAP_LANGTON.put("21224", 2);
    RULES_MAP_LANGTON.put("21226", 2);
    RULES_MAP_LANGTON.put("21227", 2);
    RULES_MAP_LANGTON.put("21422", 2);
    RULES_MAP_LANGTON.put("21522", 2);
    RULES_MAP_LANGTON.put("21622", 2);
    RULES_MAP_LANGTON.put("21722", 2);
    RULES_MAP_LANGTON.put("22227", 2);
    RULES_MAP_LANGTON.put("22244", 2);
    RULES_MAP_LANGTON.put("22246", 2);
    RULES_MAP_LANGTON.put("22276", 2);
    RULES_MAP_LANGTON.put("22277", 2);
    RULES_MAP_LANGTON.put("30001", 3);
    RULES_MAP_LANGTON.put("30002", 2);
    RULES_MAP_LANGTON.put("30004", 1);
    RULES_MAP_LANGTON.put("30007", 6);
    RULES_MAP_LANGTON.put("30012", 3);
    RULES_MAP_LANGTON.put("30042", 1);
    RULES_MAP_LANGTON.put("30062", 2);
    RULES_MAP_LANGTON.put("30102", 1);
    RULES_MAP_LANGTON.put("30122", 0);
    RULES_MAP_LANGTON.put("30251", 1);
    RULES_MAP_LANGTON.put("40112", 0);
    RULES_MAP_LANGTON.put("40122", 0);
    RULES_MAP_LANGTON.put("40125", 0);
    RULES_MAP_LANGTON.put("40212", 0);
    RULES_MAP_LANGTON.put("40222", 1);
    RULES_MAP_LANGTON.put("40232", 6);
    RULES_MAP_LANGTON.put("40252", 0);
    RULES_MAP_LANGTON.put("40322", 1);
    RULES_MAP_LANGTON.put("50002", 2);
    RULES_MAP_LANGTON.put("50021", 5);
    RULES_MAP_LANGTON.put("50022", 5);
    RULES_MAP_LANGTON.put("50023", 2);
    RULES_MAP_LANGTON.put("50027", 2);
    RULES_MAP_LANGTON.put("50052", 0);
    RULES_MAP_LANGTON.put("50202", 2);
    RULES_MAP_LANGTON.put("50212", 2);
    RULES_MAP_LANGTON.put("50215", 2);
    RULES_MAP_LANGTON.put("50222", 0);
    RULES_MAP_LANGTON.put("50224", 4);
    RULES_MAP_LANGTON.put("50272", 2);
    RULES_MAP_LANGTON.put("51212", 2);
    RULES_MAP_LANGTON.put("51222", 0);
    RULES_MAP_LANGTON.put("51242", 2);
    RULES_MAP_LANGTON.put("51272", 2);
    RULES_MAP_LANGTON.put("60001", 1);
    RULES_MAP_LANGTON.put("60002", 1);
    RULES_MAP_LANGTON.put("60212", 0);
    RULES_MAP_LANGTON.put("61212", 5);
    RULES_MAP_LANGTON.put("61213", 1);
    RULES_MAP_LANGTON.put("61222", 5);
    RULES_MAP_LANGTON.put("70007", 7);
    RULES_MAP_LANGTON.put("70112", 0);
    RULES_MAP_LANGTON.put("70122", 0);
    RULES_MAP_LANGTON.put("70125", 0);
    RULES_MAP_LANGTON.put("70212", 0);
    RULES_MAP_LANGTON.put("70222", 1);
    RULES_MAP_LANGTON.put("70225", 1);
    RULES_MAP_LANGTON.put("70232", 1);
    RULES_MAP_LANGTON.put("70252", 5);
    RULES_MAP_LANGTON.put("70272", 0);
  }


  /**
   * Constructor for the Rule class
   *
   * @param parameters - map of parameters (String to Double) for adjusting rules from default.
   */
  public LangtonRule(LangtonParameters parameters) {
    super(parameters);
  }

  @Override
  public int apply(LangtonCell cell) {
    String stateKey = getStateKey(cell, new String[]{"N", "E", "S", "W"});
    return RULES_MAP_LANGTON.getOrDefault(stateKey, cell.getCurrentState());

  }

  private String getStateKey(LangtonCell cell, String[] directions) {
    StringBuilder stateBuilder = new StringBuilder();

    stateBuilder.append(cell.getCurrentState());
    for (String dir : directions) {
      cell.getNeighbors().stream()
          .filter(neighbor -> matchesDirection(cell, neighbor, dir))
          .findFirst()
          .ifPresentOrElse(
              neighbor -> stateBuilder.append(neighbor.getCurrentState()),
              () -> stateBuilder.append("0")
              // TODO: if no neighbor on a side add 0? not sure how it supposed to behave actually
          );
    }

    return stateBuilder.toString();
  }

  private boolean matchesDirection(LangtonCell cell, LangtonCell neighbor, String direction) {
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
