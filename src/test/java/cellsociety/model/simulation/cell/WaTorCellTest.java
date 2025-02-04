package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.rules.WaTorRule;
import cellsociety.model.util.constants.CellStates.WaTorStates;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for WaTorCell
 */
class WaTorCellTest {

  private WaTorRule rule;

  @BeforeEach
  void setUp() {
    rule = new WaTorRule(Map.of(
        "fishReproductionTime", 3.0,
        "sharkEnergyGain", 2.0,
        "sharkReproductionTime", 3.0,
        "sharkInitialEnergy", 5.0));
  }

  @Test
  @DisplayName("Fish moves to empty space and does not override other planned moves")
  void fish_MovesWithoutOverriding() {
    WaTorCell fishCell = createCellWithState(WaTorStates.FISH);
    WaTorCell emptyCell = createCellWithState(WaTorStates.EMPTY);
    fishCell.setNeighbors(List.of(emptyCell));
    fishCell.calcNextState();

    assertEquals(WaTorStates.EMPTY, fishCell.getNextState());
    assertEquals(WaTorStates.FISH, emptyCell.getNextState());
  }

  @Test
  @DisplayName("Shark eats fish and fish planned move is not updated")
  void shark_EatsFishWithoutUpdatingFishMove() {
    WaTorCell sharkCell = createCellWithState(WaTorStates.SHARK);
    WaTorCell fishCell = createCellWithState(WaTorStates.FISH);
    WaTorCell emptyCell = createCellWithState(WaTorStates.EMPTY);
    sharkCell.setNeighbors(List.of(fishCell));
    fishCell.setNeighbors(List.of(emptyCell));

    // fish tries to move to empty cell
    fishCell.calcNextState();
    assertEquals(WaTorStates.EMPTY, fishCell.getNextState());
    assertEquals(WaTorStates.FISH, emptyCell.getNextState());

    // shark now moves
    sharkCell.calcNextState();
    fishCell.step();
    emptyCell.step();
    sharkCell.step();

    assertEquals(WaTorStates.EMPTY, emptyCell.getCurrentState());
    assertEquals(WaTorStates.EMPTY, sharkCell.getCurrentState());
    assertEquals(WaTorStates.SHARK, fishCell.getCurrentState());
    }

  @Test
  @DisplayName("Shark moves and does not override empty cell planned state")
  void shark_MovesWithoutOverridingEmptyCell() {
    WaTorCell sharkCell = createCellWithState(WaTorStates.SHARK);
    WaTorCell emptyCell = createCellWithState(WaTorStates.EMPTY);
    sharkCell.setNeighbors(List.of(emptyCell));
    sharkCell.calcNextState();

    assertEquals(WaTorStates.EMPTY, sharkCell.getNextState());
    assertEquals(WaTorStates.SHARK, emptyCell.getNextState());
  }

  private WaTorCell createCellWithState(WaTorStates state) {
    return new WaTorCell(state, rule);
  }

  // positive test
  @Test
  @DisplayName("Initial steps survived is set correctly")
  void stepsSurvived_Initial_Verified() {
    WaTorCell cell = new WaTorCell(WaTorStates.FISH, rule);
    cell.setStepsSurvived(5);
    assertEquals(5, cell.getStepsSurvived());
  }

  @Test
  @DisplayName("Set and get steps survived correctly")
  void stepsSurvived_SetAndGet_Verified() {
    WaTorCell cell = new WaTorCell(WaTorStates.FISH, rule);
    cell.setStepsSurvived(3);
    assertEquals(3, cell.getStepsSurvived());
  }

  @Test
  @DisplayName("Initial energy is set correctly")
  void energy_Initial_Verified() {
    WaTorCell cell = new WaTorCell(WaTorStates.SHARK, rule);
    assertEquals(5, cell.getEnergy());
  }

  @Test
  @DisplayName("Set and get energy correctly")
  void energy_SetAndGet_Verified() {
    WaTorCell cell = new WaTorCell(WaTorStates.SHARK, rule);
    cell.setEnergy(7);
    assertEquals(7, cell.getEnergy());
  }

  @Test
  @DisplayName("Initial consumed state is false")
  void consumed_Initial_False() {
    WaTorCell cell = new WaTorCell(WaTorStates.FISH, rule);
    assertFalse(cell.isConsumed());
  }

  @Test
  @DisplayName("Set and get consumed state correctly")
  void consumed_SetAndGet_Verified() {
    WaTorCell cell = new WaTorCell(WaTorStates.FISH, rule);
    cell.setConsumed(true);
    assertTrue(cell.isConsumed());
  }

  @Test
  @DisplayName("CalcNextState does not modify state when unchanged")
  void calcNextState_NoChange_Verified() {
    WaTorCell cell = new WaTorCell(WaTorStates.FISH, rule);
    cell.calcNextState();
    assertEquals(WaTorStates.FISH, cell.getCurrentState());
  }

  @Test
  @DisplayName("Step updates current state correctly")
  void step_UpdatesCurrentState_Verified() {
    WaTorCell cell = new WaTorCell(WaTorStates.FISH, rule);
    cell.setNextState(WaTorStates.SHARK);
    cell.step();
    assertEquals(WaTorStates.SHARK, cell.getCurrentState());
  }

  @Test
  @DisplayName("Reset parameters resets energy and steps survived")
  void resetParameters_ResetsValues_Verified() {
    WaTorCell cell = new WaTorCell(WaTorStates.SHARK, rule);
    cell.setNextState(WaTorStates.FISH, 3, 2);
    cell.resetParameters();
    assertEquals(3, cell.getStepsSurvived());
    assertEquals(2, cell.getEnergy());
  }

  @Test
  @DisplayName("Initialize default variables correctly")
  void initializeDefaultVariables_Verified() {
    WaTorCell cell = new WaTorCell(WaTorStates.FISH, new int[]{2, 3}, rule);
    assertEquals(0, cell.getStepsSurvived());
    assertEquals(0, cell.getEnergy());
    assertFalse(cell.isConsumed());
  }

  @Test
  @DisplayName("Constructor with position initializes correctly")
  void constructor_WithPosition_Verified() {
    int[] position = {2, 3};
    WaTorCell cell = new WaTorCell(WaTorStates.SHARK, position, rule);
    assertArrayEquals(position, cell.getPosition());
    assertEquals(5, cell.getEnergy());
  }
}

