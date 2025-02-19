package cellsociety.model.factories;

import cellsociety.model.simulation.parameters.Parameters;
import cellsociety.model.simulation.rules.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RuleFactoryTest {

    private Map<String, Double> validParameters;
    private Map<String, Double> emptyParameters;

    @BeforeEach
    void setUp() {
        validParameters = new HashMap<>();
        validParameters.put("param1", 1.0);
        validParameters.put("param2", 2.0);

        emptyParameters = new HashMap<>();
    }

    @Test
    @DisplayName("Test Create Rule Function_Valid Parameters")
    void testCreateRuleValid() {
        // Arrange
        String ruleType = "TestRule"; // Assuming "TestRule" and "TestParameters" classes exist
        Parameters mockParameters = mock(Parameters.class);
        Rule<?, ?> mockRule = mock(Rule.class);

        // Mock the behavior of the classes
        when(mockParameters.setParameters(validParameters)).thenReturn(mockParameters);
        when(mockRule.getClass().getConstructor(Parameters.class)).thenReturn(mockRule);

        // Act
        Rule<?, ?> rule = RuleFactory.createRule(ruleType, validParameters);

        // Assert
        assertNotNull(rule);
        verify(mockParameters).setParameters(validParameters);
    }

    @Test
    @DisplayName("Test Create Rule Function_Invalid Rule Type")
    void testCreateRuleInvalidRuleType() {
        // Arrange
        String invalidRuleType = "InvalidRuleType";

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            RuleFactory.createRule(invalidRuleType, validParameters);
        });

        assertTrue(exception.getMessage().contains("Error creating rule: " + invalidRuleType));
    }

    @Test
    @DisplayName("Test Create Rule Function_Empty Parameters")
    void testCreateRuleEmptyParameters() {
        // Arrange
        String ruleType = "TestRule"; // Assuming "TestRule" and "TestParameters" classes exist

        // Act
        Rule<?, ?> rule = RuleFactory.createRule(ruleType, emptyParameters);

        // Assert
        assertNotNull(rule);
    }

    @Test
    @DisplayName("Test Create Rule Function_Null Parameters")
    void testCreateRuleNullParameters() {
        // Arrange
        String ruleType = "TestRule"; // Assuming "TestRule" and "TestParameters" classes exist

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> {
            RuleFactory.createRule(ruleType, null);
        });

        assertNotNull(exception);
    }
}