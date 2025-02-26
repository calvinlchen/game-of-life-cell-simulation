package cellsociety.view.components;

import cellsociety.Main;
import cellsociety.model.util.XmlData;
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
  private final ResourceBundle myResources;

  /**
   * Creates the text box which displays current simulation info.
   */
  public InformationBox(ResourceBundle resources) {
    myTextArea = new TextArea();
    myTextArea.setPrefHeight(
        Main.SIM_WINDOW_HEIGHT * (1 - SimViewConstants.GRID_PROPORTION_OF_SCREEN));
    myTextArea.setPrefWidth(Main.SIM_WINDOW_WIDTH);
    myTextArea.setEditable(false);

    // Apply CSS class
    myTextArea.getStyleClass().add("info-box");

    // Info will display in UI language
    myResources = resources;

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
    StringBuilder infoText = new StringBuilder();
    infoText.append(myResources.getString("SimulationTypeHeader")).append(data.getType())
        .append("\n");
    infoText.append(myResources.getString("TitleHeader")).append(data.getTitle()).append("\n");
    infoText.append(myResources.getString("AuthorHeader")).append(data.getAuthor()).append("\n");
    infoText.append(myResources.getString("GridSizeHeader")).append(data.getGridRowNum())
        .append(" x ").append(data.getGridColNum()).append("\n");
    infoText.append(myResources.getString("ParametersHeader"));
    for (Map.Entry<String, Double> entry : data.getParameters().entrySet()) {
      infoText.append(entry.getKey()).append(": ").append(entry.getValue()).append(" // ");
    }
    infoText.append("\n");
    infoText.append(myResources.getString("DescriptionHeader")).append(data.getDescription())
        .append("\n");

    myTextArea.setText(infoText.toString());
  }

  /**
   * Resets the text area to default state, displaying no simulation info.
   */
  public void emptyFields() {
    myTextArea.setText(
        myResources.getString("SimulationTypeHeader") + "\n" + myResources.getString("TitleHeader")
            + "\n" + myResources.getString("AuthorHeader") + "\n" + myResources.getString(
            "GridSizeHeader") + "\n" + myResources.getString("ParametersHeader") + "\n"
            + myResources.getString("DescriptionHeader") + "\n");
  }
}
