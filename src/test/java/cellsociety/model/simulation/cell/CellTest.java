package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * Test cell for testing
 */
class TestCell extends Cell<TestCell, TestRule, TestParameters> {
  public TestCell(int state, TestRule rule) {
    super(state, rule);
  }

  @Override
  public void calcNextState() {
    return;
  }

  @Override
  public void step() {
    return;
  }

  @Override
  protected TestCell getSelf() {return this;}
}

class TestRule extends Rule<TestCell, TestParameters> {

  public TestRule(TestParameters parameters) {
    super(parameters);
  }

  @Override
  public int apply(TestCell cell) {
    return 0;
  }
}

/**
 * A minimal test parameters class for unit testing.
 *
 * @author chatgpt
 * @author Jessica Chen, modified constructor
 */
class TestParameters extends Parameters {

  public TestParameters() {
    super();

    Map<String, Double> parameters = new HashMap<>();
    parameters.put("maxHistorySize", 3.);
    setParameters(parameters);

  }
}

/**
 * Tester for cell interface
 */
class CellTest {
  private TestCell cell;
  private TestCell neighbor;
  private TestRule rule;

  @BeforeEach
  void setUp() {
    TestParameters parameters = new TestParameters();
    rule = new TestRule(parameters);
    cell = new TestCell(0, rule);
    neighbor = new TestCell(0, rule);
  }

  // Positive Checks

  @Test
  @DisplayName("Initial state is set correctly")
  void initialState_Verified() {
    assertEquals(0, cell.getCurrentState());
    assertEquals(0, cell.getNeighbors().size());
  }

  @Test
  @DisplayName("Set and get current state correctly")
  void currentState_SetAndGet_Verified() {
    assertEquals(0, cell.getCurrentState());
    cell.setCurrentState(1);
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("Set and get next state correctly")
  void nextState_SetAndGet_Verified() {
    assertEquals(0, cell.getNextState());
    cell.setNextState(1);
    assertEquals(1, cell.getNextState());
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

  @Test
  @DisplayName("Step back restores previous state")
  void stepBack_RestoresPreviousState_Verified() {
    cell.setCurrentState(1);

    cell.saveCurrentState();
    cell.setCurrentState(2);


    cell.stepBack();
    assertEquals(1, cell.getCurrentState());
    assertEquals(1, cell.getNextState());
  }

  @Test
  @DisplayName("Step back does not go beyond initial state")
  void stepBack_DoesNotExceedInitialState_Verified() {
    cell.setCurrentState(1);
    cell.stepBack();

    assertEquals(0, cell.getCurrentState());
    assertEquals(0, cell.getNextState());
  }

  @Test
  @DisplayName("Step back after multiple steps restores last saved state")
  void stepBack_AfterMultipleSteps_Verified() {
    cell.setCurrentState(1);
    cell.saveCurrentState();
    cell.setCurrentState(2);

    cell.stepBack();
    assertEquals(1, cell.getCurrentState());
    assertEquals(1, cell.getNextState());

    cell.stepBack();
    assertEquals(0, cell.getCurrentState());
    assertEquals(0, cell.getNextState());
  }

  @Test
  @DisplayName("Step back only restores up to the maxSavedStates limit")
  void stepBack_RespectsMaxSavedStatesLimit_Verified() {
    // 0
    cell.setCurrentState(1);
    cell.saveCurrentState();  // 1-> 0
    cell.setCurrentState(2);
    cell.saveCurrentState();  // 2-> 1 -> 0
    cell.setCurrentState(3);
    cell.saveCurrentState();  // 3-> 2 -> 1
    cell.setCurrentState(4);

    cell.stepBack();
    assertEquals(3, cell.getCurrentState());
    assertEquals(3, cell.getNextState());

    cell.stepBack();
    assertEquals(2, cell.getCurrentState());
    assertEquals(2, cell.getNextState());

    cell.stepBack();
    assertEquals(1, cell.getCurrentState());
    assertEquals(1, cell.getNextState());

    cell.stepBack();
    assertEquals(1, cell.getCurrentState());
    assertEquals(1, cell.getNextState());
  }



  // Negative Tests

  @Test
  @DisplayName("Set invalid number of position arguments in position constructor")
  void position_InvalidPosition_IllegalArgumentException() {
    assertThrows(SimulationException.class, () -> cell.setPosition(new int[]{0}));
  }

  @Test
  @DisplayName("Set position to null")
  void position_Null_IllegalArgumentException() {
    assertThrows(SimulationException.class, () -> cell.setPosition(null));
  }

  @Test
  @DisplayName("Add the same neighbor")
  void neighbors_AddSameNeighbor_IllegalArgumentException() {
    assertTrue(cell.addNeighbor(neighbor));
    assertThrows(SimulationException.class, () -> cell.addNeighbor(neighbor));
  }


  @Test
  @DisplayName("Add neighbor that is null")
  void neighbors_AddNull_IllegalArgumentException() {
    assertThrows(SimulationException.class, () -> cell.addNeighbor(null));
  }


  @Test
  @DisplayName("Fail to remove neighbor when not in list")
  void neighbors_RemoveNonExistentNeighbor_IllegalArgumentException() {
    assertThrows(SimulationException.class, () -> cell.removeNeighbor(neighbor));
  }

  @Test
  @DisplayName("Remove null neighbor")
  void neighbors_RemoveNull_IllegalArgumentException() {
    assertThrows(SimulationException.class, () -> cell.removeNeighbor(null));
  }



}
