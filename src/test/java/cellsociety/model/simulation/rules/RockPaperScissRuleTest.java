package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.RockPaperScissCell;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for RPSRule
 */
class RockPaperScissRuleTest {

  private RockPaperScissRule rule;

  @BeforeEach
  void setUp() {
    RockPaperScissParameters parameters = new RockPaperScissParameters();
    parameters.setParameter("numStates", 3.0);
    parameters.setParameter("percentageToWin", 0.5);
    rule = new RockPaperScissRule(parameters);
  }

  @Test
  @DisplayName("Middle cell changes state when enough winning neighbors exist")
  void middleCell_ChangesState_WhenWinningNeighborsExist() {
    RockPaperScissCell cell = createCellWithStateAndNeighbors(0, 5, 8, 1);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Middle cell stays the same when not enough winning neighbors exist")
  void middleCell_StaysSame_WhenNotEnoughWinningNeighbors() {
    RockPaperScissCell cell = createCellWithStateAndNeighbors(0, 2, 8, 1);
    assertEquals(0, rule.apply(cell));
  }

  @Test
  @DisplayName("Edge cell changes state when enough winning neighbors exist")
  void edgeCell_ChangesState_WhenWinningNeighborsExist() {
    RockPaperScissCell cell = createCellWithStateAndNeighbors(0, 3, 5, 1);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Edge cell stays the same when not enough winning neighbors exist")
  void edgeCell_StaysSame_WhenNotEnoughWinningNeighbors() {
    RockPaperScissCell cell = createCellWithStateAndNeighbors(0, 1, 5, 1);
    assertEquals(0, rule.apply(cell));
  }

  @Test
  @DisplayName("Corner cell changes state when enough winning neighbors exist")
  void cornerCell_ChangesState_WhenWinningNeighborsExist() {
    RockPaperScissCell cell = createCellWithStateAndNeighbors(0, 2, 3, 1);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Corner cell stays the same when not enough winning neighbors exist")
  void cornerCell_StaysSame_WhenNotEnoughWinningNeighbors() {
    RockPaperScissCell cell = createCellWithStateAndNeighbors(0, 0, 3, 1);
    assertEquals(0, rule.apply(cell));
  }

  private RockPaperScissCell createCellWithStateAndNeighbors(int state, int winningNeighbors, int totalNeighbors, int winningState) {
    RockPaperScissCell cell = new RockPaperScissCell(state, rule);
    List<RockPaperScissCell> neighbors = new ArrayList<>();

    for (int i = 0; i < winningNeighbors; i++) {
      neighbors.add(new RockPaperScissCell(winningState, rule));
    }

    for (int i = winningNeighbors; i < totalNeighbors; i++) {
      neighbors.add(new RockPaperScissCell(2, rule));
    }

    cell.setNeighbors(neighbors);
    return cell;
  }
}
