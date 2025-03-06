package cellsociety.view.window;

import cellsociety.model.util.XmlData;
import cellsociety.model.util.XmlUtils;
import cellsociety.model.util.exceptions.SimulationException;
import cellsociety.model.util.exceptions.XmlException;
import cellsociety.view.components.ControlPanel;
import cellsociety.view.components.SimulationViewZoomable;
import cellsociety.view.utils.FileExplorer;
import cellsociety.view.components.InformationBox;
import cellsociety.view.components.RandomSimulationGenerator;
import cellsociety.view.components.StateColorLegend;
import cellsociety.view.interfaces.CellView;
import cellsociety.view.utils.DateTime;
import cellsociety.view.utils.ResourceManager;
import cellsociety.view.utils.SimViewConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * UserView manages the main display window for the simulation.
 *
 * <p>This class is responsible for rendering the simulation, managing user interactions,
 * handling file operations, and controlling simulation speed and state.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class UserView {

  /**
   * Defines the possible states of the program.
   */
  public enum ViewState {
    EMPTY, LOAD, RUN, PAUSE, ERROR, SAVE
  }

  private final int mySceneWidth;
  private final int mySceneHeight;
  private final Stage myStage;

  private ViewState myState;
  private BorderPane myRoot;
  private Timeline myAnimation;
  // DEFAULT_SIM_STEP_TIME is DIVIDED by mySpeedFactor to get number of seconds between steps.
  private double mySpeedFactor;

  // Components of the UI
  private SimulationViewZoomable mySimulationView;
  private ControlPanel myControlPanel;
  private InformationBox myInformationBox;
  private StateColorLegend myStateColorLegend;
  private ChangeParametersView myChangeParametersWindow;
  private final XmlUtils xmlUtils;

  // Create logger
  private static final Logger logger = LogManager.getLogger(UserView.class);

  /**
   * Constructs a UserView instance and initializes the simulation window.
   *
   * @param sceneWidth  - the width of the scene
   * @param sceneHeight - the height of the scene
   * @param stage       - the primary stage of the application
   */
  public UserView(int sceneWidth, int sceneHeight, Stage stage) {
    myStage = stage;
    mySceneWidth = sceneWidth;
    mySceneHeight = sceneHeight;

    xmlUtils = new XmlUtils();

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
    mySimulationView = new SimulationViewZoomable(
        mySceneWidth * SimViewConstants.GRID_PROPORTION_OF_SCREEN,
        mySceneHeight * SimViewConstants.GRID_PROPORTION_OF_SCREEN
    );
    myControlPanel = new ControlPanel(this); // Pass reference for event handling
    myInformationBox = new InformationBox();
    myStateColorLegend = new StateColorLegend(this);  // Color-state legend, with passed reference for event handling

    // Set components in BorderPane
    myRoot.setLeft(mySimulationView.getZoomableDisplay());

    VBox rightPanel = new VBox(ControlPanel.VBOX_SPACING);
    rightPanel.getChildren().addAll(myControlPanel.getPanel(), myStateColorLegend.getScrollableLegend());
    myRoot.setRight(rightPanel);

    myRoot.setBottom(myInformationBox.getTextArea());

    // Set simulation speed to default
    mySpeedFactor = 1;

    initializeScene();
  }

  /**
   * Reset the positioning and zoom of the simulation grid view, if one exists
   */
  public void resetGridZoom() {
    if (mySimulationView != null) {
      mySimulationView.resetGridZoom();
    }
  }

  /**
   * Initializes the scene.
   */
  private void initializeScene() {
    Scene scene = new Scene(myRoot, mySceneWidth, mySceneHeight);
    myStage.setScene(scene);
    myStage.setTitle(SimViewConstants.TITLE);

    // Apply the selected theme on startup
    myControlPanel.applyCurrentlySelectedTheme();

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
      // Account for changes to simulation speed while paused
      // (user hitting speed up / slow down buttons)
      runNewAnimation();
      return;
    }

    // Otherwise, create a new animation and start it
    runNewAnimation();
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
    closeChangeParametersWindow();
    mySimulationView.resetGrid();
    myInformationBox.emptyFields();
    myStateColorLegend.clearLegend();
    mySpeedFactor = 1;
  }

  /**
   * Open the window which allows user to change simulation parameters.
   */
  public void openChangeParametersWindow() {
    if (!checkSimExistsElseAlert(ResourceManager.getCurrentErrorBundle())) { return; }

    int numEditableParams = mySimulationView.getSimulation().getNumEditableParameters();
    if (numEditableParams <= 0) {
      showMessage(
          AlertType.INFORMATION, ResourceManager.getCurrentErrorBundle().getString("NoParamsToEdit")
      );
      return;
    }

    myChangeParametersWindow = new ChangeParametersView(mySimulationView.getSimulation());
  }

  public void closeChangeParametersWindow() {
    if (myChangeParametersWindow != null) {
      myChangeParametersWindow.closeWindow();
    }
  }

  private boolean checkSimExistsElseAlert(ResourceBundle errorBundle) {
    if (!checkSimulationExists()) {
      showMessage(
          AlertType.WARNING, errorBundle.getString("NoSimulationToSave")
      );
      return false;
    }
    return true;
  }

  /**
   * Attempts to load a new simulation from a provided file.
   *
   * @param dataFile XML file containing simulation info
   */
  private void loadSimulationFromFile(File dataFile) {
    if (dataFile != null) {
      logger.debug("Loading file: " + dataFile.getName());
      stopAndResetSimulation();
      try {
        configureAndDisplaySimFromXml(xmlUtils.readXml(dataFile));
      } catch (XmlException e) {
        myState = ViewState.ERROR;
        showMessage(AlertType.ERROR, e.getMessage());
      }
    }
  }

  /**
   * prompts user to select an XML file, then attempts to load a new simulation from that file.
   */
  public void chooseFileAndLoadSimulation() {
    File dataFile = FileExplorer.getFileLoadChooser().showOpenDialog(myStage);
    loadSimulationFromFile(dataFile);
  }

  private void configureAndDisplaySimFromXml(XmlData xmlUtils) {
    // Set program state
    myState = ViewState.LOAD;

    // Upload simulation to mySimulationView
    try {
      mySimulationView.configureFromXml(xmlUtils);
      mySimulationView.initializeGridView();

      XmlData xmlData = mySimulationView.getSimulation().getXmlDataObject();

      // Update text box with simulation information
      myInformationBox.updateInfo(xmlData);

      // Update legend based on simulation type
      myStateColorLegend.updateLegend(xmlData);
    } catch (SimulationException e) {
      // e.printStackTrace();
      myState = ViewState.ERROR;
      showMessage(Alert.AlertType.ERROR, e.getMessage());
    }
  }

  /**
   * Displays an alert message to the user.
   *
   * @param type    - the type of alert (e.g., ERROR, WARNING, INFORMATION)
   * @param message - the message to be displayed
   */
  public static void showMessage(AlertType type, String message) {
    new Alert(type, message).showAndWait();
  }

  /**
   * Requests a simulation to be saved.
   */
  public void saveSimulation() {
    ResourceBundle resources = ResourceManager.getCurrentMainBundle();
    ResourceBundle errorResources = ResourceManager.getCurrentErrorBundle();

    if (!checkSimExistsElseAlert(errorResources)) {
      return;
    }

    pauseSimulation();

    // Open file chooser for saving
    FileChooser fileChooser = FileExplorer.getSaveFileChooser();
    fileChooser.setInitialFileName(
        resources.getString("DefaultFilePrefix") + DateTime.getLocalDateTime() + ".xml"); // Default filename
    File saveFile = fileChooser.showSaveDialog(myStage);

    if (saveFile != null) {
      myState = ViewState.SAVE;
      try {
        xmlUtils.writeToXml(saveFile,
            mySimulationView.getSimulation().getXmlDataObject().getTitle(),
            mySimulationView.getSimulation().getXmlDataObject().getAuthor(),
            mySimulationView.getSimulation().getXmlDataObject().getDescription(),
            mySimulationView.getSimulation());
        showMessage(Alert.AlertType.INFORMATION, resources.getString("SimulationSaved"));
      } catch (XmlException e) {
        myState = ViewState.ERROR;
        showMessage(Alert.AlertType.ERROR,
            errorResources.getString("ErrorSaving") + e.getMessage());
      }
    }
    // Return myState to paused state.
    pauseSimulation();
  }

  /**
   * Flip the displayed cell grid horizontally, if a simulation exists in this window.
   */
  public void flipGridHorizontally() {
    if (!checkSimulationExists()) {
      showMessage(
          Alert.AlertType.WARNING,
          ResourceManager.getCurrentErrorBundle().getString("NoSimulationToFlip"));
    }
    mySimulationView.flipDisplayHorizontally();
  }

  /**
   * Flip the displayed cell grid vertically, if a simulation exists in this window.
   */
  public void flipGridVertically() {
    if (!checkSimulationExists()) {
      showMessage(
          Alert.AlertType.WARNING,
          ResourceManager.getCurrentErrorBundle().getString("NoSimulationToFlip"));
    }
    mySimulationView.flipDisplayVertically();
  }

  /**
   * Change the speed of simulation view stepper based on an adjustmentFactor multiplier.
   *
   * @param adjustmentFactor Speed multiplier value. For example 2.0 will result in the animation
   *                         running twice as fast.
   */
  public void changeSimulationSpeed(double adjustmentFactor) {
    if (!checkSimulationExists()) {
      return;
    }

    double newSpeedFactor = mySpeedFactor * adjustmentFactor;
    // Only change the animation speed if the new speed is within bounds
    if (!checkStepTimeWithinBounds(SimViewConstants.DEFAULT_SIM_STEP_TIME / newSpeedFactor)) {
      return;
    }
    mySpeedFactor = newSpeedFactor;
    if (myAnimation != null && myState == ViewState.RUN) {
      runNewAnimation();
    }
  }

  private boolean checkStepTimeWithinBounds(double stepTime) {
    return (stepTime >= SimViewConstants.MIN_SIM_STEP_TIME
        && stepTime <= SimViewConstants.MAX_SIM_STEP_TIME);
  }

  /**
   * Initializes an animation based on the current mySpeedFactor.
   */
  private void runNewAnimation() {
    if (myAnimation != null) {
      myAnimation.stop();
    }

    myAnimation = new Timeline(
        new KeyFrame(Duration.seconds(SimViewConstants.DEFAULT_SIM_STEP_TIME / mySpeedFactor),
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

    XmlData randomXmlData = RandomSimulationGenerator.createRandomGameOfLifeXml();

    configureAndDisplaySimFromXml(randomXmlData);
  }

  /**
   * Asks the simulation grid to turn gridlines on or off.
   *
   * @param enable TRUE to enable gridlines, FALSE to disable gridlines
   */
  public void toggleGridlines(boolean enable) {
    mySimulationView.toggleGridlines(enable);
  }

  /**
   * Retrieve the current state of this simulation.
   *
   * @return Enum value such as EMPTY, LOAD, RUN, etc.
   */
  public ViewState getState() {
    return myState;
  }

  /**
   * Retrieve the Scene that this window is displaying.
   *
   * @return Scene object containing all on-screen elements as children
   */
  public Scene getScene() {
    return myStage.getScene();
  }

  /**
   * Retrieve a List of CellView objects that this window is displaying.
   *
   * @return ArrayList of CellView objects
   */
  public List<CellView> getCellViewList() {
    List<CellView> list = mySimulationView.getCellViewList();
    if (list == null) {
      return new ArrayList<>();
    }
    return list;
  }

  /**
   * Updates a given state to a new color, assuming the color exists.
   */
  public void updateColorForState(int state, Color newColor) {
    if (!checkSimulationExists()) {
      throw new SimulationException("NoSimulationToSave");
    }

    try {
      mySimulationView.updateCellColorsForState(state, newColor);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Return this view's animation object
   * @return current Timeline object; null if no simulation is loaded
   */
  public Timeline getMyAnimation() {
    return myAnimation;
  }

  /**
   * Return the number of cells that are currently being displayed by this view
   * @return int number of children of the SimulationView object (which this window displays as the grid)
   */
  public int getNumCellsDisplayed() {
    return mySimulationView.getDisplay().getChildren().size();
  }
}
