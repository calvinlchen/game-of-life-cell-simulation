package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.Optional;
import java.util.OptionalInt;

public class InfectCommandHandler implements DarwinCommandHandler {

  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid,
      GenericParameters parameters) {
    int steps;
    try {
      steps = DarwinCommandHandlerHelperMethods.getIntegerArgument(command);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    DarwinCommandHandlerHelperMethods.infectNearby(cell,
        (int) Math.round(parameters.getParameter("nearbyAhead")), cell, steps);

    return OptionalInt.empty();


  }
}
