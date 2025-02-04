package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.util.constants.CellStates.WaTorStates;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    rule = new WaTorRule(Map.of(
        "fishReproductionTime", 3.0,
        "sharkEnergyGain", 1.0,
        "sharkReproductionTime", 3.0,
        "sharkInitialEnergy", 5.0));
  }

  @Test
  @DisplayName("Fish moves to empty space")
  void fish_Moves() {
    WaTorCell cell = createCellWithStateAndNeighbors(WaTorStates.FISH, 4, 0, 0);
    assertEquals(WaTorStates.EMPTY, rule.apply(cell));
  }

  @Test
  @DisplayName("Fish moves to empty space and reproduces")
  void fish_MovesAndReproduces() {
    WaTorCell cell = createCellWithStateAndNeighbors(WaTorStates.FISH, 4, 0, 0);
    cell.setStepsSurvived(2);
    assertEquals(WaTorStates.FISH, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().anyMatch(neighbor -> neighbor.getNextState() == WaTorStates.FISH));
  }

  @Test
  @DisplayName("Fish stays if no empty space available")
  void fish_Stays_NoEmptySpaces() {
    WaTorCell cell = createCellWithStateAndNeighbors(WaTorStates.FISH, 0, 2, 2);
    assertNull(rule.apply(cell));
  }

  @Test
  @DisplayName("Shark moves to empty space")
  void shark_MovesToEmpty() {
    WaTorCell cell = createCellWithStateAndNeighbors(WaTorStates.SHARK, 4, 0, 0);
    assertEquals(WaTorStates.EMPTY, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().anyMatch(neighbor -> neighbor.getNextState() == WaTorStates.SHARK));
  }

  @Test
  @DisplayName("Shark eats fish and gains energy")
  void shark_EatsFish() {
    WaTorCell cell = createCellWithStateAndNeighbors(WaTorStates.SHARK,  2, 0, 2);
    assertEquals(WaTorStates.EMPTY, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().anyMatch(neighbor -> neighbor.getNextState() == WaTorStates.SHARK));
    assertTrue(cell.getNeighbors().stream().anyMatch(WaTorCell::isConsumed));
  }

  @Test
  @DisplayName("Shark moves to empty space and reproduces")
  void shark_MovesToEmptySpaceAndReproduces() {
    WaTorCell cell = createCellWithStateAndNeighbors(WaTorStates.SHARK, 4, 0, 0);
    cell.setStepsSurvived(2);
    assertEquals(WaTorStates.SHARK, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().anyMatch(neighbor -> neighbor.getNextState() == WaTorStates.SHARK));
  }

  @Test
  @DisplayName("Shark eats fish and reproduces")
  void Shark_EatsFishAndReproduces() {
    WaTorCell cell = createCellWithStateAndNeighbors(WaTorStates.SHARK,  2, 0, 2);
    cell.setStepsSurvived(2);
    assertEquals(WaTorStates.SHARK, rule.apply(cell));
    assertTrue(cell.getNeighbors().stream().anyMatch(neighbor -> neighbor.getNextState() == WaTorStates.SHARK));
    assertTrue(cell.getNeighbors().stream().anyMatch(WaTorCell::isConsumed));
  }

  @Test
  @DisplayName("Shark dies when energy reaches zero")
  void shark_Dies_WhenEnergyZero() {
    WaTorCell cell = new WaTorCell(WaTorStates.SHARK, rule);
    cell.setEnergy(1);
    assertEquals(WaTorStates.EMPTY, rule.apply(cell));
  }

  private WaTorCell createCellWithStateAndNeighbors(WaTorStates state, int emptyNeighbors, int sharkNeighbors, int fishNeighbors) {
    WaTorCell cell = new WaTorCell(state, rule);
    List<WaTorCell> neighbors = new ArrayList<>();

    for (int i = 0; i < emptyNeighbors - 1; i++) {
      neighbors.add(new WaTorCell(WaTorStates.EMPTY, rule));
    }
    for (int i = 0; i < sharkNeighbors - 1; i++) {
      neighbors.add(new WaTorCell(WaTorStates.SHARK, rule));
    }
    for (int i = 0; i < fishNeighbors - 1; i++) {
      neighbors.add(new WaTorCell(WaTorStates.FISH, rule));
    }

    cell.setNeighbors(neighbors);
    return cell;
  }
}
