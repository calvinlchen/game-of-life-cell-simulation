package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.Cell;
import cellsociety.model.simulation.cell.PercolationCell;
import cellsociety.model.simulation.parameters.Parameters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test cell for testing
 */
class TestRuleCell extends Cell<TestRuleCell, TestRule, TestParameters> {

  public TestRuleCell(int state, TestRule rule) {
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
  protected TestRuleCell getSelf() {
    return null;
  }
}

/**
 * A minimal test parameters class for unit testing.
 *
 * @author chatgpt
 */
class TestParameters extends Parameters {

  public TestParameters() {
    super();
  }

  @Override
  public Map<String, Double> getParameters() {
    return new HashMap<>(); // Empty map, since this is for testing
  }

  @Override
  public void setParameters(Map<String, Double> parameters) {
    // Do nothing (for testing purposes)
  }

  @Override
  public double getParameter(String key) {
    return 0.0; // Return a default value
  }

  @Override
  public void setParameter(String key, double value) {
    // Do nothing (for testing purposes)
  }

  @Override
  public List<String> getParameterKeys() {
    return List.of(); // Return an empty list
  }
}

/**
 * Test rule for testing
 */
class TestRule extends Rule<TestRuleCell, TestParameters> {

  public TestRule(TestParameters parameters) {
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
    rule = new TestRule(new TestParameters());
  }

  // Positive Checks
  // TODO: fix rules for parameters

  @Test
  @DisplayName("Apply method returns expected state")
  void apply_Method_ReturnsExpectedState() {
    TestRuleCell cell = new TestRuleCell(1, rule);
    assertEquals(0, rule.apply(cell));
  }
}