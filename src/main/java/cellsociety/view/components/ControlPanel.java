package cellsociety.view.components;

import cellsociety.Main;
import cellsociety.view.window.UserView;
import cellsociety.view.window.UserView.ViewState;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
  private final ResourceBundle myResources;
  private final List<Button> myButtons;

  /**
   * Contains a column of control buttons for Cell Society
   *
   * @param userView the view controller through which a button initiates its action when clicked
   */
  public ControlPanel(UserView userView) {
    myUserView = userView;
    myResources = userView.getResources();
    myPanel = new VBox(VBOX_SPACING);
    myButtons = new ArrayList<>();
    initializeControls();
  }

  private void initializeControls() {
    Button playButton = new Button(myResources.getString("PlayCommand"));
    playButton.setOnAction(e -> myUserView.playSimulation());
    myButtons.add(playButton);

    Button pauseButton = new Button(myResources.getString("PauseCommand"));
    pauseButton.setOnAction(e -> myUserView.pauseSimulation());
    myButtons.add(pauseButton);

    Button clearButton = new Button(myResources.getString("ClearCommand"));
    clearButton.setOnAction(e -> myUserView.stopAndResetSimulation());
    myButtons.add(clearButton);

    Button loadButton = new Button(myResources.getString("LoadFileCommand"));
    loadButton.setOnAction(e -> {
      if (myUserView.getState() == ViewState.EMPTY || myUserView.getState() == ViewState.ERROR) {
        myUserView.chooseFileAndLoadSimulation();
      }
      else {
        Main.startSimulationWindowWithFilePrompt(myUserView.getLanguage());
      }
    });
    myButtons.add(loadButton);

    Button saveButton = new Button(myResources.getString("SaveAsCommand"));
    saveButton.setOnAction(e -> myUserView.saveSimulation());
    myButtons.add(saveButton);

    Button randomButton = new Button(myResources.getString("RandomGameOfLifeCommand"));
    randomButton.setOnAction(e -> myUserView.loadRandomGameOfLife());
    myButtons.add(randomButton);

    myPanel.getChildren().addAll(myButtons);

    // Panel containing buttons for speeding up / slowing down the simulations
    HBox speedPanel = makeSpeedPanel();
    myPanel.getChildren().addAll(speedPanel);
  }

  private HBox makeSpeedPanel() {
    HBox speedPanel = new HBox((double) VBOX_SPACING / 2);

    Button speedUpButton = new Button(myResources.getString("SpeedUpCommand"));
    speedUpButton.setOnAction(e -> myUserView.changeSimulationSpeed(2.0));
    Button slowDownButton = new Button(myResources.getString("SlowDownCommand"));
    slowDownButton.setOnAction(e -> myUserView.changeSimulationSpeed(0.5));
    speedPanel.getChildren().addAll(speedUpButton, slowDownButton);

    return speedPanel;
  }

  /**
   * Return the vertical panel of control buttons
   *
   * @return VBox panel view containing all button elements
   */
  public VBox getPanel() {
    return myPanel;
  }
}
