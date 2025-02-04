package cellsociety.view.components;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Manages control buttons and status messages. (VBox suggested by ChatGPT.)
 */
public class ControlPanel {
  private VBox myPanel;
  private UserView myUserView;
  private Button myStartButton;
  private Button myPauseButton;
  private Button myStopButton;
  private Button myLoadButton;
  private Button mySaveButton;
  private TextArea myStatusTextBox;

  public ControlPanel(UserView userView) {
    this.myUserView = userView;
    myPanel = new VBox(10);
    initializeControls();
  }

  private void initializeControls() {
    myStartButton = new Button("Start");
    myStartButton.setOnAction(e -> myUserView.startSimulation());

    myPauseButton = new Button("Pause");
    myPauseButton.setOnAction(e -> myUserView.pauseSimulation());

    myStopButton = new Button("Reset");
    myStopButton.setOnAction(e -> myUserView.stopAndResetSimulation());

    myLoadButton = new Button("Load New");
    myLoadButton.setOnAction(e -> myUserView.loadSimulation());

    mySaveButton = new Button("Save");
    mySaveButton.setOnAction(e -> myUserView.saveSimulation());

    myStatusTextBox = new TextArea();
    myStatusTextBox.setPrefHeight(400);
    myStatusTextBox.setPrefWidth(150);
    myStatusTextBox.setEditable(false);

    myPanel.getChildren().addAll(myStartButton, myPauseButton, myStopButton, myLoadButton,
        mySaveButton, myStatusTextBox);
  }

  public VBox getPanel() {
    return myPanel;
  }
}
