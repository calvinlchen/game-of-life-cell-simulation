package cellsociety.model.util;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.StateEnum;

/**
 * An object that holds simulation data
 *
 * @author Kyaira Boughton
 */
@Setter
@Getter
public class XMLData{

  private SimType type; //enum for game type
  private String title; //title of game
  private String author; //name of author
  private String description; //description of simulation behavior
  private int gridRowNum; //number of rows in a grid
  private int gridColNum; //number of columns in grid.
  private ArrayList<List<StateEnum>> cellStateList = new ArrayList<>(); //a list of each cell's state in the grid. size unknown
  private Map<String, Double> parameters; //<parameter name as string, value>

}
