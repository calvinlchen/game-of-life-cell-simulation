package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.util.darwin.DarwinCommand;
import java.util.Optional;
import java.util.OptionalInt;

public interface DarwinCommandHandler {

  // returns the next instruction if command makes a special one
  OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid);

}
