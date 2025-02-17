package cellsociety.model.util;

import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.StateEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An object that holds simulation data
 *
 * @author Kyaira Boughton
 */
public class XMLData {

  public static int totalSimulations;


  private SimType type; //enum for game type
  private String title; //title of game
  private String author; //name of author
  private String description; //description of simulation behavior
  private int gridRowNum; //number of rows in a grid
  private int gridColNum; //number of columns in grid.
  private List<Integer> cellStateList = new ArrayList<>(); //a list of each cell's state in the grid. size unknown
  private Map<String, Double> parameters; //<parameter name as string, value>
  private int id;

  public XMLData() {
    id = totalSimulations;
    totalSimulations++;
  }

  /**
   * Returns the value of the type.
   *
   * @return the type
   */
  public SimType getType() {
    return type;
  }

  /**
   * Sets the value of the type.
   *
   * @param type the new SimType (enum) value for type
   */
  public void setType(SimType type) {
    this.type = type;
  }

  /**
   * Returns the value of the title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the value of the title.
   *
   * @param title the new String value for title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the value of the simulation author.
   *
   * @return the author name
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Sets the value of the author.
   *
   * @param author the new String value for author
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Returns the value of the simulation description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the value of the description.
   *
   * @param description the new String value for description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the value of the number of rows in simulation's grid.
   *
   * @return the number of rows in the grid
   */
  public int getGridRowNum() {
    return gridRowNum;
  }

  /**
   * Sets the value of the gridRowNum.
   *
   * @param gridRowNum the new int value for gridRowNum
   */
  public void setGridRowNum(int gridRowNum) {
    this.gridRowNum = gridRowNum;
  }

  /**
   * Returns the value of the number of columns in the simulation's grid.
   *
   * @return the number of columns in grid
   */
  public int getGridColNum() {
    return gridColNum;
  }

  /**
   * Sets the value of the gridColNum.
   *
   * @param gridColNum the new int value for cellStateList
   */
  public void setGridColNum(int gridColNum) {
    this.gridColNum = gridColNum;
  }

  /**
   * Returns the value of the cellStateList.
   *
   * @return the cellStateList
   */
  public List<Integer> getCellStateList() {
    return cellStateList;
  }

  /**
   * Sets the value of the cellStateList.
   *
   * @param cellStateList the new ArrayList<List<Integer>> value for cellStateList
   */
  public void setCellStateList(List<Integer> cellStateList) {
    this.cellStateList = cellStateList;
  }

  /**
   * Returns the value of the simulation's parameters.
   *
   * @return the parameter's map
   */
  public Map<String, Double> getParameters() {
    return parameters;
  }

  /**
   * Sets the value of the parameter's map.
   *
   * @param parameters the new Map<String, Double> value for parameters
   */
  public void setParameters(Map<String, Double> parameters) {
    this.parameters = parameters;
  }

  /**
   * Returns the id of the simulation
   *
   * @return the id of the simulation
   */
  public int getId() {
    return id;
  }

  public int getNumStates() {
    return parameters.getOrDefault("numStates", 0.).intValue();
  }
}