package cellsociety.view.window;

import cellsociety.Main;
import cellsociety.model.util.XMLData;
import cellsociety.model.util.XMLUtils;
import cellsociety.model.util.constants.exceptions.SimulationException;
import cellsociety.model.util.constants.exceptions.XMLException;
import cellsociety.view.components.ControlPanel;
import cellsociety.view.utils.FileExplorer;
import cellsociety.view.components.InformationBox;
import cellsociety.view.components.RandomSimulationGenerator;
import cellsociety.view.components.SimulationView;
import cellsociety.view.components.StateColorLegend;
import cellsociety.view.interfaces.CellView;
import cellsociety.view.utils.DateTime;
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

  private final int mySceneWidth;
  private final int mySceneHeight;
  private final Stage myStage;

  private final String myLanguage;
  private ResourceBundle myResources;
  private ResourceBundle myErrorResources;

  private ViewState myState;
  private BorderPane myRoot;
  private Timeline myAnimation;
  // DEFAULT_SIM_STEP_TIME is DIVIDED by mySpeedFactor to get number of seconds between steps.
  private double mySpeedFactor;

  // Components of the UI
  private SimulationView mySimulationView;
  private ControlPanel myControlPanel;
  private InformationBox myInformationBox;
  private StateColorLegend myStateColorLegend;
  private final XMLUtils xmlUtils;

  public UserView(int sceneWidth, int sceneHeight, Stage stage, String language) {
    myStage = stage;
    mySceneWidth = sceneWidth;
    mySceneHeight = sceneHeight;

    myLanguage = language;
    xmlUtils = new XMLUtils(myLanguage);
    try {
      myResources = ResourceBundle.getBundle(Main.DEFAULT_RESOURCE_PACKAGE + language);
    }
    catch (Exception e) {
      myResources = ResourceBundle.getBundle(Main.DEFAULT_RESOURCE_PACKAGE + "English");
      showMessage(Alert.AlertType.WARNING, myErrorResources.getString("LanguageUnavailable"));
    }
    try {
      myErrorResources = ResourceBundle.getBundle(Main.DEFAULT_RESOURCE_PACKAGE + "Errors" + language);
    }
    catch (Exception e) {
      myErrorResources = ResourceBundle.getBundle(Main.DEFAULT_RESOURCE_PACKAGE + "ErrorsEnglish");
      showMessage(Alert.AlertType.WARNING, myErrorResources.getString("AlertsInEnglish") + language + ".");
    }

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
    mySimulationView = new SimulationView(mySceneWidth * SimViewConstants.GRID_PROPORTION_OF_SCREEN,
        mySceneHeight * SimViewConstants.GRID_PROPORTION_OF_SCREEN, myLanguage);
    myControlPanel = new ControlPanel(this); // Pass reference for event handling
    myInformationBox = new InformationBox(myResources);
    myStateColorLegend = new StateColorLegend(this, myLanguage);  // Color-state legend, with passed reference for event handling

    // Set components in BorderPane
    myRoot.setLeft(mySimulationView.getDisplay());

    VBox rightPanel = new VBox(ControlPanel.VBOX_SPACING);
    rightPanel.getChildren().addAll(myControlPanel.getPanel(), myStateColorLegend.getLegendBox());
    myRoot.setRight(rightPanel);

    myRoot.setBottom(myInformationBox.getTextArea());

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
      // Account for changes to simulation speed while paused (user hitting speed up / slow down buttons)
      playNewAnimation();
      return;
    }

    // Otherwise, create a new animation and start it
    myAnimation = new Timeline(new KeyFrame(Duration.seconds(SimViewConstants.DEFAULT_SIM_STEP_TIME / mySpeedFactor),
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
    myInformationBox.emptyFields();
    myStateColorLegend.clearLegend();
    mySpeedFactor = 1;
  }

  /**
   * Attempts to load a new simulation from a provided file.
   * @param dataFile XML file containing simulation info
   */
  private void loadSimulationFromFile(File dataFile) {
    if (dataFile != null) {
      System.out.println("Loading file: " + dataFile.getName());
      stopAndResetSimulation();
      try {
        configureAndDisplaySimFromXML(xmlUtils.readXML(dataFile, myLanguage));
      }
      catch (XMLException e) {
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

  private void configureAndDisplaySimFromXML(XMLData xmlUtils) {
    // Set program state
    myState = ViewState.LOAD;

    // Upload simulation to mySimulationView
    try {
      mySimulationView.configureFromXML(xmlUtils);
      mySimulationView.initializeGridView();

      XMLData xmlData = mySimulationView.getSimulation().getXMLData();

      // Update text box with simulation information
      myInformationBox.updateInfo(xmlData);

      // Update legend based on simulation type
      myStateColorLegend.updateLegend(xmlData);
    }
    catch (SimulationException e) {
      e.printStackTrace();
      myState = ViewState.ERROR;
      showMessage(Alert.AlertType.ERROR, e.getMessage());
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
      showMessage(Alert.AlertType.WARNING, myErrorResources.getString("NoSimulationToSave"));
      return;
    }

    pauseSimulation();

    // Open file chooser for saving
    FileChooser fileChooser = FileExplorer.getSaveFileChooser();
    fileChooser.setInitialFileName(myResources.getString("DefaultFilePrefix") + DateTime.getLocalDateTime() + ".xml"); // Default filename
    File saveFile = fileChooser.showSaveDialog(myStage);

    if (saveFile != null) {
      myState = ViewState.SAVE;
      try {
        xmlUtils.writeToXML(saveFile,
            mySimulationView.getSimulation().getXMLData().getTitle(),
            mySimulationView.getSimulation().getXMLData().getAuthor(),
            mySimulationView.getSimulation().getXMLData().getDescription(),
            mySimulationView.getSimulation());
        showMessage(Alert.AlertType.INFORMATION, myResources.getString("SimulationSaved"));
      } catch (XMLException e) {
        myState = ViewState.ERROR;
        showMessage(Alert.AlertType.ERROR, myErrorResources.getString("ErrorSaving") + e.getMessage());
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
    if (checkStepTimeWithinBounds(SimViewConstants.DEFAULT_SIM_STEP_TIME / newSpeedFactor)) {
      mySpeedFactor = newSpeedFactor;
      if(myAnimation != null) {
        if(myState == ViewState.RUN) {
          playNewAnimation();
        }
      }
    }
  }

  private boolean checkStepTimeWithinBounds(double stepTime) {
    return (stepTime >= SimViewConstants.MIN_SIM_STEP_TIME && stepTime <= SimViewConstants.MAX_SIM_STEP_TIME);
  }

  /**
   * Initializes an animation based on the current mySpeedFactor
   */
  private void playNewAnimation() {
    if (myAnimation != null) {
      myAnimation.stop();
    }
    myAnimation = new Timeline(new KeyFrame(Duration.seconds(SimViewConstants.DEFAULT_SIM_STEP_TIME / mySpeedFactor),
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

    configureAndDisplaySimFromXML(randomXMLData);
  }

  /**
   * Asks the simulation grid to turn gridlines on or off.
   * @param enable TRUE to enable gridlines, FALSE to disable gridlines
   */
  public void toggleGridlines(boolean enable) {
    mySimulationView.toggleGridlines(enable);
  }

  /**
   * Retrieve the current state of this simulation
   * @return Enum value such as EMPTY, LOAD, RUN, etc.
   */
  public ViewState getState() {
    return myState;
  }

  /**
   * Retrieve the resource properties for displaying text in correct language
   * @return ResourceBundle object
   */
  public ResourceBundle getResources() {
    return myResources;
  }

  /**
   * Retrieve the currently-displayed language (assuming its .properties file exists)
   * @return name of current UI language as a String
   */
  public String getLanguage() {
    return myLanguage;
  }

  /**
   * Retrieve the Scene that this window is displaying
   * @return Scene object containing all on-screen elements as children
   */
  public Scene getScene() {
    return myStage.getScene();
  }

  /**
   * Retrieve a List of CellView objects that this window is displaying
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
   * Updates a given state to a new color, assuming the color exists
   */
  public void updateColorForState(int state, Color newColor) {
    List<CellView> cellList = getCellViewList();

    if (cellList.isEmpty()) {
      return;
    }
    if (state < 0 || state >= cellList.getFirst().getNumStates()) {
      throw new IllegalArgumentException(String.format(
          myErrorResources.getString("InvalidState"), state, cellList.getFirst().getNumStates()-1));
    }

    for (CellView cellView : cellList) {
      cellView.setColorForState(state, newColor);
      if (cellView.getCellState() == state) {
        cellView.updateViewColor();
      }
    }
  }
}
