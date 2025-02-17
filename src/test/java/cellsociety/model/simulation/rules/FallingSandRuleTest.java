package cellsociety.model.simulation.rules;

import static org.junit.jupiter.api.Assertions.*;
import static cellsociety.model.util.constants.CellStates.*;

import cellsociety.model.simulation.cell.FallingSandCell;
import cellsociety.model.simulation.parameters.FallingSandParameters;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for FallingSandRule
 */
class FallingSandRuleTest {

  private FallingSandRule rule;

  @BeforeEach
  void setUp() {
    rule = new FallingSandRule(new FallingSandParameters());
  }

  @Test
  @DisplayName("Sand in middle moves down when empty")
  void sandMiddleCell_MovesDown() {
    var result = createCellWithNeighbors(FALLINGSAND_SAND, FALLINGSAND_EMPTY, List.of(FALLINGSAND_EMPTY, FALLINGSAND_EMPTY, FALLINGSAND_EMPTY, FALLINGSAND_EMPTY));
    FallingSandCell cell = result.cell;
    List<FallingSandCell> neighbors = result.neighbors;
    assertEquals(FALLINGSAND_EMPTY, rule.apply(cell));
    assertEquals(FALLINGSAND_SAND, neighbors.getFirst().getNextState());
  }

  @Test
  @DisplayName("Sand in middle moves down when water below")
  void sandMiddleCell_MovesDown_WaterBelow() {
    var result = createCellWithNeighbors(FALLINGSAND_SAND, FALLINGSAND_WATER, List.of(FALLINGSAND_EMPTY, FALLINGSAND_EMPTY, FALLINGSAND_EMPTY, FALLINGSAND_EMPTY));
    FallingSandCell cell = result.cell;
    List<FallingSandCell> neighbors = result.neighbors;
    assertEquals(FALLINGSAND_WATER, rule.apply(cell));
    assertEquals(FALLINGSAND_SAND, neighbors.getFirst().getNextState());
  }

  @Test
  @DisplayName("Sand in middle moves diagonally if directly below is blocked")
  void sandMiddleCell_MovesDiagonally() {
    var result = createCellWithNeighbors(FALLINGSAND_SAND, FALLINGSAND_SAND, List.of(FALLINGSAND_EMPTY, FALLINGSAND_STEEL, FALLINGSAND_EMPTY, FALLINGSAND_EMPTY));
    FallingSandCell cell = result.cell;
    List<FallingSandCell> neighbors = result.neighbors;
    assertEquals(FALLINGSAND_EMPTY, rule.apply(cell));
    assertEquals(FALLINGSAND_SAND, neighbors.get(4).getNextState());
  }

  @Test
  @DisplayName("Sand in middle stays if no empty spaces arround it")
  void sandMiddleCell_StaysInPlace() {
    var result = createCellWithNeighbors(FALLINGSAND_SAND, FALLINGSAND_SAND, List.of(FALLINGSAND_EMPTY, FALLINGSAND_STEEL, FALLINGSAND_EMPTY, FALLINGSAND_SAND));
    FallingSandCell cell = result.cell;
    assertEquals(FALLINGSAND_SAND, rule.apply(cell));
  }

  @Test
  @DisplayName("Water in middle moves down when empty")
  void waterMiddleCell_MovesDown() {
    var result = createCellWithNeighbors(FALLINGSAND_WATER, FALLINGSAND_EMPTY, List.of(FALLINGSAND_EMPTY, FALLINGSAND_EMPTY, FALLINGSAND_EMPTY, FALLINGSAND_EMPTY));
    FallingSandCell cell = result.cell;
    assertEquals(FALLINGSAND_EMPTY, rule.apply(cell));
  }

  @Test
  @DisplayName("Water in middle moves sideways if blocked below")
  void waterMiddleCell_MovesSideways() {
    var result = createCellWithNeighbors(FALLINGSAND_WATER, FALLINGSAND_SAND, List.of(FALLINGSAND_STEEL, FALLINGSAND_EMPTY, FALLINGSAND_EMPTY, FALLINGSAND_EMPTY));
    FallingSandCell cell = result.cell;
    List<FallingSandCell> neighbors = result.neighbors;
    assertEquals(FALLINGSAND_EMPTY, rule.apply(cell));
    assertEquals(FALLINGSAND_WATER, neighbors.get(3).getNextState());
  }

  @Test
  @DisplayName("Water in middle stays if no empty spaces")
  void waterMiddleCell_StaysInPlace() {
    var result = createCellWithNeighbors(FALLINGSAND_WATER, FALLINGSAND_SAND, List.of(FALLINGSAND_SAND, FALLINGSAND_STEEL, FALLINGSAND_SAND, FALLINGSAND_STEEL));
    FallingSandCell cell = result.cell;
    assertEquals(FALLINGSAND_WATER, rule.apply(cell));
  }

  @Test
  @DisplayName("Steel cell remains steel")
  void steelCell_RemainsSteel() {
    var result = createCellWithNeighbors(FALLINGSAND_STEEL, FALLINGSAND_EMPTY, List.of(FALLINGSAND_EMPTY));
    FallingSandCell cell = result.cell;
    assertEquals(FALLINGSAND_STEEL, rule.apply(cell));
  }

  @Test
  @DisplayName("Empty cell remains empty")
  void emptyCell_RemainsEmpty() {
    var result = createCellWithNeighbors(FALLINGSAND_EMPTY, FALLINGSAND_EMPTY, List.of(FALLINGSAND_EMPTY));
    FallingSandCell cell = result.cell;
    assertEquals(FALLINGSAND_EMPTY, rule.apply(cell));
  }

  // generated with the help of chatgpt o return cell and neighbors
  // order of neighbors is below, right, bottom right, left, bottom left
  private record CellWithNeighbors(FallingSandCell cell, List<FallingSandCell> neighbors) {}

  private CellWithNeighbors createCellWithNeighbors(int state, int belowState, List<Integer> sideStates) {
    FallingSandCell cell = new FallingSandCell(state, rule);
    cell.setPosition(new int[]{2,2});
    List<FallingSandCell> neighbors = new ArrayList<>();

    FallingSandCell below = new FallingSandCell(belowState, rule);
    below.setPosition(new int[]{2,3});
    neighbors.add(below);

    int[][] positions = {{3,2}, {3,3}, {1,2}, {1,3}};
    for (int i = 0; i < sideStates.size(); i++) {
      FallingSandCell side = new FallingSandCell(sideStates.get(i), rule);
      side.setPosition(positions[i]);
      neighbors.add(side);
    }

    cell.setNeighbors(neighbors);
    return new CellWithNeighbors(cell, neighbors);
  }
}
