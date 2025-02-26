package cellsociety.model.util.constants;

/**
 * State constants to use for rules to keep them central
 *
 * <p> I tried doing them in resource properties originally, but you can't use them in switch
 * statements
 *
 * @author Jessica Chen
 */
public class CellStates {

  public static final int GAMEOFLIFE_DEAD = 0;
  public static final int GAMEOFLIFE_ALIVE = 1;
  public static final int GAMEOFLIFE_MAXSTATE = 2;

  public static final int PERCOLATION_BLOCKED = 0;
  public static final int PERCOLATION_OPEN = 1;
  public static final int PERCOLATION_PERCOLATED = 2;
  public static final int PERCOLATION_MAXSTATE = 3;

  public static final int FIRE_EMPTY = 0;
  public static final int FIRE_TREE = 1;
  public static final int FIRE_BURNING = 2;
  public static final int FIRE_MAXSTATE = 3;

  public static final int SEGREGATION_EMPTY = 0;
  public static final int SEGREGATION_A = 1;
  public static final int SEGREGATION_B = 2;
  public static final int SEGREGATION_MAXSTATE = 3;

  public static final int WATOR_EMPTY = 0;
  public static final int WATOR_FISH = 1;
  public static final int WATOR_SHARK = 2;
  public static final int WATOR_MAXSTATE = 3;

  public static final int FALLINGSAND_EMPTY = 0;
  public static final int FALLINGSAND_STEEL = 1;
  public static final int FALLINGSAND_SAND = 2;
  public static final int FALLINGSAND_WATER = 3;
  public static final int FALLINGSAND_MAXSTATE = 4;

  public static final int CHOUREG2_MAXSTATE = 8;
  public static final int LANGTON_MAXSTATE = 8;
  public static final int PETELKA_MAXSTATE = 5;

}
