package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.util.darwin.DarwinCommand;
import java.util.OptionalInt;

public interface DarwinCommandHandler {

  // returns the next instruction if command makes a special one
  OptionalInt execute(DarwinCommand command, DarwinCell cell);

}
