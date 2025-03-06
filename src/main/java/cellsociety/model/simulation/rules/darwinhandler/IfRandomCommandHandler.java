package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IfRandomCommandHandler implements DarwinCommandHandler {

  // Perform the given instruction next half of the time (otherwise continue
  // with the next instruction half of the time)
  // This allows some creatures to exercise what might be called the rudiments of “free will”
  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid,
      GenericParameters parameters) {
    int nextInstruction;
    try {
      nextInstruction = DarwinCommandHandlerHelperMethods.getIntegerArgument(command);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    if (DarwinCommandHandlerHelperMethods.getRandom().nextBoolean()) {
      return OptionalInt.of(nextInstruction);
    }

    return OptionalInt.empty();
  }


}
