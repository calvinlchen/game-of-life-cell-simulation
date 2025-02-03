package cellsociety.view.components;

import cellsociety.model.util.constants.CellStates.GameOfLifeStates;
import cellsociety.view.interfaces.CellView;
import javafx.scene.paint.Color;

public class GameOfLifeCellView extends CellView<GameOfLifeStates> {

  public GameOfLifeCellView(double x, double y, double width, double height, GameOfLifeStates cellState) {
    super(x, y, width, height, cellState);
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
