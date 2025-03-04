package cellsociety.model.simulation.rules;

import static cellsociety.model.util.SimulationTypes.SimType.FallingSand;
import static cellsociety.model.util.SimulationTypes.SimType.Fire;
import static cellsociety.model.util.SimulationTypes.SimType.GameOfLife;
import static cellsociety.model.util.SimulationTypes.SimType.Langton;
import static cellsociety.model.util.SimulationTypes.SimType.Percolation;
import static cellsociety.model.util.SimulationTypes.SimType.Petelka;
import static cellsociety.model.util.SimulationTypes.SimType.RockPaperSciss;
import static cellsociety.model.util.SimulationTypes.SimType.Segregation;
import static cellsociety.model.util.SimulationTypes.SimType.WaTor;
import static cellsociety.model.util.constants.CellStates.FIRE_BURNING;
import static cellsociety.model.util.constants.CellStates.PERCOLATION_BLOCKED;
import static cellsociety.model.util.constants.CellStates.PERCOLATION_OPEN;
import static cellsociety.model.util.constants.CellStates.PERCOLATION_PERCOLATED;
import static cellsociety.model.util.constants.CellStates.WATOR_EMPTY;
import static cellsociety.model.util.constants.CellStates.WATOR_FISH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import cellsociety.model.simulation.cell.FallingSandCell;
import cellsociety.model.simulation.cell.FireCell;
import cellsociety.model.simulation.cell.GameOfLifeCell;
import cellsociety.model.simulation.cell.LangtonCell;
import cellsociety.model.simulation.cell.PercolationCell;
import cellsociety.model.simulation.cell.RockPaperScissCell;
import cellsociety.model.simulation.cell.SegregationCell;
import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.simulation.rules.LangtonRule;
import cellsociety.model.simulation.rules.RockPaperScissRule;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.constants.GridTypes.EdgeType;
import cellsociety.model.util.constants.GridTypes.NeighborhoodType;
import cellsociety.model.util.constants.GridTypes.ShapeType;
import cellsociety.model.util.constants.exceptions.SimulationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

/**
 * Helper private methods for rule tests
 */
public class HelperMethodTest {

  // None for ChouReg2Rule, love that for me

  @Nested
  @DisplayName("Tests for just falling sand helper methods")
  class FallingSandRuleTests {

    private FallingSandRule rule;

    @BeforeEach
    void setUp() {
      GenericParameters parameters = new GenericParameters(FallingSand);
      rule = new FallingSandRule(parameters);
    }

    @Test
    @DisplayName("FindValidNeighbor finds valid neighbor when it exist")
    void findValidNeighbor_ValidNeighbor_ReturnsValidNeighbor() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      FallingSandCell neighbor = new FallingSandCell(1, rule);
      cell.setDirectionalNeighbors(Map.of(DirectionType.S, List.of(neighbor)));

      Optional<FallingSandCell> validNeighbor = rule.findValidNeighbor(cell, DirectionType.S, 1);
      assertTrue(validNeighbor.isPresent());
      assertEquals(neighbor, validNeighbor.get());
    }

    @Test
    @DisplayName("FindValidNeighbor finds valid neighbor when multiple valid and returns one")
    void findValidNeighbor_MultipleValidNeighbors_ReturnsOneValidNeighbor() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      List<FallingSandCell> neighbors = List.of(new FallingSandCell(1, rule),
          new FallingSandCell(1, rule));
      cell.setDirectionalNeighbors(Map.of(DirectionType.S, neighbors));

