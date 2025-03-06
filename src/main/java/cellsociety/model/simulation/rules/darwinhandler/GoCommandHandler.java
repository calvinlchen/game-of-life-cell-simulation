package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.exceptions.SimulationException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code GoCommandHandler} class handles the execution of the "GO" command in the Darwin
 * simulation.
 *
 * <p>This command is responsible for directing an organism to jump to a specific instruction
 * index, allowing for loops and conditional execution in Darwin-based simulations.</p>
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Extracts the integer argument from the {@link DarwinCommand}.</li>
 *   <li>Returns the specified instruction index to facilitate execution jumps.</li>
 *   <li>Throws an exception if the argument extraction fails.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * DarwinCommand goCommand = new DarwinCommand(GO, 5);
 * GoCommandHandler handler = new GoCommandHandler();
 * OptionalInt nextIndex = handler.execute(goCommand, cell, Optional.of(grid), parameters);
 *
 * if (nextIndex.isPresent()) {
 *     cell.setNextInstruction(nextIndex.getAsInt());
 * }
 * </pre>
 *
 * <h2>Return Behavior:</h2>
 * <ul>
 *   <li>{@code OptionalInt.of(n)} → Jump to instruction index {@code n}.</li>
 *   <li>Throws {@link SimulationException} if argument parsing fails.</li>
 * </ul>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with javadocs
 */
public class GoCommandHandler implements DarwinCommandHandler {

  /**
   * Executes the "GO" command, directing the Darwin organism to jump to a specific instruction
   * index.
   *
   * <p>The command contains an integer argument specifying the target instruction.
   * If parsing fails, an exception is thrown.</p>
   *
   * @param command    the {@link DarwinCommand} representing the "GO" operation.
   * @param cell       the {@link DarwinCell} executing the command.
   * @param grid       an optional {@link Grid} representing the simulation space (unused here).
   * @param parameters additional simulation parameters (unused here).
   * @return an {@code OptionalInt} containing the next instruction index.
   * @throws SimulationException if the integer argument cannot be extracted.
   */
  @Override
  public OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid,
      GenericParameters parameters) {
    int nextInstruction;
    try {
      nextInstruction = DarwinCommandHandlerHelperMethods.getIntegerArgument(command);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }

    return OptionalInt.of(nextInstruction);
  }
}
