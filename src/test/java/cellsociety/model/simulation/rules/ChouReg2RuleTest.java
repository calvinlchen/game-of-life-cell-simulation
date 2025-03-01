package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.ChouReg2Cell;
import cellsociety.model.simulation.cell.LangtonCell;
import cellsociety.model.simulation.parameters.ChouReg2Parameters;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for ChouReg2Rule
 */
class ChouReg2RuleTest {

  private ChouReg2Rule rule;

  @BeforeEach
  void setUp() {
    rule = new ChouReg2Rule(new ChouReg2Parameters());
  }

  @Test
  @DisplayName("State transition according to RULES_MAP_CHOUREG2")
  void apply_testStateTransitionsMiddleCell_chouReg2Rules() {
    assertStateTransition("00000", 0);
    assertStateTransition("00044", 0);
    assertStateTransition("00054", 7);
    assertStateTransition("41103", 3);
    assertStateTransition("43103", 3);
    assertStateTransition("50003", 3);
    assertStateTransition("50333", 0);
    assertStateTransition("10004", 5);
    assertStateTransition("10001", 1);
    assertStateTransition("10041", 4);
    assertStateTransition("00710", 4);
    assertStateTransition("00404", 0);
    assertStateTransition("00444", 5);
  }

  @Test
  @DisplayName("Unknown state retains current state")
  void apply_testEdgeCells_retainCurrentState() {
    assertStateTransition("770", 7);
  }

  private void assertStateTransition(String stateKey, int expectedState) {
    ChouReg2Cell cell = createCellWithStateAndNeighbors(stateKey);
    assertEquals(expectedState, rule.apply(cell));
  }

  private ChouReg2Cell createCellWithStateAndNeighbors(String stateKey) {
    int cellState = Character.getNumericValue(stateKey.charAt(0));
    ChouReg2Cell cell = new ChouReg2Cell(cellState, rule);
    cell.setPosition(new int[]{2, 2});

    List<ChouReg2Cell> neighbors = new ArrayList<>();
    Map<DirectionType, List<ChouReg2Cell>> directionNeighbors = new HashMap<>();

    int[][] positions = {{2, 1}, {3, 2}, {2, 3}, {1, 2}};
    DirectionType[] directions = {DirectionType.N, DirectionType.E, DirectionType.S,
        DirectionType.W};

    for (int i = 1; i < stateKey.length(); i++) {
      int neighborState = Character.getNumericValue(stateKey.charAt(i));
      ChouReg2Cell neighbor = new ChouReg2Cell(neighborState, rule);
      neighbor.setPosition(positions[i - 1]);
      neighbors.add(neighbor);
      directionNeighbors.put(directions[i - 1], List.of(neighbor));
    }

    cell.setNeighbors(neighbors);
    cell.setDirectionalNeighbors(directionNeighbors);

    return cell;
  }
}
