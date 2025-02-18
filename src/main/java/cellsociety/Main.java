package cellsociety;

import cellsociety.view.window.SplashScreenView;
import cellsociety.view.window.UserView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Feel free to completely change this code or delete it entirely.
 */
public class Main extends Application {

  // default to start in the data folder to make it easy on the user to find
  public static final String DEFAULT_DATA_FOLDER = System.getProperty("user.dir") + "/data";

  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.resourceproperty.";
  public static final String DEFAULT_RESOURCE_FOLDER = DEFAULT_RESOURCE_PACKAGE.replace(".", "/");

  // width and height of application window
  public static final int SIM_WINDOW_WIDTH = 1200;
  public static final int SIM_WINDOW_HEIGHT = 800;

  // positional variables for each generated Stage
  private static final int STAGE_OFFSET = 30; // determines how far a new stage is offset from the previous
  private static double nextStageX = 100;  // stores the X position of the next-generated simulation window
  private static double nextStageY = 100;  // stores the Y position of the next-generated simulation window

  /**
   * @see Application#start(Stage)
   */
  @Override
  public void start(Stage primaryStage) {
    new SplashScreenView(primaryStage);
  }

  public static UserView startEmptySimulationWindow(String language) {
    // Create a new window
    Stage newStage = new Stage();
    newStage.setX(nextStageX);
    newStage.setY(nextStageY);

    // Update nextStage position variables in case another window is generated later
    setNextStagePosition();

    UserView view = new UserView(SIM_WINDOW_WIDTH, SIM_WINDOW_HEIGHT, newStage, language);
    view.resetView();
    return view;
  }

  public static void startSimulationWindowWithFilePrompt(String language) {
    startEmptySimulationWindow(language).chooseFileAndLoadSimulation();
  }

  public static void startSimulationWindowWithRandomGameOfLife(String language) {
    startEmptySimulationWindow(language).loadRandomGameOfLife();
  }

  /**
   * Update nextStageX and nextStageY based on offset value and screen bounds.
   * Code for fetching screen bounds was advised by ChatGPT.
   * "How do I get the screen size to ensure that the stage won't be off the screen?"
   */
  private static void setNextStagePosition() {
    // Get the screen size (excluding taskbars)
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    // Calculate and store x-position for next window (in case a new window is generated later)
    if (nextStageX + SIM_WINDOW_WIDTH + STAGE_OFFSET > screenBounds.getMaxX()) {
      nextStageX = screenBounds.getMinX() + STAGE_OFFSET;
    }
    else {
      nextStageX += STAGE_OFFSET;
    }

    // Calculate and store y-position for next window
    if (nextStageY + SIM_WINDOW_HEIGHT + STAGE_OFFSET > screenBounds.getMaxY()) {
      nextStageY = screenBounds.getMinY() + STAGE_OFFSET;
    }
    else {
      nextStageY += STAGE_OFFSET;
    }
  }

  /**
   * Start the program, give complete control to JavaFX. Default version of main() is actually
   * included within JavaFX, so this is not technically necessary!
   */
  public static void main(String[] args) {
    launch(args);
  }
}
