package cellsociety.model.simulation.cell;

import static cellsociety.model.util.constants.GridTypes.DirectionType.E;
import static cellsociety.model.util.constants.SimulationConstants.NULL_STATE;

import cellsociety.model.simulation.rules.DarwinRule;
import cellsociety.model.simulation.rules.darwinhandler.DarwinCommandHandlerHelperMethods;
import cellsociety.model.statefactory.CellStateFactory;
import cellsociety.model.statefactory.handler.CellStateHandler;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.constants.GridTypes.DirectionType;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.darwin.DarwinProgram;
import cellsociety.model.util.darwin.DarwinProgramFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// note I am not implementing the aprameters, if so you would just change the rules to look
// at neighbors recursively based on the depth of your sight

public class DarwinCell extends Cell<DarwinCell, DarwinRule> {

  private static Logger logger = LogManager.getLogger(DarwinCell.class);

  private DarwinProgram program;
  private int currentInstruction;
  private int nextInstruction;

  private int awaitingInfectionTimer; // how many steps before infected
  private DarwinCell infectingCell;

  private int infectedTimer;          // how many times to revert
  private int oldProgram;             // additional thing to wator world

  private boolean infectedThisStep;  // no infected this stage starts tracking till the next step

  private DirectionType direction;
  private DirectionType nextDirection;


  /**
   * Constructs a cell with the specified initial state and a rule for determining its behavior.
   *
   * @param state - the initial state of the cell
   * @param rule  - the rule used to calculate the next state of the cell
   */
  public DarwinCell(int state, DarwinRule rule) {
    super(state, rule);
    program = getProgramForSpecies(state);
    currentInstruction = 0;
    nextInstruction = 0;

    // waiting to be infected
    awaitingInfectionTimer = NULL_STATE;
    infectingCell = null;

    // reverting after infection
    infectedTimer = NULL_STATE;
    oldProgram = NULL_STATE;

    // everyone can just start facing east
    direction = E;
    nextDirection = E;
  }

  @Override
  protected boolean shouldSkipCalculation() {
    // if they have been infected this step
    // they don't get to do anything till next step

    if (infectedThisStep) {
      return false;
    }

    // on all steps want to check for awaitingInfection
    // if this cell is in infection steps still
    if (DarwinCommandHandlerHelperMethods.checkStillInfecting(this, infectingCell,
        (int) Math.round(this.getRule().getParameters().getParameter("nearbyAhead")),
        infectingCell.getDirection())) {
      awaitingInfectionTimer--;
      if (awaitingInfectionTimer == 0) {
        // going to assume infection timer has been set as has old program
        setNextState(this.getCurrentState());
        setNextInstruction(0);  // set it to the start of the new program

        // if its infected this state will never calculate its values
        return shouldSkipCalculation();
      }
    } else {
      awaitingInfectionTimer = NULL_STATE;
      infectingCell = null;

      oldProgram = NULL_STATE;
      infectedTimer = NULL_STATE;
    }

    // on the step they become their new program that's all they do
    if (infectedTimer > 0) {
      infectedTimer--;
      if (infectedTimer == 0) {
        setNextState(oldProgram);
        setNextInstruction(0);  // set it to the start of the new program

        infectedTimer = NULL_STATE;
        oldProgram = NULL_STATE;
      }
    }

    // empty cells should also not have to do anything
    return getCurrentState() != getNextState();
  }

  @Override
  protected void postProcessNextState(int newState) {
    // fun fact darwin always returns -1, since process will set next stae if needed
    if (getNextState() != getCurrentState()) {
      program = getProgramForSpecies(newState);
    }
  }


  @Override
  public void resetParameters() {
    currentInstruction = nextInstruction;
    direction = nextDirection;

    infectedThisStep = false;
  }

  @Override
  protected DarwinCell getSelf() {
    return null;
  }

  @Override
  protected int getMaxState() {
    return 0;
  }


  private DarwinProgram getProgramForSpecies(int species) {
    // get the current handler for Darwin, xml should have already updated it with the state
    CellStateHandler handler = CellStateFactory.getHandler(-1, SimType.Darwin, -1);

    return DarwinProgramFactory.getProgram(handler.statetoString(species));
  }

  public int getCurrentInstruction() {
    return currentInstruction;
  }

  /**
   * Based on its currentInstruction, all cell does is get its current command
   */
  public DarwinCommand getCommand(int instruction) {
    return program.getDarwinCommand(instruction);
  }

  /**
   * After rule applies, should set its next instruction to the instruction
   */
  public void setNextInstruction(int instruction) {
    nextInstruction = instruction;
  }

  public int getNextProgramInstruction(int instruction) {
    return program.nextInstructionNumber(instruction);
  }

  public DirectionType getDirection() {
    return direction;
  }

  public void setDirection(DirectionType direction) {
    nextDirection = direction;
  }

  public void setInfectingCell(DarwinCell infectingCell) {
    this.infectingCell = infectingCell;
  }

  public void setAwaitingInfectionTimer(int steps) {
    awaitingInfectionTimer = steps;
  }

  public void setInfectedTimer(int steps) {
    infectedTimer = steps;
  }

  public void setOldProgram(int currentState) {
    oldProgram = currentState;
  }

  public void setInfectedThisStep() {
    infectedThisStep = true;
  }

  public boolean infectedThisStep() {
    return infectedThisStep;
  }

  public boolean isInfected() {
    return infectingCell != null;
  }
}
