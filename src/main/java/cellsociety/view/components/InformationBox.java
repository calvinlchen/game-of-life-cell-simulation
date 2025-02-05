package cellsociety.view.components;

import cellsociety.Main;
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
  }

  /**
   * Return text/information box view
   * @return TextArea object
   */
  public TextArea getTextArea() {
    return myTextArea;
  }
}
