package cellsociety.view.utils;

/**
 * Utility class for storing simulation view constants.
 *
 * <p>This class defines constants related to the graphical user interface of the simulation,
 * such as window titles, grid proportions, and simulation step timing constraints.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class SimViewConstants {

  public static final String TITLE = "Cell Society";
  public static final double GRID_PROPORTION_OF_SCREEN = 0.85;
  public static final double DEFAULT_SIM_STEP_TIME = 0.5; // in seconds
  public static final double MIN_SIM_STEP_TIME = 0.02;
  public static final double MAX_SIM_STEP_TIME = 4;

}
