package cellsociety.view.components;

import cellsociety.Main;
import javafx.scene.control.TextArea;

public class InformationBox {
  private final TextArea myTextArea;

  public InformationBox() {
    myTextArea = new TextArea();
    myTextArea.setPrefHeight(Main.SCENE_HEIGHT * (1 - UserView.GRID_PROPORTION_OF_SCREEN));
    myTextArea.setPrefWidth(Main.SCENE_WIDTH);
    myTextArea.setEditable(false);
  }

  public TextArea getTextArea() {
    return myTextArea;
  }
}
