package cellsociety.view.components;

import cellsociety.Main;
import cellsociety.model.util.XMLData;
import java.util.Map;
import javafx.scene.control.TextArea;

public class InformationBox {

  private final TextArea myTextArea;

  /**
   * Creates the text box which displays current simulation info
   */
  public InformationBox() {
    myTextArea = new TextArea();
    myTextArea.setPrefHeight(Main.SCENE_HEIGHT * (1 - UserView.GRID_PROPORTION_OF_SCREEN));
    myTextArea.setPrefWidth(Main.SCENE_WIDTH);
    myTextArea.setEditable(false);
    emptyFields();
  }

  /**
   * Return text/information box view
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
  public void updateInfo(XMLData data) {
    StringBuilder infoText = new StringBuilder();
    infoText.append("Simulation Type: ").append(data.getType()).append("\n");
    infoText.append("Title: ").append(data.getTitle()).append("\n");
    infoText.append("Author: ").append(data.getAuthor()).append("\n");
    infoText.append("Grid Size: ").append(data.getGridRowNum()).append(" x ")
        .append(data.getGridColNum()).append("\n");
    infoText.append("Parameters: ");
    for (Map.Entry<String, Double> entry : data.getParameters().entrySet()) {
      infoText.append(entry.getKey()).append(": ").append(entry.getValue()).append(" // ");
    }
    infoText.append("\n");
    infoText.append("Description: ").append(data.getDescription()).append("\n");

    myTextArea.setText(infoText.toString());
  }

  /**
   * Resets the text area to default state, displaying no simulation info
   */
  public void emptyFields() {
    myTextArea.setText("""
        Simulation Type:
        Title:
        Author:
        Grid Size:
        Parameters:
        Description:
        """);
  }
}
