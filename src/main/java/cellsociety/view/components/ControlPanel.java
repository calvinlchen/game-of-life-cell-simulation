package cellsociety.view.components;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Manages control buttons and status messages. (VBox suggested by ChatGPT.)
 */
public class ControlPanel {
  public static final int VBOX_SPACING = 12;

  private final VBox myPanel;
  private final UserView myUserView;
  private final List<Button> myButtons;

  /**
   * Contains a column of control buttons for Cell Society
   * @param userView the view controller through which a button initiates its action when clicked
   */
  public ControlPanel(UserView userView) {
    myUserView = userView;
    myPanel = new VBox(VBOX_SPACING);
    myButtons = new ArrayList<>();
    initializeControls();
  }

  private void initializeControls() {
    Button playButton = new Button("Play");
    playButton.setOnAction(e -> myUserView.playSimulation());
    myButtons.add(playButton);

    Button pauseButton = new Button("Pause");
    pauseButton.setOnAction(e -> myUserView.pauseSimulation());
    myButtons.add(pauseButton);

    Button clearButton = new Button("Clear Grid");
    clearButton.setOnAction(e -> myUserView.stopAndResetSimulation());
    myButtons.add(clearButton);

    Button loadButton = new Button("Load New File");
    loadButton.setOnAction(e -> myUserView.loadSimulation());
    myButtons.add(loadButton);

    Button saveButton = new Button("Save As");
    saveButton.setOnAction(e -> myUserView.saveSimulation());
    myButtons.add(saveButton);

    Button randomButton = new Button("Random Game of Life");
    randomButton.setOnAction(e -> myUserView.loadRandomGameOfLife());
    myButtons.add(randomButton);

    myPanel.getChildren().addAll(myButtons);

    // Panel containing buttons for speeding up / slowing down the simulations
    HBox speedPanel = makeSpeedPanel();
    myPanel.getChildren().addAll(speedPanel);
  }

  private HBox makeSpeedPanel() {
    HBox speedPanel = new HBox((double)VBOX_SPACING/2);

    Button speedUpButton = new Button("Speed Up");
    speedUpButton.setOnAction(e -> myUserView.changeSimulationSpeed(2.0));
    Button slowDownButton = new Button("Slow Down");
    slowDownButton.setOnAction(e -> myUserView.changeSimulationSpeed(0.5));
    speedPanel.getChildren().addAll(speedUpButton, slowDownButton);

    return speedPanel;
  }

  /**
   * Return the vertical panel of control buttons
   * @return VBox panel view containing all button elements
   */
  public VBox getPanel() {
    return myPanel;
  }
}
