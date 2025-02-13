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

    Map<Integer, String> stateNameMap = getStateNameMap(simulationType);
    Map<Integer, Color> stateColorMap = getStateColorMap(simulationType);

    for (Map.Entry<Integer, String> entry : stateNameMap.entrySet()) {
      int stateValue = entry.getKey();
      String stateName = entry.getValue();
      Color stateColor = stateColorMap.getOrDefault(stateValue, Color.GRAY);

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
   * Retrieves the appropriate `getColorForState()` method based on simulation type.
   */
  private Map<Integer, Color> getStateColorMap(SimType simulationType) {
    Map<Integer, Color> stateColorMap = new HashMap<>();

    switch (simulationType) {
      case GAMEOFLIFE -> {
        stateColorMap.put(0, new GameOfLifeCellView(0, 0, 0, 0, 0).getColorForState(0));
        stateColorMap.put(1, new GameOfLifeCellView(0, 0, 0, 0, 1).getColorForState(1));
      }
      case FIRE -> {
        stateColorMap.put(0, new FireCellView(0, 0, 0, 0, 0).getColorForState(0));
        stateColorMap.put(1, new FireCellView(0, 0, 0, 0, 1).getColorForState(1));
        stateColorMap.put(2, new FireCellView(0, 0, 0, 0, 2).getColorForState(2));
      }
      case PERCOLATION -> {
        stateColorMap.put(0, new PercolationCellView(0, 0, 0, 0, 0).getColorForState(0));
        stateColorMap.put(1, new PercolationCellView(0, 0, 0, 0, 1).getColorForState(1));
        stateColorMap.put(2, new PercolationCellView(0, 0, 0, 0, 2).getColorForState(2));
      }
      case SEGREGATION -> {
        stateColorMap.put(0, new SegregationCellView(0, 0, 0, 0, 0).getColorForState(0));
        stateColorMap.put(1, new SegregationCellView(0, 0, 0, 0, 1).getColorForState(1));
        stateColorMap.put(2, new SegregationCellView(0, 0, 0, 0, 2).getColorForState(2));
      }
      case WATOR -> {
        stateColorMap.put(0, new WaTorCellView(0, 0, 0, 0, 0).getColorForState(0));
        stateColorMap.put(1, new WaTorCellView(0, 0, 0, 0, 1).getColorForState(1));
        stateColorMap.put(2, new WaTorCellView(0, 0, 0, 0, 2).getColorForState(2));
      }
    }

    return stateColorMap;
  }

  /**
   * Maps each state value to its corresponding name.
   */
  private Map<Integer, String> getStateNameMap(SimType simulationType) {
    return switch (simulationType) {
      case GAMEOFLIFE -> Map.of(0, "Dead", 1, "Alive");
      case FIRE -> Map.of(0, "Empty", 1, "Tree", 2, "Burning");
      case PERCOLATION -> Map.of(0, "Blocked", 1, "Open", 2, "Percolated");
      case SEGREGATION -> Map.of(0, "Empty", 1, "Group A", 2, "Group B");
      case WATOR -> Map.of(0, "Empty", 1, "Fish", 2, "Shark");
    };
  }
}
