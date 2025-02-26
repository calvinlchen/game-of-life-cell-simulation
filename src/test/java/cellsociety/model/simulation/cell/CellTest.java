package cellsociety.model.simulation.cell;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.simulation.rules.Rule;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class CellTest {

  private TestCell testCell;
  private TestRule mockRule;
  private Parameters mockParameters;

  // Mockito set up done with the help of chatGPT,
  // although edited to fit the actual generic structure
  static class TestCell extends Cell<TestCell, TestRule, Parameters> {

    public TestCell(int state, TestRule rule) {
      super(state, rule);
    }

    @Override
    protected TestCell getSelf() {
      return this;
    }
  }

  static class TestRule extends Rule<TestCell, Parameters> {

    public TestRule(Parameters parameters) {
      super(parameters);
    }

    @Override
    public int apply(TestCell cell) {
      return 0;
    }
  }


  @BeforeEach
  void setUp() {
    mockRule = Mockito.mock(TestRule.class);
    mockParameters = Mockito.mock(Parameters.class);
    when(mockRule.getParameters()).thenReturn(mockParameters);
    when(mockParameters.getParameter("maxHistorySize")).thenReturn(3.);

    testCell = new TestCell(1, mockRule);
  }

  @Test
  @DisplayName("Initial state is set correctly")
  void cell_TestInitializedState_ReturnInitialCorrectStates() {
    assertEquals(1, testCell.getCurrentState());
    assertEquals(1, testCell.getNextState());
    assertEquals(0, testCell.getNeighbors().size());

    testCell.stepBack();
    assertEquals(1, testCell.getCurrentState());
    assertEquals(1, testCell.getNextState());
  }

  @Test
  @DisplayName("Save current state saves current state")
  void saveCurrentState_TestSaveState_SaveCurrentState() {
    testCell.setCurrentState(3);

    testCell.stepBack();
    assertEquals(1, testCell.getCurrentState());
    assertEquals(1, testCell.getNextState());

    testCell.setCurrentState(3);
    testCell.saveCurrentState();
    testCell.setCurrentState(2);
    testCell.stepBack();
    assertEquals(3, testCell.getCurrentState());
    assertEquals(3, testCell.getNextState());
  }

  @Test
  @DisplayName("Save only up to history amount of steps")
  void saveCurrentState_TestSaveOnlyUpToHistoryAmountOfSteps_SaveLast3States() {
    // 1
    testCell.setCurrentState(2);
    testCell.saveCurrentState();  // 1 -> 2

    testCell.setCurrentState(3);
    testCell.saveCurrentState();  // 1 -> 2 -> 3

    testCell.setCurrentState(4);
    testCell.saveCurrentState();  // 2 -> 3 -> 4

    testCell.setCurrentState(5);

    testCell.stepBack();
    assertEquals(4, testCell.getCurrentState());
    assertEquals(4, testCell.getNextState());

    testCell.stepBack();
    assertEquals(3, testCell.getCurrentState());
    assertEquals(3, testCell.getNextState());

    testCell.stepBack();
    assertEquals(2, testCell.getCurrentState());
    assertEquals(2, testCell.getNextState());

    testCell.stepBack();
    assertEquals(2, testCell.getCurrentState());
    assertEquals(2, testCell.getNextState());
  }

  @Test
  @DisplayName("Throw error if maxHistorySize is invalid")
  void saveCurrentState_InvalidMaxHistorySize_ThrowSimulationException() {
    TestRule mockRule2 = Mockito.mock(TestRule.class);
    Parameters mockParameters2 = Mockito.mock(Parameters.class);
    when(mockRule2.getParameters()).thenReturn(mockParameters2);
    when(mockParameters2.getParameter("maxHistorySize")).thenReturn(-3.);

    assertThrows(SimulationException.class, () -> new TestCell(1, mockRule2));
  }

  @Test
  @DisplayName("Test calculate next state")
  void calcNextState_TestCalcNextState_SetNextStateToRuleApply() {
    assertEquals(1, testCell.getCurrentState());
    assertEquals(1, testCell.getNextState());

    testCell.calcNextState();

    assertEquals(1, testCell.getCurrentState());
    assertEquals(0, testCell.getNextState());
  }

  @Test
  @DisplayName("Test step updates to next step")
  void step_TestStep_SetCurrentStateToNextStep() {
    assertEquals(1, testCell.getCurrentState());
    assertEquals(1, testCell.getNextState());

    testCell.setNextState(2);
    assertEquals(1, testCell.getCurrentState());

    testCell.step();
    assertEquals(2, testCell.getCurrentState());
  }

  @Test
  @DisplayName("Test get position of cell returns current position")
  void getPosition_TestGetPosition_ReturnCurrentPosition() {
    int[] position = new int[]{1, 2};
    testCell.setPosition(position);
    int[] result = testCell.getPosition();
    assertEquals(position.length, result.length);
    assertEquals(position[0], result[0]);
    assertEquals(position[1], result[1]);
  }

  @Test
  @DisplayName("Get position throws error if cell does not have position")
  void getPosition_TestGetPosition_ThrowsSimulationExceptionIfNullPosition() {
    assertThrows(SimulationException.class, () -> testCell.getPosition());
  }

  @Test
  @DisplayName("Set position throws simulation exception if trying to set position to null")
  void setPosition_TestSetPosition_ThrowsSimulationExceptionIfNullPosition() {
    assertThrows(SimulationException.class, () -> testCell.setPosition(null));
  }

  @Test
  @DisplayName("Set position throws simulation exception if trying to set position to invalid length")
  void setPosition_TestSetPosition_ThrowsSimulationExceptionIfInvalidLength() {
    assertThrows(SimulationException.class, () -> testCell.setPosition(new int[]{1}));
  }

  @Test
  @DisplayName("Correctly returns neighbors")
  void getNeighbors_TestGetNeighbors_ReturnNeighbors() {
    assertEquals(0, testCell.getNeighbors().size());

    TestCell neighbor1 = new TestCell(2, mockRule);
    TestCell neighbor2 = new TestCell(3, mockRule);
    TestCell nonNeighbor = new TestCell(4, mockRule);

    testCell.addNeighbor(neighbor1);
    testCell.addNeighbor(neighbor2);

    assertEquals(2, testCell.getNeighbors().size());
    assertTrue(testCell.getNeighbors().contains(neighbor1));
    assertTrue(testCell.getNeighbors().contains(neighbor2));
    assertFalse(testCell.getNeighbors().contains(nonNeighbor));
  }

  @Test
  @DisplayName("Correctly can set neighbors")
  void setNeighbors_TestSetNeighbors_SetNeighbors() {
    TestCell neighbor1 = new TestCell(2, mockRule);
    TestCell neighbor2 = new TestCell(3, mockRule);
    TestCell nonNeighbor = new TestCell(4, mockRule);

    testCell.addNeighbor(neighbor1);
    testCell.addNeighbor(neighbor2);

    assertEquals(2, testCell.getNeighbors().size());
    assertTrue(testCell.getNeighbors().contains(neighbor1));
    assertTrue(testCell.getNeighbors().contains(neighbor2));
    assertFalse(testCell.getNeighbors().contains(nonNeighbor));

    testCell.setNeighbors(List.of(nonNeighbor));
    assertEquals(1, testCell.getNeighbors().size());
    assertFalse(testCell.getNeighbors().contains(neighbor1));
    assertFalse(testCell.getNeighbors().contains(neighbor2));
    assertTrue(testCell.getNeighbors().contains(nonNeighbor));
  }

  @Test
  @DisplayName("Throws simulation exception if you try to set neighbors to null")
  void setNeighbors_TestIllegalParameters_ThrowsSimulationExceptionIfNullNeighbors() {
    assertThrows(SimulationException.class, () -> testCell.setNeighbors(null));
  }

  @Test
  @DisplayName("Throws simulation exception if you try to add null neighbor")
  void addNeighbor_TestAddIllegalNeighbor_ThrowsSimulationExceptionIfNullNeighbor() {
    assertThrows(SimulationException.class, () -> testCell.addNeighbor(null));
  }

  @Test
  @DisplayName("Throws simulation exception if you try to add a neighbor already in the list")
  void addNeighbor_TestAddIllegalNeighbor_ThrowsSimulationExceptionIfNeighborAlreadyInList() {
    TestCell neighbor = new TestCell(1, mockRule);
    testCell.addNeighbor(neighbor);
    assertThrows(SimulationException.class, () -> testCell.addNeighbor(neighbor));
  }

  @Test
  @DisplayName("Test that remove neighbors properly removes neighbors")
  void removeNeighbors_TestRemoveNeighbors_RemoveNeighbor2() {
    TestCell neighbor1 = new TestCell(2, mockRule);
    TestCell neighbor2 = new TestCell(3, mockRule);

    testCell.addNeighbor(neighbor1);
    testCell.addNeighbor(neighbor2);

    assertEquals(2, testCell.getNeighbors().size());
    assertTrue(testCell.getNeighbors().contains(neighbor1));
    assertTrue(testCell.getNeighbors().contains(neighbor2));

    testCell.removeNeighbor(neighbor2);
    assertEquals(1, testCell.getNeighbors().size());
    assertTrue(testCell.getNeighbors().contains(neighbor1));
    assertFalse(testCell.getNeighbors().contains(neighbor2));
  }

  @Test
  @DisplayName("Throws Simulation Exception if try to remove cell that is not a neighbor")
  void removeNeighbor_TestRemoveNeighborInvalidParameter_ThrowsSimulationExceptionIfCellIsNotANeighbor() {
    assertThrows(SimulationException.class,
        () -> testCell.removeNeighbor(new TestCell(1, mockRule)));
  }

  @Test
  @DisplayName("Returns rule correctly")
  void getRule_TestGetRule_ReturnRule() {
    assertEquals(mockRule, testCell.getRule());
  }

  @Test
  @DisplayName("No error if passed in state is valid")
  void validateState_TestIsValidState_NoError() {
    testCell.validateState(2, 3);
  }

  @Test
  @DisplayName("error if passed in state is negative")
  void validateState_TestIsNegativeState_ThrowSimulationException() {
    assertThrows(SimulationException.class, () -> testCell.validateState(-1, 3));
  }

  @Test
  @DisplayName("error if passed in cell is greater than max")
  void validateState_TestIsGreaterMaxState_ThrowSimulationException() {
    assertThrows(SimulationException.class, () -> testCell.validateState(1, 1));
  }
}
