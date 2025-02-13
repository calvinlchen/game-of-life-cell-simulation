package cellsociety.model.interfaces;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

enum RuleTestState {ALIVE, DEAD;}   // testing enum for states

/**
 * Test cell for testing
 */
class TestRuleCell extends Cell<TestRuleCell> {

  public TestRuleCell(int state) {
    super(state);
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
 * Test rule for testing
 */
class TestRule extends Rule<TestRuleCell> {

  public TestRule(Map<String, Double> parameters) {
    super(parameters);
  }

  @Override
  public int apply(TestRuleCell cell) {
    return 0;
  }
}

/**
 * Tester for cell interface
 */
class RuleTest {
  private TestRule rule;
  private Map<String, Double> validParameters;

  @BeforeEach
  void setUp() {
    validParameters = new HashMap<>();
    validParameters.put("threshold", 0.5);
    rule = new TestRule(validParameters);
  }

  // Positive Checks
  @Test
  @DisplayName("Constructor initializes parameters correctly")
  void constructor_ValidParameters_InitializedCorrectly() {
    assertEquals(validParameters, rule.getParameters());
  }

  @Test
  @DisplayName("Get and set parameters correctly")
  void parameters_SetAndGet_Verified() {
    Map<String, Double> newParameters = new HashMap<>();
    newParameters.put("newThreshold", 0.8);
    rule.setParameters(newParameters);
    assertEquals(newParameters, rule.getParameters());
  }

  @Test
  @DisplayName("Apply method returns expected state")
  void apply_Method_ReturnsExpectedState() {
    TestRuleCell cell = new TestRuleCell(1);
    assertEquals(0, rule.apply(cell));
  }
}