package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.rules.PercolationRule;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tester for Percolation Cell
 */
class PercolationCellTest {
  private PercolationRule rule;

  @BeforeEach
  void setUp() {
    rule = new PercolationRule(new PercolationParameters());
  }

  @Test
  @DisplayName("PercolationCell correctly initializes state")
  void cell_Initializes_Correctly() {
    PercolationCell cell = new PercolationCell(1, rule);
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("PercolationCell correctly updates state based on rule")
  void cell_Updates_State_Correctly() {
    PercolationCell cell = new PercolationCell(1, rule);
    List<PercolationCell> neighbors = createNeighbors(1, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(2, cell.getCurrentState());
  }

  @Test
  @DisplayName("PercolationCell remains open if no percolated neighbors")
  void cell_Remains_Open_Without_Percolated_Neighbor() {
    PercolationCell cell = new PercolationCell(1, rule);
    List<PercolationCell> neighbors = createNeighbors(0, rule);
    cell.setNeighbors(neighbors);
    cell.calcNextState();
    cell.step();
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("PercolationCell in PERCOLATED state remains PERCOLATED")
  void cell_Remains_Percolated() {
    PercolationCell cell = new PercolationCell(2, rule);
    cell.calcNextState();
    cell.step();
    assertEquals(2, cell.getCurrentState());
  }

  @Test
  @DisplayName("Blocked PercolationCell remains BLOCKED")
  void blockedCell_Remains_Blocked() {
    PercolationCell cell = new PercolationCell(0, rule);
    cell.calcNextState();
    cell.step();
    assertEquals(0, cell.getCurrentState());
  }

  private List<PercolationCell> createNeighbors(int percolatedCount, PercolationRule rule) {
    List<PercolationCell> neighbors = new ArrayList<>();
    for (int i = 0; i < percolatedCount; i++) {
      neighbors.add(new PercolationCell(2, rule));
    }
    for (int i = percolatedCount; i < 4; i++) {
      neighbors.add(new PercolationCell(1, rule));
    }
    return neighbors;
  }
}
