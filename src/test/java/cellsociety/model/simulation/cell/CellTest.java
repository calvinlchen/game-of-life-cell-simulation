package cellsociety.model.simulation.cell;

import static cellsociety.model.util.SimulationTypes.SimType.Langton;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.simulation.rules.LangtonRule;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CellTest {

  private GenericParameters parameters;
  private LangtonRule rule;
  private LangtonCell cell;

  @BeforeEach
  void setUp() {
    // for simplicity just going to use Langton's as it does not override any of the rules method beyond the abstract one
    parameters = new GenericParameters(Langton);
    rule = new LangtonRule(parameters);
    cell = new LangtonCell(0, rule);
  }

  @Test
  @DisplayName("Cell constructor properly initializes defaults. "
      + "Also does positive checks on several getters, set states, and step back")
  void cell_DefaultConstructor_HasDefaultValues() {
    assertEquals(1, cell.getStateLength());
    assertTrue(cell.getNeighbors().isEmpty());
    assertTrue(cell.getDirectionalNeighbors(DirectionType.N).isEmpty());

    cell.setCurrentState(1);
    cell.setNextState(1);
    cell.stepBack();
    assertEquals(0, cell.getCurrentState());
    assertEquals(0, cell.getNextState());
  }

  @Test
  @DisplayName("Cell constructor throws exception if rules are null")
  void cell_NullRules_ThrowsException() {
    assertThrows(SimulationException.class, () -> new LangtonCell(0, null));
  }

  @Test
  @DisplayName("Cell constructor throws exception if state is invalid")
  void cell_InvalidState_ThrowsException() {
    assertThrows(SimulationException.class, () -> new LangtonCell(99, rule));
  }

  @Test
  @DisplayName("SaveCurrentState throws exception if maxHistory size is invalid")
  void saveCurrentState_InvalidMaxHistorySize_ThrowsException() {
    cell.getRule().getParameters().setParameter("maxHistorySize", -1.0);
    assertThrows(SimulationException.class, () -> cell.saveCurrentState());
  }

  @Test
  @DisplayName("SaveCurrentState correctly removes additional items if over maxHistory")
  void saveCurrentState_OverMaxHistory_RemovesEarliest() {
    cell.getRule().getParameters().setParameter("maxHistorySize", 2.0);

    // by default saves 0
    cell.setCurrentState(1);
    cell.saveCurrentState(); // 0 -> 1

    cell.setCurrentState(2);
    cell.saveCurrentState(); // 1 -> 2

    cell.setCurrentState(3);

    cell.stepBack();
    assertEquals(2, cell.getCurrentState());
    cell.stepBack();
    assertEquals(1, cell.getCurrentState());
    cell.stepBack();
    assertEquals(1, cell.getCurrentState());
  }

  @Test
  @DisplayName("CalcNextState throws error if any of the internal steps has a simulation error")
  void calcNextState_RuleApplyThrowsSimulationError_ThrowsException() {
    assertThrows(SimulationException.class, () -> cell.calcNextState() );
  }

  @Test
  @DisplayName("Step correctly updates new state length and current state if valid next state")
  void step_ValidNextState_UpdatesStateLengthAndCurrentState() {
    cell.setNextState(2);
    assertEquals(1, cell.getStateLength());
    assertEquals(0, cell.getCurrentState());

    cell.step();
    assertEquals(1, cell.getStateLength());
    assertEquals(2, cell.getCurrentState());
  }

  @Test
  @DisplayName("UpdateStateLength correctly increases statelenght if state remains the same")
  void updateStateLength_SameState_IncreasesStateLength() {
    cell.setNextState(0);
    assertEquals(1, cell.getStateLength());

    cell.updateStateLength();
    assertEquals(2, cell.getStateLength());
  }

  @Test
  @DisplayName("SetPosition correctly sets position for valid position. "
      + "Also checks for positive getPosition functionality.")
  void setPosition_ValidPosition_SetsPositionCorrectly() {
    int[] position = {1, 2};
    cell.setPosition(position);
    assertEquals(position[0], cell.getPosition()[0]);
    assertEquals(position[1], cell.getPosition()[1]);
  }

  @Test
  @DisplayName("SetPosition throws exception if passed in position is null")
  void setPosition_NullPosition_ThrowsException() {
    assertThrows(SimulationException.class, () -> cell.setPosition(null));
  }

  @Test
  @DisplayName("SetPosition throws exception if passed in position is in the wrong dimension")
  void setPosition_WrongDimension_ThrowsException() {
    int[] position = {1, 2, 3};
    assertThrows(SimulationException.class, () -> cell.setPosition(position));
  }

  @Test
  @DisplayName("SetNeighbors throws exception if attempts to set neighbors to null")
  void setNeighbors_NullNeighbors_ThrowsException() {
    assertThrows(SimulationException.class, () -> cell.setNeighbors(null));
  }

  @Test
  @DisplayName("SetDirectionalNeighbors throws exception if attempts to set directional neighbors to null")
  void setDirectionalNeighbors_NullDirectionalNeighbors_ThrowsException() {
    assertThrows(SimulationException.class, () -> cell.setDirectionalNeighbors(null));
  }

  @Test
  @DisplayName("ClearNeighbors properly clears all neighbors and directional neighbors")
  void clearNeighbors_ClearsAllNeighbors_ClearsNeighbors() {
    cell.setNeighbors(List.of(new LangtonCell(0, rule)));
    cell.setDirectionalNeighbors(Map.of(DirectionType.N, List.of(new LangtonCell(1, rule))));

    cell.clearNeighbors();

    assertTrue(cell.getNeighbors().isEmpty());
    assertTrue(cell.getDirectionalNeighbors(DirectionType.N).isEmpty());
  }

  @Nested
  @DisplayName("Tests for calculating steps with mocked rules")
  class CellTestMockRules {

    private GenericParameters parameters;
    private LangtonRule rule;
    private LangtonCell cell;

    @BeforeEach
    void setUp() {
      // for simplicity just going to use Langton's as it does not override any of the rules method beyond the abstract one
      parameters = new GenericParameters(Langton);
      rule = mock(LangtonRule.class);
      when(rule.getParameters()).thenReturn(parameters);
      cell = new LangtonCell(0, rule);
    }

    // ChatGpt helped get the initial mocking structure to be sert up
    @Test
    @DisplayName("calcNextState correctly sets next state based on rule.apply() result. "
        + "This uses nonoverriden shouldSkipCalculation and postProcessNextState")
    void calcNextState_DefaultWithCorrectStateFromRule_SetsNextStateCorrectly() {
      // Mock rule.apply() to return 2
      when(rule.apply(any(LangtonCell.class))).thenReturn(2);

      // Call method under test
      cell.calcNextState();

      // Assert that the next state was updated
      assertEquals(2, cell.getNextState());
    }

    @Test
    @DisplayName("CalcNextState throws exception because next state is invalid")
    void calcNextState_WithInvalidStateFromRule_ThrowsException () {
      when(rule.apply(any(LangtonCell.class))).thenReturn(-1);

      assertThrows(SimulationException.class, () -> cell.calcNextState());
    }
  }
}
