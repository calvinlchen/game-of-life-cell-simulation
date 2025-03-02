package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.GameOfLifeCell;
import java.util.ArrayList;
import java.util.List;
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
    rule = new GameOfLifeRule(new GameOfLifeParameters());  // game of life doesn't need parameters
  }

  @Test
  @DisplayName("Alive middle cell with 2 or 3 neighbors survives")
  void aliveMiddleCell_Survives_WithTwoOrThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(1, 2, 8);
    assertEquals(1, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(1, 3, 8);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Alive middle cell with <2 or >3 neighbors dies")
  void aliveMiddleCell_Dies_WithTooFewOrTooManyNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(1, 1, 8);
    assertEquals(0, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(1, 4, 8);
    assertEquals(0, rule.apply(cell));
  }

  @Test
  @DisplayName("Dead middle cell with exactly 3 neighbors becomes alive")
  void deadMiddleCell_BecomesAlive_WithThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(0, 3, 8);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Dead middle cell with non-3 neighbors remains dead")
  void deadMiddleCell_RemainsDead_WithNonThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(0, 2, 8);
    assertEquals(0, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(0, 4, 8);
    assertEquals(0, rule.apply(cell));
  }

  @Test
  @DisplayName("Alive edge cell with 2 or 3 neighbors survives")
  void aliveEdgeCell_Survives_WithTwoOrThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(1, 2, 5);
    assertEquals(1, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(1, 3, 5);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Alive edge cell with <2 or >3 neighbors dies")
  void aliveEdgeCell_Dies_WithTooFewOrTooManyNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(1, 1, 5);
    assertEquals(0, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(1, 4, 5);
    assertEquals(0, rule.apply(cell));
  }

  @Test
  @DisplayName("Dead edge cell with exactly 3 neighbors becomes alive")
  void deadEdgeCell_BecomesAlive_WithThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(0, 3, 5);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Dead edge cell with non-3 neighbors remains dead")
  void deadEdgeCell_RemainsDead_WithNonThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(0, 2, 5);
    assertEquals(0, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(0, 4, 5);
    assertEquals(0, rule.apply(cell));
  }

  @Test
  @DisplayName("Alive corner cell with 2 or 3 neighbors survives")
  void aliveCornerCell_Survives_WithTwoOrThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(1, 2, 3);
    assertEquals(1, rule.apply(cell));

    cell = createCellWithStateAndNeighbors(1, 3, 3);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Alive corner cell with <2 or >3 neighbors dies")
  void aliveCornerCell_Dies_WithTooFewOrTooManyNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(1, 1, 3);
    assertEquals(0, rule.apply(cell));
  }

  @Test
  @DisplayName("Dead corner cell with exactly 3 neighbors becomes alive")
  void deadCornerCell_BecomesAlive_WithThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(0, 3, 3);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Dead corner cell with non-3 neighbors remains dead")
  void deadCornerCell_RemainsDead_WithNonThreeNeighbors() {
    GameOfLifeCell cell = createCellWithStateAndNeighbors(0, 2, 3);
    assertEquals(0, rule.apply(cell));
  }


  private GameOfLifeCell createCellWithStateAndNeighbors(int state, int aliveNeighbors, int totalNeighbors) {
    GameOfLifeCell cell = new GameOfLifeCell(state, rule);
    List<GameOfLifeCell> neighbors = new ArrayList<>();
    for (int i = 0; i < aliveNeighbors; i++) {
      neighbors.add(new GameOfLifeCell(1, rule));
    }
    for (int i = aliveNeighbors; i < totalNeighbors; i++) {
      neighbors.add(new GameOfLifeCell(0, rule));
    }
    cell.setNeighbors(neighbors);
    return cell;
  }
}
