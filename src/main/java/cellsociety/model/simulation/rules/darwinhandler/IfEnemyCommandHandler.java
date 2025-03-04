package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.OptionalInt;

public class IfEnemyCommandHandler implements DarwinCommandHandler {

  // Perform the given instruction next only if the space nearby ahead of the creature is
  // occupied by a creature of any different species;
  // otherwise, go on with the next instruction
  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell) {
    int nextInstruction;
    try {
      nextInstruction = DarwinCommandHandlerHelperMethods.getIntegerArgument(command);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    if (DarwinCommandHandlerHelperMethods.nearbyEnemy(cell)) {
      return OptionalInt.of(nextInstruction);
    }

    return OptionalInt.empty();
  }
}
