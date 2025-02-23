package cellsociety.view.components;

import cellsociety.Main;
import cellsociety.view.utils.ResourceAnalyzer;
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
  private final ResourceBundle myResources;
  private final List<Button> myButtons;
  private ChoiceBox<String> myThemeChoices;

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

    Button flipHorizontalButton = new Button(myResources.getString("FlipGridHorizontal"));
    flipHorizontalButton.setOnAction(e -> myUserView.flipGridHorizontally());
    myButtons.add(flipHorizontalButton);

    Button flipVerticalButton = new Button(myResources.getString("FlipGridVertical"));
    flipVerticalButton.setOnAction(e -> myUserView.flipGridVertically());
    myButtons.add(flipVerticalButton);

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

    Button speedUpButton = new Button(myResources.getString("SpeedUpCommand"));
    speedUpButton.setOnAction(e -> myUserView.changeSimulationSpeed(2.0));
    Button slowDownButton = new Button(myResources.getString("SlowDownCommand"));
    slowDownButton.setOnAction(e -> myUserView.changeSimulationSpeed(0.5));
    speedPanel.getChildren().addAll(speedUpButton, slowDownButton);

    return speedPanel;
  }

  private HBox makeOutlinePanel() {
    HBox outlinePanel = new HBox((double) VBOX_SPACING / 2);

    CheckBox outlineCheckbox = new CheckBox(myResources.getString("ShowGridlines"));
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

    Text themeTitle = new Text(myResources.getString("ThemeHeader"));
    themeTitle.getStyleClass().add("bold-text");
    themePanel.getChildren().addAll(themeTitle);

    List<String> themes = ResourceAnalyzer.getAvailableStylesheets();
    if (themes.isEmpty()) {
      // if no theme option(s) exist, display None
      Text noneText = new Text(myResources.getString("None"));
      themeTitle.getStyleClass().add("bold-text");
      themePanel.getChildren().addAll(noneText);
    }
    else {
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
   * Apply the theme that is currently selected in the dropdown menu
   */
  public void applyCurrentlySelectedTheme() {
    applyTheme(myThemeChoices.getValue());
  }

  private void applyTheme(String themeName) {
    String stylesheetPath = "/" + Main.DEFAULT_STYLESHEET_FOLDER + themeName + ".css";
    Scene scene = myUserView.getScene();

    if (scene != null) {
      scene.getStylesheets().clear(); // Remove old theme
      scene.getStylesheets().add(getClass().getResource(stylesheetPath).toExternalForm());
    }
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
