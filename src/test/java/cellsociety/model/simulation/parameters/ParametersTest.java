package cellsociety.model.simulation.parameters;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParametersTest {

  private Parameters parameters;

  static class TestParameters extends Parameters {
    public TestParameters() {
      super();
    }
    public TestParameters(String language) {
      super(language);
    }
  }

  @BeforeEach
  void setUp() {
    parameters = new TestParameters();
  }

  @Test
  @DisplayName("Parameters initializes with default values")
  void parameters_Initialization_HasDefaultValues() {
    assertEquals(10.0, parameters.getParameter("maxHistorySize"));
  }

  @Test
  @DisplayName("Set and retrieve parameter values")
  void setAndGetParameter_ValidKey_SetsAndGetsCorrectly() {
    parameters.setParameter("testKey", 5.0);
    assertEquals(5.0, parameters.getParameter("testKey"));
  }

  @Test
  @DisplayName("Throws exception for null parameter map")
  void setParameters_NullMap_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> parameters.setParameters(null));
  }

  @Test
  @DisplayName("Set parameters updates correctly")
  void setParameters_ValidMap_UpdatesParameters() {
    Map<String, Double> newParams = new HashMap<>();
    newParams.put("speed", 2.5);
    parameters.setParameters(newParams);
    assertEquals(2.5, parameters.getParameter("speed"));
  }

  @Test
  @DisplayName("Throws exception for missing parameter key")
  void getParameter_MissingKey_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> parameters.getParameter("nonExistentKey"));
  }

  @Test
  @DisplayName("Returns correct parameter keys")
  void getParameterKeys_CheckKeys_ReturnsCorrectList() {
    parameters.setParameter("newParam", 7.0);
    List<String> keys = parameters.getParameterKeys();
    assertTrue(keys.contains("maxHistorySize"));
    assertTrue(keys.contains("newParam"));
  }

  @Test
  @DisplayName("Valid key check returns correct results")
  void isValidKey_CheckValidAndInvalidKeys_ReturnsCorrectBoolean() {
    assertTrue(parameters.isValidKey("maxHistorySize"));
    assertFalse(parameters.isValidKey("invalidKey"));
  }

  @Test
  @DisplayName("Get all parameters returns the correct map")
  void getParameters_ReturnsCorrectly_ReturnsCorrectMap() {
    Map<String, Double> params = parameters.getParameters();
    assertNotNull(params);
    assertEquals(10.0, params.get("maxHistorySize"));
  }

  @Test
  @DisplayName("Throws exception when getting parameter with null or empty key")
  void getParameter_NullOrEmptyKey_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> parameters.getParameter(null));
    assertThrows(SimulationException.class, () -> parameters.getParameter(""));
  }

  @Test
  @DisplayName("Throws exception when setting parameter with null or empty key")
  void setParameter_NullOrEmptyKey_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> parameters.setParameter(null, 5.0));
    assertThrows(SimulationException.class, () -> parameters.setParameter("", 5.0));
  }
}
