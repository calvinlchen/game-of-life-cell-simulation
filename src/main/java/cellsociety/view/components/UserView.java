package cellsociety.view.components;

import cellsociety.model.util.XMLData;
import cellsociety.model.util.XMLUtils;
import cellsociety.model.util.constants.exceptions.XMLException;
import cellsociety.view.utils.DateTime;
import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * UserView manages the main display window for the simulation.
 */
public class UserView {
  /**
   * Defines the possible states of the program
   */
  public enum ViewState {
    EMPTY, LOAD, RUN, PAUSE, ERROR, SAVE
  }

  public static final String TITLE = "Cell Society";
  public static final double GRID_PROPORTION_OF_SCREEN = 0.85;
  public static final double DEFAULT_SIM_STEP_TIME = 0.5; // in seconds
  public static final double MIN_SIM_STEP_TIME = 0.02;
  public static final double MAX_SIM_STEP_TIME = 4;

  private final int mySceneWidth;
  private final int mySceneHeight;
  private final Stage myStage;

  private ViewState myState;
  private BorderPane myRoot;
  private Timeline myAnimation;
  // DEFAULT_SIM_STEP_TIME is DIVIDED by mySpeedFactor to get number of seconds between steps.
  private double mySpeedFactor;

  // Components of the UI
  private SimulationView mySimulationView;
  private ControlPanel myControlPanel;
  private final XMLUtils xmlUtils = new XMLUtils();

  public UserView(int sceneWidth, int sceneHeight, Stage stage) {
    myStage = stage;
    mySceneWidth = sceneWidth;
    mySceneHeight = sceneHeight;

    // by default, run simulation at default speed
    mySpeedFactor = 1;
    resetView();
  }

  /**
   * Sets view to its initial state with an empty grid area.
   */
  public void resetView() {
    myState = ViewState.EMPTY;
    myRoot = new BorderPane();

    // Initialize UI components
    mySimulationView = new SimulationView(mySceneWidth*GRID_PROPORTION_OF_SCREEN,
        mySceneHeight*GRID_PROPORTION_OF_SCREEN);
    myControlPanel = new ControlPanel(this); // Pass reference for event handling

    // Set components in BorderPane
    myRoot.setLeft(mySimulationView.getDisplay());
    myRoot.setRight(myControlPanel.getPanel());

    // Set simulation speed to default
    mySpeedFactor = 1;

    initializeScene();
  }

  /**
   * Initializes the scene.
   */
  private void initializeScene() {
    Scene scene = new Scene(myRoot, mySceneWidth, mySceneHeight);
    myStage.setScene(scene);
    myStage.setTitle(TITLE);
    myStage.show();
  }

  private boolean checkSimulationExists() {
    return (mySimulationView != null && mySimulationView.getSimulation() != null);
  }

  /**
   * Starts the simulation animation.
   */
  public void playSimulation() {
    if (!checkSimulationExists()) {
      // No simulation is loaded, so do nothing
      return;
    }

    if (myAnimation != null && myState == ViewState.RUN) {
      // If the animation is already running, do nothing
      return;
    }

    if (myAnimation != null && myState == ViewState.PAUSE) {
      // If the animation is paused, resume it
      myAnimation.stop();
      // Account for changes to simulation speed while paused (user hitting speed up / slow down buttons)
      playNewAnimation();
      return;
    }

    // Otherwise, create a new animation and start it
    myAnimation = new Timeline(new KeyFrame(Duration.seconds(DEFAULT_SIM_STEP_TIME / mySpeedFactor),
        e -> mySimulationView.stepGridSimulation()));
    myAnimation.setCycleCount(Timeline.INDEFINITE);
    myAnimation.play();
    myState = ViewState.RUN;
  }

  /**
   * Pauses the simulation animation.
   */
  public void pauseSimulation() {
    if (myAnimation != null) {
      myAnimation.pause();
      myState = ViewState.PAUSE;
    }
  }

  /**
   * Stops and resets the simulation.
   */
  public void stopAndResetSimulation() {
    myState = ViewState.EMPTY;
    if (myAnimation != null) {
      myAnimation.stop();
    }
    mySimulationView.resetGrid();
    mySpeedFactor = 1;
  }

