package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.simulation.rules.darwinhandler.DarwinCommandHandler;
import cellsociety.model.statefactory.CellStateFactory;
import cellsociety.model.statefactory.handler.CellStateHandler;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.darwin.DarwinProgram;
import cellsociety.model.util.darwin.DarwinProgramFactory;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.OptionalInt;

public class DarwinRule extends Rule<DarwinCell> {

  /**
   * Constructs a {@code Rule} object and initializes it with the provided parameters. The parameters
   * are checked and set to ensure validity.
   *
   * @param parameters the {@code GenericParameters} object containing the configuration and settings
   *                   required for the rule. Must not be {@code null}.
   * @throws SimulationException if the parameters are {@code null}.
   */
  public DarwinRule(GenericParameters parameters) {
    super(parameters);
  }

  @Override
  public int apply(DarwinCell cell) {
    try {
      int instruction = cell.getCurrentInstruction();

      // lol basically if you get an action command you break otherwise really could
      // just be forever
      while (true) {
        DarwinCommand command = cell.getCommand(instruction);

        // handler is essentially the switch statement for what method should be executed
        DarwinCommandHandler handler = DarwinCommandFactory.getHandler(command.getType());

        OptionalInt newInstruction = handler.execute(command, cell);
        if (newInstruction.isPresent()) {
          instruction = newInstruction.getAsInt();
        } else {
          instruction = cell.getNextProgramInstruction(instruction);
        }

        if (command.getType().isAction()) {
          break;
        }
      }

      cell.setNextInstruction(instruction);

      return 0;
    }
    catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

}

