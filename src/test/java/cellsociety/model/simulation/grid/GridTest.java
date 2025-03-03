package cellsociety.model.simulation.grid;

import static cellsociety.model.util.SimulationTypes.SimType.Langton;
import static cellsociety.model.util.SimulationTypes.SimType.RockPaperSciss;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;

import cellsociety.model.simulation.cell.LangtonCell;
import cellsociety.model.simulation.cell.RockPaperScissCell;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;


public class GridTest {

  @Nested
  @DisplayName("Tests for just generic grid")
  class GridGenericTest {

    Grid<LangtonCell> grid;
    List<LangtonCell> cells;

    @BeforeEach
    void setUp() {
      GenericParameters parameters = new GenericParameters(Langton);
      LangtonRule rule = new LangtonRule(parameters);
      cells = List.of(new LangtonCell(0, rule), new LangtonCell(1, rule), new LangtonCell(2, rule),
          new LangtonCell(3, rule), new LangtonCell(4, rule), new LangtonCell(5, rule),
          new LangtonCell(6, rule), new LangtonCell(7, rule), new LangtonCell(0, rule));
      grid = new Grid<LangtonCell>(cells, 3, 3, ShapeType.RECTANGLE, NeighborhoodType.MOORE,
          EdgeType.NONE);
    }

