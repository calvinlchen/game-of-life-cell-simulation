package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.ChouReg2Cell;
import cellsociety.model.simulation.cell.PetelkaCell;
import cellsociety.model.simulation.parameters.PetelkaParameters;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for PetelkaRule
 */
class PetelkaRuleTest {

  private PetelkaRule rule;

  @BeforeEach
  void setUp() {
    rule = new PetelkaRule(new PetelkaParameters());
  }

  @Test
  @DisplayName("State transition according to RULES_MAP_PETELKA")
  void apply_testStateTransitionsMiddleCell_petelkaRules() {
    assertStateTransition("014000000", 1);
    assertStateTransition("123400000", 2);
    assertStateTransition("234100000", 2);
    assertStateTransition("041000000", 4);
    assertStateTransition("432100000", 2);
    assertStateTransition("341200000", 3);
    assertStateTransition("133400000", 2);
    assertStateTransition("333200000", 4);
  }

  @Test
  @DisplayName("Unknown state retains current state")
  void apply_testEdgeCells_retainCurrentState() {
    assertStateTransition("33320", 3);
  }

  @Test
  @DisplayName("Unknown states not in list get set to 0")
  void apply_testMiddleCellsNotInList_setTo0() {
    assertStateTransition("123420000", 0);
  }

  private void assertStateTransition(String stateKey, int expectedState) {
    PetelkaCell cell = createCellWithStateAndNeighbors(stateKey);
    assertEquals(expectedState, rule.apply(cell));
  }

  private PetelkaCell createCellWithStateAndNeighbors(String stateKey) {
    int cellState = Character.getNumericValue(stateKey.charAt(0));
    PetelkaCell cell = new PetelkaCell(cellState, rule);
    cell.setPosition(new int[]{2, 2});

    List<PetelkaCell> neighbors = new ArrayList<>();
    Map<DirectionType, List<PetelkaCell>> directionNeighbors = new HashMap<>();

    int[][] positions = {{2, 1}, {3, 1}, {3, 2}, {3, 3}, {2, 3}, {1, 3}, {1, 2}, {1, 1}};
    DirectionType[] directions = {DirectionType.N, DirectionType.NE, DirectionType.E,
        DirectionType.SE, DirectionType.S, DirectionType.SW, DirectionType.W, DirectionType.NW};

    for (int i = 1; i < stateKey.length(); i++) {
      int neighborState = Character.getNumericValue(stateKey.charAt(i));
      PetelkaCell neighbor = new PetelkaCell(neighborState, rule);
      neighbor.setPosition(positions[i - 1]);
      neighbors.add(neighbor);
      directionNeighbors.put(directions[i - 1], List.of(neighbor));
    }

    cell.setNeighbors(neighbors);
    cell.setDirectionalNeighbors(directionNeighbors);

    return cell;
  }
}
