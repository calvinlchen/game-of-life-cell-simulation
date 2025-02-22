package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.simulation.parameters.WaTorParameters;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for WaTorRule
 */
class WaTorRuleTest {

  private WaTorRule rule;

  @BeforeEach
  void setUp() {
    rule = new WaTorRule(new WaTorParameters());
  }

  @Test
  @DisplayName("Fish moves to empty space")
  void fish_Moves() {
    WaTorCell cell = createCellWithStateAndNeighbors(1, 4, 0, 0);
    assertEquals(0, rule.apply(cell));
  }

  @Test
  @DisplayName("Fish moves to empty space and reproduces")
  void fish_MovesAndReproduces() {
    WaTorCell cell = createCellWithStateAndNeighbors(1, 4, 0, 0);
    cell.setStepsSurvived(2);
    assertEquals(1, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().anyMatch(neighbor -> neighbor.getNextState() == 1));
  }

  @Test
  @DisplayName("Fish stays if no empty space available")
  void fish_Stays_NoEmptySpaces() {
    WaTorCell cell = createCellWithStateAndNeighbors(1, 0, 2, 2);
    assertEquals(-1, rule.apply(cell));
  }

  @Test
  @DisplayName("Shark moves to empty space")
  void shark_MovesToEmpty() {
    WaTorCell cell = createCellWithStateAndNeighbors(2, 4, 0, 0);
    assertEquals(0, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().anyMatch(neighbor -> neighbor.getNextState() == 2));
  }

  @Test
  @DisplayName("Shark eats fish and gains energy")
  void shark_EatsFish() {
    WaTorCell cell = createCellWithStateAndNeighbors(2,  2, 0, 2);
    assertEquals(0, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().anyMatch(neighbor -> neighbor.getNextState() == 2));
    assertTrue(cell.getNeighbors().stream().anyMatch(WaTorCell::isConsumed));
  }

  @Test
  @DisplayName("Shark moves to empty space and reproduces")
  void shark_MovesToEmptySpaceAndReproduces() {
    WaTorCell cell = createCellWithStateAndNeighbors(2, 4, 0, 0);
    cell.setStepsSurvived(2);
    assertEquals(2, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().anyMatch(neighbor -> neighbor.getNextState() == 2));
  }

  @Test
  @DisplayName("Shark eats fish and reproduces")
  void Shark_EatsFishAndReproduces() {
    WaTorCell cell = createCellWithStateAndNeighbors(2,  2, 0, 2);
    cell.setStepsSurvived(2);
    assertEquals(2, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().anyMatch(neighbor -> neighbor.getNextState() == 2));
    assertTrue(cell.getNeighbors().stream().anyMatch(WaTorCell::isConsumed));
  }

  @Test
  @DisplayName("Shark dies when energy reaches zero")
  void shark_Dies_WhenEnergyZero() {
    WaTorCell cell = new WaTorCell(2, rule);
    cell.setEnergy(1);
    assertEquals(0, rule.apply(cell));
  }

  private WaTorCell createCellWithStateAndNeighbors(int state, int emptyNeighbors, int sharkNeighbors, int fishNeighbors) {
    WaTorCell cell = new WaTorCell(state, rule);
    List<WaTorCell> neighbors = new ArrayList<>();

    for (int i = 0; i < emptyNeighbors - 1; i++) {
      neighbors.add(new WaTorCell(0, rule));
    }
    for (int i = 0; i < sharkNeighbors - 1; i++) {
      neighbors.add(new WaTorCell(2, rule));
    }
    for (int i = 0; i < fishNeighbors - 1; i++) {
      neighbors.add(new WaTorCell(1, rule));
    }

    cell.setNeighbors(neighbors);
    return cell;
  }
}