    @Test
    @DisplayName("Grid default constructor properly sets up grid")
    void grid_DefaultConstructor_SetsUpInitialGrid() {
      // check initial grize grid
      assertEquals(3, grid.getRows());
      assertEquals(3, grid.getCols());

      // check initialize cells
      assertEquals(cells, grid.getCells());
      assertEquals(cells.get(0), grid.getCell(0, 0));
      assertEquals(cells.get(1), grid.getCell(0, 1));
      assertEquals(cells.get(2), grid.getCell(0, 2));
      assertEquals(cells.get(3), grid.getCell(1, 0));
      assertEquals(cells.get(4), grid.getCell(1, 1));
      assertEquals(cells.get(5), grid.getCell(1, 2));
      assertEquals(cells.get(6), grid.getCell(2, 0));
      assertEquals(cells.get(7), grid.getCell(2, 1));
      assertEquals(cells.get(8), grid.getCell(2, 2));

      // check set neighborhoods
      assertEquals(8, grid.getNeighbors(1, 1).size());
      assertEquals(3, grid.getNeighbors(0, 0).size());
      assertEquals(5, grid.getNeighbors(0, 1).size());

      List<LangtonCell> neighbors = grid.getNeighbors(1, 1);
      assertTrue(neighbors.contains(cells.get(0)));
      assertTrue(neighbors.contains(cells.get(1)));
      assertTrue(neighbors.contains(cells.get(2)));
      assertTrue(neighbors.contains(cells.get(3)));
      assertTrue(neighbors.contains(cells.get(5)));
      assertTrue(neighbors.contains(cells.get(6)));
      assertTrue(neighbors.contains(cells.get(7)));
      assertTrue(neighbors.contains(cells.get(8)));

      LangtonCell cell = grid.getCell(1, 1);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(7)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(8)));
    }

    @Test
    @DisplayName("Grid throws exception if initialize grid gets negative rows or columns")
    void initializeGrid_NegativeRowsOrCols_ThrowsException() {
      assertThrows(SimulationException.class,
          () -> new Grid<LangtonCell>(cells, -1, 3, ShapeType.RECTANGLE, NeighborhoodType.MOORE,
              EdgeType.NONE));
      assertThrows(SimulationException.class,
          () -> new Grid<LangtonCell>(cells, 3, -1, ShapeType.RECTANGLE, NeighborhoodType.MOORE,
              EdgeType.NONE));
    }

    @Test
    @DisplayName("Grid throws exception when initalize cells fails because cells is null")
    void validateCells_NullCells_ThrowsException() {
      assertThrows(SimulationException.class,
          () -> new Grid<LangtonCell>(null, 3, 3, ShapeType.RECTANGLE, NeighborhoodType.MOORE,
              EdgeType.NONE));
    }

    @Test
    @DisplayName("Grid throws exception when initialize cells fails because cell size does not match expected size")
    void validateCells_MisMatchCellCount_ThrowsException() {
      assertThrows(SimulationException.class,
          () -> new Grid<LangtonCell>(cells, 3, 4, ShapeType.RECTANGLE, NeighborhoodType.MOORE,
              EdgeType.NONE));
    }

    @Test
    @DisplayName("SetNeighborsAllCells correctly switches cell neighborhood after initialization")
    void setNeighborsAllCells_NewNeighborhoodTopology_ResetsNeighborsCorrectly() {
      grid.setNeighborsAllCells(ShapeType.RECTANGLE, NeighborhoodType.VON_NEUMANN, EdgeType.NONE);

      assertEquals(4, grid.getNeighbors(1, 1).size());
      assertEquals(2, grid.getNeighbors(0, 0).size());
      assertEquals(3, grid.getNeighbors(0, 1).size());

      List<LangtonCell> neighbors = grid.getNeighbors(1, 1);
      assertTrue(neighbors.contains(cells.get(1)));
      assertTrue(neighbors.contains(cells.get(3)));
      assertTrue(neighbors.contains(cells.get(5)));
      assertTrue(neighbors.contains(cells.get(7)));

      LangtonCell cell = grid.getCell(1, 1);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(7)));
    }

    @Test
    @DisplayName("SetNeighborsAllCells throws exception if cannot find direction set")
    void setNeighborsAllCells_InvalidGradeShapeNeighborhood_ThrowsException() {
      try (MockedStatic<GridDirectionRegistry> mockedRegistry = mockStatic(
          GridDirectionRegistry.class)) {
        mockedRegistry.when(
                () -> GridDirectionRegistry.getDirections(any(), any(), anyInt(), anyInt()))
            .thenReturn(Optional.empty());

        assertThrows(SimulationException.class,
            () -> grid.setNeighborsAllCells(ShapeType.RECTANGLE, NeighborhoodType.MOORE,
                EdgeType.NONE));
      }
    }

    @Test
    @DisplayName("SetNeighborsAllCells thorws exception if edge handler cannot find edge type")
    void setNeighborsAllCells_InvalidEdgeType_ThrowsException() {
      try (MockedStatic<EdgeFactory> mockedEdgeFactory = mockStatic(EdgeFactory.class)) {
        mockedEdgeFactory.when(() -> EdgeFactory.getHandler(any())).thenReturn(Optional.empty());

        assertThrows(SimulationException.class,
            () -> grid.setNeighborsAllCells(ShapeType.RECTANGLE, NeighborhoodType.MOORE,
                EdgeType.NONE));
      }
    }

    @Test
    @DisplayName("SetNeighborsAllCells throws exception if cannot compute direction")
    void setNeighborsAllCells_InvalidDirection_ThrowsException() {
      try (MockedStatic<DirectionRegistry> mockedDirectionRegistry = mockStatic(
          DirectionRegistry.class)) {
        mockedDirectionRegistry.when(() -> DirectionRegistry.getDirection(any()))
            .thenReturn(Optional.empty());

        assertThrows(SimulationException.class,
            () -> grid.setNeighborsAllCells(ShapeType.RECTANGLE, NeighborhoodType.MOORE,
                EdgeType.NONE));
      }
    }

    @Test
    @DisplayName("GetCell throws exception if not valid position")
    void getCell_InvalidPosition_ThrowsException() {
      assertThrows(SimulationException.class, () -> grid.getCell(3, 0));
    }

  }

  @Nested
  @DisplayName("Tests for just rectangular setup")
  class RectangleGridTest {

    RockPaperScissRule rule;

    @BeforeEach
    void setUp() {
      GenericParameters parameters = new GenericParameters(RockPaperSciss,
          Map.of("numStates", 25.));
      rule = new RockPaperScissRule(parameters);
    }

    // rectangle moore none done by above

    @Test
    @DisplayName("Grid Rectangle, VonNeumann, None is set up properly")
    void grid_RectangleVonNeumannNone_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.RECTANGLE,
          NeighborhoodType.VON_NEUMANN, EdgeType.NONE);

      // check set neighborhoods
      assertEquals(4, grid.getNeighbors(1, 1).size());
      assertEquals(2, grid.getNeighbors(0, 0).size());
      assertEquals(3, grid.getNeighbors(0, 1).size());

      RockPaperScissCell cell = grid.getCell(1, 1);
      // only checking directions because of how niehgbors are generated based on what is in directions
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(7)));
    }

    @Test
    @DisplayName("Grid Rectangle, ExtendedMoore, None is set up properly if not enough cells for full neighborhood")
    void grid_RectangleExtendedMooreNoneIncompleteNeighborhood_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.RECTANGLE,
          NeighborhoodType.EXTENDED_MOORE, EdgeType.NONE);

      // should behave like moore
      assertEquals(8, grid.getNeighbors(1, 1).size());
      assertEquals(8, grid.getNeighbors(0, 0).size());
      assertEquals(8, grid.getNeighbors(0, 1).size());

      RockPaperScissCell cell = grid.getCell(1, 1);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(7)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(8)));
    }

    @Test
    @DisplayName("Grid Rectangle, ExtendedMoore, None is set up properly")
    void grid_RectangleExtendedMooreNonCompleteNeighborhood_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule), new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule),
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 5, 5, ShapeType.RECTANGLE,
          NeighborhoodType.EXTENDED_MOORE, EdgeType.NONE);

      assertEquals(24, grid.getNeighbors(2, 2).size()); // full neighborhood
      assertEquals(8, grid.getNeighbors(0, 0).size());  // corner
      assertEquals(11, grid.getNeighbors(0, 1).size()); // edge corner
      assertEquals(15, grid.getNeighbors(1, 1).size()); // corner diagonal
      assertEquals(14, grid.getNeighbors(0, 2).size()); // edge edge
      assertEquals(19, grid.getNeighbors(1, 2).size()); // edge edge + 1

      RockPaperScissCell cell = grid.getCell(2, 2);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(4)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(7)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(8)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(9)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(13)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(14)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(15)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(16)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(17)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(18)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(19)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(20)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(21)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(22)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(23)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(24)));
    }


    @Test
    @DisplayName("Grid Rectangle, Moore, Toroidal is set up properly")
    void grid_RectangleMooreToroidal_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.RECTANGLE,
          NeighborhoodType.MOORE, EdgeType.TOROIDAL);

      // all cells should have complete neighborhoods
      assertEquals(8, grid.getNeighbors(1, 1).size());
      assertEquals(8, grid.getNeighbors(0, 0).size());
      assertEquals(8, grid.getNeighbors(0, 1).size());

      // going to do corner since that should capture the most behavior
      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(8)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(7)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(4)));
    }

    @Test
    @DisplayName("Grid Rectangle, VonNeumann, Toroidal is set up properly")
    void grid_RectangleVonNeumannToroidal_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.RECTANGLE,
          NeighborhoodType.VON_NEUMANN, EdgeType.TOROIDAL);

      // all cells should have complete neighborhoods
      assertEquals(4, grid.getNeighbors(1, 1).size());
      assertEquals(4, grid.getNeighbors(0, 0).size());
      assertEquals(4, grid.getNeighbors(0, 1).size());

      // going to do corner since that should capture the most behavior
      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(3)));
    }

    @Test
    @DisplayName("Grid Rectangle, ExtendedMoore, Toroidal is set up properly")
    void grid_RectangleExtendedMooreToroidal_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule),
          new RockPaperScissCell(15, rule), new RockPaperScissCell(16, rule),
          new RockPaperScissCell(17, rule), new RockPaperScissCell(18, rule),
          new RockPaperScissCell(19, rule),
          new RockPaperScissCell(20, rule), new RockPaperScissCell(21, rule),
          new RockPaperScissCell(22, rule), new RockPaperScissCell(23, rule),
          new RockPaperScissCell(24, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 5, 5, ShapeType.RECTANGLE,
          NeighborhoodType.EXTENDED_MOORE, EdgeType.TOROIDAL);

      assertEquals(24, grid.getNeighbors(2, 2).size()); // full neighborhood
      assertEquals(24, grid.getNeighbors(0, 0).size());  // corner
      assertEquals(24, grid.getNeighbors(0, 1).size()); // edge corner
      assertEquals(24, grid.getNeighbors(1, 1).size()); // corner diagonal
      assertEquals(24, grid.getNeighbors(0, 2).size()); // edge edge
      assertEquals(24, grid.getNeighbors(1, 2).size()); // edge edge + 1

      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(18)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(19)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(15)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(16)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(17)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(23)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(24)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(20)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(21)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(22)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(4)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(2)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(8)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(7)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(13)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(14)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(12)));
    }

    @Test
    @DisplayName("Grid Rectangle, Moore, Mirror is set up properly")
    void grid_RectangleMooreMirror_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.RECTANGLE,
          NeighborhoodType.MOORE, EdgeType.MIRROR);

      // all cells should have complete neighborhoods
      assertEquals(8, grid.getNeighbors(1, 1).size());
      assertEquals(8, grid.getNeighbors(0, 0).size());
      assertEquals(8, grid.getNeighbors(0, 1).size());

      // going to do corner since that should capture the most behavior
      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(4)));
    }

    @Test
    @DisplayName("Grid Rectangle, VonNeumann, Mirror is set up properly")
    void grid_RectangleVonNeumannMirror_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.RECTANGLE,
          NeighborhoodType.VON_NEUMANN, EdgeType.MIRROR);

      // all cells should have complete neighborhoods
      assertEquals(4, grid.getNeighbors(1, 1).size());
      assertEquals(4, grid.getNeighbors(0, 0).size());
      assertEquals(4, grid.getNeighbors(0, 1).size());

      // going to do corner since that should capture the most behavior
      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(3)));
    }

    @Test
    @DisplayName("Grid Rectangle, ExtendedMoore, Mirror is set up properly")
    void grid_RectangleExtendedMooreMirror_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule),
          new RockPaperScissCell(15, rule), new RockPaperScissCell(16, rule),
          new RockPaperScissCell(17, rule), new RockPaperScissCell(18, rule),
          new RockPaperScissCell(19, rule),
          new RockPaperScissCell(20, rule), new RockPaperScissCell(21, rule),
          new RockPaperScissCell(22, rule), new RockPaperScissCell(23, rule),
          new RockPaperScissCell(24, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 5, 5, ShapeType.RECTANGLE,
          NeighborhoodType.EXTENDED_MOORE, EdgeType.MIRROR);

      assertEquals(24, grid.getNeighbors(2, 2).size()); // full neighborhood
      assertEquals(24, grid.getNeighbors(0, 0).size());  // corner
      assertEquals(24, grid.getNeighbors(0, 1).size()); // edge corner
      assertEquals(24, grid.getNeighbors(1, 1).size()); // corner diagonal
      assertEquals(24, grid.getNeighbors(0, 2).size()); // edge edge
      assertEquals(24, grid.getNeighbors(1, 2).size()); // edge edge + 1

      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(7)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(2)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(2)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(7)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(12)));
    }
  }

  @Nested
  @DisplayName("Tests for just hexagon setup")
  class HexagonGridTest {

    RockPaperScissRule rule;

    @BeforeEach
    void setUp() {
      GenericParameters parameters = new GenericParameters(RockPaperSciss,
          Map.of("numStates", 25.));
      rule = new RockPaperScissRule(parameters);
    }

    @Test
    @DisplayName("Grid Hexagon, Moore, None is set up properly")
    void grid_HexagonMooreNone_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule),
          new RockPaperScissCell(15, rule), new RockPaperScissCell(16, rule),
          new RockPaperScissCell(17, rule), new RockPaperScissCell(18, rule),
          new RockPaperScissCell(18, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 4, 5, ShapeType.HEXAGON,
          NeighborhoodType.MOORE, EdgeType.NONE);

      // check set neighborhoods
      // this jis the most detail I'm going to get to say I have every case
      assertEquals(6, grid.getNeighbors(1, 1).size());  // center
      assertEquals(2, grid.getNeighbors(0, 0).size());  // even left corner
      assertEquals(4, grid.getNeighbors(0, 1).size());  // top/bottom edge
      assertEquals(3, grid.getNeighbors(0, 4).size());   // even right corner
      assertEquals(5, grid.getNeighbors(1, 0).size());   // odd side edge
      assertEquals(3, grid.getNeighbors(2, 0).size());   // even side edge
      assertEquals(3, grid.getNeighbors(3, 0).size());  // odd left corner
      assertEquals(2, grid.getNeighbors(3, 4).size());   // odd right corner

      // complete odd cell -- cell 6
      RockPaperScissCell cell = grid.getCell(1, 1);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).isEmpty());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(7)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).isEmpty());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(12)));
    }

    @Test
    @DisplayName("Grid Hexagon, VonNeumann, None is set up properly")
    void grid_HexagonVonNeumannNone_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule), new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.HEXAGON,
          NeighborhoodType.VON_NEUMANN, EdgeType.NONE);

      // check set neighborhoods
      assertEquals(2, grid.getNeighbors(1, 1).size());
      assertEquals(1, grid.getNeighbors(0, 0).size());
      assertEquals(2, grid.getNeighbors(0, 1).size());

      RockPaperScissCell cell = grid.getCell(1, 1);
      // only checking directions because of how niehgbors are generated based on what is in directions
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).isEmpty());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).isEmpty());
    }

    @Test
    @DisplayName("Grid Hexagon, ExtendedMoore, None is set up properly")
    void grid_HexagonExtendedMooreNone_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule),
          new RockPaperScissCell(15, rule), new RockPaperScissCell(16, rule),
          new RockPaperScissCell(17, rule), new RockPaperScissCell(18, rule),
          new RockPaperScissCell(18, rule),
          new RockPaperScissCell(20, rule), new RockPaperScissCell(21, rule),
          new RockPaperScissCell(22, rule), new RockPaperScissCell(23, rule),
          new RockPaperScissCell(24, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 5, 5, ShapeType.HEXAGON,
          NeighborhoodType.EXTENDED_MOORE, EdgeType.NONE);

      // should behave like moore
      assertEquals(18, grid.getNeighbors(2, 2).size());  // center
      assertEquals(6, grid.getNeighbors(0, 0).size());  // even left corner
      assertEquals(9, grid.getNeighbors(0, 1).size());  // top/bottom edge
      assertEquals(7, grid.getNeighbors(0, 4).size());   // even right corner
      assertEquals(10, grid.getNeighbors(1, 0).size());   // odd side edge
      assertEquals(10, grid.getNeighbors(2, 0).size());   // even side edge
      assertEquals(6, grid.getNeighbors(4, 0).size());  // odd left corner
      assertEquals(7, grid.getNeighbors(4, 4).size());   // odd right corner

      RockPaperScissCell cell = grid.getCell(2, 2);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(3)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(7)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(8)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(13)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(14)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(15)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(16)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(17)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(18)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(21)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(22)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(23)));
    }

    @Test
    @DisplayName("Grid Hexagon, Moore, Toroidal is set up properly")
    void grid_HexagonMooreToroidal_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule), new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule), new RockPaperScissCell(10, rule),
          new RockPaperScissCell(11, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 4, 3, ShapeType.HEXAGON,
          NeighborhoodType.MOORE, EdgeType.TOROIDAL);

      // check set neighborhoods
      assertEquals(6, grid.getNeighbors(1, 1).size());
      assertEquals(6, grid.getNeighbors(0, 0).size());
      assertEquals(6, grid.getNeighbors(0, 1).size());

      // corner behavior + even
      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).isEmpty());

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).isEmpty());

      // just to also check odd edge
      cell = grid.getCell(3, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).isEmpty());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(7)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(10)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).isEmpty());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(1)));
    }

    @Test
    @DisplayName("Grid Hexagon, VonNeumann, Toroidal is set up properly")
    void grid_HexagonVonNeumannToroidal_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule), new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.HEXAGON,
          NeighborhoodType.VON_NEUMANN, EdgeType.TOROIDAL);

      // check set neighborhoods
      assertEquals(2, grid.getNeighbors(1, 1).size());
      assertEquals(2, grid.getNeighbors(0, 0).size());
      assertEquals(2, grid.getNeighbors(0, 1).size());

      // corner behavior + even
      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));

      cell = grid.getCell(1, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(4)));
    }

    @Test
    @DisplayName("Grid Hexagon, ExtendedMoore, Toroidal is set up properly")
    void grid_HexagonExtendedMooreToroidal_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule),
          new RockPaperScissCell(15, rule), new RockPaperScissCell(16, rule),
          new RockPaperScissCell(17, rule), new RockPaperScissCell(18, rule),
          new RockPaperScissCell(19, rule),
          new RockPaperScissCell(20, rule), new RockPaperScissCell(21, rule),
          new RockPaperScissCell(22, rule), new RockPaperScissCell(23, rule),
          new RockPaperScissCell(24, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 5, 5, ShapeType.HEXAGON,
          NeighborhoodType.EXTENDED_MOORE, EdgeType.TOROIDAL);

      // should behave like moore
      assertEquals(18, grid.getNeighbors(2, 2).size());  // center
      assertEquals(18, grid.getNeighbors(0, 0).size());  // even left corner
      assertEquals(18, grid.getNeighbors(0, 1).size());  // top/bottom edge
      assertEquals(18, grid.getNeighbors(0, 4).size());   // even right corner
      assertEquals(18, grid.getNeighbors(1, 0).size());   // odd side edge
      assertEquals(18, grid.getNeighbors(2, 0).size());   // even side edge
      assertEquals(18, grid.getNeighbors(4, 0).size());  // odd left corner
      assertEquals(18, grid.getNeighbors(4, 4).size());   // odd right corner

      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(19)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(15)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(16)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(23)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(24)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(20)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(21)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(4)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(2)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(8)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(6)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(14)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(11)));

      // you only going to get one out of me for this
    }


    @Test
    @DisplayName("Grid Hexagon, Moore, Mirror is set up properly")
    void grid_HexagonMooreMirror_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule), new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule), new RockPaperScissCell(10, rule),
          new RockPaperScissCell(11, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 4, 3, ShapeType.HEXAGON,
          NeighborhoodType.MOORE, EdgeType.MIRROR);

      // check set neighborhoods
      assertEquals(6, grid.getNeighbors(1, 1).size());
      assertEquals(6, grid.getNeighbors(0, 0).size());
      assertEquals(6, grid.getNeighbors(0, 1).size());

      // corner behavior + even
      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).isEmpty());

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).isEmpty());

      // just to also check odd edge
      cell = grid.getCell(3, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).isEmpty());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(7)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(10)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).isEmpty());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(10)));
    }

    @Test
    @DisplayName("Grid Hexagon, VonNeumann, Mirror is set up properly")
    void grid_HexagonVonNeumannMirror_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule), new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.HEXAGON,
          NeighborhoodType.VON_NEUMANN, EdgeType.MIRROR);

      // check set neighborhoods
      assertEquals(2, grid.getNeighbors(1, 1).size());
      assertEquals(2, grid.getNeighbors(0, 0).size());
      assertEquals(2, grid.getNeighbors(0, 1).size());

      // corner behavior + even
      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));

      cell = grid.getCell(1, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(4)));
    }

    @Test
    @DisplayName("Grid Hexagon, ExtendedMoore, Mirror is set up properly")
    void grid_HexagonExtendedMooreMirror_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule),
          new RockPaperScissCell(15, rule), new RockPaperScissCell(16, rule),
          new RockPaperScissCell(17, rule), new RockPaperScissCell(18, rule),
          new RockPaperScissCell(19, rule),
          new RockPaperScissCell(20, rule), new RockPaperScissCell(21, rule),
          new RockPaperScissCell(22, rule), new RockPaperScissCell(23, rule),
          new RockPaperScissCell(24, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 5, 5, ShapeType.HEXAGON,
          NeighborhoodType.EXTENDED_MOORE, EdgeType.MIRROR);

      // should behave like moore
      assertEquals(18, grid.getNeighbors(2, 2).size());  // center
      assertEquals(18, grid.getNeighbors(0, 0).size());  // even left corner
      assertEquals(18, grid.getNeighbors(0, 1).size());  // top/bottom edge
      assertEquals(18, grid.getNeighbors(0, 4).size());   // even right corner
      assertEquals(18, grid.getNeighbors(1, 0).size());   // odd side edge
      assertEquals(18, grid.getNeighbors(2, 0).size());   // even side edge
      assertEquals(18, grid.getNeighbors(4, 0).size());  // odd left corner
      assertEquals(18, grid.getNeighbors(4, 4).size());   // odd right corner

      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(6)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(1)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(2)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(6)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(11)));

      // you only going to get one out of me for this
    }
  }

  @Nested
  @DisplayName("Tests for just triangle setup")
  class TriangleGridTest {

    RockPaperScissRule rule;

    @BeforeEach
    void setUp() {
      GenericParameters parameters = new GenericParameters(RockPaperSciss,
          Map.of("numStates", 45.));
      rule = new RockPaperScissRule(parameters);
    }

    @Test
    @DisplayName("Grid Triangle, Moore, None is set up properly")
    void grid_TriangleMooreNone_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 5, ShapeType.TRIANGLE,
          NeighborhoodType.MOORE, EdgeType.NONE);

      assertEquals(12, grid.getNeighbors(1, 2).size());
      assertEquals(5, grid.getNeighbors(0, 0).size());
      assertEquals(6, grid.getNeighbors(0, 1).size());
      assertEquals(9, grid.getNeighbors(0, 2).size());
      assertEquals(7, grid.getNeighbors(1, 0).size());
      assertEquals(10, grid.getNeighbors(1, 1).size());

      RockPaperScissCell cell = grid.getCell(1, 2);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(4)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(8)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(9)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(12)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(13)));
    }

    @Test
    @DisplayName("Grid Triangle, VonNeumann, None is set up properly")
    void grid_TriangleVonNeumannNone_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule), new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.TRIANGLE,
          NeighborhoodType.VON_NEUMANN, EdgeType.NONE);

      // check set neighborhoods
      assertEquals(3, grid.getNeighbors(1, 1).size());
      assertEquals(2, grid.getNeighbors(0, 0).size());
      assertEquals(2, grid.getNeighbors(0, 1).size());
      assertEquals(2, grid.getNeighbors(1, 0).size());
      assertEquals(3, grid.getNeighbors(2, 1).size());

      RockPaperScissCell cell = grid.getCell(1, 1);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).isEmpty());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(7)));
    }

    @Test
    @DisplayName("Grid Triangle, ExtendedMoore, None is set up properly")
    void grid_TriangleExtendedMooreNone_SetsUpNeighborsCorrectly() {
      // realized I should have done this in the before each, but I'm so close to being done
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule), new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule), new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule), new RockPaperScissCell(15, rule),
          new RockPaperScissCell(16, rule), new RockPaperScissCell(17, rule),
          new RockPaperScissCell(18, rule), new RockPaperScissCell(18, rule),
          new RockPaperScissCell(20, rule), new RockPaperScissCell(21, rule),
          new RockPaperScissCell(22, rule), new RockPaperScissCell(23, rule),
          new RockPaperScissCell(24, rule), new RockPaperScissCell(25, rule),
          new RockPaperScissCell(26, rule), new RockPaperScissCell(27, rule),
          new RockPaperScissCell(28, rule), new RockPaperScissCell(29, rule),
          new RockPaperScissCell(30, rule), new RockPaperScissCell(31, rule),
          new RockPaperScissCell(32, rule), new RockPaperScissCell(33, rule),
          new RockPaperScissCell(34, rule), new RockPaperScissCell(35, rule),
          new RockPaperScissCell(36, rule), new RockPaperScissCell(37, rule),
          new RockPaperScissCell(38, rule), new RockPaperScissCell(39, rule),
          new RockPaperScissCell(40, rule), new RockPaperScissCell(41, rule),
          new RockPaperScissCell(42, rule), new RockPaperScissCell(43, rule),
          new RockPaperScissCell(44, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 5, 9, ShapeType.TRIANGLE,
          NeighborhoodType.EXTENDED_MOORE, EdgeType.NONE);

      // should behave like moore
      assertEquals(36, grid.getNeighbors(2, 4).size());
      assertEquals(34, grid.getNeighbors(2, 5).size());
      assertEquals(29, grid.getNeighbors(1, 4).size());
      assertEquals(31, grid.getNeighbors(3, 4).size());

      RockPaperScissCell cell = grid.getCell(2, 4);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(4)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(6)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(12)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(13)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(14)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(15)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(16)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(18)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(19)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(20)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(21)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(23)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(24)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(25)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(26)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(27)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(28)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(29)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(30)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(31)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(32)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(33)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(34)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(35)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(37)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(38)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(39)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(40)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(41)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(42)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(43)));
    }

    @Test
    @DisplayName("Grid Triangle, Moore, Toroidal is set up properly")
    void grid_TriangleMooreToroidal_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 5, ShapeType.TRIANGLE,
          NeighborhoodType.MOORE, EdgeType.TOROIDAL);

      // check set neighborhoods
      assertEquals(12, grid.getNeighbors(1, 2).size());
      assertEquals(12, grid.getNeighbors(0, 0).size());
      assertEquals(12, grid.getNeighbors(0, 1).size());
      assertEquals(12, grid.getNeighbors(0, 2).size());
      assertEquals(12, grid.getNeighbors(1, 0).size());
      assertEquals(12, grid.getNeighbors(1, 1).size());

      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(14)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(11)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(4)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(2)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(8)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(7)));

    }

    @Test
    @DisplayName("Grid Triangle, VonNeumann, Toroidal is set up properly")
    void grid_TriangleVonNeumannToroidal_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule), new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.TRIANGLE,
          NeighborhoodType.VON_NEUMANN, EdgeType.TOROIDAL);

      // check set neighborhoods
      assertEquals(3, grid.getNeighbors(1, 1).size());
      assertEquals(3, grid.getNeighbors(0, 0).size());
      assertEquals(3, grid.getNeighbors(0, 1).size());
      assertEquals(3, grid.getNeighbors(1, 0).size());
      assertEquals(3, grid.getNeighbors(2, 1).size());

      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).isEmpty());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(3)));

      cell = grid.getCell(0, 1);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(7)));
    }

    @Test
    @DisplayName("Grid Triangle, ExtendedMoore, Toroidal is set up properly")
    void grid_TriangleExtendedMooreToroidal_SetsUpNeighborsCorrectly() {
      // realized I should have done this in the before each, but I'm so close to being done
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule), new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule), new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule), new RockPaperScissCell(15, rule),
          new RockPaperScissCell(16, rule), new RockPaperScissCell(17, rule),
          new RockPaperScissCell(18, rule), new RockPaperScissCell(18, rule),
          new RockPaperScissCell(20, rule), new RockPaperScissCell(21, rule),
          new RockPaperScissCell(22, rule), new RockPaperScissCell(23, rule),
          new RockPaperScissCell(24, rule), new RockPaperScissCell(25, rule),
          new RockPaperScissCell(26, rule), new RockPaperScissCell(27, rule),
          new RockPaperScissCell(28, rule), new RockPaperScissCell(29, rule),
          new RockPaperScissCell(30, rule), new RockPaperScissCell(31, rule),
          new RockPaperScissCell(32, rule), new RockPaperScissCell(33, rule),
          new RockPaperScissCell(34, rule), new RockPaperScissCell(35, rule),
          new RockPaperScissCell(36, rule), new RockPaperScissCell(37, rule),
          new RockPaperScissCell(38, rule), new RockPaperScissCell(39, rule),
          new RockPaperScissCell(40, rule), new RockPaperScissCell(41, rule),
          new RockPaperScissCell(42, rule), new RockPaperScissCell(43, rule),
          new RockPaperScissCell(44, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 5, 9, ShapeType.TRIANGLE,
          NeighborhoodType.EXTENDED_MOORE, EdgeType.TOROIDAL);

      // should behave like moore
      assertEquals(36, grid.getNeighbors(2, 4).size());
      assertEquals(36, grid.getNeighbors(2, 5).size());
      assertEquals(36, grid.getNeighbors(1, 4).size());
      assertEquals(36, grid.getNeighbors(3, 4).size());

      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(34)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(35)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(27)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(28)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(29)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(42)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(43)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(44)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(36)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(37)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(38)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(39)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(7)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(8)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(4)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(14)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(15)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(16)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(17)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(12)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(13)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(24)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(25)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(26)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(18)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(19)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(20)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(21)));
    }

    @Test
    @DisplayName("Grid Triangle, Moore, Mirror is set up properly")
    void grid_TriangleMooreMirror_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule), new RockPaperScissCell(6, rule),
          new RockPaperScissCell(7, rule), new RockPaperScissCell(8, rule),
          new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 5, ShapeType.TRIANGLE,
          NeighborhoodType.MOORE, EdgeType.MIRROR);

      // check set neighborhoods
      assertEquals(12, grid.getNeighbors(1, 2).size());
      assertEquals(12, grid.getNeighbors(0, 0).size());
      assertEquals(12, grid.getNeighbors(0, 1).size());
      assertEquals(12, grid.getNeighbors(0, 2).size());
      assertEquals(12, grid.getNeighbors(1, 0).size());
      assertEquals(12, grid.getNeighbors(1, 1).size());

      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(1)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(2)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(5)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(6)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(7)));

    }

    @Test
    @DisplayName("Grid Triangle, VonNeumann, Mirror is set up properly")
    void grid_TriangleVonNeumannMirror_SetsUpNeighborsCorrectly() {
      List<RockPaperScissCell> cells = List.of(new RockPaperScissCell(0, rule),
          new RockPaperScissCell(1, rule), new RockPaperScissCell(2, rule),
          new RockPaperScissCell(3, rule), new RockPaperScissCell(4, rule),
          new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule));
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 3, 3, ShapeType.TRIANGLE,
          NeighborhoodType.VON_NEUMANN, EdgeType.MIRROR);

      // check set neighborhoods
      assertEquals(3, grid.getNeighbors(1, 1).size());
      assertEquals(3, grid.getNeighbors(0, 0).size());
      assertEquals(3, grid.getNeighbors(0, 1).size());
      assertEquals(3, grid.getNeighbors(1, 0).size());
      assertEquals(3, grid.getNeighbors(2, 1).size());

      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).isEmpty());
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(3)));

      cell = grid.getCell(0, 1);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(1)));
    }

    @Test
    @DisplayName("Grid Triangle, ExtendedMoore, Mirror is set up properly")
    void grid_TriangleExtendedMooreMirror_SetsUpNeighborsCorrectly() {
      // realized I should have done this in the before each, but I'm so close to being done
      List<RockPaperScissCell> cells = List.of(
          new RockPaperScissCell(0, rule), new RockPaperScissCell(1, rule),
          new RockPaperScissCell(2, rule), new RockPaperScissCell(3, rule),
          new RockPaperScissCell(4, rule), new RockPaperScissCell(5, rule),
          new RockPaperScissCell(6, rule), new RockPaperScissCell(7, rule),
          new RockPaperScissCell(8, rule), new RockPaperScissCell(9, rule),
          new RockPaperScissCell(10, rule), new RockPaperScissCell(11, rule),
          new RockPaperScissCell(12, rule), new RockPaperScissCell(13, rule),
          new RockPaperScissCell(14, rule), new RockPaperScissCell(15, rule),
          new RockPaperScissCell(16, rule), new RockPaperScissCell(17, rule),
          new RockPaperScissCell(18, rule), new RockPaperScissCell(18, rule),
          new RockPaperScissCell(20, rule), new RockPaperScissCell(21, rule),
          new RockPaperScissCell(22, rule), new RockPaperScissCell(23, rule),
          new RockPaperScissCell(24, rule), new RockPaperScissCell(25, rule),
          new RockPaperScissCell(26, rule), new RockPaperScissCell(27, rule),
          new RockPaperScissCell(28, rule), new RockPaperScissCell(29, rule),
          new RockPaperScissCell(30, rule), new RockPaperScissCell(31, rule),
          new RockPaperScissCell(32, rule), new RockPaperScissCell(33, rule),
          new RockPaperScissCell(34, rule), new RockPaperScissCell(35, rule),
          new RockPaperScissCell(36, rule), new RockPaperScissCell(37, rule),
          new RockPaperScissCell(38, rule), new RockPaperScissCell(39, rule),
          new RockPaperScissCell(40, rule), new RockPaperScissCell(41, rule),
          new RockPaperScissCell(42, rule), new RockPaperScissCell(43, rule),
          new RockPaperScissCell(44, rule)
      );
      Grid<RockPaperScissCell> grid = new Grid<RockPaperScissCell>(cells, 5, 9, ShapeType.TRIANGLE,
          NeighborhoodType.EXTENDED_MOORE, EdgeType.MIRROR);

      // should behave like moore
      assertEquals(36, grid.getNeighbors(2, 4).size());
      assertEquals(36, grid.getNeighbors(2, 5).size());
      assertEquals(36, grid.getNeighbors(1, 4).size());
      assertEquals(36, grid.getNeighbors(3, 4).size());

      RockPaperScissCell cell = grid.getCell(0, 0);
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(11)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NW).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.N).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.NE).contains(cells.get(3)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.W).contains(cells.get(0)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(1)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(2)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(3)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.E).contains(cells.get(4)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(12)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(9)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(10)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(11)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(12)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(13)));

      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(20)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(19)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SW).contains(cells.get(18)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.S).contains(cells.get(18)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(19)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(20)));
      assertTrue(cell.getDirectionalNeighbors(DirectionType.SE).contains(cells.get(21)));
    }
  }

}
