package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.OptionalInt;

public class IfEmptyCommandHandler implements DarwinCommandHandler {

  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell) {
    int nextInstruction;
    try {
      nextInstruction = DarwinCommandHandlerHelperMethods.getIntegerArgument(command);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    if (DarwinCommandHandlerHelperMethods.nearbyState(cell, 0)) {
      return OptionalInt.of(nextInstruction);
    }

    return OptionalInt.empty();
  }
}
