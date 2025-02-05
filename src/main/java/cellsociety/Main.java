package cellsociety;

import cellsociety.view.components.UserView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {

    // default to start in the data folder to make it easy on the user to find
    public static final String DATA_FILE_FOLDER = System.getProperty("user.dir") + "/data";

    // width and height of application window
    public static final int SCENE_WIDTH = 1200;
    public static final int SCENE_HEIGHT = 800;

    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start (Stage primaryStage) {
        UserView view = new UserView(SCENE_WIDTH, SCENE_HEIGHT, primaryStage);
        view.resetView();
    }

    /**
     * Start the program, give complete control to JavaFX.
     * Default version of main() is actually included within JavaFX, so this is not technically necessary!
     */
    public static void main (String[] args) {
        launch(args);
    }
}
