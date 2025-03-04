package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.statefactory.CellStateFactory;
import cellsociety.model.statefactory.handler.CellStateHandler;
import cellsociety.model.util.SimulationTypes.SimType;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.darwin.DarwinProgram;
import cellsociety.model.util.darwin.DarwinProgramFactory;
import cellsociety.model.util.exceptions.SimulationException;

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
    int instruction = cell.getCurrentInstruction();

    // lol basically if you get an action command you break otherwise really could
    // just be forever
    while (true) {
      boolean isAction = process(cell.getCommand(cell.getCurrentInstruction()));
      if (isAction) {
        break;
      }
    }


    return 0;
  }

  /**
   * ir it sets something it sets it here
   */
  private boolean process(DarwinCommand command) {

    return command.getType().isAction();
  }

}