      Optional<FallingSandCell> validNeighbor = rule.findValidNeighbor(cell, DirectionType.S, 1);
      assertTrue(validNeighbor.isPresent());
      assertEquals(neighbors.getFirst(), validNeighbor.get());

    }

    @Test
    @DisplayName("FindValidNeighbor returns empty optional when no direction neighbors exist")
    void findValidNeighbor_NoNeighborsInDirection_ReturnsEmptyOptional() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      Optional<FallingSandCell> validNeighbor = rule.findValidNeighbor(cell, DirectionType.S, 1);
      assertFalse(validNeighbor.isPresent());
    }

    @Test
    @DisplayName("FindNeighbor returns empty optional when neighbor does not have currect current state")
    void findNeighbor_NeighborHasWrongCurrentState_ReturnsEmptyOptional() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      FallingSandCell neighbor = new FallingSandCell(1, rule);
      cell.setDirectionalNeighbors(Map.of(DirectionType.S, List.of(neighbor)));

      Optional<FallingSandCell> validNeighbor = rule.findValidNeighbor(cell, DirectionType.S, 2);
      assertFalse(validNeighbor.isPresent());
    }

    @Test
    @DisplayName("FindNeighbor returns empty optional when neighbor does not have correct next state")
    void findNeighbor_NeighborHasWrongNextState_ReturnsEmptyOptional() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      FallingSandCell neighbor = new FallingSandCell(1, rule);
      neighbor.setNextState(2);

      cell.setDirectionalNeighbors(Map.of(DirectionType.S, List.of(neighbor)));

      Optional<FallingSandCell> validNeighbor = rule.findValidNeighbor(cell, DirectionType.S, 1);
      assertFalse(validNeighbor.isPresent());
    }

    @Test
    @DisplayName("FindNeighbor returns correct neighbor when correct direction but one has correct state and one does not")
    void findNeighbor_CorrectDirectionButOneHasWrongState_ReturnsCorrectNeighbor() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      List<FallingSandCell> neighbors = List.of(new FallingSandCell(2, rule),
          new FallingSandCell(1, rule));
      cell.setDirectionalNeighbors(Map.of(DirectionType.S, neighbors));

      Optional<FallingSandCell> validNeighbor = rule.findValidNeighbor(cell, DirectionType.S, 1);
      assertTrue(validNeighbor.isPresent());
      assertEquals(neighbors.getLast(), validNeighbor.get());
    }

    @Test
    @DisplayName("AttemptMovement returns cells current state if no replaceable neighbor")
    void attemptMovement_NoReplaceableNeighbor_ReturnsCurrentState() {
      FallingSandCell cell = new FallingSandCell(0, rule);

      assertEquals(0, rule.attemptMovement(cell, List.of(), 1, List.of()));
    }

    @Test
    @DisplayName("AttemptMovement hits primary movement and returns the found replaceable neighbor")
    void attemptMovement_PrimaryMovement_ReturnsCorrectNeighbor() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      FallingSandCell neighbor = new FallingSandCell(1, rule);
      cell.setDirectionalNeighbors(Map.of(DirectionType.S, List.of(neighbor)));

      assertEquals(1, rule.attemptMovement(cell, List.of(), 2, List.of(0, 1)));
      assertEquals(2, neighbor.getNextState());
    }

    @Test
    @DisplayName("AttemptMovement prioritizes primary movement even if valid secondary neighbor")
    void attemptMovement_ValidPrimaryAndSecondaryMovement_ReturnsPrimaryMovement() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      FallingSandCell neighbor = new FallingSandCell(1, rule);
      FallingSandCell neighbor2 = new FallingSandCell(2, rule);
      cell.setDirectionalNeighbors(
          Map.of(DirectionType.S, List.of(neighbor), DirectionType.W, List.of(neighbor2)));

      assertEquals(1, rule.attemptMovement(cell, List.of(DirectionType.W), 3, List.of(1, 2)));
      assertEquals(3, neighbor.getNextState());
      assertEquals(2, neighbor2.getNextState());
    }

    @Test
    @DisplayName("AttemptMovement goes to valid secondary neighbor")
    void attemptMovement_ValidSecondaryMovement_ReturnsCorrectNeighbor() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      List<FallingSandCell> neighbors = List.of(new FallingSandCell(2, rule),
          new FallingSandCell(1, rule));
      cell.setDirectionalNeighbors(Map.of(DirectionType.W, neighbors));

      assertEquals(1, rule.attemptMovement(cell, List.of(DirectionType.W), 3, List.of(1)));
      assertEquals(2, neighbors.getFirst().getNextState());
    }

    @Test
    @DisplayName("AttemptMovement cycles through different replaceable neighbors and directions")
    void attemptMovement_OnlySecondReplaceableNeighborTypeValid_ReturnsCorrectNeighbor() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      FallingSandCell neighbor = new FallingSandCell(1, rule);
      FallingSandCell neighbor2 = new FallingSandCell(2, rule);
      cell.setDirectionalNeighbors(
          Map.of(DirectionType.E, List.of(neighbor), DirectionType.W, List.of(neighbor2)));

      assertEquals(2,
          rule.attemptMovement(cell, List.of(DirectionType.W, DirectionType.E), 3, List.of(3, 2)));
      assertEquals(1, neighbor.getNextState());
      assertEquals(3, neighbor2.getNextState());
    }

    @Test
    @DisplayName("AttemptMovement throws exception if you pass in an invalid new state")
    void attemptMovement_InvalidNewState_ThrowsException() {
      FallingSandCell cell = new FallingSandCell(0, rule);
      FallingSandCell neighbor = new FallingSandCell(1, rule);
      cell.setDirectionalNeighbors(Map.of(DirectionType.S, List.of(neighbor)));

      assertThrows(SimulationException.class,
          () -> rule.attemptMovement(cell, List.of(), -1, List.of(0, 1)));
    }
  }

  @Nested
  @DisplayName("Tests for just fire simulation helper methods")
  class FireRuleTests {

    private GenericParameters parameters;
    private FireRule rule;

    @BeforeEach
    void setUp() {
      parameters = new GenericParameters(Fire);
      rule = new FireRule(parameters);
    }

    @Test
    @DisplayName("HasBurningNeighbor returns true if neighbor is burning")
    void hasBurningNeighbor_BurningNeighbor_ReturnsTrue() {
      FireCell cell = new FireCell(0, rule);
      FireCell neighbor = new FireCell(FIRE_BURNING, rule);
      cell.setNeighbors(List.of(neighbor));

      assertTrue(rule.hasBurningNeighbor(cell));
    }

    @Test
    @DisplayName("HasBurningNeighbor returns true if neighbor is current burning even though not burning next")
    void hasBurningNeighbor_BurningNeighborButNotNextState_ReturnsTrue() {
      FireCell cell = new FireCell(0, rule);
      FireCell neighbor = new FireCell(FIRE_BURNING, rule);
      neighbor.setNextState(0);
      cell.setNeighbors(List.of(neighbor));

      assertTrue(rule.hasBurningNeighbor(cell));
    }

    @Test
    @DisplayName("HasBurnignNeighbor returns false if no neighbors are currently burning")
    void hasBurningNeighbor_NoBurningNeighbors_ReturnsFalse() {
      FireCell cell = new FireCell(0, rule);
      FireCell neighbor = new FireCell(1, rule);
      neighbor.setNextState(FIRE_BURNING);
      cell.setNeighbors(List.of(neighbor));

      assertFalse(rule.hasBurningNeighbor(cell));
    }


    @Test
    @DisplayName("EvaluateEmptyState throws exception if treeSpanLikelihood does not exist")
    void evaluateEmptyState_NoParameter_ThrowsException() {
      GenericParameters mockParameters = mock(GenericParameters.class);
      when(mockParameters.getParameter(any())).thenThrow(
          new SimulationException("ParameterNotFound", List.of("some key")));
      FireRule mockRule = new FireRule(mockParameters);

      FireCell cell = new FireCell(0, rule);

      assertThrows(SimulationException.class, () -> mockRule.evaluateEmptyState(cell));
    }

    @Test
    @DisplayName("EvaluateEmptyState return tree if random value is less than treeSpawnLikelihood")
    void evaluateEmptyState_LessThanTreeSpawnLikelihood_ReturnsTree() {
      FireCell cell = new FireCell(0, rule);
      parameters.setParameters(Map.of("treeSpawnLikelihood", 0.5));

      Random mockRandom = mock(Random.class);
      when(mockRandom.nextDouble()).thenReturn(0.3);

      rule.setRandom(mockRandom);

      assertEquals(1, rule.evaluateEmptyState(cell));
    }

    @Test
    @DisplayName("EvaluateEmptyState returns empty if random value is greater than treeSpawnLikelihood")
    void evaluateEmptyState_GreaterThanTreeSpawnLikelihood_ReturnsEmpty() {
      FireCell cell = new FireCell(0, rule);
      parameters.setParameters(Map.of("treeSpawnLikelihood", 0.5));

      Random mockRandom = mock(Random.class);
      when(mockRandom.nextDouble()).thenReturn(0.7);

      rule.setRandom(mockRandom);

      assertEquals(0, rule.evaluateEmptyState(cell));
    }

    @Test
    @DisplayName("EvaluateTreeState throws exception if treeSpanLikelihood does not exist")
    void evaluateTreeState_NoParameter_ThrowsException() {
      GenericParameters mockParameters = mock(GenericParameters.class);
      when(mockParameters.getParameter(any())).thenThrow(
          new SimulationException("ParameterNotFound", List.of("some key")));
      FireRule mockRule = new FireRule(mockParameters);

      FireCell cell = new FireCell(0, rule);

      assertThrows(SimulationException.class, () -> mockRule.evaluateTreeState(cell));
    }

    @Test
    @DisplayName("EvaluateTreeState return burning if random value is less than treeSpawnLikelihood")
    void evaluateTreeState_LessThanTreeSpawnLikelihood_ReturnsBurning() {
      FireCell cell = new FireCell(1, rule);
      parameters.setParameters(Map.of("ignitionLikelihood", 0.5));

      Random mockRandom = mock(Random.class);
      when(mockRandom.nextDouble()).thenReturn(0.3);

      rule.setRandom(mockRandom);

      assertEquals(2, rule.evaluateTreeState(cell));
    }

    @Test
    @DisplayName("EvaluateTreeState returns tree if random value is greater than treeSpawnLikelihood")
    void evaluateTreeState_GreaterThanTreeSpawnLikelihood_ReturnsTree() {
      FireCell cell = new FireCell(1, rule);
      parameters.setParameters(Map.of("ignitionLikelihood", 0.5));

      Random mockRandom = mock(Random.class);
      when(mockRandom.nextDouble()).thenReturn(0.7);

      rule.setRandom(mockRandom);

      assertEquals(1, rule.evaluateTreeState(cell));
    }

    @Test
    @DisplayName("EvaluateTreeState returns buring if any neighbors are burning")
    void evaluateTreeState_AnyNeighborsBurning_ReturnsBurning() {
      FireCell cell = new FireCell(1, rule);
      FireCell neighbor = new FireCell(FIRE_BURNING, rule);
      cell.setNeighbors(List.of(neighbor));

      parameters.setParameters(Map.of("ignitionLikelihood", 0.0));
      assertEquals(2, rule.evaluateTreeState(cell));
    }

  }

  @Nested
  @DisplayName("Tests for game of life helper methods")
  class GameOfLifeRuleTests {

    private GenericParameters parameters;
    private GameOfLifeRule rule;

    @BeforeEach
    void setUp() {
      parameters = new GenericParameters(GameOfLife);
      rule = new GameOfLifeRule(parameters);
    }

    @Test
    @DisplayName("RetrieveParameterList throws exception if parameter not a list of integers")
    void retrieveParameterList_NonIntegerListSurvivalParams_ThrowsException() {
      parameters.setAdditionalParameter("S", List.of("a", "b"));
      assertThrows(SimulationException.class, () -> rule.retrieveParameterList("S"));
    }

    @Test
    @DisplayName("RetrieveParameterList correctly returns list of integer parameters for a list of any type of number")
    void retrieveParameterList_IntegerListSurvivalParams_ReturnsCorrectList() {
      parameters.setAdditionalParameter("S", List.of(1., 2, 3.2));
      List<Integer> survivalParams = rule.retrieveParameterList("S");
      assertEquals(List.of(1, 2, 3), survivalParams);
    }

    @Test
    @DisplayName("CountAliveNeighbors correctly counts the number of alive neighbors a cell has")
    void countAliveNeighbors_CellHasTwoAliveNeighbors_ReturnsTwo() {
      GameOfLifeCell cell = new GameOfLifeCell(0, rule);
      cell.setNeighbors(List.of(new GameOfLifeCell(1, rule), new GameOfLifeCell(1, rule)));

      assertEquals(2, rule.countAliveNeighbors(cell));
    }

    @Test
    @DisplayName("CountAliveNeighbors correctly returns 0 if 0 alive neighbors")
    void countAliveNeighbors_NoAliveNeighbors_ReturnsZero() {
      GameOfLifeCell cell = new GameOfLifeCell(0, rule);
      assertEquals(0, rule.countAliveNeighbors(cell));
    }
  }

  @Nested
  @DisplayName("Tests for percolation helper methods")
  class PercolationTests {

    private PercolationRule rule;

    @BeforeEach
    void setUp() {
      GenericParameters parameters = new GenericParameters(Percolation);
      rule = new PercolationRule(parameters);
    }

    @Test
    @DisplayName("NeighborIsPercolated returns true if any neighbor is percolated")
    void neighborIsPercolated_AnyNeighborPercolated_ReturnsTrue() {
      PercolationCell cell = new PercolationCell(0, rule);

      cell.setNeighbors(List.of(new PercolationCell(PERCOLATION_BLOCKED, rule),
          new PercolationCell(PERCOLATION_BLOCKED, rule),
          new PercolationCell(PERCOLATION_BLOCKED, rule),
          new PercolationCell(PERCOLATION_PERCOLATED, rule)));

      assertTrue(rule.neighborIsPercolated(cell));
    }

    @Test
    @DisplayName("NeighborIsPercolated returns false if no neighbors are percolated ")
    void neighborIsPercolated_NoNeighborsPercolated_ReturnsFalse() {
      PercolationCell cell = new PercolationCell(0, rule);

      cell.setNeighbors(List.of(new PercolationCell(PERCOLATION_BLOCKED, rule),
          new PercolationCell(PERCOLATION_BLOCKED, rule),
          new PercolationCell(PERCOLATION_BLOCKED, rule),
          new PercolationCell(PERCOLATION_OPEN, rule)));

      assertFalse(rule.neighborIsPercolated(cell));
    }
  }

  // Petelka I going to check each ones helper methods in the actual thing, I'll just check
  // all reflect symmetries and then it'll work


  // RockPaperScissors has none
  @Nested
  @DisplayName("Tests for Segregation helper methods")
  class SegregationTests {

    private SegregationRule rule;

    @BeforeEach
    void setUp() {
      GenericParameters parameters = new GenericParameters(Segregation);
      rule = new SegregationRule(parameters);
    }

    @Test
    @DisplayName("FindAdjacentEmptyCell returns if valid empty neighbor that is empty and still empty")
    void findAdjacentEmptyCell_ValidEmptyNeighbor_ReturnsValidEmptyCell() {
      SegregationCell cell = new SegregationCell(0, rule);
      SegregationCell neighbor = new SegregationCell(0, rule);
      neighbor.setNextState(0);
      cell.setNeighbors(List.of(neighbor));

      Optional<SegregationCell> emptyCell = rule.findAdjacentEmptyCell(cell);
      assertTrue(emptyCell.isPresent());
      assertEquals(neighbor, emptyCell.get());
    }

    @Test
    @DisplayName("FindAdjacentEmptyCell returns empty if neighbor was empty but is no longer")
    void findAdjacentEmptyCell_NeighborWasEmptyButIsNowNotEmpty_ReturnsEmpty() {
      SegregationCell cell = new SegregationCell(0, rule);
      SegregationCell neighbor = new SegregationCell(0, rule);
      neighbor.setNextState(1);
      cell.setNeighbors(List.of(neighbor));

      Optional<SegregationCell> emptyCell = rule.findAdjacentEmptyCell(cell);
      assertFalse(emptyCell.isPresent());
    }

    @Test
    @DisplayName("FindAdjacentEmptyCell returns empty if neighbor not empty but is now empty")
    void findAdjacentEmptyCell_NeighborNotEmptyButIsNowEmpty_ReturnsEmpty() {
      SegregationCell cell = new SegregationCell(0, rule);
      SegregationCell neighbor = new SegregationCell(1, rule);
      neighbor.setNextState(0);
      cell.setNeighbors(List.of(neighbor));

      Optional<SegregationCell> emptyCell = rule.findAdjacentEmptyCell(cell);
      assertFalse(emptyCell.isPresent());
    }

    @Test
    @DisplayName("CalculateSimilarityRatio correctly calculates similarity ratio for AgentA cell")
    void calculateSimilarityRatio_AgentACell_ReturnsCorrectRatio() {
      SegregationCell cell = new SegregationCell(1, rule);
      // 2 empty, 2 match, 2 different should
      cell.setNeighbors(List.of(new SegregationCell(0, rule), new SegregationCell(0, rule),
          new SegregationCell(1, rule), new SegregationCell(1, rule),
          new SegregationCell(2, rule), new SegregationCell(2, rule)));

      assertEquals(.5, rule.calculateSimilarityRatio(cell, cell.getNeighbors()));
    }

    @Test
    @DisplayName("CalculateSimilarityRatio correctly calculates similarity ratio for AgentB cell")
    void calculateSimilarityRatio_AgentBCell_ReturnsCorrectRatio() {
      SegregationCell cell = new SegregationCell(2, rule);
      // 2 empty, 2 match, 2 different should
      cell.setNeighbors(List.of(new SegregationCell(0, rule), new SegregationCell(0, rule),
          new SegregationCell(1, rule), new SegregationCell(1, rule),
          new SegregationCell(2, rule), new SegregationCell(2, rule)));

      assertEquals(.5, rule.calculateSimilarityRatio(cell, cell.getNeighbors()));
    }

    @Test
    @DisplayName("IsSatisfied returns true if cell has no neighbors")
    void isSatisfied_NoNeighbors_ReturnsTrue() {
      SegregationCell cell = new SegregationCell(0, rule);

      assertTrue(rule.isSatisfied(cell, 1));
    }

    @Test
    @DisplayName("IsSatisfied returns true if cell has all empty neighbors")
    void isSatisfied_AllEmptyNeighbors_ReturnsTrue() {
      SegregationCell cell = new SegregationCell(1, rule);
      cell.setNeighbors(List.of(new SegregationCell(0, rule), new SegregationCell(0, rule),
          new SegregationCell(0, rule), new SegregationCell(0, rule)));

      assertTrue(rule.isSatisfied(cell, 1));
    }

    @Test
    @DisplayName("IsSatisfied returns true if cell similarity ratio is greater than or equal to threshold")
    void isSatisfied_SimilarityRatioGreaterThanThreshold_ReturnsTrue() {
      SegregationCell cell = new SegregationCell(2, rule);
      // 2 empty, 2 match, 2 different should
      cell.setNeighbors(List.of(new SegregationCell(0, rule), new SegregationCell(0, rule),
          new SegregationCell(1, rule), new SegregationCell(1, rule),
          new SegregationCell(2, rule), new SegregationCell(2, rule)));

      assertTrue(rule.isSatisfied(cell, .5));
    }

    @Test
    @DisplayName("IsSatisfied returns false if cell similarity ratio is less than threshold")
    void isSatisfied_SimilarityRatioLessThanThreshold_ReturnsFalse() {
      SegregationCell cell = new SegregationCell(2, rule);
      // 2 empty, 2 match, 2 different should
      cell.setNeighbors(List.of(new SegregationCell(0, rule), new SegregationCell(0, rule),
          new SegregationCell(1, rule), new SegregationCell(1, rule),
          new SegregationCell(2, rule), new SegregationCell(2, rule)));

      assertFalse(rule.isSatisfied(cell, .51));
    }

  }

  @Nested
  @DisplayName("Tests for WaTor helper methods")
  class WaTorTests {

    private WaTorRule rule;

    @BeforeEach
    void setUp() {
      GenericParameters parameters = new GenericParameters(WaTor);
      rule = new WaTorRule(parameters);
    }

    @Test
    @DisplayName("FindFishCell returns fish neighbor if fish is currently and still fish")
    void findFishCell_CurrentAndNextStateFishNeighbor_ReturnsNeighborFish() {
      WaTorCell cell = new WaTorCell(0, rule);
      WaTorCell neighbor = new WaTorCell(WATOR_FISH, rule);
      cell.setNeighbors(List.of(neighbor));

      Optional<WaTorCell> fishCell = rule.findFishCell(cell);
      assertTrue(fishCell.isPresent());
      assertEquals(neighbor, fishCell.get());
    }

    @Test
    @DisplayName("FindFishCell returns fish neighbor if fish was fish even though no longer is")
    void findFishCell_WasFishButIsNowEmpty_ReturnsNeighborFish() {
      WaTorCell cell = new WaTorCell(0, rule);
      WaTorCell neighbor = new WaTorCell(WATOR_FISH, rule);
      neighbor.setNextState(0);
      cell.setNeighbors(List.of(neighbor));

      Optional<WaTorCell> fishCell = rule.findFishCell(cell);
      assertTrue(fishCell.isPresent());
      assertEquals(neighbor, fishCell.get());
    }

    @Test
    @DisplayName("FindFishCell empty false if no neighbors that was fish")
    void findFishCell_NoNeighborsWithCurrentFishState_ReturnsEmpty() {
      WaTorCell cell = new WaTorCell(0, rule);
      WaTorCell neighbor = new WaTorCell(0, rule);
      neighbor.setNextState(WATOR_FISH);
      cell.setNeighbors(List.of(neighbor));

      Optional<WaTorCell> fishCell = rule.findFishCell(cell);
      assertFalse(fishCell.isPresent());
    }

    @Test
    @DisplayName("FindEmptyCell returns empty neighbor if empty is currently and still empty")
    void findEmptyCell_CurrentAndNextStateFishNeighbor_ReturnEmptyCell() {
      WaTorCell cell = new WaTorCell(0, rule);
      WaTorCell neighbor = new WaTorCell(WATOR_EMPTY, rule);
      cell.setNeighbors(List.of(neighbor));

      Optional<WaTorCell> emptyCell = rule.findEmptyCell(cell);
      assertTrue(emptyCell.isPresent());
      assertEquals(neighbor, emptyCell.get());
    }

    @Test
    @DisplayName("FindEmptyCell returns empty optional if empty was empty even though no longer is")
    void findEmptyCell_WasFishButIsNowEmpty_ReturnsOptionalEmpty() {
      WaTorCell cell = new WaTorCell(0, rule);
      WaTorCell neighbor = new WaTorCell(WATOR_FISH, rule);
      neighbor.setNextState(0);
      cell.setNeighbors(List.of(neighbor));

      Optional<WaTorCell> emptyCell = rule.findEmptyCell(cell);
      assertFalse(emptyCell.isPresent());
    }

    @Test
    @DisplayName("FindEmptyCell empty Optional if neighbor wasn't empty but now is")
    void findEmptyCell_NoNeighborsWithCurrentFishState_ReturnsEmptyOptional() {
      WaTorCell cell = new WaTorCell(0, rule);
      WaTorCell neighbor = new WaTorCell(WATOR_FISH, rule);
      neighbor.setNextState(0);
      cell.setNeighbors(List.of(neighbor));

      Optional<WaTorCell> emptyCell = rule.findEmptyCell(cell);
      assertFalse(emptyCell.isPresent());
    }

    // the rest are combo together to test how things interact together so will test them in simulation
  }
}
