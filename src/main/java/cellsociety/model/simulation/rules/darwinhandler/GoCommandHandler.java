package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GoCommandHandler implements DarwinCommandHandler {

  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid) {
    int nextInstruction;
    try {
      nextInstruction = DarwinCommandHandlerHelperMethods.getIntegerArgument(command);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    return OptionalInt.of(nextInstruction);
  }
}
