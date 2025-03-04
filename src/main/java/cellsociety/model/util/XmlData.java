package cellsociety.model.util;

import static cellsociety.model.util.constants.ResourcePckg.getErrorSimulationResourceBundle;

import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.util.SimulationTypes.SimType;

import cellsociety.model.util.constants.GridTypes.EdgeType;
import cellsociety.model.util.constants.GridTypes.NeighborhoodType;
import cellsociety.model.util.constants.GridTypes.ShapeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * An object that holds simulation data.
 *
 * <p>This class stores metadata, parameters, grid dimensions, and cell states
 * for a simulation. It also provides methods to retrieve and modify simulation
 * settings.
 *
 * @author Kyaira Boughton
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class XmlData {

  private SimType type; //enum for game type
  private String title; //title of game
  private String author; //name of author
  private String description; //description of simulation behavior
  private String theme; //saving the (color??) theme of the simulation
  private String language; //language of default & error text on screen
  //the number of states that the simulation will hold for the process of reversal.
  private int reverseStateNum;
  private Map<Integer, String> customColorMap; //a map of the custom colors
  private int gridRowNum; //number of rows in a grid
  private int gridColNum; //number of columns in grid.
  //a list of each cell's state in the grid. size unknown
  private List<Integer> cellStateList = new ArrayList<>();
  private Map<String, Object> parameters; //<parameter name as string, value>
  private int id;
  public static int totalSimulations;

  private ResourceBundle myErrorResources;

  /**
   * Initializes an XmlData object with default language (English).
   */
  public XmlData() {
    myErrorResources = getErrorSimulationResourceBundle("English");
    initializeXmlData();
  }

  /**
   * Initializes an XmlData object with a specified language.
   *
   * @param language - the language for error message localization
   */
  public XmlData(String language) {
    myErrorResources = getErrorSimulationResourceBundle(language);
    initializeXmlData();
  }

  private void initializeXmlData() {
    id = totalSimulations;
    totalSimulations++;
  }

  /**
   * Retrieves the simulation type.
   *
   * @return the simulation type as a SimType enum
   */
  public SimType getType() {
    return type;
  }

  /**
   * Sets the simulation type.
   *
   * @param type - the new SimType value for the simulation
   */
  public void setType(SimType type) {
    this.type = type;
  }

  /**
   * Retrieves the title of the simulation.
   *
   * @return the title of the simulation
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the simulation.
   *
   * @param title - the new title for the simulation
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Retrieves the author's name.
   *
   * @return the author's name
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Sets the author's name.
   *
   * @param author - the new author name for the simulation
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Retrieves the description of the simulation.
   *
   * @return the simulation description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the simulation.
   *
   * @param description - the new description for the simulation
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Retrieves the number of rows in the simulation grid.
   *
   * @return the number of rows in the grid
   */
  public int getGridRowNum() {
    return gridRowNum;
  }

  /**
   * Sets the number of rows in the simulation grid.
   *
   * @param gridRowNum - the new number of rows in the grid
   */
  public void setGridRowNum(int gridRowNum) {
    this.gridRowNum = gridRowNum;
  }

  /**
   * Retrieves the number of columns in the simulation grid.
   *
   * @return the number of columns in the grid
   */
  public int getGridColNum() {
    return gridColNum;
  }

  /**
   * Sets the number of columns in the simulation grid.
   *
   * @param gridColNum - the new number of columns in the grid
   */
  public void setGridColNum(int gridColNum) {
    this.gridColNum = gridColNum;
  }

  /**
   * Retrieves the list of cell states in the simulation grid.
   *
   * @return a list of integers representing the cell states
   */
  public List<Integer> getCellStateList() {
    return cellStateList;
  }

  /**
   * Sets the list of cell states in the simulation grid.
   *
   * @param cellStateList - the new list of cell states
   */
  public void setCellStateList(List<Integer> cellStateList) {
    this.cellStateList = cellStateList;
  }

  /**
   * Retrieves the parameters of the simulation.
   *
   * @return a map of parameter names and their values
   */
  public Map<String, Object> getParameters() {
    return parameters;
  }

  /**
   * Sets the parameters of the simulation.
   *
   * @param parameters - the new map of parameter names and values
   */
  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }

  /**
   * Retrieves the theme of the simulation.
   *
   * @return the theme name
   */
  public String getTheme() {
    return theme;
  }

  /**
   * Sets the theme of the simulation.
   *
   * @param theme - the new theme name
   */
  public void setTheme(String theme) {
    this.theme = theme;
  }

  /**
   * Retrieves the language setting of the simulation.
   *
   * @return the language name
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Sets the language setting of the simulation.
   *
   * @param language - the new language name
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Retrieves the custom color mapping for cell states.
   *
   * @return a map of cell state values to color codes
   */
  public Map<Integer, String> getCustomColorMap() {
    return customColorMap;
  }

  /**
   * Sets the custom color mapping for cell states.
   *
   * @param customColorMap - the new map of cell state values to color codes
   */
  public void setCustomColorMap(Map<Integer, String> customColorMap) {
    this.customColorMap = customColorMap;
  }

  /**
   * Retrieves the number of states used for the reversal process.
   *
   * @return the number of states used for reversal
   */
  public int getReverseStateNum() {
    return reverseStateNum;
  }

  /**
   * Sets the number of states used for the reversal process.
   *
   * @param reverseStateNum - the new number of states for reversal
   */
  public void setReverseStateNum(int reverseStateNum) {
    this.reverseStateNum = reverseStateNum;
  }

  /**
   * Retrieves the simulation ID.
   *
   * @return the simulation ID
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the simulation ID.
   *
   * @param id - the new simulation ID
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Retrieves the number of states in the simulation (for dynamic state count).
   *
   * @return the number of states in the simulation
   * @throws IllegalStateException if the parameters map is null or missing the "numStates" key
   */
  public int getNumStates() {
    if (parameters == null) {
      throw new IllegalStateException(myErrorResources.getString("NullParameterMap"));
    }

    double numStates = 0.0; // Default value

    Object value = parameters.get("numStates");

    if (value instanceof Number) {
      numStates = ((Number) value).doubleValue(); // Safely convert any number type to Double
    } else if (value instanceof String) {
      try {
        numStates = Double.parseDouble((String) value); // Convert from String if needed
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(myErrorResources.getString("InvalidNumStates"));
      }
    }

    System.out.println("numStates: " + numStates);

    return (int) numStates;
  }


  // placement methods Jessica made so she could test simulation
  // simulation tests use a mock version of these so ckeep the structure change the insides
  public ShapeType getShape() {
    // right now just always rectangle
    return ShapeType.RECTANGLE;
  }

  public EdgeType getEdge() {
    return EdgeType.NONE;
  }

  public NeighborhoodType getNeighborhood() {
    SimType simType = getType();

    if (simType.isDefaultRectangularGrid()) {
      return NeighborhoodType.MOORE;
    } else {
      return NeighborhoodType.VON_NEUMANN;
    }
  }
}