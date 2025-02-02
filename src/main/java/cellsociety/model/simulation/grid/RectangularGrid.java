package cellsociety.model.simulation.grid;

import cellsociety.model.interfaces.Grid;
import cellsociety.model.interfaces.Cell;
import cellsociety.model.util.constants.CellStates.GameOfLifeStates;
import cellsociety.model.util.constants.CellStates.SimulationTypes;
import java.util.ArrayList;
import java.util.List;

/**
 * RectangularGrid represents a 2D grid of cells
 *
 * <p>Default like what you think of when you think of a grid</p>
 *
 * @param <S> - the type of state for the cells, must be an Enum
 * @param <T> - the type of cell in the grid, must extend Cell<S>
 * @author Jessica Chen
 */
public class RectangularGrid<S extends Enum<S>, T extends Cell<S, T>> extends Grid<S, T> {

  /**
   * Constructs a RectangularGrid with specified dimensions.
   *
   * @param cells - cells to be added
   * @param rows  - number of rows in the grid
   * @param cols  - number of columns in the grid
   */
  public RectangularGrid(List<T> cells, int rows, int cols) {
   super(cells, rows, cols);
    setNeighbors();
  }

  /**
   * Set neighbors for all cells in the grid.
   *
   * <p> Neighbors are in all 8 directions
   */
  @Override
  public void setNeighbors() {
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    for (int i = 0; i < getRows(); i++) {
      for (int j = 0; j < getCols(); j++) {
        T cell = getGrid().get(i).get(j);
        if (cell != null) {
          List<T> neighbors = new ArrayList<>();
          for (int[] dir : directions) {
            int newRow = i + dir[0];
            int newCol = j + dir[1];
            if (isValidPosition(newRow, newCol)) {
              neighbors.add(getGrid().get(newRow).get(newCol));
            }
          }
          cell.setNeighbors(neighbors);
        }
      }
    }
  }
}
