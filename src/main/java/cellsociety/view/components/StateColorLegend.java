package cellsociety.view.components;

import static cellsociety.model.util.constants.ResourcePckg.getErrorSimulationResourceBundle;
import static cellsociety.view.interfaces.CellView.DEFAULT_OUTLINE_CLASS;

import cellsociety.Main;
import cellsociety.model.factories.statefactory.CellStateFactory;
import cellsociety.model.factories.statefactory.handler.CellStateHandler;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.XMLData;
import cellsociety.view.interfaces.CellView;
import cellsociety.view.window.UserView;
import java.util.ResourceBundle;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;

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
  private final ResourceBundle myResources;
  private final ResourceBundle myErrorResources;
  private final UserView myUserView;
  private XMLData myLastXMLData;
  private boolean colorPickerIsOpen = false;

  public static final int COLOR_BOX_LENGTH = 20;
  public static final int COLOR_PICKER_MAX_LENGTH = 4 * COLOR_BOX_LENGTH;

  public StateColorLegend(UserView userView, String language) {
    myLegendBox = new VBox();
    myLegendBox.setSpacing((double) ControlPanel.VBOX_SPACING / 2);

    myResources = ResourceBundle.getBundle(Main.DEFAULT_RESOURCE_PACKAGE + language);
    myErrorResources = getErrorSimulationResourceBundle(language);
    myUserView = userView;
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
   * Removes all elements from the legend display
   */
  public void clearLegend() {
    myLegendBox.getChildren().clear();
  }

  /**
   * Updates the legend based on the current simulation type based on an XMLData file.
   * @param xmlData - XMLData object containing complete fields
   */
  public void updateLegend(XMLData xmlData) {
    clearLegend();

    if (myUserView == null || myUserView.getCellViewList().isEmpty()) {
      return;
    }

    Text clickToChangeText = new Text(myResources.getString("ClickToChangeColors"));
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

    myLastXMLData = xmlData;
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
    }
    else {
      updateLegend(myLastXMLData); // if the color picker is already open, then close the color picker by resetting the legend
      colorPickerIsOpen = false;
    }
  }

  /**
   * Retrieves the appropriate `getColorForState()` method based on simulation type.
   * No longer in use since this only retrieves the default color mapping, not including any dynamic customizations
   */
  @Deprecated
  private Map<Integer, Color> getColorMapFromXML(XMLData xmlData) {
    SimType simulationType= xmlData.getType();

    CellStateHandler handler = CellStateFactory.getHandler(xmlData.getId(), simulationType,
        xmlData.getNumStates());
    if (handler == null) {
      throw new IllegalArgumentException(myErrorResources.getString("UnknownSimType") + simulationType);
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
   * Maps colors to their states according to the current simulation configuration
   */
  private Map<Integer, Color> getColorMapFromUserView(UserView userView) {
    Map<Integer, Color> colorMap = new HashMap<>();
    if (userView == null) {
      return colorMap;
    }

    CellView cellView = userView.getCellViewList().getFirst();
    for(int k = 0; k < cellView.getNumStates(); k++) {
      colorMap.put(k, cellView.getColorForState(k));
    }
    return colorMap;
  }

  /**
   * Maps each state value to its corresponding name.
   */
  private Map<Integer, String> getStateNameMap(XMLData xmlData) {
    if (xmlData == null) {
      return new HashMap<>();
    }

    SimType simulationType= xmlData.getType();
    
    CellStateHandler handler = CellStateFactory.getHandler(xmlData.getId(), simulationType,
        xmlData.getNumStates());
    if (handler == null) {
      throw new IllegalArgumentException(myErrorResources.getString("UnknownSimType") + simulationType);
    }

    Map<Integer, String> stateNameMap = new HashMap<>();
    for (int state : handler.getStateInt()) {
      stateNameMap.put(state, handler.statetoString(state));
    }
    return stateNameMap;
  }
}
