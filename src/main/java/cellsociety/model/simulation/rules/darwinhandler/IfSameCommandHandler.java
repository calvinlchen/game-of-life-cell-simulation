package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.Optional;
import java.util.OptionalInt;

public class IfSameCommandHandler implements DarwinCommandHandler {

  // Perform the given instruction next only if the space nearby ahead of the creature
  // is occupied by a creature of the same species;
  // otherwise, go on with the next instruction
  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid,
      GenericParameters parameters) {
    int nextInstruction;
    try {
      nextInstruction = DarwinCommandHandlerHelperMethods.getIntegerArgument(command);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    if (DarwinCommandHandlerHelperMethods.nearbyState(cell, cell.getCurrentState(),
        (int) Math.round(parameters.getParameter("nearbyAhead")), cell.getDirection())) {
      return OptionalInt.of(nextInstruction);
    }

    return OptionalInt.empty();
  }
}
