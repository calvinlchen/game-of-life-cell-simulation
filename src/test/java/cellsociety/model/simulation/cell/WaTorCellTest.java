package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.rules.WaTorRule;
import java.util.List;
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
    rule = new WaTorRule(new WaTorParameters());
  }

  @Test
  @DisplayName("Fish moves to empty space and does not override other planned moves")
  void fish_MovesWithoutOverriding() {
    WaTorCell fishCell = createCellWithState(1);
    WaTorCell emptyCell = createCellWithState(0);
    fishCell.setNeighbors(List.of(emptyCell));
    fishCell.calcNextState();

    assertEquals(0, fishCell.getNextState());
    assertEquals(1, emptyCell.getNextState());
  }

  @Test
  @DisplayName("Shark eats fish and fish planned move is not updated")
  void shark_EatsFishWithoutUpdatingFishMove() {
    WaTorCell sharkCell = createCellWithState(2);
    WaTorCell fishCell = createCellWithState(1);
    WaTorCell emptyCell = createCellWithState(0);
    sharkCell.setNeighbors(List.of(fishCell));
    fishCell.setNeighbors(List.of(emptyCell));

    // fish tries to move to empty cell
    fishCell.calcNextState();
    assertEquals(0, fishCell.getNextState());
    assertEquals(1, emptyCell.getNextState());

    // shark now moves
    sharkCell.calcNextState();
    fishCell.step();
    emptyCell.step();
    sharkCell.step();

    assertEquals(0, emptyCell.getCurrentState());
    assertEquals(0, sharkCell.getCurrentState());
    assertEquals(2, fishCell.getCurrentState());
    }

  @Test
  @DisplayName("Shark moves and does not override empty cell planned state")
  void shark_MovesWithoutOverridingEmptyCell() {
    WaTorCell sharkCell = createCellWithState(2);
    WaTorCell emptyCell = createCellWithState(0);
    sharkCell.setNeighbors(List.of(emptyCell));
    sharkCell.calcNextState();

    assertEquals(0, sharkCell.getNextState());
    assertEquals(2, emptyCell.getNextState());
  }

  private WaTorCell createCellWithState(int state) {
    return new WaTorCell(state, rule);
  }

  // positive test
  @Test
  @DisplayName("Initial steps survived is set correctly")
  void stepsSurvived_Initial_Verified() {
    WaTorCell cell = new WaTorCell(1, rule);
    cell.setStepsSurvived(5);
    assertEquals(5, cell.getStepsSurvived());
  }

  @Test
  @DisplayName("Set and get steps survived correctly")
  void stepsSurvived_SetAndGet_Verified() {
    WaTorCell cell = new WaTorCell(1, rule);
    cell.setStepsSurvived(3);
    assertEquals(3, cell.getStepsSurvived());
  }

  @Test
  @DisplayName("Initial energy is set correctly")
  void energy_Initial_Verified() {
    WaTorCell cell = new WaTorCell(2, rule);
    assertEquals(5, cell.getEnergy());
  }

  @Test
  @DisplayName("Set and get energy correctly")
  void energy_SetAndGet_Verified() {
    WaTorCell cell = new WaTorCell(2, rule);
    cell.setEnergy(7);
    assertEquals(7, cell.getEnergy());
  }

  @Test
  @DisplayName("Initial consumed state is false")
  void consumed_Initial_False() {
    WaTorCell cell = new WaTorCell(1, rule);
    assertFalse(cell.isConsumed());
  }

  @Test
  @DisplayName("Set and get consumed state correctly")
  void consumed_SetAndGet_Verified() {
    WaTorCell cell = new WaTorCell(1, rule);
    cell.setConsumed(true);
    assertTrue(cell.isConsumed());
  }

  @Test
  @DisplayName("CalcNextState does not modify state when unchanged")
  void calcNextState_NoChange_Verified() {
    WaTorCell cell = new WaTorCell(1, rule);
    cell.calcNextState();
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("Step updates current state correctly")
  void step_UpdatesCurrentState_Verified() {
    WaTorCell cell = new WaTorCell(1, rule);
    cell.setNextState(2);
    cell.step();
    assertEquals(2, cell.getCurrentState());
  }

  @Test
  @DisplayName("Reset parameters resets energy and steps survived")
  void resetParameters_ResetsValues_Verified() {
    WaTorCell cell = new WaTorCell(2, rule);
    cell.setNextState(1, 3, 2);
    cell.resetParameters();
    assertEquals(3, cell.getStepsSurvived());
    assertEquals(2, cell.getEnergy());
  }
}

