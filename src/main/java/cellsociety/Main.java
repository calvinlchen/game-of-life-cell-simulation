package cellsociety;

import cellsociety.view.window.SplashScreenView;
import cellsociety.view.window.UserView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The main entry point for the CellSociety application.
 * This class initializes and manages the primary JavaFX stages, including the splash screen
 * and simulation windows. It provides methods to start new simulation windows either empty,
 * with a file prompt, or with a randomly generated Game of Life simulation.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT for java docs
 */
public class Main extends Application {

  // default to start in the data folder to make it easy on the user to find
  public static final String DEFAULT_DATA_FOLDER = System.getProperty("user.dir") + "/data";

  // width and height of application window
  public static final int SIM_WINDOW_WIDTH = 1200;
  public static final int SIM_WINDOW_HEIGHT = 800;

  // positional variables for each generated Stage
  // determines how far a new stage is offset from the previous
  private static final int STAGE_OFFSET = 30;
  // stores the X position of the next-generated simulation window
  private static double nextStageX = 100;
  // stores the Y position of the next-generated simulation window
  private static double nextStageY = 100;

  /**
   * Starts the JavaFX application by displaying the splash screen.
   *
   * @param primaryStage the primary stage for this application
   * @see Application#start(Stage)
   */
  @Override
  public void start(Stage primaryStage) {
    new SplashScreenView(primaryStage);
  }

  /**
   * Starts an empty simulation window with the given language setting.
   *
   * @return a new UserView instance representing the simulation window
   */
  public static UserView startEmptySimulationWindow() {
    // Create a new window
    Stage newStage = new Stage();
    newStage.setX(nextStageX);
    newStage.setY(nextStageY);

    // Update nextStage position variables in case another window is generated later
    setNextStagePosition();

    UserView view = new UserView(SIM_WINDOW_WIDTH, SIM_WINDOW_HEIGHT, newStage);
    view.resetView();
    return view;
  }

  /**
   * Starts a new simulation window and prompts the user to choose a file to load a simulation.
   */
  public static void startSimulationWindowWithFilePrompt() {
    startEmptySimulationWindow().chooseFileAndLoadSimulation();
  }

  /**
   * Starts a new simulation window and loads a randomly generated Game of Life simulation.
   */
  public static void startSimulationWindowWithRandomGameOfLife() {
    startEmptySimulationWindow().loadRandomGameOfLife();
  }

  /**
   * Update nextStageX and nextStageY based on offset value and screen bounds. Code for fetching
   * screen bounds was advised by ChatGPT. "How do I get the screen size to ensure that the stage
   * won't be off the screen?"
   */
  private static void setNextStagePosition() {
    // Get the screen size (excluding taskbars)
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    // Calculate and store x-position for next window (in case a new window is generated later)
    if (nextStageX + SIM_WINDOW_WIDTH + STAGE_OFFSET > screenBounds.getMaxX()) {
      nextStageX = screenBounds.getMinX() + STAGE_OFFSET;
    } else {
      nextStageX += STAGE_OFFSET;
    }

    // Calculate and store y-position for next window
    if (nextStageY + SIM_WINDOW_HEIGHT + STAGE_OFFSET > screenBounds.getMaxY()) {
      nextStageY = screenBounds.getMinY() + STAGE_OFFSET;
    } else {
      nextStageY += STAGE_OFFSET;
    }
  }

  /**
   * The main entry point of the program. Launches the JavaFX application.
   *
   * @param args command-line arguments passed to the program
   */
  public static void main(String[] args) {
    launch(args);
  }
}
