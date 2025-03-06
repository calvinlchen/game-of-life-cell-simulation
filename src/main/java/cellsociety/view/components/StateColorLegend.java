package cellsociety.view.components;

import static cellsociety.view.interfaces.CellView.DEFAULT_OUTLINE_CLASS;

import cellsociety.model.statefactory.CellStateFactory;
import cellsociety.model.statefactory.handler.CellStateHandler;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.XmlData;
import cellsociety.view.interfaces.CellView;
import cellsociety.view.utils.ResourceManager;
import cellsociety.view.utils.exceptions.ViewException;
import cellsociety.view.window.UserView;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;

import java.util.Map;
import java.util.HashMap;
import javafx.scene.text.Text;

/**
 * StateColorLegend dynamically displays state names and colors based on the loaded simulation
 * type.
 *
 * @author ChatGPT 4o
 */
public class StateColorLegend {

  private final VBox myLegendBox;
  private final UserView myUserView;
  private XmlData myLastXmlData;
  private boolean colorPickerIsOpen = false;

  public static final int COLOR_BOX_LENGTH = 20;
  public static final double LEGEND_MAX_HEIGHT = 200;
  public static final int COLOR_PICKER_MAX_LENGTH = 4 * COLOR_BOX_LENGTH;

  /**
   * Constructs a StateColorLegend that dynamically updates based on the simulation type.
   *
   * @param userView - the UserView instance that manages the simulation window
   */
  public StateColorLegend(UserView userView) {
    myLegendBox = new VBox();
    myLegendBox.setSpacing((double) ControlPanel.VBOX_SPACING / 2);
    myUserView = userView;
  }

  /**
   * Return a scrollable view of color-state legend.
   *
   * @return ScrollPane containing the color legend VBox
   */
  public ScrollPane getScrollableLegend() {
    ScrollPane scrollPane = new ScrollPane(myLegendBox);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefWidth(COLOR_BOX_LENGTH);
    scrollPane.setPrefHeight(LEGEND_MAX_HEIGHT);

    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    return scrollPane;
  }


  /**
   * Removes all elements from the legend display.
   */
  public void clearLegend() {
    myLegendBox.getChildren().clear();
  }

  /**
   * Updates the legend based on the current simulation type based on an XMLData file.
   *
   * @param xmlData - XMLData object containing complete fields
   */
  public void updateLegend(XmlData xmlData) {
    clearLegend();

    if (myUserView == null || myUserView.getCellViewList().isEmpty()) {
      return;
    }

    Text clickToChangeText = new Text(
        String.format(ResourceManager.getCurrentMainBundle().getString("ClickToChangeColors")));
    clickToChangeText.getStyleClass().add("bold-text");
    myLegendBox.getChildren().add(clickToChangeText);

    Map<Integer, String> stateNameMap = getStateNameMap(xmlData);
    Map<Integer, Color> stateColorMap = getColorMapFromUserView(myUserView);

    for (Map.Entry<Integer, String> entry : stateNameMap.entrySet()) {
      int stateValue = entry.getKey();
      String stateName = entry.getValue();
      Color stateColor = stateColorMap.getOrDefault(stateValue, Color.TRANSPARENT);

      HBox legendItem = createLegendItem(stateColor, stateName, stateValue);
      myLegendBox.getChildren().add(legendItem);
    }

    myLastXmlData = xmlData;
  }

  private HBox createLegendItem(Color stateColor, String stateName, int stateValue) {
    HBox legendItem = new HBox();
    legendItem.setSpacing(10);

    // Create color preview
    Rectangle colorBox = new Rectangle(COLOR_BOX_LENGTH, COLOR_BOX_LENGTH, stateColor);
    colorBox.getStyleClass().addAll(DEFAULT_OUTLINE_CLASS); // add outline according to CSS styling

    colorBox.setOnMouseClicked(e -> openColorPicker(colorBox, stateValue));

    // Create label
    Label stateLabel = new Label(stateName);

    // Add to HBox
    legendItem.getChildren().addAll(colorBox, stateLabel);
    return legendItem;
  }

  /**
   * Opens a ColorPicker to change the state's color and updates the simulation view.
   */
  private void openColorPicker(Rectangle colorBox, int stateValue) {
    if (!colorPickerIsOpen) {
      colorPickerIsOpen = true;

      ColorPicker colorPicker = new ColorPicker((Color) colorBox.getFill());
      colorPicker.setMaxWidth(COLOR_PICKER_MAX_LENGTH);
      // Add the ColorPicker temporarily to the legend
      HBox parent = (HBox) colorBox.getParent();
      parent.getChildren().add(colorPicker);
      colorPicker.show();

      colorPicker.setOnAction(event -> {
        Color newColor = colorPicker.getValue();
        colorBox.setFill(newColor);  // Update legend color
        myUserView.updateColorForState(stateValue, newColor);  // Update simulation cells
        parent.getChildren().remove(colorPicker);

        colorPickerIsOpen = false;
      });
    } else {
      // if the color picker is already open, then close the color picker by resetting the legend
      updateLegend(myLastXmlData);
      colorPickerIsOpen = false;
    }
  }

  /**
   * Maps colors to their states according to the current simulation configuration.
   */
  private Map<Integer, Color> getColorMapFromUserView(UserView userView) {
    Map<Integer, Color> colorMap = new HashMap<>();
    if (userView == null) {
      return colorMap;
    }

    CellView cellView = userView.getCellViewList().getFirst();
    for (int k = 0; k < cellView.getNumStates(); k++) {
      colorMap.put(k, cellView.getColorForState(k));
    }
    return colorMap;
  }

  /**
   * Maps each state value to its corresponding name.
   */
  private Map<Integer, String> getStateNameMap(XmlData xmlData) {
    if (xmlData == null) {
      return new HashMap<>();
    }

    SimType simulationType = xmlData.getType();

    CellStateHandler handler = CellStateFactory.getHandler(xmlData.getId(), simulationType,
        myUserView.getCellViewList().getFirst().getNumStates());
    if (handler == null) {
      throw new ViewException("UnknownSimType", simulationType);
    }

    Map<Integer, String> stateNameMap = new HashMap<>();
    for (int state : handler.getStateInt()) {
      stateNameMap.put(state, handler.statetoString(state));
    }
    return stateNameMap;
  }
}
