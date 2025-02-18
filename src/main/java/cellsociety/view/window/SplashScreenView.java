package cellsociety.view.window;

import cellsociety.Main;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SplashScreenView {
  private Stage myStage;

  public SplashScreenView(Stage stage) {
    myStage = stage;
    initializeSplashScreen();
  }

  private void initializeSplashScreen() {
    Label titleLabel = new Label("Cell Society");
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    ChoiceBox<String> languageDropdown = new ChoiceBox<>();
    languageDropdown.getItems().addAll("English", "Spanish", "French"); // Add more languages if needed
    languageDropdown.setValue("English"); // Default selection

    // TODO: make language variable
    Button loadFileButton = new Button("Load Simulation from File");
    loadFileButton.setOnAction(e -> Main.startSimulationWindowWithFilePrompt("English"));

    // TODO: make language variable
    Button randomSimButton = new Button("Generate Random Game of Life");
    randomSimButton.setOnAction(e -> Main.startSimulationWindowWithRandomGameOfLife("English"));

    VBox layout = new VBox(15);
    layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");
    layout.getChildren().addAll(titleLabel, languageDropdown, loadFileButton, randomSimButton);

    Scene splashScene = new Scene(layout, 300, 200);
    myStage.setScene(splashScene);
    myStage.setTitle("Welcome to Cell Society");
    myStage.show();

  }

}
