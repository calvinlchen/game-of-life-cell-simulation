package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.LangtonCell;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for LangtonRule
 */
class LangtonRuleTest {

  private LangtonRule rule;

  @BeforeEach
  void setUp() {
    rule = new LangtonRule(new LangtonParameters());
  }

  @Test
  @DisplayName("State transition according to RULES_MAP_LANGTON")
  void apply_testStateTransitionsMiddleCell_langtonRules() {
    assertStateTransition("00000", 0);
    assertStateTransition("00001", 2);
    assertStateTransition("00006", 3);
    assertStateTransition("00007", 1);
    assertStateTransition("00212", 5);
    assertStateTransition("10024", 4);
    assertStateTransition("10227", 7);
  }

  @Test
  @DisplayName("Unknown state retains current state")
  void apply_testEdgeCells_retainCurrentState() {
    assertStateTransition("300", 3);
  }

  private void assertStateTransition(String stateKey, int expectedState) {
    LangtonCell cell = createCellWithStateAndNeighbors(stateKey);
    assertEquals(expectedState, rule.apply(cell));
  }

  private LangtonCell createCellWithStateAndNeighbors(String stateKey) {
    int cellState = Character.getNumericValue(stateKey.charAt(0));
    LangtonCell cell = new LangtonCell(cellState, rule);
    cell.setPosition(new int[]{2, 2});

    List<LangtonCell> neighbors = new ArrayList<>();
    Map<DirectionType, List<LangtonCell>> directionNeighbors = new HashMap<>();

    int[][] positions = {{2, 1}, {3, 2}, {2, 3}, {1, 2}};
    DirectionType[] directions = {DirectionType.N, DirectionType.E, DirectionType.S,
        DirectionType.W};

    for (int i = 1; i < stateKey.length(); i++) {
      int neighborState = Character.getNumericValue(stateKey.charAt(i));
      LangtonCell neighbor = new LangtonCell(neighborState, rule);
      neighbor.setPosition(positions[i - 1]);
      neighbors.add(neighbor);
      directionNeighbors.put(directions[i - 1], List.of(neighbor));
    }

    cell.setNeighbors(neighbors);
    cell.setDirectionalNeighbors(directionNeighbors);
    return cell;
  }
}
