package cellsociety.model.simulation.cell;

import cellsociety.model.simulation.rules.DarwinRule;
import cellsociety.model.statefactory.CellStateFactory;
import cellsociety.model.statefactory.handler.CellStateHandler;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.darwin.DarwinProgram;
import cellsociety.model.util.darwin.DarwinProgramFactory;
import cellsociety.model.util.exceptions.SimulationException;

// note I am not implementing the aprameters, if so you would just change the rules to look
// at neighbors recursively based on the depth of your sight

public class DarwinCell extends Cell<DarwinCell, DarwinRule> {
  private DarwinProgram program;
  private int currentInstruction;
  private int nextInstruction;

  private boolean infectedThisStep;


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
    infectedThisStep = false;
  }

  @Override
  protected boolean shouldSkipCalculation() {
    // if they have been infected this step
    // they don't get to do anything till next step

    // empty cells should also not have to do anything
    return getCurrentState() == 0 || infectedThisStep;
  }

  @Override
  protected void postProcessNextState(int newState) {
    // fun fact darwin always returns -1, since process will set next stae if needed
    if (getNextState() != getCurrentState()) {
      program = getProgramForSpecies(newState);
    }

    // otherwise no change to your state
  }

  // I think infected creatures need to be treated the same way as consumed things
  // in wator world aka their movement gets canceled
  // TODO: implement cancleing of infected things moves
  @Override
  public void step() {
    super.step();
    // in addition to doing super step also update your next instruction
    currentInstruction = nextInstruction;
  }

  @Override
  public void resetParameters() {
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

}
