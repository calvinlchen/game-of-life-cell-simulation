package cellsociety.view.components;

import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.CellStates.FireStates;
import cellsociety.model.util.constants.CellStates.GameOfLifeStates;
import cellsociety.model.util.constants.CellStates.PercolationStates;
import cellsociety.model.util.constants.CellStates.SegregationStates;
import cellsociety.model.util.constants.CellStates.WaTorStates;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

/**
 * StateColorLegend dynamically displays state names and colors based on the loaded simulation type.
 * @author ChatGPT 4o
 */
public class StateColorLegend {
  private final VBox myLegendBox;

  public StateColorLegend() {
    myLegendBox = new VBox();
    myLegendBox.setSpacing((double) ControlPanel.VBOX_SPACING/2);
  }

  /**
   * Return view of color-state legend
   * @return VBox object containing legend view
   */
  public VBox getLegendBox() {
    return myLegendBox;
  }

  /**
   * Updates the legend based on the current simulation type.
   */
  public void updateLegend(SimType simulationType) {
    myLegendBox.getChildren().clear();

    Map<String, Color> stateColorMap = getStateColorMap(simulationType);

    for (Map.Entry<String, Color> entry : stateColorMap.entrySet()) {
      String stateName = entry.getKey();
      Color stateColor = entry.getValue();

      HBox legendItem = new HBox();
      legendItem.setSpacing(10);

      // Create color preview
      Rectangle colorBox = new Rectangle(20, 20, stateColor);
      colorBox.setStroke(Color.BLACK);

      // Create label
      Label stateLabel = new Label(stateName);

      // Add to HBox
      legendItem.getChildren().addAll(colorBox, stateLabel);
      myLegendBox.getChildren().add(legendItem);
    }
  }

  /**
   * Uses each simulation type’s corresponding CellView subclass to get colors dynamically.
   */
  private Map<String, Color> getStateColorMap(SimType simulationType) {
    Map<String, Color> stateColorMap = new HashMap<>();

    Function<Enum<?>, Color> colorFunction = getColorFunction(simulationType);

    for (Enum<?> state : getStateEnums(simulationType)) {
      stateColorMap.put(state.name(), colorFunction.apply(state));
    }

    return stateColorMap;
  }

  /**
   * Retrieves the appropriate `getColorForState()` method based on simulation type.
   */
  private Function<Enum<?>, Color> getColorFunction(SimType simulationType) {
    return switch (simulationType) {
      case GAMEOFLIFE -> state -> new GameOfLifeCellView(0, 0, 0, 0, (GameOfLifeStates) state).getColorForState((GameOfLifeStates) state);
      case FIRE -> state -> new FireCellView(0, 0, 0, 0, (FireStates) state).getColorForState((FireStates) state);
      case PERCOLATION -> state -> new PercolationCellView(0, 0, 0, 0, (PercolationStates) state).getColorForState((PercolationStates) state);
      case SEGREGATION -> state -> new SegregationCellView(0, 0, 0, 0, (SegregationStates) state).getColorForState((SegregationStates) state);
      case WATOR -> state -> new WaTorCellView(0, 0, 0, 0, (WaTorStates) state).getColorForState((WaTorStates) state);
    };
  }

  /**
   * Retrieves the correct Enum values for each simulation type.
   */
  private Enum<?>[] getStateEnums(SimType simulationType) {
    return switch (simulationType) {
      case GAMEOFLIFE -> cellsociety.model.util.constants.CellStates.GameOfLifeStates.values();
      case FIRE -> cellsociety.model.util.constants.CellStates.FireStates.values();
      case PERCOLATION -> cellsociety.model.util.constants.CellStates.PercolationStates.values();
      case SEGREGATION -> cellsociety.model.util.constants.CellStates.SegregationStates.values();
      case WATOR -> cellsociety.model.util.constants.CellStates.WaTorStates.values();
    };
  }
}
