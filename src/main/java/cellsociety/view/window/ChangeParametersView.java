package cellsociety.view.window;

import cellsociety.model.simulation.Simulation;
import cellsociety.view.utils.ResourceManager;
import cellsociety.view.utils.exceptions.ViewException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ChangeParametersView {
  public static final int CHANGE_PARAMS_WINDOW_WIDTH = 400;
  public static final int CHANGE_PARAMS_WINDOW_HEIGHT = 400;

  private final Stage myStage;
  private final UserView myUserView;
  private final Simulation<?> mySimulation;
  private final VBox layout;
  // Map to keep track of parameter keys and their corresponding text fields
  private final Map<String, TextField> parameterFields;

  /**
   * Constructs a new window for changing simulation parameters.
   *
   * @param simulation the Simulation instance whose parameters can be updated.
   */
  public ChangeParametersView(UserView userView, Simulation<?> simulation) {
    myUserView = userView;
    mySimulation = simulation;
    myStage = new Stage();
    layout = new VBox(10);
    layout.setPadding(new Insets(10, 0, 0, 10));
    parameterFields = new HashMap<>();
    buildUI();
    Scene scene = new Scene(layout, CHANGE_PARAMS_WINDOW_WIDTH, CHANGE_PARAMS_WINDOW_HEIGHT);
    myStage.setScene(scene);
    myStage.setTitle(ResourceManager.getCurrentMainBundle().getString("ChangeParameters"));
    myStage.show();
  }

  /**
   * Terminate this window, such as if the user clears the current simulation in UserView.
   */
  public void closeWindow() {
    myStage.close();
  }

  /**
   * Builds the UI form by iterating over all available simulation parameters.
   */
  private void buildUI() {
    // For each parameter, create a row with a label and a text field initialized with its current value.
    for (String key : mySimulation.getParameterKeys()) {
      if (key.equals("maxHistorySize")) {
        continue;
      }
      double currentValue = mySimulation.getParameter(key);
      Label label = new Label(key + ":");
      TextField textField = new TextField(String.valueOf(currentValue));
      parameterFields.put(key, textField);
      HBox row = new HBox(10, label, textField);
      layout.getChildren().add(row);
    }
    // Add a save button at the bottom of the form.
    Button saveButton = new Button(ResourceManager.getCurrentMainBundle().getString("SaveParameters"));
    saveButton.setOnAction(e -> saveParameters());
    layout.getChildren().add(saveButton);
  }

  /**
   * Reads the new parameter values from the text fields and updates the simulation.
   * Displays an error dialog if any value is invalid.
   */
  private void saveParameters() {
    for (Map.Entry<String, TextField> entry : parameterFields.entrySet()) {
      String key = entry.getKey();
      try {
        double newValue = Double.parseDouble(entry.getValue().getText());
        mySimulation.updateParameter(key, newValue);
      } catch (NumberFormatException ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
            (new ViewException("InvalidParameterValue", key)).getMessage());
        alert.showAndWait();
        myUserView.updateInformationBox();
        return; // Abort saving if any value is invalid.
      }
    }
    // Update main-window information panel and close this window if all updates are successful.
    myUserView.updateInformationBox();
    myStage.close();
  }
}
