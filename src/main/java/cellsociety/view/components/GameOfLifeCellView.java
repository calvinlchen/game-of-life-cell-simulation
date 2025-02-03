package cellsociety.view.components;

import cellsociety.model.simulation.cell.GameOfLifeCell;
import cellsociety.model.util.constants.CellStates.GameOfLifeStates;
import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class GameOfLifeCellView extends CellView<GameOfLifeStates, GameOfLifeCell> {

  public GameOfLifeCellView(double x, double y, double width, double height, GameOfLifeCell cell) {
    super(x, y, width, height, cell);
  }

  /**
   * Maps a Game of Life state to its corresponding color.
   */
  @Override
  protected Color getColorForState(GameOfLifeStates state) {
    return switch (state) {
      case ALIVE -> Color.GREEN;
      case DEAD -> DEFAULT_FILL;
    };
  }
}
