package cellsociety.model.view;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.Main;
import cellsociety.view.window.UserView;
import javafx.animation.Animation.Status;
import javafx.animation.Timeline;
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
}
