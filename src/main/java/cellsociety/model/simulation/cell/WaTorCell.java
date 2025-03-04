package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.CellStates.WATOR_MAXSTATE;
import static cellsociety.model.util.constants.CellStates.WATOR_EMPTY;
import static cellsociety.model.util.constants.CellStates.WATOR_SHARK;

import cellsociety.model.simulation.rules.WaTorRule;
import cellsociety.model.util.constants.exceptions.SimulationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code WaTorCell} class represents a single cell in the Wa-Tor World simulation.
 *
 * <p>This simulation models an ecosystem of fish and sharks, where fish move and reproduce, and
 * sharks hunt for fish while managing their energy levels.</p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Interacts with the {@link WaTorRule} to determine movement, reproduction, and
 *   survival.</li>
 *   <li>Tracks survival steps and energy levels for individual sharks.</li>
 *   <li>Overrides the Template Method to accommodate movement dynamics.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * WaTorRule rule = new WaTorRule(parameters);
 * WaTorCell cell = new WaTorCell(WATOR_SHARK, rule);
 * cell.calcNextState();
 * cell.step();
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
public class WaTorCell extends Cell<WaTorCell, WaTorRule> {

  private static final Logger logger = LogManager.getLogger(WaTorCell.class);

  private int myStepsSurvived;
  private int myEnergy;

  private int myNextStepsSurvived;
  private int myNextEnergy;

  private boolean consumed;
  private WaTorCell movedFrom;

  /**
   * Constructs a WaTorCell with a specified initial state and rule.
   *
   * @param state - the initial state of the cell (must be a state from WaTorStates)
   * @param rule  - the WaTorRule to calculate the next state
   */
  public WaTorCell(int state, WaTorRule rule) {
    super(state, rule);

    try {
      initializeDefaultVariables(state);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  private void initializeDefaultVariables(int state) {
    validateState(state, WATOR_MAXSTATE);

    myStepsSurvived = 0;
    try {
      myEnergy =
          state == WATOR_SHARK ? (int) getRule().getParameters().getParameter("sharkInitialEnergy")
              : 0;
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
    myNextStepsSurvived = 0;
    myNextEnergy = 0;

    consumed = false;
    movedFrom = null;
  }

  /**
   * Determines whether the state calculation should be skipped.
   *
   * <p>State calculations should only occur when the cell remains unchanged from the previous
   * state.</p>
   *
   * @return {@code true} if the calculation should be skipped, {@code false} otherwise.
   */
  @Override
  protected boolean shouldSkipCalculation() {
    return getCurrentState() != getNextState();
  }

  /**
   * Processes the next computed state before applying it.
   *
   * <p>Handles movement, resetting survival steps, and energy adjustments.</p>
   *
   * @param newState The newly computed state.
   */
  @Override
  protected void postProcessNextState(int newState) {
    try {
      if (newState != -1) {
        setNextState(newState);

        myNextStepsSurvived = 0;
        myNextEnergy = newState == WATOR_SHARK ? (int) getRule().getParameters()
            .getParameter("sharkInitialEnergy") : 0;
      }
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Steps the current state to the next state as well as the cell values.
   *
   * <p>Since this is the only cell type that modifies step, did not use the template method
   * on it</p>
   */
  @Override
  public void step() {
    try {
      if (movedFrom != null && movedFrom.isConsumed()) {
        // basically for this one it was empty, and since fish is consumed its still empty
        // so can update steps after we know it will always add 1 to stateLength
        logger.debug("Cell at {} was moved from a consumed cell, resetting to EMPTY.",
            getPosition());
        setNextState(WATOR_EMPTY);
        setCurrentState(WATOR_EMPTY);
        updateStateLength();
      } else {
        updateStateLength();
        setCurrentState(getNextState());
      }
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  @Override
  protected WaTorCell getSelf() {
    return this;
  }

  @Override
  public void resetParameters() {
    myStepsSurvived = myNextStepsSurvived;
    myEnergy = myNextEnergy;
    consumed = false;
    movedFrom = null;
  }

  @Override
  protected int getMaxState() {
    return WATOR_MAXSTATE;
  }

  /**
   * Sets the next state of the cell to the given cell with the given cell values.
   *
   * @param state         - the state the next state of the cell should be
   * @param stepsSurvived - the number of steps the next state of the cell should have
   * @param energy        - the amount of energy the next state of the cell should have
   */
  public void setNextState(int state, int stepsSurvived, int energy) {
    try {
      setNextState(state);

      myNextStepsSurvived = stepsSurvived;
      myNextEnergy = energy;
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  /**
   * Sets the next state of the cell to the given cell with the given cell values along with
   * tracking which cell it came from.
   *
   * @param state         - the state the next state of the cell should be
   * @param stepsSurvived - the number of steps the next state of the cell should have
   * @param energy        - the amount of energy the next state of the cell should have
   * @param movedFrom     - where the new state moved from, basically the old cell that state was
   */
  public void setNextState(int state, int stepsSurvived, int energy, WaTorCell movedFrom) {
    try {
      this.movedFrom = movedFrom;
      setNextState(state, stepsSurvived, energy);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  // Start of Setters and Getters ------

  /**
   * Retrieves the number of steps the cell has survived in its current state.
   *
   * @return the number of steps the cell has survived
   */
  public int getStepsSurvived() {
    return myStepsSurvived;
  }

  /**
   * Sets the number of steps the cell has survived in its current state.
   *
   * <p>Currently only used in tests, because can set in other ways in rules, but can be used by
   * rules
   *
   * @param stepsSurvived the number of steps the cell has survived
   */
  public void setStepsSurvived(int stepsSurvived) {
    myStepsSurvived = stepsSurvived;
  }

  /**
   * Sets the energy level of the cell.
   *
   * <p>Currently only used in tests, because can set in other ways in rules, but can be used by
   * rules
   *
   * @param energy the energy value to be assigned to the cell
   */
  public void setEnergy(int energy) {
    myEnergy = energy;
  }

  /**
   * Get the current energy level of the cell.
   *
   * @return the energy level of the cell
   */
  public int getEnergy() {
    return myEnergy;
  }

  /**
   * Return if this cell has been consumed.
   *
   * @return true if the cell in the current state has been consumed
   */
  public boolean isConsumed() {
    return consumed;
  }

  /**
   * Set the state of if the cell has been consumed.
   *
   * @param consumed - true if the cell is consumed
   */
  public void setConsumed(boolean consumed) {
    this.consumed = consumed;
  }

  int getNextStepsSurvived() {
    return myNextStepsSurvived;
  }

  int getNextEnergy() {
    return myNextEnergy;
  }
}
