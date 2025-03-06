package cellsociety.model.view;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.Main;
import cellsociety.view.utils.ResourceManager;
import cellsociety.view.utils.SimViewConstants;
import cellsociety.view.window.UserView;
import javafx.animation.Animation.Status;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

public class UserViewTest extends DukeApplicationTest {
  private UserView myUserView;

  @Override
  public void start(Stage stage) {
    // Initialize UserView with a fixed window size
    myUserView = new UserView(Main.SIM_WINDOW_WIDTH, Main.SIM_WINDOW_HEIGHT, stage);
    stage.setScene(myUserView.getScene());
    stage.show();
  }

  @Test
  public void testInitialState() {
    // Verify that when the application starts, the state is EMPTY.
    assertEquals(UserView.ViewState.EMPTY, myUserView.getState());
  }

  @Test
  public void testPlayWithoutSimulation() {
    assertEquals(UserView.ViewState.EMPTY, myUserView.getState());
    clickOn("Play");
    WaitForAsyncUtils.waitForFxEvents();
    // The state should still be EMPTY.
    assertEquals(UserView.ViewState.EMPTY, myUserView.getState());
  }

  @Test
  public void testLoadRandomGameOfLife() {
    // Simulate clicking the "Random Game of Life" button using clickOn() from ApplicationTest.
    clickOn("Random Game of Life");
    WaitForAsyncUtils.waitForFxEvents();

    // After loading, the view state should be LOAD.
    assertEquals(UserView.ViewState.LOAD, myUserView.getState());
    // Also check that some cell views exist.
    assertFalse(myUserView.getCellViewList().isEmpty(), "CellView list should not be empty after loading simulation");
  }

  @Test
  public void testPlayAndPauseSimulation() {
    // First, load a random simulation.
    clickOn("Random Game of Life");
    WaitForAsyncUtils.waitForFxEvents();

    // Start the simulation by clicking the "Play" button.
    clickOn("Play");
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(UserView.ViewState.RUN, myUserView.getState());

    // Then pause the simulation.
    clickOn("Pause");
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(UserView.ViewState.PAUSE, myUserView.getState());

    // Then resume the simulation.
    clickOn("Play");
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(UserView.ViewState.RUN, myUserView.getState());
  }

  @Test
  public void testDuplicatePlayClicks() {
    // First, load a random simulation.
    clickOn("Random Game of Life");
    WaitForAsyncUtils.waitForFxEvents();

    // Start the simulation by clicking the "Play" button.
    clickOn("Play");
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(UserView.ViewState.RUN, myUserView.getState());

    Timeline currentAnimation = myUserView.getMyAnimation();

    // Attempt to start duplicate animations by clicking "Play" again
    clickOn("Play");
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(UserView.ViewState.RUN, myUserView.getState());
    // Animation should not change upon duplicate Play button press.
    assertEquals(myUserView.getMyAnimation(), currentAnimation);
  }

  @Test
  public void testManySpeedUpClicks() {
    clickOn("Random Game of Life");
    WaitForAsyncUtils.waitForFxEvents();

    clickOn("Play");
    WaitForAsyncUtils.waitForFxEvents();

    // Attempt to click the "Speed Up" button many times
    for (int k = 0; k < 30; k++ ) {
      clickOn("Speed Up");
    }
    double simStepTime = SimViewConstants.DEFAULT_SIM_STEP_TIME / myUserView.getSpeedFactor();
    assertTrue(simStepTime >= SimViewConstants.MIN_SIM_STEP_TIME
        && simStepTime <= SimViewConstants.MAX_SIM_STEP_TIME);
  }

  @Test
  public void testManySlowDownClicks() {
    clickOn("Random Game of Life");
    WaitForAsyncUtils.waitForFxEvents();

    clickOn("Play");
    WaitForAsyncUtils.waitForFxEvents();

    // Attempt to click the "Speed Up" button many times
    for (int k = 0; k < 30; k++ ) {
      clickOn("Slow Down");
    }
    double simStepTime = SimViewConstants.DEFAULT_SIM_STEP_TIME / myUserView.getSpeedFactor();
    assertTrue(simStepTime >= SimViewConstants.MIN_SIM_STEP_TIME
        && simStepTime <= SimViewConstants.MAX_SIM_STEP_TIME);
  }

  @Test
  public void testClearGrid() {
    // First, load a random simulation.
    clickOn("Random Game of Life");
    WaitForAsyncUtils.waitForFxEvents();

    // Start the simulation by clicking the "Play" button.
    clickOn("Play");
    WaitForAsyncUtils.waitForFxEvents();

    // Clear the simulation
    clickOn("Clear Grid");
    WaitForAsyncUtils.waitForFxEvents();
    assertEquals(UserView.ViewState.EMPTY, myUserView.getState());
    assertEquals(Status.STOPPED, myUserView.getMyAnimation().getStatus());
    assertEquals(0, myUserView.getNumCellsDisplayed(), "Number of cells displayed after clearing simulation");
  }

