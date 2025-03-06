package cellsociety.view.components;

import cellsociety.Main;
import cellsociety.view.utils.ResourceManager;
import cellsociety.view.window.UserView;
import cellsociety.view.window.UserView.ViewState;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Manages control buttons and status messages. (VBox suggested by ChatGPT.)
 */
public class ControlPanel {

  public static final int VBOX_SPACING = 12;

  private final VBox myPanel;
  private final UserView myUserView;
  private final List<Button> myButtons;
  private ChoiceBox<String> myThemeChoices;

  /**
   * Contains a column of control buttons for Cell Society.
   *
   * @param userView the view controller through which a button initiates its action when clicked
   */
  public ControlPanel(UserView userView) {
    myUserView = userView;
    myPanel = new VBox(VBOX_SPACING);
    myButtons = new ArrayList<>();
    initializeControls();
  }

  private void initializeControls() {
    ResourceBundle resources = ResourceManager.getCurrentMainBundle();

    Button playButton = new Button(resources.getString("PlayCommand"));
    playButton.setOnAction(e -> myUserView.playSimulation());
    myButtons.add(playButton);

    Button pauseButton = new Button(resources.getString("PauseCommand"));
    pauseButton.setOnAction(e -> myUserView.pauseSimulation());
    myButtons.add(pauseButton);

    Button resetZoomButton = new Button(resources.getString("ResetZoomCommand"));
    resetZoomButton.setOnAction(e -> myUserView.resetGridZoom());
    myButtons.add(resetZoomButton);

    Button clearButton = new Button(resources.getString("ClearCommand"));
    clearButton.setOnAction(e -> myUserView.stopAndResetSimulation());
    myButtons.add(clearButton);

    Button loadButton = new Button(resources.getString("LoadFileCommand"));
    loadButton.setOnAction(e -> {
      if (myUserView.getState() == ViewState.EMPTY || myUserView.getState() == ViewState.ERROR) {
        myUserView.chooseFileAndLoadSimulation();
      } else {
        Main.startSimulationWindowWithFilePrompt();
      }
    });
    myButtons.add(loadButton);

    Button saveButton = new Button(resources.getString("SaveAsCommand"));
    saveButton.setOnAction(e -> myUserView.saveSimulation());
    myButtons.add(saveButton);

    Button randomButton = new Button(resources.getString("RandomGameOfLifeCommand"));
    randomButton.setOnAction(e -> myUserView.loadRandomGameOfLife());
    myButtons.add(randomButton);

    Button flipHorizontalButton = new Button(resources.getString("FlipGridHorizontal"));
    flipHorizontalButton.setOnAction(e -> myUserView.flipGridHorizontally());
    myButtons.add(flipHorizontalButton);

    Button flipVerticalButton = new Button(resources.getString("FlipGridVertical"));
    flipVerticalButton.setOnAction(e -> myUserView.flipGridVertically());
    myButtons.add(flipVerticalButton);

    Button changeParamtersButton = new Button(resources.getString("ChangeParameters"));
    changeParamtersButton.setOnAction(e -> myUserView.openChangeParametersWindow());
    myButtons.add(changeParamtersButton);

    myPanel.getChildren().addAll(myButtons);

    // Panel containing buttons for speeding up / slowing down the simulations
    HBox speedPanel = makeSpeedPanel();
    // Panel containing theme options
    VBox themePanel = makeThemePanel();
    // Panel containing grid-line toggle
    HBox outlinePanel = makeOutlinePanel();

    myPanel.getChildren().addAll(speedPanel, themePanel, outlinePanel);
  }

  private HBox makeSpeedPanel() {
    HBox speedPanel = new HBox((double) VBOX_SPACING / 2);

    Button speedUpButton = new Button(ResourceManager.getCurrentMainBundle().getString("SpeedUpCommand"));
    speedUpButton.setOnAction(e -> myUserView.changeSimulationSpeed(2.0));
    Button slowDownButton = new Button(ResourceManager.getCurrentMainBundle().getString("SlowDownCommand"));
    slowDownButton.setOnAction(e -> myUserView.changeSimulationSpeed(0.5));
    speedPanel.getChildren().addAll(speedUpButton, slowDownButton);

    return speedPanel;
  }

  private HBox makeOutlinePanel() {
    HBox outlinePanel = new HBox((double) VBOX_SPACING / 2);

    CheckBox outlineCheckbox = new CheckBox(ResourceManager.getCurrentMainBundle().getString("ShowGridlines"));
    outlineCheckbox.setSelected(true); // Default to showing outlines

    outlineCheckbox.setOnAction(e -> toggleOutlines(outlineCheckbox.isSelected()));

    outlinePanel.getChildren().add(outlineCheckbox);
    return outlinePanel;
  }

  private void toggleOutlines(boolean enable) {
    myUserView.toggleGridlines(enable);
  }

  private VBox makeThemePanel() {
    VBox themePanel = new VBox((double) VBOX_SPACING / 2);

    Text themeTitle = new Text(ResourceManager.getCurrentMainBundle().getString("ThemeHeader"));
    themeTitle.getStyleClass().add("bold-text");
    themePanel.getChildren().addAll(themeTitle);

    List<String> themes = ResourceManager.getAvailableStylesheets();
    if (themes.isEmpty()) {
      // if no theme option(s) exist, display None
      Text noneText = new Text(ResourceManager.getCurrentMainBundle().getString("None"));
      themeTitle.getStyleClass().add("bold-text");
      themePanel.getChildren().addAll(noneText);
    } else {
      myThemeChoices = new ChoiceBox<>();
      myThemeChoices.getItems().addAll(themes);
      myThemeChoices.setValue(themes.getFirst()); // default to first available theme

      // Update theme when selection changes
      myThemeChoices.setOnAction(event -> applyCurrentlySelectedTheme());

      themePanel.getChildren().addAll(myThemeChoices);
    }

    return themePanel;
  }

  /**
   * Apply the theme that is currently selected in the dropdown menu.
   */
  public void applyCurrentlySelectedTheme() {
    if (myThemeChoices != null) { applyTheme(myThemeChoices.getValue()); }
  }

  private void applyTheme(String themeName) {
    String stylesheetPath = "/" + ResourceManager.DEFAULT_STYLESHEET_FOLDER + themeName + ".css";
    Scene scene = myUserView.getScene();

    if (scene != null) {
      scene.getStylesheets().clear(); // Remove old theme
      scene.getStylesheets().add(getClass().getResource(stylesheetPath).toExternalForm());
    }
  }


  /**
   * Return the vertical panel of control buttons.
   *
   * @return VBox panel view containing all button elements
   */
  public VBox getPanel() {
    return myPanel;
  }
}
