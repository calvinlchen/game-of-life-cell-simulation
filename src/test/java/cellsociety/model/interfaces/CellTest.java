package cellsociety.model.interfaces;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

enum CellTestState { ALIVE, DEAD; }   // testing enum for states

/**
 * Test cell for testing
 */
class TestCell extends Cell<CellTestState, TestCell> {
  public TestCell(CellTestState state) {
    super(state);
  }

  public TestCell(CellTestState state, int[] position) {
    super(state, position);
  }

  @Override
  public void calcNextState() {
    return;
  }

  @Override
  public void step() {
    return;
  }
}

/**
 * Tester for cell interface
 */
class CellTest {
  private TestCell cell;
  private TestCell neighbor;

  @BeforeEach
  void setUp() {
    cell = new TestCell(CellTestState.ALIVE);
    neighbor = new TestCell(CellTestState.DEAD);
  }

  // Positive Checks

  @Test
  @DisplayName("Initial state is set correctly")
  void initialState_Verified() {
    assertEquals(CellTestState.ALIVE, cell.getCurrentState());
    assertEquals(0, cell.getNeighbors().size());
  }

  @Test
  @DisplayName("Set and get current state correctly")
  void currentState_SetAndGet_Verified() {
    assertEquals(CellTestState.ALIVE, cell.getCurrentState());
    cell.setCurrentState(CellTestState.DEAD);
    assertEquals(CellTestState.DEAD, cell.getCurrentState());
  }

  @Test
  @DisplayName("Set and get next state correctly")
  void nextState_SetAndGet_Verified() {
    assertNull(cell.getNextState());
    cell.setNextState(CellTestState.DEAD);
    assertEquals(CellTestState.DEAD, cell.getNextState());
  }

  @Test
  @DisplayName("Set position constructor correctly")
  void positionConstructor_Verified() {
    int[] position = {2, 3};
    Cell<CellTestState, TestCell> cellWithPosition = new TestCell(CellTestState.ALIVE, position);
    assertArrayEquals(position, cellWithPosition.getPosition());
  }

  @Test
  @DisplayName("Set and get position correctly")
  void position_SetAndGet_Verified() {
    int[] position = {2, 3};
    cell.setPosition(position);
    assertArrayEquals(position, cell.getPosition());
  }

  @Test
  @DisplayName("Set and get neighbors correctly")
  void neighbors_SetAndGet_Verified() {
    List<TestCell> neighborsToAdd = new ArrayList<>();
    neighborsToAdd.add(neighbor);

    cell.setNeighbors(neighborsToAdd);
    List<TestCell> neighbors = cell.getNeighbors();
    assertEquals(1, neighbors.size());
    assertTrue(neighbors.contains(neighbor));
  }

  @Test
  @DisplayName("Successfully add neighbor")
  void neighbors_AddSingleNeighbor_Verified() {
    assertTrue(cell.addNeighbor(neighbor));

    List<TestCell> neighbors = cell.getNeighbors();
    assertEquals(1, neighbors.size());
    assertTrue(neighbors.contains(neighbor));
  }

  @Test
  @DisplayName("Successfully remove neighbor")
  void neighbors_RemoveSingleNeighbor_Verified() {
    assertTrue(cell.addNeighbor(neighbor));
    assertTrue(cell.removeNeighbor(neighbor));
  }

  // Negative Tests

  @Test
  @DisplayName("Set initial state to null")
  void initialState_Null_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new TestCell(null));
  }

  @Test
  @DisplayName("Set current state to null")
  void currentState_Null_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cell.setCurrentState(null));
  }

  @Test
  @DisplayName("Set next state to null")
  void nextState_Null_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cell.setNextState(null));
  }

  @Test
  @DisplayName("Set invalid number of position arguments in position constructor")
  void positionConstructor_InvalidPosition_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new TestCell(CellTestState.ALIVE, new int[]{0}));
  }

  @Test
  @DisplayName("Set position to null in position constructor")
  void positionConstructor_Null_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new TestCell(CellTestState.ALIVE, null));
  }

  @Test
  @DisplayName("Set invalid number of position arguments in position constructor")
  void position_InvalidPosition_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cell.setPosition(new int[]{0}));
  }

  @Test
  @DisplayName("Set position to null")
  void position_Null_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cell.setPosition(null));
  }

  @Test
  @DisplayName("Set neighbors to null")
  void neighbors_Null_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cell.setNeighbors(null));
  }


  @Test
  @DisplayName("Add the same neighbor")
  void neighbors_AddSameNeighbor_IllegalArgumentException() {
    assertTrue(cell.addNeighbor(neighbor));
    assertThrows(IllegalArgumentException.class, () -> cell.addNeighbor(neighbor));
  }


  @Test
  @DisplayName("Add neighbor that is null")
  void neighbors_AddNull_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cell.addNeighbor(null));
  }


  @Test
  @DisplayName("Fail to remove neighbor when not in list")
  void neighbors_RemoveNonExistentNeighbor_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cell.removeNeighbor(neighbor));
  }

  @Test
  @DisplayName("Remove null neighbor")
  void neighbors_RemoveNull_IllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> cell.removeNeighbor(null));
  }



}
