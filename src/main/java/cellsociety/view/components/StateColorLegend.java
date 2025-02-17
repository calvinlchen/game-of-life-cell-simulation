package cellsociety.view.components;

import cellsociety.model.factories.statefactory.CellStateFactory;
import cellsociety.model.factories.statefactory.handler.CellStateHandler;
import cellsociety.model.factories.statefactory.handler.CellStateHandlerStatic;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.XMLData;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;

import java.util.Map;
import java.util.HashMap;

/**
 * StateColorLegend dynamically displays state names and colors based on the loaded simulation
 * type.
 *
 * @author ChatGPT 4o
 */
public class StateColorLegend {

  private final VBox myLegendBox;

  public StateColorLegend() {
    myLegendBox = new VBox();
    myLegendBox.setSpacing((double) ControlPanel.VBOX_SPACING / 2);
  }

  /**
   * Return view of color-state legend
   *
   * @return VBox object containing legend view
   */
  public VBox getLegendBox() {
    return myLegendBox;
  }

  /**
   * Updates the legend based on the current simulation type.
   */
  public void updateLegend(XMLData xmlData) {
    myLegendBox.getChildren().clear();

    Map<Integer, String> stateNameMap = getStateNameMap(xmlData);
    Map<Integer, Color> stateColorMap = getStateColorMap(xmlData);

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
  private Map<Integer, Color> getStateColorMap(XMLData xmlData) {
    SimType simulationType= xmlData.getType();

    CellStateHandler handler = CellStateFactory.getHandler(xmlData.getId(), simulationType,
        xmlData.getNumStates());
    if (handler == null) {
      throw new IllegalArgumentException("Unknown simulation type: " + simulationType);
    }

    Map<Integer, Color> stateColorMap = new HashMap<>();
    for (int state : handler.getStateInt()) {
      stateColorMap.put(state,
          CellViewFactory.createCellView(simulationType, new double[]{0, 0}, 0, 0, state)
              .getColorForState(state));
    }
    return stateColorMap;
  }

  /**
   * Maps each state value to its corresponding name.
   */
  private Map<Integer, String> getStateNameMap(XMLData xmlData) {
    SimType simulationType= xmlData.getType();
    
    CellStateHandler handler = CellStateFactory.getHandler(xmlData.getId(), simulationType,
        xmlData.getNumStates());
    if (handler == null) {
      throw new IllegalArgumentException("Unknown simulation type: " + simulationType);
    }

    Map<Integer, String> stateNameMap = new HashMap<>();
    for (int state : handler.getStateInt()) {
      stateNameMap.put(state, handler.statetoString(state));
    }
    return stateNameMap;
  }
}
