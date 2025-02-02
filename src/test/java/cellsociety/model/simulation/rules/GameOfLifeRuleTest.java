package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.GameOfLifeCell;
import cellsociety.model.util.constants.CellStates.GameOfLifeStates;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * Test for testing Game of Life Rules
 */
class GameOfLifeRuleTest {
  private GameOfLifeRule rule;

  @BeforeEach
  void setUp() {
    rule = new GameOfLifeRule(Map.of());  // game of life doesn't need parameters
  }

  @Test
  @DisplayName("Alive middle cell with 2 or 3 neighbors survives")
  void aliveMiddleCell_Survives_WithTwoOrThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(GameOfLifeStates.ALIVE, 2);
    assertEquals(GameOfLifeStates.ALIVE, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(GameOfLifeStates.ALIVE, 3);
    assertEquals(GameOfLifeStates.ALIVE, rule.apply(cell));
  }

  @Test
  @DisplayName("Alive middle cell with <2 or >3 neighbors dies")
  void aliveMiddleCell_Dies_WithTooFewOrTooManyNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(GameOfLifeStates.ALIVE, 1);
    assertEquals(GameOfLifeStates.DEAD, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(GameOfLifeStates.ALIVE, 4);
    assertEquals(GameOfLifeStates.DEAD, rule.apply(cell));
  }

  @Test
  @DisplayName("Dead middle cell with exactly 3 neighbors becomes alive")
  void deadMiddleCell_BecomesAlive_WithThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(GameOfLifeStates.DEAD, 3);
    assertEquals(GameOfLifeStates.ALIVE, rule.apply(cell));
  }

  @Test
  @DisplayName("Dead middle cell with non-3 neighbors remains dead")
  void deadMiddleCell_RemainsDead_WithNonThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(GameOfLifeStates.DEAD, 2);
    assertEquals(GameOfLifeStates.DEAD, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(GameOfLifeStates.DEAD, 4);
    assertEquals(GameOfLifeStates.DEAD, rule.apply(cell));
  }

  @Test
  @DisplayName("Edge cell with correct rule application")
  void edgeCell_RulesAppliedCorrectly() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(GameOfLifeStates.ALIVE, 1);
    assertEquals(GameOfLifeStates.DEAD, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(GameOfLifeStates.DEAD, 3);
    assertEquals(GameOfLifeStates.ALIVE, rule.apply(cell));
  }

  @Test
  @DisplayName("Corner cell with correct rule application")
  void cornerCell_RulesAppliedCorrectly() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(GameOfLifeStates.ALIVE, 0);
    assertEquals(GameOfLifeStates.DEAD, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(GameOfLifeStates.DEAD, 3);
    assertEquals(GameOfLifeStates.ALIVE, rule.apply(cell));
  }

  private GameOfLifeCell createCellWithStateAndNeighbors(GameOfLifeStates state, int aliveNeighbors) {
    GameOfLifeCell cell = new GameOfLifeCell(state, rule);
    List<GameOfLifeCell> neighbors = new ArrayList<>();
    for (int i = 0; i < aliveNeighbors; i++) {
      neighbors.add(new GameOfLifeCell(GameOfLifeStates.ALIVE, rule));
    }
    for (int i = aliveNeighbors; i < 8; i++) { 
      neighbors.add(new GameOfLifeCell(GameOfLifeStates.DEAD, rule));
    }
    cell.setNeighbors(neighbors);
    return cell;
  }
}
