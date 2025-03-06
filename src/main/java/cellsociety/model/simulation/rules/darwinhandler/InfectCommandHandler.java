package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.darwin.DarwinCommand;
import java.util.Optional;
import java.util.OptionalInt;

public class InfectCommandHandler implements DarwinCommandHandler {

  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid,
      GenericParameters parameters) {
    // going to do you last need to make cell remember how long its been infected and then its old
    // state
    return OptionalInt.empty();
  }
}
