package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RuleTest {

  private TestRule rule;
  private TestRuleCell mockCell;
  private TestParameters mockParameters;

  static class TestRuleCell extends Cell<TestRuleCell, TestRule, TestParameters> {
    public TestRuleCell(int state, TestRule rule) {
      super(state, rule);
    }

    @Override
    protected TestRuleCell getSelf() {
      return this;
    }
  }

  static class TestParameters extends Parameters {
    @Override
    public Map<String, Double> getParameters() {
      return Map.of("dummy", 1.0);
    }
  }

  static class TestRule extends Rule<TestRuleCell, TestParameters> {
    public TestRule(TestParameters parameters) {
      super(parameters);
    }

    @Override
    public int apply(TestRuleCell cell) {
      return 1; // Dummy implementation
    }
  }

  @BeforeEach
  void setUp() {
    mockParameters = Mockito.mock(TestParameters.class);
    mockCell = Mockito.mock(TestRuleCell.class);
    rule = new TestRule(mockParameters);

    when(mockParameters.getParameter("dummy")).thenReturn(1.0);
  }

  @Test
  @DisplayName("Throws SimulationException when parameters are null")
  void constructor_NullParameters_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> new TestRule(null));
  }

  @Test
  @DisplayName("Apply method returns expected state")
  void apply_TestMethodCorrectness_ReturnsExpectedState() {
    assertEquals(1, rule.apply(mockCell));
  }

  @Test
  @DisplayName("Get parameters returns correct parameter object")
  void getParameters_TestMethodCorrectness_ReturnsCorrectParameters() {
    assertEquals(mockParameters, rule.getParameters());
  }


  @Test
  @DisplayName("Matches direction correctly identifies neighboring cells")
  void matchesDirection_TestMethodCorrectness_IdentifiesNeighborsCorrectly() {
    TestRuleCell cell1 = Mockito.mock(TestRuleCell.class);
    TestRuleCell cell2 = Mockito.mock(TestRuleCell.class);

    when(cell1.getPosition()).thenReturn(new int[]{2, 2});
    when(cell2.getPosition()).thenReturn(new int[]{2, 2});

    assertFalse(rule.matchesDirection(cell1, cell2, "S"));
    when(cell2.getPosition()).thenReturn(new int[]{2, 3});
    assertTrue(rule.matchesDirection(cell1, cell2, "S"));

    assertFalse(rule.matchesDirection(cell1, cell2, "N"));
    when(cell2.getPosition()).thenReturn(new int[]{2, 1});
    assertTrue(rule.matchesDirection(cell1, cell2, "N"));

    assertFalse(rule.matchesDirection(cell1, cell2, "W"));
    when(cell2.getPosition()).thenReturn(new int[]{1, 2});
    assertTrue(rule.matchesDirection(cell1, cell2, "W"));

    assertFalse(rule.matchesDirection(cell1, cell2, "E"));
    when(cell2.getPosition()).thenReturn(new int[]{3, 2});
    assertTrue(rule.matchesDirection(cell1, cell2, "E"));

    assertFalse(rule.matchesDirection(cell1, cell2, "SE"));
    when(cell2.getPosition()).thenReturn(new int[]{3, 3});
    assertTrue(rule.matchesDirection(cell1, cell2, "SE"));

    assertFalse(rule.matchesDirection(cell1, cell2, "NW"));
    when(cell2.getPosition()).thenReturn(new int[]{1, 1});
    assertTrue(rule.matchesDirection(cell1, cell2, "NW"));

    assertFalse(rule.matchesDirection(cell1, cell2, "NE"));
    when(cell2.getPosition()).thenReturn(new int[]{3, 1});
    assertTrue(rule.matchesDirection(cell1, cell2, "NE"));

    assertFalse(rule.matchesDirection(cell1, cell2, "SW"));
    when(cell2.getPosition()).thenReturn(new int[]{1, 3});
    assertTrue(rule.matchesDirection(cell1, cell2, "SW"));

    assertFalse(rule.matchesDirection(cell1, cell2, "S"));
    when(cell2.getPosition()).thenReturn(new int[]{4, 4});
    assertFalse(rule.matchesDirection(cell1, cell2, "S"));
  }

  @Test
  @DisplayName("Throws SimulationException for invalid direction")
  void matchesDirection_InvalidDirection_ThrowsSimulationException() {
    TestRuleCell cell1 = Mockito.mock(TestRuleCell.class);
    TestRuleCell cell2 = Mockito.mock(TestRuleCell.class);

    when(cell1.getPosition()).thenReturn(new int[]{2, 2});
    when(cell2.getPosition()).thenReturn(new int[]{2, 3});

    assertThrows(SimulationException.class, () -> rule.matchesDirection(cell1, cell2, "X"));
  }

  @Test
  @DisplayName("Throws SimulationException if neighbor is null")
  void matchesDirection_NullNeighbor_ThrowsSimulationException() {
    TestRuleCell cell1 = Mockito.mock(TestRuleCell.class);

    assertThrows(SimulationException.class, () -> rule.matchesDirection(cell1, null, "E"));
  }

  @Test
  @DisplayName("Throws SimulationException if cell is null")
  void matchesDirection_NullCell_ThrowsSimulationException() {
    TestRuleCell cell1 = Mockito.mock(TestRuleCell.class);
    assertThrows(SimulationException.class, () -> rule.matchesDirection(null, cell1, "E"));
  }

  @Test
  @DisplayName("ThrowsSimulationException if cell or neighbor position is null")
  void matchesDirection_NullPosition_ThrowsSimulationException() {
    TestRuleCell cell1 = Mockito.mock(TestRuleCell.class);
    TestRuleCell cell2 = Mockito.mock(TestRuleCell.class);

    when(cell1.getPosition()).thenReturn(new int[]{2, 2});
    when(cell2.getPosition()).thenReturn(null);
    assertThrows(SimulationException.class, () -> rule.matchesDirection(cell1, cell2, "E"));

  }

  @Test
  @DisplayName("Correctly generates state key for full NESW neighbors")
  void getStateKey_FullNESWNeighbors_CorrectStateKey() {
    TestRuleCell cell = Mockito.mock(TestRuleCell.class);
    TestRuleCell north = Mockito.mock(TestRuleCell.class);
    TestRuleCell east = Mockito.mock(TestRuleCell.class);
    TestRuleCell south = Mockito.mock(TestRuleCell.class);
    TestRuleCell west = Mockito.mock(TestRuleCell.class);

    when(cell.getPosition()).thenReturn(new int[]{2, 2});
    when(cell.getCurrentState()).thenReturn(1);
    when(north.getPosition()).thenReturn(new int[]{2, 1});
    when(north.getCurrentState()).thenReturn(2);
    when(east.getPosition()).thenReturn(new int[]{3, 2});
    when(east.getCurrentState()).thenReturn(3);
    when(south.getPosition()).thenReturn(new int[]{2, 3});
    when(south.getCurrentState()).thenReturn(4);
    when(west.getPosition()).thenReturn(new int[]{1, 2});
    when(west.getCurrentState()).thenReturn(5);
    when(cell.getNeighbors()).thenReturn(List.of(north, east, south, west));

    String stateKey = rule.getStateKey(cell, new String[]{"N", "E", "S", "W"});
    assertEquals("12345", stateKey);
  }

  @Test
  @DisplayName("Correctly generates state key for NE neighbor only")
  void getStateKey_NENeighborOnly_CorrectStateKey() {
    TestRuleCell cell = Mockito.mock(TestRuleCell.class);
    TestRuleCell north = Mockito.mock(TestRuleCell.class);
    TestRuleCell east = Mockito.mock(TestRuleCell.class);
    TestRuleCell south = Mockito.mock(TestRuleCell.class);
    TestRuleCell west = Mockito.mock(TestRuleCell.class);

    when(cell.getPosition()).thenReturn(new int[]{2, 2});
    when(cell.getCurrentState()).thenReturn(1);
    when(north.getPosition()).thenReturn(new int[]{2, 1});
    when(north.getCurrentState()).thenReturn(2);
    when(east.getPosition()).thenReturn(new int[]{3, 2});
    when(east.getCurrentState()).thenReturn(3);
    when(south.getPosition()).thenReturn(new int[]{2, 3});
    when(south.getCurrentState()).thenReturn(4);
    when(west.getPosition()).thenReturn(new int[]{1, 2});
    when(west.getCurrentState()).thenReturn(5);
    when(cell.getNeighbors()).thenReturn(List.of(north, east));

    String stateKey = rule.getStateKey(cell, new String[]{"N", "E", "S", "W"});
    assertEquals("123", stateKey);
  }
}
