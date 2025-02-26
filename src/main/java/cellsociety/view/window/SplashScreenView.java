package cellsociety.view.window;

import cellsociety.Main;
import cellsociety.view.utils.ResourceAnalyzer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The splash screen for the Cell Society application.
 *
 * <p>This class creates the initial interface where users can select a language
 * and either load a simulation from a file or generate a random Game of Life simulation.
 *
 * @author Conner Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class SplashScreenView {

  public static int SPLASH_WIDTH = 300;
  public static int SPLASH_HEIGHT = 200;

  private final Stage myStage;
  private ChoiceBox<String> myLanguageDropdown;

  /**
   * Constructs a SplashScreenView and initializes the splash screen UI.
   *
   * @param stage - the primary stage of the application
   */
  public SplashScreenView(Stage stage) {
    myStage = stage;
    initializeSplashScreen();
  }

  private void initializeSplashScreen() {
    Label titleLabel = new Label("Cell Society");
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    myLanguageDropdown = new ChoiceBox<>();
    myLanguageDropdown.getItems().addAll(ResourceAnalyzer.getAvailableLanguages());
    myLanguageDropdown.setValue("English"); // Default to English

    Button loadFileButton = new Button("Load Simulation from File");
    loadFileButton.setOnAction(
        e -> Main.startSimulationWindowWithFilePrompt(myLanguageDropdown.getValue()));

    Button randomGameOfLifeButton = new Button("Generate Random Game of Life");
    randomGameOfLifeButton.setOnAction(
        e -> Main.startSimulationWindowWithRandomGameOfLife(myLanguageDropdown.getValue()));

    VBox layout = new VBox(15);
    layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");
    layout.getChildren()
        .addAll(titleLabel, myLanguageDropdown, loadFileButton, randomGameOfLifeButton);

    Scene splashScene = new Scene(layout, SPLASH_WIDTH, SPLASH_HEIGHT);
    myStage.setScene(splashScene);
    myStage.setTitle("Welcome to Cell Society");
    myStage.show();

  }

}
