package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.OptionalInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UnknownCommandHandler implements DarwinCommandHandler {

  private static final Logger logger = LogManager.getLogger(UnknownCommandHandler.class);

  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell) {
    logger.error("If you got to handler, quite impressive. But still unknown command");
    throw new SimulationException("UnknownDarwinInstruction");
  }
}