  /**
   * Requests a file to be loaded into the simulation.
   */
  public void loadSimulation() {
    File dataFile = FileExplorer.getFileLoadChooser().showOpenDialog(myStage);
    if (dataFile != null) {
      System.out.println("Loading file: " + dataFile.getName());
      try {
        stopAndResetSimulation();
        myState = ViewState.LOAD;
        // Upload simulation to mySimulationView
        mySimulationView.configureFromXML(xmlUtils.readXML(dataFile));
        mySimulationView.initializeGridView();
      }
      catch (XMLException e) {
        myState = ViewState.ERROR;
        showMessage(AlertType.ERROR, e.getMessage());
      }
    }
  }

  // display given message to user using the given type of Alert dialog box
  public static void showMessage (AlertType type, String message) {
    new Alert(type, message).showAndWait();
  }

  /**
   * Requests a simulation to be saved.
   */
  public void saveSimulation() {
    if (!checkSimulationExists()) {
      showMessage(Alert.AlertType.WARNING, "No simulation to save.");
      return;
    }

    pauseSimulation();

    // Open file chooser for saving
    FileChooser fileChooser = FileExplorer.getSaveFileChooser();
    fileChooser.setInitialFileName("simulation_" + DateTime.getLocalDateTime() + ".xml"); // Default filename
    File saveFile = fileChooser.showSaveDialog(myStage);

    if (saveFile != null) {
      myState = ViewState.SAVE;
      try {
        xmlUtils.writeToXML(saveFile,
            mySimulationView.getSimulation().getXMLData().getTitle(),
            mySimulationView.getSimulation().getXMLData().getAuthor(),
            mySimulationView.getSimulation().getXMLData().getDescription(),
            mySimulationView.getSimulation());
        showMessage(Alert.AlertType.INFORMATION, "Simulation saved successfully!");
      } catch (Exception e) {
        showMessage(Alert.AlertType.ERROR, "Error saving simulation: " + e.getMessage());
      }
    }
    // Return myState to paused state.
    pauseSimulation();
  }

  /**
   * Change the speed of simulation view stepper based on an adjustmentFactor multiplier.
   * @param adjustmentFactor Speed multiplier value. For example 2.0 will result in the animation running twice as fast.
   */
  public void changeSimulationSpeed(double adjustmentFactor) {
    if (!checkSimulationExists()) {
      return;
    }
    double newSpeedFactor = mySpeedFactor * adjustmentFactor;
    // Only change the animation speed if the new speed is within bounds
    if (checkStepTimeWithinBounds(DEFAULT_SIM_STEP_TIME / newSpeedFactor)) {
      mySpeedFactor = newSpeedFactor;
      if(myAnimation != null) {
        if(myState == ViewState.RUN) {
          playNewAnimation();
        }
      }
    }
  }

  private boolean checkStepTimeWithinBounds(double stepTime) {
    return (stepTime >= MIN_SIM_STEP_TIME && stepTime <= MAX_SIM_STEP_TIME);
  }

  /**
   * Initializes an animation based on the current mySpeedFactor
   */
  private void playNewAnimation() {
    if (myAnimation != null) {
      myAnimation.stop();
    }
    myAnimation = new Timeline(new KeyFrame(Duration.seconds(DEFAULT_SIM_STEP_TIME / mySpeedFactor),
        e -> mySimulationView.stepGridSimulation()));
    myAnimation.setCycleCount(Timeline.INDEFINITE);
    myAnimation.play();
    myState = ViewState.RUN;
  }

  /**
   * Loads a GAME OF LIFE simulation with random cell state layout. Code generated by ChatGPT.
   */
  public void loadRandomGameOfLife() {
    stopAndResetSimulation();

    XMLData randomXMLData = RandomSimulationGenerator.createRandomGameOfLifeXML();

    mySimulationView.configureFromXML(randomXMLData);
    mySimulationView.initializeGridView();
    myState = ViewState.LOAD;
  }
}
