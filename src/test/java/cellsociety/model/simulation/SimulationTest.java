package cellsociety.model.simulation;


import static cellsociety.model.util.constants.CellStates.FALLINGSAND_EMPTY;
import static cellsociety.model.util.constants.CellStates.FALLINGSAND_SAND;
import static cellsociety.model.util.constants.CellStates.FALLINGSAND_STEEL;
import static cellsociety.model.util.constants.CellStates.FALLINGSAND_WATER;
import static cellsociety.model.util.constants.CellStates.FIRE_BURNING;
import static cellsociety.model.util.constants.CellStates.FIRE_EMPTY;
import static cellsociety.model.util.constants.CellStates.FIRE_TREE;
import static cellsociety.model.util.constants.CellStates.GAMEOFLIFE_ALIVE;
import static cellsociety.model.util.constants.CellStates.GAMEOFLIFE_DEAD;
import static cellsociety.model.util.constants.CellStates.PERCOLATION_BLOCKED;
import static cellsociety.model.util.constants.CellStates.PERCOLATION_OPEN;
import static cellsociety.model.util.constants.CellStates.PERCOLATION_PERCOLATED;
import static cellsociety.model.util.constants.CellStates.SEGREGATION_A;
import static cellsociety.model.util.constants.CellStates.SEGREGATION_B;
import static cellsociety.model.util.constants.CellStates.SEGREGATION_EMPTY;
import static cellsociety.model.util.constants.CellStates.WATOR_EMPTY;
import static cellsociety.model.util.constants.CellStates.WATOR_FISH;
import static cellsociety.model.util.constants.CellStates.WATOR_SHARK;
import static org.mockito.Mockito.mock;

import cellsociety.model.simulation.cell.ChouReg2Cell;
import cellsociety.model.simulation.cell.LangtonCell;
import cellsociety.model.simulation.cell.PetelkaCell;
import cellsociety.model.simulation.cell.WaTorCell;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.XmlData;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.constants.GridTypes.EdgeType;
import cellsociety.model.util.constants.GridTypes.NeighborhoodType;
import cellsociety.model.util.constants.GridTypes.ShapeType;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


// if I had time I would really want to improve this, but it tests what it needs to.
public class SimulationTest {

  private final XmlData data = mock(XmlData.class);

  @Nested
  @DisplayName("Tests for General simulation")
  class GeneralSimulationTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Simulation throws simulation exception if Xml data is null")
    void simulation_NullXmlData_ThrowsException() {
      assertThrows(SimulationException.class, () -> new Simulation(null));
    }

