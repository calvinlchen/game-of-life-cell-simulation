package cellsociety.model.simulation.parameters;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GameOfLifeParametersTest {

  private GameOfLifeParameters gameOfLifeParameters;

  @BeforeEach
  void setUp() {
    gameOfLifeParameters = new GameOfLifeParameters();
  }

  @Test
  @DisplayName("GameOfLifeParameters initializes with default rules")
  void gameOfLifeParameters_Initialization_HasDefaultRules() {
    assertEquals(List.of(2, 3), gameOfLifeParameters.getSurviveRules());
    assertEquals(List.of(3), gameOfLifeParameters.getBornRules());
  }

  @Test
  @DisplayName("Set and retrieve survive rules")
  void setAndGetSurviveRules_ValidRules_SetsAndGetsCorrectly() {
    List<Integer> newSurviveRules = Arrays.asList(1, 4);
    gameOfLifeParameters.setSurviveRules(newSurviveRules);
    assertEquals(newSurviveRules, gameOfLifeParameters.getSurviveRules());
  }

  @Test
  @DisplayName("Set and retrieve born rules")
  void setAndGetBornRules_ValidRules_SetsAndGetsCorrectly() {
    List<Integer> newBornRules = Arrays.asList(5, 6);
    gameOfLifeParameters.setBornRules(newBornRules);
    assertEquals(newBornRules, gameOfLifeParameters.getBornRules());
  }

  @Test
  @DisplayName("Throws exception for null survive rules")
  void setSurviveRules_NullRules_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> gameOfLifeParameters.setSurviveRules(null));
  }

  @Test
  @DisplayName("Throws exception for null born rules")
  void setBornRules_NullRules_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> gameOfLifeParameters.setBornRules(null));
  }

  @Test
  @DisplayName("Constructor initializes with custom rules")
  void gameOfLifeParameters_CustomInitialization_HasCorrectRules() {
    List<Integer> customSurvive = Arrays.asList(1, 3, 5);
    List<Integer> customBorn = Arrays.asList(4, 6);
    GameOfLifeParameters customParameters = new GameOfLifeParameters(customSurvive, customBorn);
    assertEquals(customSurvive, customParameters.getSurviveRules());
    assertEquals(customBorn, customParameters.getBornRules());
  }

  @Test
  @DisplayName("Constructor throws exception for null survive rules")
  void gameOfLifeParameters_NullSurviveRules_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> new GameOfLifeParameters(null, List.of(3)));
  }

  @Test
  @DisplayName("Constructor throws exception for null born rules")
  void gameOfLifeParameters_NullBornRules_ThrowsSimulationException() {
    assertThrows(SimulationException.class, () -> new GameOfLifeParameters(List.of(2, 3), null));
  }
}
