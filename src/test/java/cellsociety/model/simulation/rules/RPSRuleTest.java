package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.RPSCell;
import cellsociety.model.simulation.parameters.RPSParameters;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for RPSRule
 */
class RPSRuleTest {

  private RPSRule rule;

  @BeforeEach
  void setUp() {
    RPSParameters parameters = new RPSParameters();
    parameters.setParameter("numStates", 3.0);
    parameters.setParameter("percentageToWin", 0.5);
    rule = new RPSRule(parameters);
  }

  @Test
  @DisplayName("Middle cell changes state when enough winning neighbors exist")
  void middleCell_ChangesState_WhenWinningNeighborsExist() {
    RPSCell cell = createCellWithStateAndNeighbors(0, 5, 8, 1);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Middle cell stays the same when not enough winning neighbors exist")
  void middleCell_StaysSame_WhenNotEnoughWinningNeighbors() {
    RPSCell cell = createCellWithStateAndNeighbors(0, 2, 8, 1);
    assertEquals(0, rule.apply(cell));
  }

  @Test
  @DisplayName("Edge cell changes state when enough winning neighbors exist")
  void edgeCell_ChangesState_WhenWinningNeighborsExist() {
    RPSCell cell = createCellWithStateAndNeighbors(0, 3, 5, 1);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Edge cell stays the same when not enough winning neighbors exist")
  void edgeCell_StaysSame_WhenNotEnoughWinningNeighbors() {
    RPSCell cell = createCellWithStateAndNeighbors(0, 1, 5, 1);
    assertEquals(0, rule.apply(cell));
  }

  @Test
  @DisplayName("Corner cell changes state when enough winning neighbors exist")
  void cornerCell_ChangesState_WhenWinningNeighborsExist() {
    RPSCell cell = createCellWithStateAndNeighbors(0, 2, 3, 1);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Corner cell stays the same when not enough winning neighbors exist")
  void cornerCell_StaysSame_WhenNotEnoughWinningNeighbors() {
    RPSCell cell = createCellWithStateAndNeighbors(0, 0, 3, 1);
    assertEquals(0, rule.apply(cell));
  }

  private RPSCell createCellWithStateAndNeighbors(int state, int winningNeighbors, int totalNeighbors, int winningState) {
    RPSCell cell = new RPSCell(state, rule);
    List<RPSCell> neighbors = new ArrayList<>();

    for (int i = 0; i < winningNeighbors; i++) {
      neighbors.add(new RPSCell(winningState, rule));
    }

    for (int i = winningNeighbors; i < totalNeighbors; i++) {
      neighbors.add(new RPSCell(2, rule));
    }

    cell.setNeighbors(neighbors);
    return cell;
  }
}