    @Test
    @DisplayName("Simulation throws simulation exception if Xml data cells mismatched cell length")
    void simulation_BadMisMatchCellLength_ThrowsException() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 0, 3,
              2, 1, 7,
              3, 4, 3)
      );
      when(data.getType()).thenReturn(SimType.Langton);
      when(data.getGridColNum()).thenReturn(4);
      when(data.getGridRowNum()).thenReturn(3);
      // getParameters() -> Langton's don't have any shouldn't be a problemm

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.VON_NEUMANN);

      // mismatched cells
      assertThrows(SimulationException.class, () -> new Simulation(data));

    }

    @Test
    @DisplayName("Simulation throws simulation exception if Xml data cells have bad states")
    void simulation_BadCellStates_ThrowsException() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 0, 0,
              2, 1, 7,
              3, 4, 3)
      );
      when(data.getType()).thenReturn(SimType.GameOfLife);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.VON_NEUMANN);

      assertThrows(SimulationException.class, () -> new Simulation(data));

    }

    @Test
    @DisplayName("Simulation can change topology within a simulation")
    void simulation_ChangeTopologyAfterInitialize_GraphTopologyChanges() {
      when(data.getType()).thenReturn(SimType.Fire);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      when(data.getParameters()).thenReturn(
          Map.of("ignitionLikelihood", 0, "treeSpawnLikelihood", 0));
      // so to test functionality just makes it always ignite, then do a different one where it never ignites or spawns

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.VON_NEUMANN);

      when(data.getCellStateList()).thenReturn(
          List.of(FIRE_TREE, FIRE_TREE, FIRE_TREE,
              FIRE_TREE, FIRE_TREE, FIRE_TREE,
              FIRE_BURNING, FIRE_TREE, FIRE_TREE)
      );

      Simulation sim = new Simulation(data);

      sim.step();
      assertEquals(FIRE_BURNING, sim.getCurrentState(1, 0));
      // if I were to step again with von neuman the 0, 0 tree should burn
      sim.changeTopology(ShapeType.HEXAGON, NeighborhoodType.VON_NEUMANN, EdgeType.NONE);
      sim.step();
      assertEquals(FIRE_TREE, sim.getCurrentState(0, 0));
      sim.step();
      assertEquals(FIRE_TREE, sim.getCurrentState(0, 0));
    }

    @Test
    @DisplayName("Test stepback, step, getStateLength, getIterations, changing parameters within a simulation")
    void simulation_SmallIterationMethods_WorkAsIntented() {
      when(data.getType()).thenReturn(SimType.Fire);
      when(data.getGridColNum()).thenReturn(1);
      when(data.getGridRowNum()).thenReturn(1);
      Map<String, Object> params = new HashMap<>();
      params.put("ignitionLikelihood", 1);
      params.put("treeSpawnLikelihood", 1);
      when(data.getParameters()).thenReturn(params);
      // so to test functionality just makes it always ignite, then do a different one where it never ignites or spawns

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.VON_NEUMANN);

      when(data.getCellStateList()).thenReturn(
          List.of(FIRE_EMPTY)
      );

      Simulation sim = new Simulation(data);
      sim.updateParameter("maxHistorySize", 2);

      // iteration 0
      assertEquals(0, sim.getCurrentState(0, 0));
      assertEquals(0, sim.getTotalIterations());
      assertEquals(1, sim.getStateLength(0, 0));

      sim.step();
      assertEquals(FIRE_TREE, sim.getCurrentState(0, 0));
      assertEquals(1, sim.getTotalIterations());
      assertEquals(1, sim.getStateLength(0, 0));
      sim.step();
      assertEquals(FIRE_BURNING, sim.getCurrentState(0, 0));
      assertEquals(2, sim.getTotalIterations());
      assertEquals(1, sim.getStateLength(0, 0));
      sim.step();
      assertEquals(FIRE_EMPTY, sim.getCurrentState(0, 0));
      assertEquals(3, sim.getTotalIterations());
      assertEquals(1, sim.getStateLength(0, 0));

      sim.stepBack();
      assertEquals(FIRE_BURNING, sim.getCurrentState(0, 0));
      assertEquals(2, sim.getTotalIterations());
      sim.stepBack();
      assertEquals(FIRE_TREE, sim.getCurrentState(0, 0));
      assertEquals(1, sim.getTotalIterations());
      sim.stepBack();
      assertEquals(FIRE_TREE, sim.getCurrentState(0, 0));
      assertEquals(1, sim.getTotalIterations());

      sim.updateParameter("ignitionLikelihood", 0.);
      sim.step();
      assertEquals(FIRE_TREE, sim.getCurrentState(0, 0));
      assertEquals(2, sim.getTotalIterations());
      assertEquals(2, sim.getStateLength(0, 0));

      sim.step();
      assertEquals(FIRE_TREE, sim.getCurrentState(0, 0));
      assertEquals(3, sim.getTotalIterations());
      assertEquals(3, sim.getStateLength(0, 0));

      List<String> parameterKeys = sim.getParameterKeys();
      assertTrue(parameterKeys.contains("maxHistorySize"));
      assertTrue(parameterKeys.contains("ignitionLikelihood"));
      assertTrue(parameterKeys.contains("treeSpawnLikelihood"));
      assertFalse(parameterKeys.contains("numStates"));

      assertEquals(0., sim.getParameter("ignitionLikelihood"));
      assertThrows(SimulationException.class, () -> sim.getParameter("numStates"));
    }

    @Test
    @DisplayName("Simulation can properly get AdidiotnalParameter keys and value")
    void simulation_GetAddiotnalParameterKeys_CanView() {

      when(data.getType()).thenReturn(SimType.GameOfLife);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.MOORE);

      when(data.getCellStateList()).thenReturn(
          List.of(GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD)
      );

      Simulation sim = new Simulation(data);

      List<String> parameterKeys = sim.getAdditionalParameterKeys();
      assertTrue(parameterKeys.contains("S"));
      assertTrue(parameterKeys.contains("B"));
      assertFalse(parameterKeys.contains("maxHistorySize"));

      List<?> parameterList = (List<?>) sim.getAdditionalParameter("S", List.class)
          .orElse(List.of());

      List<Integer> integersList = new ArrayList<>();

      if (parameterList.stream().allMatch(e -> e instanceof Number)) {
        integersList = parameterList.stream().map(e -> ((Number) e).intValue()).toList();
      }

      assertTrue(integersList.contains(2));
      assertTrue(integersList.contains(2));
      assertFalse(integersList.contains(1));

      assertEquals(3, sim.getNumEditableParameters());

    }

    @Test
    @DisplayName("Simulation throws excpetion if try to modify unmodifiable parameter")
    void simulation_ModifyUnmodifiableParameter_ThrowsException() {

      when(data.getType()).thenReturn(SimType.RockPaperSciss);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      when(data.getParameters()).thenReturn(Map.of("numStates", 9, "percentageToWin", 0));

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.MOORE);

      when(data.getCellStateList()).thenReturn(
          List.of(2, 0, 2,
              2, 1, 0,
              2, 4, 1)
      );

      Simulation sim = new Simulation(data);

      assertThrows(SimulationException.class, () -> sim.updateParameter("numStates", 10.));

      assertTrue(sim.getUnmodifiableParameterKeys().contains("numStates"));
    }

    // do all the negative testing here just to catch errors, grids already been tested

  }

  @Nested
  @DisplayName("Tests for Langton simulation")
  class LangtonSimulationTest {

    @BeforeEach
    void setUp() {
      when(data.getType()).thenReturn(SimType.Langton);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      // getParameters() -> Langton's don't have any shouldn't be a problemm

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.VON_NEUMANN);

    }

    // for doing each one change the return of getCellStateList to test different things
    // types of keys and rotations

    @Test
    @DisplayName("Langton's cell set up test and test non length state keys")
    void langton_SetUp10542_TestGeneralFunctionality() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 0, 3,
              2, 1, 5,
              3, 4, 3)
      );

      Simulation sim = new Simulation(data);
      List<LangtonCell> cells = sim.getAllCells();

      // focus mainly on the center one
      LangtonCell cell = cells.get(4);
      assertEquals(4, cell.getNeighbors().size());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(7)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));

      assertEquals(1, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(7, sim.getCurrentState(1, 1));
      assertEquals(3, sim.getCurrentState(0, 0));
      assertEquals(0, sim.getCurrentState(0, 1));
      assertEquals(2, sim.getCurrentState(1, 0));

      // TODO: move this to not here this one should really just be testing langton's
//      assertEquals(1, sim.getStateLength(1, 1));
//      assertEquals(2, sim.getStateLength(0, 0));
//      assertEquals(1, sim.getTotalIterations());
//
//      sim.stepBack();
//      assertEquals(1, cell.getCurrentState());
//      assertEquals(0, sim.getTotalIterations());
    }

    @Test
    @DisplayName("Langton's cell check rotation90")
    void langton_SetUp10542Rotated90_CenterCellIs7() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 2, 3,
              4, 1, 0,
              3, 5, 3)
      );

      Simulation sim = new Simulation(data);

      assertEquals(1, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(7, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("Langton's cell check rotation180")
    void langton_SetUp10542Rotated180_CenterCellIs7() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 4, 3,
              5, 1, 2,
              3, 0, 3)
      );

      Simulation sim = new Simulation(data);

      assertEquals(1, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(7, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("Langton's cell check rotation270")
    void langton_SetUp10542Rotated270_CenterCellIs7() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 5, 3,
              0, 1, 4,
              3, 2, 3)
      );

      Simulation sim = new Simulation(data);

      assertEquals(1, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(7, sim.getCurrentState(1, 1));
    }
  }

  @Nested
  @DisplayName("Tests for ChouReg2 simulation")
  class ChouReg2SimulationTest {

    @BeforeEach
    void setUp() {
      when(data.getType()).thenReturn(SimType.ChouReg2);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      // getParameters() -> ChouReg2's don't have any shouldn't be a problemm

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.VON_NEUMANN);
    }

    // for doing each one change the return of getCellStateList to test different things

    // types of keys and rotations

    @Test
    @DisplayName("ChouReg2 cell set up test and test non length state keys")
    void chouReg2_SetUp40031_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 0, 3,
              1, 4, 0,
              3, 3, 3)
      );

      Simulation sim = new Simulation(data);
      List<ChouReg2Cell> cells = sim.getAllCells();

      // focus mainly on the center one
      ChouReg2Cell cell = cells.get(4);
      assertEquals(4, cell.getNeighbors().size());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(7)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));

      assertEquals(4, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
      assertEquals(3, sim.getCurrentState(0, 0));
      assertEquals(0, sim.getCurrentState(0, 1));
      assertEquals(1, sim.getCurrentState(1, 0));
    }

    @Test
    @DisplayName("ChouReg2 cell check rotation90")
    void chouReg2_SetUp40031Rotated90_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 1, 3,
              3, 4, 0,
              3, 0, 3)
      );

      Simulation sim = new Simulation(data);

      assertEquals(4, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("ChouReg2 cell check rotation180")
    void chouReg2_SetUp40031Rotated180_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 3, 3,
              0, 4, 1,
              3, 0, 3)
      );

      Simulation sim = new Simulation(data);

      assertEquals(4, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("ChouReg2 cell check rotation270")
    void chouReg2_SetUp40031Rotated270_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 0, 3,
              0, 4, 3,
              3, 1, 3)
      );

      Simulation sim = new Simulation(data);

      assertEquals(4, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("ChouReg2 remains in current state if not a valid map input")
    void chouReg2_InValidMapKey_StaysCurrentState() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 1, 3,
              4, 4, 3,
              3, 2, 3)
      );

      Simulation sim = new Simulation(data);

      assertEquals(4, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(4, sim.getCurrentState(1, 1));
    }

  }

  @Nested
  @DisplayName("Tests for Petelka simulation")
  class PetelkaSimulationTest {

    @BeforeEach
    void setUp() {
      when(data.getType()).thenReturn(SimType.Petelka);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      // getParameters() -> Petelka's don't have any shouldn't be a problemm

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.MOORE);
    }

    // for doing each one change the return of getCellStateList to test different things

    // types of keys and rotations
    @Test
    @DisplayName("Petelka cell set up test and test non length state keys")
    void chouReg2_SetUp223241000_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(0, 2, 3,
              0, 2, 2,
              0, 1, 4)
      );

      Simulation sim = new Simulation(data);
      List<PetelkaCell> cells = sim.getAllCells();

      // focus mainly on the center one
      PetelkaCell cell = cells.get(4);
      assertEquals(8, cell.getNeighbors().size());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(8)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(7)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));

      assertEquals(2, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
      assertEquals(0, sim.getCurrentState(0, 0));
      assertEquals(2, sim.getCurrentState(0, 1));
      assertEquals(0, sim.getCurrentState(1, 0));
    }

    @Test
    @DisplayName("Petelka cell check rotation90")
    void chouReg2_SetUp223241000Rotated90_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(0, 0, 0,
              1, 2, 2,
              4, 2, 3)
      );

      Simulation sim = new Simulation(data);

      assertEquals(2, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("Petelka cell check rotation180")
    void chouReg2_SetUp223241000Rotated180_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(4, 1, 0,
              2, 2, 0,
              3, 2, 0)
      );

      Simulation sim = new Simulation(data);

      assertEquals(2, sim.getCurrentState(1, 1));

      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("Petelka cell check rotation270")
    void chouReg2_SetUp223241000Rotated270_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 2, 4,
              2, 2, 1,
              0, 0, 0)
      );

      Simulation sim = new Simulation(data);

      assertEquals(2, sim.getCurrentState(1, 1));

      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("Petelka cell check Horizonal reflection")
    void chouReg2_SetUp223241000HorizontalReflection_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(0, 1, 4,
              0, 2, 2,
              0, 2, 3)
      );

      Simulation sim = new Simulation(data);

      assertEquals(2, sim.getCurrentState(1, 1));

      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("Petelka cell check Vertical reflection")
    void chouReg2_SetUp223241000VerticalReflection_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 2, 0,
              2, 2, 0,
              4, 1, 0)
      );

      Simulation sim = new Simulation(data);

      assertEquals(2, sim.getCurrentState(1, 1));

      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("Petelka cell check Diaganol1 reflection")
    void chouReg2_SetUp223241000Diaganol1Reflection_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(0, 0, 0,
              2, 2, 1,
              3, 2, 4)
      );

      Simulation sim = new Simulation(data);

      assertEquals(2, sim.getCurrentState(1, 1));

      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("Petelka cell check Diaganol2 reflection")
    void chouReg2_SetUp223241000Diaganol2Reflection_CenterCellIs3() {
      when(data.getCellStateList()).thenReturn(
          List.of(4, 2, 3,
              1, 2, 2,
              0, 0, 0)
      );

      Simulation sim = new Simulation(data);

      assertEquals(2, sim.getCurrentState(1, 1));

      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(3, sim.getCurrentState(1, 1));
    }

    @Test
    @DisplayName("Petelka remains is set to 0 if not in the rule set")
    void chouReg2_NotInMapKey_SetsTo0() {
      when(data.getCellStateList()).thenReturn(
          List.of(3, 1, 3,
              4, 4, 2,
              3, 2, 3)
      );

      Simulation sim = new Simulation(data);

      assertEquals(4, sim.getCurrentState(1, 1));
      sim.step();
      // center one is the only one with full neighbors so only one that should switch
      assertEquals(0, sim.getCurrentState(1, 1));
    }

  }

  @Nested
  @DisplayName("Tests for Percolation simulation")
  class PercolationSimulationTest {

    @BeforeEach
    void setUp() {
      when(data.getType()).thenReturn(SimType.Percolation);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      // getParameters() -> Percolation's don't have any shouldn't be a problemm

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.VON_NEUMANN);
    }

    // for doing each one change the return of getCellStateList to test different things
    // types of keys and rotations


    // sorry I know these names suck its just the unit I'm testing is if the rules and cells
    // work together hello now my the way that I expect
    @Test
    @DisplayName("Percolation: test blocked stays blocked, percolated stays percolated, "
        + "open becomes percolated if it has a direct neighbor that is percolated")
    void percolation_SimpleSetup_RulesBehaveAsExpected() {
      when(data.getCellStateList()).thenReturn(
          List.of(PERCOLATION_OPEN, PERCOLATION_BLOCKED, PERCOLATION_OPEN,
              PERCOLATION_BLOCKED, PERCOLATION_OPEN, PERCOLATION_OPEN,
              PERCOLATION_OPEN, PERCOLATION_PERCOLATED, PERCOLATION_OPEN)
      );

      Simulation sim = new Simulation(data);

      sim.step();
      // changes this step are 1, 1 || 2, 0 || 2, 2
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(1, 1));
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(2, 0));
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(2, 2));

      sim.step();
      // changes this step are 1, 2
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(1, 2));

      sim.step();
      // changes this step are 0, 2
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(0, 2));

      // so in total
      assertEquals(PERCOLATION_OPEN, sim.getCurrentState(0, 0));
      assertEquals(4, sim.getStateLength(0, 0));  // start, 1, 2, 3
      assertEquals(PERCOLATION_BLOCKED, sim.getCurrentState(0, 1));
      assertEquals(4, sim.getStateLength(0, 1));
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(0, 2));
      assertEquals(1, sim.getStateLength(0, 2));
      assertEquals(PERCOLATION_BLOCKED, sim.getCurrentState(1, 0));
      assertEquals(4, sim.getStateLength(1, 0));
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(1, 1));
      assertEquals(3, sim.getStateLength(1, 1));
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(1, 2));
      assertEquals(2, sim.getStateLength(1, 2));
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(2, 0));
      assertEquals(3, sim.getStateLength(2, 0));
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(2, 1));
      assertEquals(4, sim.getStateLength(2, 1));
      assertEquals(PERCOLATION_PERCOLATED, sim.getCurrentState(2, 2));
      assertEquals(3, sim.getStateLength(2, 2));
    }

  }

  @Nested
  @DisplayName("Tests for FallingSand simulation")
  class FallingSandSimulationTest {

    @BeforeEach
    void setUp() {
      when(data.getType()).thenReturn(SimType.FallingSand);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      // getParameters() -> FallingSand's don't have any shouldn't be a problemm

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.MOORE);
    }

    // for doing each one change the return of getCellStateList to test different things
    // types of keys and rotations

    @Test
    @DisplayName("Sand steps past obstacles diagally and sinks beneath water "
        + "Water steps past obstacles to a side then down. Steel stays put. Air always goes "
        + "to the top if it can")
    void fallingSand_SimpleSetup_RulesBehaveAsExpected() {
      when(data.getCellStateList()).thenReturn(
          List.of(FALLINGSAND_SAND, FALLINGSAND_SAND, FALLINGSAND_EMPTY,
              FALLINGSAND_STEEL, FALLINGSAND_WATER, FALLINGSAND_WATER,
              FALLINGSAND_EMPTY, FALLINGSAND_STEEL, FALLINGSAND_WATER)
      );

      Simulation sim = new Simulation(data);
      // when doing these go left to right down to top
      sim.step();
      /*
       W W E
       X S S
       E X W
       */
      assertEquals(FALLINGSAND_WATER, sim.getCurrentState(0, 0));
      assertEquals(FALLINGSAND_SAND, sim.getCurrentState(1, 1));  // swap
      assertEquals(FALLINGSAND_WATER, sim.getCurrentState(0, 1));
      assertEquals(FALLINGSAND_SAND, sim.getCurrentState(1, 2));  // swap
      // since the interesting things swapped nothing else is done this step

      sim.step();
      /*
      W E W
      X E W
      S X S
       */
      // first water can't do anything
      // second water moves right
      assertEquals(FALLINGSAND_WATER, sim.getCurrentState(0, 2));
      assertEquals(FALLINGSAND_EMPTY, sim.getCurrentState(0, 1));
      // sand prioritizes empty over water moves to the crack
      assertEquals(FALLINGSAND_SAND, sim.getCurrentState(2, 0));
      assertEquals(FALLINGSAND_EMPTY, sim.getCurrentState(1, 1));
      // now other sand flops with it
      assertEquals(FALLINGSAND_SAND, sim.getCurrentState(2, 2));
      assertEquals(FALLINGSAND_WATER, sim.getCurrentState(1, 2));

      /*
      E W W
      X E W
      S X S
       */
      sim.step();
      // the only other thing now is the water just kinda wobbles back in form
      assertEquals(FALLINGSAND_EMPTY, sim.getCurrentState(0, 0));
      assertEquals(FALLINGSAND_WATER, sim.getCurrentState(0, 1));

      /*
      E E W
      X W W
      S X S
       */
      sim.step();
      assertEquals(FALLINGSAND_EMPTY, sim.getCurrentState(0, 1));
      assertEquals(FALLINGSAND_WATER, sim.getCurrentState(1, 1));

      // now the top water just wiggles indefinitely
      sim.step();
      assertEquals(FALLINGSAND_WATER, sim.getCurrentState(0, 1));
      assertEquals(FALLINGSAND_EMPTY, sim.getCurrentState(0, 2));
    }
  }

  @Nested
  @DisplayName("Tests for Fire simulation")
  class FireSimulationTest {

    @BeforeEach
    void setUp() {
      when(data.getType()).thenReturn(SimType.Fire);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      Map<String, Object> params = new HashMap<>();
      params.put("ignitionLikelihood", 1);
      params.put("treeSpawnLikelihood", 1);
      when(data.getParameters()).thenReturn(params);
      // so to test functionality just makes it always ignite, then do a different one where it never ignites or spawns

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.VON_NEUMANN);
    }

    // for doing each one change the return of getCellStateList to test different things
    // types of keys and rotations
    @Test
    @DisplayName("FireRrule under conditions where always lights nad all always spawns trees")
    void fire_AlwaysIgniteAlwaysSpawn_InfinteCycleOfSteps() {
      when(data.getCellStateList()).thenReturn(
          List.of(FIRE_TREE, FIRE_EMPTY, FIRE_BURNING,
              FIRE_EMPTY, FIRE_TREE, FIRE_EMPTY,
              FIRE_BURNING, FIRE_EMPTY, FIRE_TREE)
      );

      Simulation sim = new Simulation(data);
      sim.step();
      assertEquals(FIRE_BURNING, sim.getCurrentState(0, 0));
      assertEquals(FIRE_BURNING, sim.getCurrentState(1, 1));
      assertEquals(FIRE_BURNING, sim.getCurrentState(2, 2));
      assertEquals(FIRE_TREE, sim.getCurrentState(0, 1));
      assertEquals(FIRE_TREE, sim.getCurrentState(1, 2));
      assertEquals(FIRE_TREE, sim.getCurrentState(1, 0));
      assertEquals(FIRE_TREE, sim.getCurrentState(2, 1));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(2, 0));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(0, 2));

      sim.step();
      assertEquals(FIRE_EMPTY, sim.getCurrentState(0, 0));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(1, 1));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(2, 2));
      assertEquals(FIRE_BURNING, sim.getCurrentState(0, 1));
      assertEquals(FIRE_BURNING, sim.getCurrentState(1, 2));
      assertEquals(FIRE_BURNING, sim.getCurrentState(1, 0));
      assertEquals(FIRE_BURNING, sim.getCurrentState(2, 1));
      assertEquals(FIRE_TREE, sim.getCurrentState(2, 0));
      assertEquals(FIRE_TREE, sim.getCurrentState(0, 2));

      sim.step();
      assertEquals(FIRE_TREE, sim.getCurrentState(0, 0));
      assertEquals(FIRE_TREE, sim.getCurrentState(1, 1));
      assertEquals(FIRE_TREE, sim.getCurrentState(2, 2));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(0, 1));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(1, 2));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(1, 0));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(2, 1));
      assertEquals(FIRE_BURNING, sim.getCurrentState(2, 0));
      assertEquals(FIRE_BURNING, sim.getCurrentState(0, 2));

      // now in a loop
    }

    @Test
    @DisplayName("FireRule under conditions when only changes when it lights from neighbor, no light, no spawns")
    void fire_OnlyIgniteWhenNeighbor_AllEmptyAtEnd() {
      when(data.getCellStateList()).thenReturn(
          List.of(FIRE_TREE, FIRE_TREE, FIRE_BURNING,
              FIRE_TREE, FIRE_EMPTY, FIRE_TREE,
              FIRE_BURNING, FIRE_TREE, FIRE_TREE)
      );

      Simulation sim = new Simulation(data);
      sim.updateParameter("ignitionLikelihood", 0);
      sim.updateParameter("treeSpawnLikelihood", 0);

      /*
        init
        T T B
        T 1 T
        B T T

        step
        T B 1
        B 2 B
        1 B T

        step
        B 1 2
        1 3 1
        2 1 B

        step
        1 2 3
        2 4 2
        3 2 1
       */
      sim.step();
      sim.step();
      sim.step();

      assertEquals(FIRE_EMPTY, sim.getCurrentState(0, 0));
      assertEquals(1, sim.getStateLength(0, 0));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(0, 1));
      assertEquals(2, sim.getStateLength(0, 1));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(0, 2));
      assertEquals(3, sim.getStateLength(0, 2));

      assertEquals(FIRE_EMPTY, sim.getCurrentState(1, 0));
      assertEquals(2, sim.getStateLength(1, 0));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(1, 1));
      assertEquals(4, sim.getStateLength(1, 1));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(1, 2));
      assertEquals(2, sim.getStateLength(1, 2));

      assertEquals(FIRE_EMPTY, sim.getCurrentState(2, 0));
      assertEquals(3, sim.getStateLength(2, 0));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(2, 1));
      assertEquals(2, sim.getStateLength(2, 1));
      assertEquals(FIRE_EMPTY, sim.getCurrentState(2, 2));
      assertEquals(1, sim.getStateLength(2, 2));
    }

  }

  @Nested
  @DisplayName("Tests for RockPaperSciss simulation")
  class RockPaperScissSimulationTest {

    @BeforeEach
    void setUp() {
      when(data.getType()).thenReturn(SimType.RockPaperSciss);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      Map<String, Object> params = new HashMap<>();
      params.put("numStates", 9);
      params.put("percentageToWin", 0);
      when(data.getParameters()).thenReturn(params);

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.MOORE);
    }

    // for doing each one change the return of getCellStateList to test different things
    // types of keys and rotations
    @Test
    @DisplayName("RPS test changing num states and correctly determining winning state since percentageToWin is 0")
    void rps_9statesAlwaysWin_cycleThroughStates() {
      when(data.getCellStateList()).thenReturn(
          List.of(0, 1, 2,
              3, 4, 5,
              6, 7, 8)
      );

      Simulation sim = new Simulation(data);
      sim.step();
      assertEquals(1, sim.getCurrentState(0, 0));
      assertEquals(2, sim.getCurrentState(0, 1));
      assertEquals(3, sim.getCurrentState(0, 2));
      assertEquals(4, sim.getCurrentState(1, 0));
      assertEquals(5, sim.getCurrentState(1, 1));
      assertEquals(6, sim.getCurrentState(1, 2));
      assertEquals(7, sim.getCurrentState(2, 0));
      assertEquals(8, sim.getCurrentState(2, 1));
      assertEquals(0, sim.getCurrentState(2, 2));

      // I'd do this again but it just be this again and again
    }

    @Test
    @DisplayName("RPS test throws exception if you try to change the num of states")
    void rps_changeNumStatesAfterInit_throwsException() {
      // just need cells
      when(data.getCellStateList()).thenReturn(
          List.of(0, 1, 2,
              3, 4, 5,
              6, 7, 8)
      );
      Simulation sim = new Simulation(data);

      assertThrows(SimulationException.class, () -> sim.updateParameter("numStates", 10.));
    }

    @Test
    @DisplayName("RPS behaves as exxpected with reasonable percentage to win")
    void rps_9statesWinWith50Percent_all2Except5Stays5() {
      when(data.getCellStateList()).thenReturn(
          List.of(0, 1, 2,
              1, 2, 5,
              2, 1, 2)
      );
      Simulation sim = new Simulation(data);
      sim.updateParameter("percentageToWin", 0.5);

      sim.step();
      assertEquals(1, sim.getCurrentState(0, 0)); // 2 / 3 > .5
      assertEquals(1, sim.getCurrentState(0, 1)); // 2/ 5 < .5
      assertEquals(2, sim.getCurrentState(0, 2)); // no 3's so 2's always stay the same
      assertEquals(1, sim.getCurrentState(1, 0));
      assertEquals(2, sim.getCurrentState(1, 1));
      assertEquals(5, sim.getCurrentState(1, 2));
      assertEquals(2, sim.getCurrentState(2, 0));
      assertEquals(2, sim.getCurrentState(2, 1));
      assertEquals(2, sim.getCurrentState(2, 2));

      /*
      1 1 2
      1 2 5
      2 2 2
       */
      sim.step();
      assertEquals(1, sim.getCurrentState(0, 0));
      assertEquals(1, sim.getCurrentState(0, 1));
      assertEquals(2, sim.getCurrentState(0, 2));
      assertEquals(2, sim.getCurrentState(1, 0));
      assertEquals(2, sim.getCurrentState(1, 1));
      assertEquals(5, sim.getCurrentState(1, 2));
      assertEquals(2, sim.getCurrentState(2, 0));
      assertEquals(2, sim.getCurrentState(2, 1));
      assertEquals(2, sim.getCurrentState(2, 2));

      /*
      1 1 2
      2 2 5
      2 2 2
       */
      sim.step();
      assertEquals(2, sim.getCurrentState(0, 0));
      assertEquals(2, sim.getCurrentState(0, 1));
      assertEquals(2, sim.getCurrentState(0, 2));
      assertEquals(2, sim.getCurrentState(1, 0));
      assertEquals(2, sim.getCurrentState(1, 1));
      assertEquals(5, sim.getCurrentState(1, 2));
      assertEquals(2, sim.getCurrentState(2, 0));
      assertEquals(2, sim.getCurrentState(2, 1));
      assertEquals(2, sim.getCurrentState(2, 2));
    }

  }

  @Nested
  @DisplayName("Tests for GameOfLife simulation")
  class GameOfLifeSimulationTest {

    @BeforeEach
    void setUp() {
      when(data.getType()).thenReturn(SimType.GameOfLife);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.MOORE);
    }

    // for doing each one change the return of getCellStateList to test different things
    // getParameters() -> GameOfLife's change this to test between this and the general one
    // types of keys and rotations
    @Test
    @DisplayName("Default GameOfLife runs as expected")
    void gameOfLife_DefaultParamLine_SpinningLine() {
      when(data.getCellStateList()).thenReturn(
          List.of(GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD)
      );

      Simulation sim = new Simulation(data);

      sim.step();

      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 2));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(1, 0));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(1, 1));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(1, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 2));

      sim.step();

      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 0));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(0, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 0));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(1, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 0));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(2, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 2));

    }

    @Test
    @DisplayName("General GameOfLife modified params through updating parameters")
    void gameOfLife_modifyParameter_AllDead() {
      when(data.getCellStateList()).thenReturn(
          List.of(GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD)
      );

      Simulation sim = new Simulation(data);
      sim.updateAdditionalParameter("S", new ArrayList<>(List.of(8)));
      sim.updateAdditionalParameter("B", new ArrayList<>(List.of(4)));

      sim.step();
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 2));

    }

    @Test
    @DisplayName("General GameOfLife modified params through init parameters")
    void gameOfLife_generalGameOfLifeThroughXML_AllDead() {
      when(data.getCellStateList()).thenReturn(
          List.of(GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD)
      );

      when(data.getParameters()).thenReturn(Map.of("S", List.of(8), "B", List.of(4)));

      Simulation sim = new Simulation(data);

      sim.step();
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 2));

    }

    // the difference between this is how rule factory parses things from the XML, it assumes
    // you wanted a double
    @Test
    @DisplayName("Number on XML defaults to general game of life rules")
    void gameOfLife_numberOnXMLParameters_SpinningLine() {
      when(data.getCellStateList()).thenReturn(
          List.of(GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD)
      );

      when(data.getParameters()).thenReturn(Map.of("S", 8, "B", 4));

      Simulation sim = new Simulation(data);

      sim.step();

      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 2));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(1, 0));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(1, 1));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(1, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 2));

      sim.step();

      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 0));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(0, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 0));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(1, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 0));
      assertEquals(GAMEOFLIFE_ALIVE, sim.getCurrentState(2, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 2));
    }

    @Test
    @DisplayName("NotList or Number on XML all die when try to step")
    void gameOfLife_notListOrNumberOnXMLParameters_allDead() {
      when(data.getCellStateList()).thenReturn(
          List.of(GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD)
      );

      when(data.getParameters()).thenReturn(Map.of("S", List.of(8), "B", "something"));

      Simulation sim = new Simulation(data);
      sim.step();

      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 2));
    }

    @Test
    @DisplayName("Not List on AdditionalParameters all die")
    void gameOfLife_updateParameterToNonListNumber_doesNothing() {
      when(data.getCellStateList()).thenReturn(
          List.of(GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD,
              GAMEOFLIFE_DEAD, GAMEOFLIFE_ALIVE, GAMEOFLIFE_DEAD)
      );

      Simulation sim = new Simulation(data);
      sim.updateAdditionalParameter("S", new ArrayList<>(8));
      sim.updateAdditionalParameter("B", new ArrayList<>(List.of(4)));
      sim.step();

      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(0, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(1, 2));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 0));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 1));
      assertEquals(GAMEOFLIFE_DEAD, sim.getCurrentState(2, 2));
    }

  }


  @Nested
  @DisplayName("Tests for Segregation simulation")
  class SegregationSimulationTest {

    @BeforeEach
    void setUp() {
      when(data.getType()).thenReturn(SimType.Segregation);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      // getParameters() -- it has it but don't really know how much need to change this one for it

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.MOORE);
    }

    // for doing each one change the return of getCellStateList to test different things
    // types of keys and rotations

    @Test
    @DisplayName("Segregation all cells satsisfied no movement")
    void segregation_allCellsSatisfied_noMovement() {
      when(data.getCellStateList()).thenReturn(
          List.of(SEGREGATION_EMPTY, SEGREGATION_EMPTY, SEGREGATION_EMPTY,
              SEGREGATION_A, SEGREGATION_EMPTY, SEGREGATION_B,
              SEGREGATION_EMPTY, SEGREGATION_EMPTY, SEGREGATION_EMPTY)
      );

      Simulation sim = new Simulation(data);
      sim.updateParameter("toleranceThreshold", 0.5);
      sim.step();
      assertEquals(SEGREGATION_A, sim.getCurrentState(1, 0));
      assertEquals(SEGREGATION_B, sim.getCurrentState(1, 2));
    }

    @Test
    @DisplayName("Segregation cell not satisfied moves to cell")
    void segregation_cellNotSatisfied_movesToCell() {
      when(data.getCellStateList()).thenReturn(
          List.of(SEGREGATION_B, SEGREGATION_B, SEGREGATION_EMPTY,
              SEGREGATION_A, SEGREGATION_A, SEGREGATION_B,
              SEGREGATION_EMPTY, SEGREGATION_B, SEGREGATION_EMPTY)
      );

      Simulation sim = new Simulation(data);
      sim.updateParameter("toleranceThreshold", 0.5);
      sim.step();
      assertEquals(SEGREGATION_EMPTY, sim.getCurrentState(1, 0));
      assertEquals(SEGREGATION_A, sim.getCurrentState(2, 0));

    }

    @Test
    @DisplayName("Segregation cell not satisified moves to cell, blocks another dissatisfied agent from moving")
    void segregation_cellNotSatisfied_movesToCellBlocksAnother() {
      when(data.getCellStateList()).thenReturn(
          List.of(SEGREGATION_B, SEGREGATION_B, SEGREGATION_EMPTY,
              SEGREGATION_A, SEGREGATION_A, SEGREGATION_B,
              SEGREGATION_EMPTY, SEGREGATION_B, SEGREGATION_A)
      );

      Simulation sim = new Simulation(data);
      sim.updateParameter("toleranceThreshold", 0.5);
      sim.step();
      assertEquals(SEGREGATION_EMPTY, sim.getCurrentState(1, 0));
      assertEquals(SEGREGATION_A, sim.getCurrentState(2, 0));
      assertEquals(SEGREGATION_B, sim.getCurrentState(2, 1)); // b disasitisfied but stuck
    }

  }

  @Nested
  @DisplayName("Tests for WaTor simulation")
  class WaTorSimulationTest {

    @BeforeEach
    void setUp() {
      when(data.getType()).thenReturn(SimType.WaTor);
      when(data.getGridColNum()).thenReturn(3);
      when(data.getGridRowNum()).thenReturn(3);
      // getParameters() -- it has it but don't really know how much need to change this one for it

      when(data.getShape()).thenReturn(ShapeType.RECTANGLE);
      when(data.getEdge()).thenReturn(EdgeType.NONE);
      when(data.getNeighborhood()).thenReturn(NeighborhoodType.VON_NEUMANN);
    }

    // for doing each one change the return of getCellStateList to test different things
    // types of keys and rotations

    @Test
    @DisplayName("WaTor fish moves to empty cell")
    void wator_emptyCell_fishMoves() {
      when(data.getCellStateList()).thenReturn(
          List.of(WATOR_FISH, WATOR_FISH, WATOR_FISH,
              WATOR_EMPTY, WATOR_FISH, WATOR_FISH,
              WATOR_FISH, WATOR_FISH, WATOR_FISH)
      );

      Simulation sim = new Simulation(data);
      sim.step();

      assertEquals(WATOR_EMPTY, sim.getCurrentState(0, 0));
      assertEquals(WATOR_FISH, sim.getCurrentState(1, 0));
    }

    @Test
    @DisplayName("WaTor fish moves to empty cell, then reproduces fish")
    void wator_emptyCell_fishMovesThenReproduce() {
      when(data.getCellStateList()).thenReturn(
          List.of(WATOR_FISH, WATOR_FISH, WATOR_FISH,
              WATOR_EMPTY, WATOR_FISH, WATOR_FISH,
              WATOR_FISH, WATOR_FISH, WATOR_FISH)
      );
      Simulation sim = new Simulation(data);
      sim.updateParameter("fishReproductionTime", 1.);

      sim.step();
      assertEquals(WATOR_FISH, sim.getCurrentState(0, 0));
      assertEquals(WATOR_FISH, sim.getCurrentState(1, 0));
    }

    @Test
    @DisplayName("WaTor shark moves to empty cell if no fish")
    void wator_emptyCellNofish_sharkMovesToEmptySpot() {
      when(data.getCellStateList()).thenReturn(
          List.of(WATOR_SHARK, WATOR_SHARK, WATOR_SHARK,
              WATOR_EMPTY, WATOR_SHARK, WATOR_SHARK,
              WATOR_SHARK, WATOR_SHARK, WATOR_SHARK)
      );

      Simulation sim = new Simulation(data);
      sim.step();

      assertEquals(WATOR_EMPTY, sim.getCurrentState(0, 0));
      assertEquals(WATOR_SHARK, sim.getCurrentState(1, 0));
    }

    @Test
    @DisplayName("WaTor shark moves to empty cell, then reproduces shark")
    void wator_emptyCellNoFish_sharkMovesThenReproduce() {
      when(data.getCellStateList()).thenReturn(
          List.of(WATOR_SHARK, WATOR_SHARK, WATOR_SHARK,
              WATOR_EMPTY, WATOR_SHARK, WATOR_SHARK,
              WATOR_SHARK, WATOR_SHARK, WATOR_SHARK)
      );
      Simulation sim = new Simulation(data);
      sim.updateParameter("sharkReproductionTime", 1.);

      sim.step();
      assertEquals(WATOR_SHARK, sim.getCurrentState(0, 0));
      assertEquals(WATOR_SHARK, sim.getCurrentState(1, 0));
    }

    @Test
    @DisplayName("WaTor shark moves to fish over empty")
    void wator_emptyCellAndFish_sharkMovesToFish() {
      when(data.getCellStateList()).thenReturn(
          List.of(WATOR_SHARK, WATOR_EMPTY, WATOR_EMPTY,
              WATOR_FISH, WATOR_EMPTY, WATOR_EMPTY,
              WATOR_EMPTY, WATOR_EMPTY, WATOR_EMPTY)
      );
      Map<String, Object> params = new HashMap<>();
      params.put("sharkInitialEnergy", 2.);
      when(data.getParameters()).thenReturn(params);
      Simulation sim = new Simulation(data);
      sim.updateParameter("sharkEnergyGain", 1.);

      sim.step();

      assertEquals(WATOR_EMPTY, sim.getCurrentState(0, 0));
      assertEquals(WATOR_SHARK, sim.getCurrentState(1, 0));
      List<WaTorCell> cells = sim.getAllCells();
      assertEquals(2., cells.get(3).getEnergy());
    }

    @Test
    @DisplayName("WaTor shark moves to fish cell, then reproduces shark")
    void wator_emptyCellAndFish_sharkMovesToFishThenReproduce() {
      when(data.getCellStateList()).thenReturn(
          List.of(WATOR_SHARK, WATOR_EMPTY, WATOR_EMPTY,
              WATOR_FISH, WATOR_EMPTY, WATOR_EMPTY,
              WATOR_EMPTY, WATOR_EMPTY, WATOR_EMPTY)
      );
      Simulation sim = new Simulation(data);
      sim.updateParameter("sharkReproductionTime", 1.);

      sim.step();

      assertEquals(WATOR_SHARK, sim.getCurrentState(0, 0));
      assertEquals(WATOR_SHARK, sim.getCurrentState(1, 0));
    }

    @Test
    @DisplayName("Wator shark dies because of lack of enrgy")
    void wator_noEnergy_sharkDies() {
      when(data.getCellStateList()).thenReturn(
          List.of(WATOR_SHARK, WATOR_EMPTY, WATOR_EMPTY,
              WATOR_EMPTY, WATOR_EMPTY, WATOR_EMPTY,
              WATOR_EMPTY, WATOR_EMPTY, WATOR_EMPTY)
      );
      when(data.getParameters()).thenReturn(Map.of("sharkInitialEnergy", 1.));

      Simulation sim = new Simulation(data);

      sim.step();

      assertEquals(WATOR_EMPTY, sim.getCurrentState(0, 0));
      assertEquals(WATOR_EMPTY, sim.getCurrentState(1, 0));
    }

    @Test
    @DisplayName("Fish would have moved but shark ate it")
    void wator_fishMovesSharkEats_fishNoMove() {
      when(data.getCellStateList()).thenReturn(
          List.of(WATOR_FISH, WATOR_EMPTY, WATOR_EMPTY,
              WATOR_SHARK, WATOR_EMPTY, WATOR_EMPTY,
              WATOR_EMPTY, WATOR_EMPTY, WATOR_EMPTY)
      );
      Simulation sim = new Simulation(data);

      sim.step();

      assertEquals(WATOR_SHARK, sim.getCurrentState(0, 0));
      assertEquals(WATOR_EMPTY, sim.getCurrentState(1, 0));
    }

  }

}
