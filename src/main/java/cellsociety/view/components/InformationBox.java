package cellsociety.view.components;

import cellsociety.Main;
import cellsociety.model.util.XmlData;
import cellsociety.view.utils.ResourceManager;
import cellsociety.view.utils.SimViewConstants;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.TextArea;

/**
 * InformationBox displays the details of the currently loaded simulation.
 *
 * <p>This class creates a non-editable text area that provides metadata about
 * the simulation, including its type, title, author, grid size, parameters,
 * and description.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class InformationBox {

  private final TextArea myTextArea;

  /**
   * Creates the text box which displays current simulation info.
   */
  public InformationBox() {
    myTextArea = new TextArea();
    myTextArea.setPrefHeight(
        Main.SIM_WINDOW_HEIGHT * (1 - SimViewConstants.GRID_PROPORTION_OF_SCREEN));
    myTextArea.setPrefWidth(Main.SIM_WINDOW_WIDTH);
    myTextArea.setEditable(false);

    // Apply CSS class
    myTextArea.getStyleClass().add("info-box");

    emptyFields();
  }

  /**
   * Return text/information box view.
   *
   * @return TextArea object
   */
  public TextArea getTextArea() {
    return myTextArea;
  }

  /**
   * Updates the text area with the given XMLData's information.
   *
   * @param data XMLData containing the simulation's details.
   */
  public void updateInfo(XmlData data) {
    ResourceBundle resources = ResourceManager.getCurrentMainBundle();
    StringBuilder infoText = new StringBuilder();
    infoText.append(resources.getString("SimulationTypeHeader")).append(data.getType())
        .append("\n");
    infoText.append(resources.getString("TitleHeader")).append(data.getTitle()).append("\n");
    infoText.append(resources.getString("AuthorHeader")).append(data.getAuthor()).append("\n");
    infoText.append(resources.getString("GridSizeHeader")).append(data.getGridRowNum())
        .append(" x ").append(data.getGridColNum()).append("\n");
    infoText.append(resources.getString("ParametersHeader"));
    for (Map.Entry<String, Object> entry : data.getParameters().entrySet()) {
      infoText.append(entry.getKey()).append(": ").append(entry.getValue()).append(" // ");
    }
    infoText.append("\n");
    infoText.append(resources.getString("DescriptionHeader")).append(data.getDescription())
        .append("\n");

    myTextArea.setText(infoText.toString());
  }

  /**
   * Resets the text area to default state, displaying no simulation info.
   */
  public void emptyFields() {
    ResourceBundle resources = ResourceManager.getCurrentMainBundle();
    myTextArea.setText(
        resources.getString("SimulationTypeHeader") + "\n" + resources.getString("TitleHeader")
            + "\n" + resources.getString("AuthorHeader") + "\n" + resources.getString(
            "GridSizeHeader") + "\n" + resources.getString("ParametersHeader") + "\n"
            + resources.getString("DescriptionHeader") + "\n");
  }
}
