package cellsociety.model.simulation.rules;

import static cellsociety.model.util.SimulationTypes.SimType.Langton;
import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.simulation.cell.LangtonCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RuleTest {

  private GenericParameters parameters;
  private LangtonRule rule;

  @BeforeEach
  void setUp() {
    // for simplicity just going to use Langton's as it does not override any of the rules method beyond the abstract one
    parameters = new GenericParameters(Langton);
    rule = new LangtonRule(parameters);
  }

  @Test
  @DisplayName("Rule default constructor properly initializes parameters. "
      + "Additionally checks positive behavior of getParameters")
  void rule_DefaultConstructor_HasDefaultValues() {
    assertEquals(10.0, rule.getParameters().getParameter("maxHistorySize"));
  }

  @Test
  @DisplayName("Rule throws error if passed in parameters is null")
  void rule_NullParameters_ThrowsException() {
    assertThrows(SimulationException.class, () -> new LangtonRule(null));
  }

  @Test
  @DisplayName("GetStateKey correctly builds state key for valid inputs. "
      + "This also checks positive matchesDirection")
  void getStateKey_ValidInputs_ReturnsCorrectKey() {
    LangtonCell cell = new LangtonCell(0, rule);

    Map<DirectionType, List<LangtonCell>> directionalNeighbors = Map.of(
        DirectionType.N, List.of(new LangtonCell(1, rule)),
        DirectionType.E, List.of(new LangtonCell(2, rule)),
        DirectionType.W, List.of(new LangtonCell(3, rule)),
        DirectionType.SE, List.of(new LangtonCell(4, rule))
    );

    cell.setDirectionalNeighbors(directionalNeighbors);
    cell.setNeighbors((directionalNeighbors.values().stream().flatMap(List::stream).toList()));

    assertEquals("0123", rule.getStateKey(cell,
        new DirectionType[]{DirectionType.N, DirectionType.E, DirectionType.S, DirectionType.W}));
  }

  @Test
  @DisplayName("GetStateKey throws exception because cell is null")
  void getStateKey_NullCell_ThrowsException() {
    assertThrows(SimulationException.class, () -> rule.getStateKey(null,
        new DirectionType[]{DirectionType.N, DirectionType.E, DirectionType.S, DirectionType.W}));
  }

}
