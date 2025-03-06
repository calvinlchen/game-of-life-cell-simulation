package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * The {@code IfEmptyCommandHandler} class handles the execution of the "IFEMPTY" command in the
 * Darwin simulation.
 *
 * <p>This command checks whether the cell's front-facing space is empty (state 0). If empty, the
 * command causes a jump to a specified instruction index; otherwise, execution continues
 * normally.</p>
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Retrieves the integer argument (target instruction index) from the command.</li>
 *   <li>Checks if the space directly ahead is empty using the {@code nearbyState} helper method.
 *   </li>
 *   <li>Returns the specified instruction index if the condition is met, otherwise returns an
 *   empty result.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * DarwinCommand ifEmptyCommand = new DarwinCommand(IFEMPTY, 3);
 * IfEmptyCommandHandler handler = new IfEmptyCommandHandler();
 * OptionalInt nextIndex = handler.execute(ifEmptyCommand, cell, Optional.of(grid), parameters);
 *
 * if (nextIndex.isPresent()) {
 *     cell.setNextInstruction(nextIndex.getAsInt());
 * }
 * </pre>
 *
 * <h2>Return Behavior:</h2>
 * <ul>
 *   <li>{@code OptionalInt.of(n)} → Jump to instruction index {@code n} if the space ahead
 *   is empty.</li>
 *   <li>{@code OptionalInt.empty()} → Continue execution normally if space ahead is occupied.</li>
 *   <li>Throws {@link SimulationException} if argument parsing fails.</li>
 * </ul>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with javadocs
 */
public class IfEmptyCommandHandler implements DarwinCommandHandler {

  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid,
      GenericParameters parameters) {
    int nextInstruction;
    try {
      nextInstruction = DarwinCommandHandlerHelperMethods.getIntegerArgument(command);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    if (DarwinCommandHandlerHelperMethods.nearbyState(cell, 0,
        (int) Math.round(parameters.getParameter("nearbyAhead")), cell.getDirection())) {
      return OptionalInt.of(nextInstruction);
    }

    return OptionalInt.empty();
  }
}
