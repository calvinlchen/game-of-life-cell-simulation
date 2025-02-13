package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.PercolationCell;
import cellsociety.model.util.constants.CellStates.PercolationStates;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test for testing Percolation Rules
 */
class PercolationRuleTest {
  private PercolationRule rule;

  @BeforeEach
  void setUp() {
    rule = new PercolationRule(Map.of()); // no parameters either
  }

  @Test
  @DisplayName("Open middle cell with percolated neighbor becomes percolated")
  void openMiddleCell_BecomesPercolated_WithPercolatedNeighbor() {
    PercolationCell cell = createCellWithStateAndNeighbors(1, 1, 4);
    assertEquals(2, rule.apply(cell));
  }

  @Test
  @DisplayName("Open middle cell without percolated neighbor remains open")
  void openMiddleCell_RemainsOpen_WithoutPercolatedNeighbor() {
    PercolationCell cell = createCellWithStateAndNeighbors(1, 0, 4);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Open edge cell with percolated neighbor becomes percolated")
  void openEdgeCell_BecomesPercolated_WithPercolatedNeighbor() {
    PercolationCell cell = createCellWithStateAndNeighbors(1, 1, 3);
    assertEquals(2, rule.apply(cell));
  }

  @Test
  @DisplayName("Open edge cell without percolated neighbor remains open")
  void openEdgeCell_RemainsOpen_WithoutPercolatedNeighbor() {
    PercolationCell cell = createCellWithStateAndNeighbors(1, 0, 3);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Open corner cell with percolated neighbor becomes percolated")
  void openCornerCell_BecomesPercolated_WithPercolatedNeighbor() {
    PercolationCell cell = createCellWithStateAndNeighbors(1, 1, 2);
    assertEquals(2, rule.apply(cell));
  }

  @Test
  @DisplayName("Open corner cell without percolated neighbor remains open")
  void openCornerCell_RemainsOpen_WithoutPercolatedNeighbor() {
    PercolationCell cell = createCellWithStateAndNeighbors(1, 0, 2);
    assertEquals(1, rule.apply(cell));
  }

  @Test
  @DisplayName("Percolated cell remains percolated")
  void percolatedCell_RemainsPercolated() {
    PercolationCell cell = createCellWithStateAndNeighbors(2, 2, 4);
    assertEquals(2, rule.apply(cell));
  }

  @Test
  @DisplayName("Blocked cell remains blocked")
  void blockedCell_RemainsBlocked() {
    PercolationCell cell = createCellWithStateAndNeighbors(0, 2, 4);
    assertEquals(0, rule.apply(cell));
  }

  private PercolationCell createCellWithStateAndNeighbors(int state, int percolatedNeighbors, int totalNeighbors) {
    PercolationCell cell = new PercolationCell(state, rule);
    List<PercolationCell> neighbors = new ArrayList<>();
    for (int i = 0; i < percolatedNeighbors; i++) {
      neighbors.add(new PercolationCell(2, rule));
    }
    for (int i = percolatedNeighbors; i < totalNeighbors; i++) {
      neighbors.add(new PercolationCell(1, rule));
    }
    cell.setNeighbors(neighbors);
    return cell;
  }
}
