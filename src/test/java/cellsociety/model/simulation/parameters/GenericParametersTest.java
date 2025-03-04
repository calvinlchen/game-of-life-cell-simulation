package cellsociety.model.simulation.parameters;

import static cellsociety.model.util.SimulationTypes.SimType.GameOfLife;
import static cellsociety.model.util.SimulationTypes.SimType.Langton;
import static cellsociety.model.util.SimulationTypes.SimType.RockPaperSciss;
import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.util.exceptions.SimulationException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Since GenericParameters is just an extension of Parameters, we can use it to test Parameters
 */
public class GenericParametersTest {

  @Nested
  @DisplayName("Tests for simulations with no special things")
  class ParametersTest {

    private GenericParameters parameters;

    @BeforeEach
    void setUp() {
      // since Langton does not have any special parameter rules, we will use this to test the default Lanton's
      parameters = new GenericParameters(Langton);
    }

    @Test
    @DisplayName("Parameter default constructor initializes MaxHistorySize to 10 and does not have any other parameters. Also tests positive behavior of getParameter() and getParameterKeys()")
    void parameters_DefaultConstructor_HasDefaultValues() {
      assertEquals(10.0, parameters.getParameter("maxHistorySize"));
      assertEquals(1, parameters.getParameterKeys().size());
    }

    @Test
    @DisplayName("setParameters correctly updates valid parameter pairs")
    void setParameters_ExistingKeyAndNewKey_CorrectlyUpdatesParameter() {
      parameters.setParameters(Map.of("maxHistorySize", 3.0, "newParam", 0.5));
      assertEquals(3.0, parameters.getParameter("maxHistorySize"));
      assertEquals(0.5, parameters.getParameter("newParam"));
      assertEquals(2, parameters.getParameterKeys().size());
    }

    @Test
    @DisplayName("SetParameters throws exception if newParams are null.")
    void setParameters_NullNewParams_ThrowsSimulationException() {
      assertThrows(SimulationException.class, () -> parameters.setParameters(null));
    }

    @Test
    @DisplayName("GetParameter throws exception if key is null.")
    void getParameter_NullKey_ThrowsSimulationException() {
      assertThrows(SimulationException.class, () -> parameters.getParameter(null));
    }

    @Test
    @DisplayName("GetParameter throws exception if parameters does not contain key.")
    void getParameter_NonExistentKey_ThrowsSimulationException() {
      assertThrows(SimulationException.class, () -> parameters.getParameter("nonExistentKey"));
    }

    @Test
    @DisplayName("SetParameter correctly updates valid existing parameter")
    void setParameter_ExistingKey_CorrectlyUpdatesParameter() {
      parameters.setParameter("maxHistorySize", 3.0);
      assertEquals(3.0, parameters.getParameter("maxHistorySize"));
      assertEquals(1, parameters.getParameterKeys().size());
    }

    @Test
    @DisplayName("SetParameter correctly updates new paramter")
    void setParameter_NewKey_CorrectlyUpdatesParameter() {
      parameters.setParameter("newParam", 0.5);
      assertEquals(0.5, parameters.getParameter("newParam"));
      assertEquals(2, parameters.getParameterKeys().size());
    }

    @Test
    @DisplayName("SetParameter throws exception if key is null.")
    void setParameter_NullKey_ThrowsSimulationException() {
      assertThrows(SimulationException.class, () -> parameters.setParameter(null, 0.5));
    }
  }

  @Nested
  @DisplayName("Tests for simulations with additional values")
  class AdditionalValuesParametersTest {

    private GenericParameters parameters;

    @BeforeEach
    void setUp() {
      parameters = new GenericParameters(GameOfLife);
    }

    @Test
    @DisplayName("GenericParameter properly initializes values for game of life. "
        + "Also tests positive behavior of getAdditionalParameter and getAdditionalParameterKeys")
    void genericParameter_GameOfLife_HasDefaultAdditionalValues() {
      assertTrue(parameters.getAdditionalParameter("S", List.class).isPresent());
      assertEquals(List.of(2, 3), parameters.getAdditionalParameter("S", List.class).get());
      assertEquals(2, parameters.getAdditionalParameterKeys().size());
    }

    @Test
    @DisplayName("SetAdditionalParameter correctly updates existing keys")
    void setAdditionalParameters_ExistingKey_CorrectlyUpadatesParameter() {
      parameters.setAdditionalParameter("S", "new value");
      assertTrue(parameters.getAdditionalParameter("S", String.class).isPresent());
      assertEquals("new value", parameters.getAdditionalParameter("S", String.class).get());
      assertEquals(2, parameters.getAdditionalParameterKeys().size());
    }

    @Test
    @DisplayName("SetAdditionalParameter correctly updates new keys")
    void setAdditionalParameters_NewKey_CorrectlyUpadatesParameter() {
      parameters.setAdditionalParameter("newParam", "hello");
      assertTrue(parameters.getAdditionalParameter("newParam", String.class).isPresent());
      assertEquals("hello", parameters.getAdditionalParameter("newParam", String.class).get());
      assertEquals(3, parameters.getAdditionalParameterKeys().size());
    }

    @Test
    @DisplayName("GetAdditionalParameter correctly returns Optional empty for non existent key")
    void getAdditionalParameter_NonExistentKey_ReturnEmptyOptional() {
      assertFalse(parameters.getAdditionalParameter("nonExistentKey", String.class).isPresent());
    }

    @Test
    @DisplayName("GetAdditionalParameter correctly returns Optional empty for parameter of wrong type")
    void getAdditionalParameter_WrongType_ReturnEmptyOptional() {
      assertFalse(parameters.getAdditionalParameter("S", String.class).isPresent());
    }
  }

  @Nested
  @DisplayName("Tests for simulations with normal double default parameters")
  class DefaultParametersAndUnmodifiableParametersTest {

    private GenericParameters parameters;

    @BeforeEach
    void setUp() {
      parameters = new GenericParameters(RockPaperSciss);
    }

    @Test
    @DisplayName("GenericParameters sets the default parameters")
    void genericParameters_DefaultParameters_CorrectlySetsDefaultParameters() {
      assertEquals(0.5, parameters.getParameter("percentageToWin"));
      assertEquals(3, parameters.getParameterKeys().size());
    }

    @Test
    @DisplayName("setParameter throws exception if modifying an unmodifiable param."
        + "Also checks that GenericParameter correctly set up unmodifiable params")
    void setParameter_UnmodifiableParameterKey_ThrowsException() {
      assertThrows(SimulationException.class, () -> parameters.setParameter("numStates", 6.0));
      assertEquals(3.0, parameters.getParameter("numStates"));
    }
  }

  @Test
  @DisplayName("GenericParameters bypass unmodifiableparam through initalization")
  void genericParameters_UnmodifiableParameterKey_BypassesUnmodifiableParameter() {
    GenericParameters parameters = new GenericParameters(RockPaperSciss, Map.of("numStates", 10.0));
    assertEquals(10.0, parameters.getParameter("numStates"));
  }
}
