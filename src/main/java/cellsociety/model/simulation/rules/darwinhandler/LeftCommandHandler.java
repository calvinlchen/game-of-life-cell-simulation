package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.Optional;
import java.util.OptionalInt;

public class LeftCommandHandler implements DarwinCommandHandler {

  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid) {
    int degrees;
    try {
      degrees = DarwinCommandHandlerHelperMethods.getIntegerArgument(command);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    DarwinCommandHandlerHelperMethods.rotate(cell, degrees, false);

    return OptionalInt.empty();
  }
}
