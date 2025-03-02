package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.rules.GameOfLifeRule;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tester for Game of Life Cell
 */
public class GameOfLifeCellTest {
  private GameOfLifeRule rule;

  @BeforeEach
  void setUp() {
    rule = new GameOfLifeRule(new GameOfLifeParameters());
  }

  @Test
  @DisplayName("Correctly initializes state")
  void cell_Initializes_Correctly() {
    GameOfLifeCell cell = new GameOfLifeCell(1, rule);
    assertEquals(1, cell.getCurrentState());
  }

  // more actual test about the rules are in the rules, just it only takes in game of life rule
  @Test
  @DisplayName("Correctly updates state based on rule")
  void cell_Updates_State_Correctly() {
    GameOfLifeCell cell = new GameOfLifeCell(1, rule);
    List<GameOfLifeCell> neighbors = createNeighbors(3, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("Correctly transitions from ALIVE to DEAD based on rule")
  void cell_Transitions_From_Alive_To_Dead() {
    GameOfLifeCell cell = new GameOfLifeCell(1, rule);
    List<GameOfLifeCell> neighbors = createNeighbors(1, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(0, cell.getCurrentState());
  }

  @Test
  @DisplayName("Correctly transitions from DEAD to ALIVE with exactly 3 neighbors")
  void cell_Transitions_From_Dead_To_Alive() {
    GameOfLifeCell cell = new GameOfLifeCell(0, rule);
    List<GameOfLifeCell> neighbors = createNeighbors(3, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(1, cell.getCurrentState());
  }

  private List<GameOfLifeCell> createNeighbors(int aliveCount, GameOfLifeRule rule) {
    List<GameOfLifeCell> neighbors = new ArrayList<>();
    for (int i = 0; i < aliveCount; i++) {
      neighbors.add(new GameOfLifeCell(1, rule));
    }
    for (int i = aliveCount; i < 8; i++) {
      neighbors.add(new GameOfLifeCell(0, rule));
    }
    return neighbors;
  }
}
