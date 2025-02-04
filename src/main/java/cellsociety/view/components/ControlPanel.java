package cellsociety.view.components;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Manages control buttons and status messages. (VBox suggested by ChatGPT.)
 */
public class ControlPanel {
  private VBox myPanel;
  private UserView myUserView;
  private Button myPlayButton;
  private Button myPauseButton;
  private Button myStopButton;
  private Button myLoadButton;
  private Button mySaveButton;
  private Button myRandomButton;
  private HBox mySpeedPanel;
  private TextArea myStatusTextBox;

  public ControlPanel(UserView userView) {
    this.myUserView = userView;
    myPanel = new VBox(10);
    initializeControls();
  }

  private void initializeControls() {
    myPlayButton = new Button("Play");
    myPlayButton.setOnAction(e -> myUserView.playSimulation());

    myPauseButton = new Button("Pause");
    myPauseButton.setOnAction(e -> myUserView.pauseSimulation());

    myStopButton = new Button("Reset");
    myStopButton.setOnAction(e -> myUserView.stopAndResetSimulation());

    myLoadButton = new Button("Load New");
    myLoadButton.setOnAction(e -> myUserView.loadSimulation());

    mySaveButton = new Button("Save");
    mySaveButton.setOnAction(e -> myUserView.saveSimulation());

    myRandomButton = new Button("Random Game of Life");
    myRandomButton.setOnAction(e -> myUserView.loadRandomGameOfLife());

    mySpeedPanel = makeSpeedPanel();

    myStatusTextBox = new TextArea();
    myStatusTextBox.setPrefHeight(400);
    myStatusTextBox.setPrefWidth(150);
    myStatusTextBox.setEditable(false);

    myPanel.getChildren().addAll(myPlayButton, myPauseButton, myStopButton, myLoadButton,
        mySaveButton, myRandomButton, mySpeedPanel, myStatusTextBox);
  }

  private HBox makeSpeedPanel() {
    HBox speedPanel = new HBox(10);

    Button speedUpButton = new Button("Speed Up");
    speedUpButton.setOnAction(e -> myUserView.changeSimulationSpeed(2.0));
    Button slowDownButton = new Button("Slow Down");
    slowDownButton.setOnAction(e -> myUserView.changeSimulationSpeed(0.5));
    speedPanel.getChildren().addAll(speedUpButton, slowDownButton);

    return speedPanel;
  }

  public VBox getPanel() {
    return myPanel;
  }
}