  @Test
  public void testSaveWithoutSimulationShowsWarningAlert() {
    // Ensure no simulation is currently loaded
    interact(() -> myUserView.stopAndResetSimulation());
    assertEquals(UserView.ViewState.EMPTY, myUserView.getState());

    // Clicking the "Save As" button
    clickOn("Save As");
    WaitForAsyncUtils.waitForFxEvents();

    // Searching for alert with class dialog-pane
    Node dialogPane = lookup(".dialog-pane").query();
    assertNotNull(dialogPane, "Expected an alert dialog to appear when saving without a simulation.");

    // Verify that the dialog content contains the expected warning message
    Label contentLabel = lookup(".dialog-pane .content").queryAs(Label.class);
    String expectedMessage = ResourceManager.getCurrentErrorBundle().getString("NoSimulationToSave");
    assertTrue(contentLabel.getText().contains(expectedMessage),
        "Alert dialog should contain the warning message: " + expectedMessage);

    // Dismiss the alert
    clickOn("OK");
    WaitForAsyncUtils.waitForFxEvents();
  }

  @Test
  public void testParamWithoutSimulationShowsWarningAlert() {
    // Ensure no simulation is currently loaded
    interact(() -> myUserView.stopAndResetSimulation());
    assertEquals(UserView.ViewState.EMPTY, myUserView.getState());

    // Clicking the "Change Parameters" button
    clickOn("Change Parameters");
    WaitForAsyncUtils.waitForFxEvents();

    // Searching for alert with class dialog-pane
    Node dialogPane = lookup(".dialog-pane").query();
    assertNotNull(dialogPane, "Expected an alert dialog to appear when saving without a simulation.");

    // Verify that the dialog content contains the expected warning message
    Label contentLabel = lookup(".dialog-pane .content").queryAs(Label.class);
    String expectedMessage = ResourceManager.getCurrentErrorBundle().getString("NoSimulationToSave");
    assertTrue(contentLabel.getText().contains(expectedMessage),
        "Alert dialog should contain the warning message: " + expectedMessage);

    // Dismiss the alert
    clickOn("OK");
    WaitForAsyncUtils.waitForFxEvents();
  }

//  @Test
//  public void testFlipHorizontalWithoutSimulationShowsWarningAlert() {
//    // Ensure no simulation is currently loaded
//    interact(() -> myUserView.stopAndResetSimulation());
//    assertEquals(UserView.ViewState.EMPTY, myUserView.getState());
//
//    // Clicking the "Change Parameters" button
//    clickOn("Flip Grid Horizontally");
//    WaitForAsyncUtils.waitForFxEvents();
//
//    // Searching for alert with class dialog-pane
//    Node dialogPane = lookup(".dialog-pane").query();
//    assertNotNull(dialogPane, "Expected an alert dialog to appear when saving without a simulation.");
//
//    // Verify that the dialog content contains the expected warning message
//    Label contentLabel = lookup(".dialog-pane .content").queryAs(Label.class);
//    String expectedMessage = ResourceManager.getCurrentErrorBundle().getString("NoSimulationToFlip");
//    assertTrue(contentLabel.getText().contains(expectedMessage),
//        "Alert dialog should contain the warning message: " + expectedMessage);
//
//    // Dismiss the alert
//    clickOn("OK");
//    WaitForAsyncUtils.waitForFxEvents();
//  }
//
//  @Test
//  public void testFlipVerticalWithoutSimulationShowsWarningAlert() {
//    // Ensure no simulation is currently loaded
//    interact(() -> myUserView.stopAndResetSimulation());
//    assertEquals(UserView.ViewState.EMPTY, myUserView.getState());
//
//    // Clicking the "Change Parameters" button
//    clickOn("Flip Grid Vertically");
//    WaitForAsyncUtils.waitForFxEvents();
//
//    // Searching for alert with class dialog-pane
//    Node dialogPane = lookup(".dialog-pane").query();
//    assertNotNull(dialogPane, "Expected an alert dialog to appear when saving without a simulation.");
//
//    // Verify that the dialog content contains the expected warning message
//    Label contentLabel = lookup(".dialog-pane .content").queryAs(Label.class);
//    String expectedMessage = ResourceManager.getCurrentErrorBundle().getString("NoSimulationToFlip");
//    assertTrue(contentLabel.getText().contains(expectedMessage),
//        "Alert dialog should contain the warning message: " + expectedMessage);
//
//    // Dismiss the alert
//    clickOn("OK");
//    WaitForAsyncUtils.waitForFxEvents();
//  }
}
