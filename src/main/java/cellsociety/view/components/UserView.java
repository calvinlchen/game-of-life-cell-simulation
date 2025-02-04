package cellsociety.view.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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

  private int mySceneWidth;
  private int mySceneHeight;
  private ViewState myState;
  private Stage myStage;
  private BorderPane myRoot;
  private Timeline myAnimation;

  // Components of the UI
  private SimulationView mySimulationView;
  private ControlPanel myControlPanel;

  public UserView(int sceneWidth, int sceneHeight, Stage stage) {
    myStage = stage;
    mySceneWidth = sceneWidth;
    mySceneHeight = sceneHeight;
    resetView();
  }

  /**
   * Sets view to its initial state with an empty grid area.
   */
  public void resetView() {
    myState = ViewState.EMPTY;
    myRoot = new BorderPane();

    // Initialize UI components
    mySimulationView = new SimulationView((mySceneWidth*2)/3, (mySceneHeight*2)/3);
    myControlPanel = new ControlPanel(this); // Pass reference for event handling

    // Set components in BorderPane
    myRoot.setLeft(mySimulationView.getDisplay());
    myRoot.setRight(myControlPanel.getPanel());

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

  /**
   * Starts the simulation animation.
   */
  public void startSimulation() {
    myState = ViewState.RUN;
    myAnimation = new Timeline(new KeyFrame(Duration.seconds(1),
        e -> mySimulationView.stepGridSimulation()));
    myAnimation.setCycleCount(Timeline.INDEFINITE);
    myAnimation.play();
  }

  /**
   * Pauses the simulation animation.
   */
  public void pauseSimulation() {
    myState = ViewState.PAUSE;
    if (myAnimation != null) {
      myAnimation.pause();
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
  }

  /**
   * Requests a file to be loaded into the simulation.
   */
  public void loadSimulation() {
    myState = ViewState.LOAD;
  }

  /**
   * Requests a simulation to be saved.
   */
  public void saveSimulation() {
    myState = ViewState.SAVE;
  }
}
