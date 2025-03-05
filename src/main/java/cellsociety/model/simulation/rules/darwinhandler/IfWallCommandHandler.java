package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.Optional;
import java.util.OptionalInt;

// uhh I have no clue how to do this since currently the cell has no impact on borders
// also how does this interact with edges
// maybe it can be like if it doesnt have full neighbor capacity it not an edge ? I guess when grid
// initalizes cells it could also give it consciousness about grid information
// and then update it as grid changes? and then this affects it
public class IfWallCommandHandler implements DarwinCommandHandler {


  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid) {
    int nextInstruction;
    try {
      nextInstruction = DarwinCommandHandlerHelperMethods.getIntegerArgument(command);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    if (grid.isEmpty()) {
      return OptionalInt.empty();
    }

    if (DarwinCommandHandlerHelperMethods.nearbyBoundary(cell, grid.get())) {
      return OptionalInt.of(nextInstruction);
    }

    return OptionalInt.empty();
  }
}
