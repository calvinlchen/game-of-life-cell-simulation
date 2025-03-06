package cellsociety.model.simulation.rules.darwinhandler;

import cellsociety.model.simulation.cell.DarwinCell;
import cellsociety.model.simulation.grid.Grid;
import cellsociety.model.simulation.parameters.GenericParameters;
import cellsociety.model.util.darwin.DarwinCommand;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * The {@code DarwinCommandHandler} interface defines a contract for handling Darwin simulation
 * commands.
 *
 * <p>In the Darwin simulation, organisms follow a series of instructions, known as Darwin
 * commands, that dictate their movement, interaction, and survival. This interface provides an
 * abstraction for executing these commands on a {@link DarwinCell}.</p>
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Processes a given {@link DarwinCommand} within the simulation context.</li>
 *   <li>Updates the state of the executing {@link DarwinCell} based on the command.</li>
 *   <li>Provides access to the simulation's {@link Grid} if necessary for interaction.</li>
 *   <li>Returns the next instruction index if the command requires an explicit jump.</li>
 * </ul>
 *
 * <h2>Extensibility:</h2>
 *
 * <p>To add new commands, simply implement {@code DarwinCommandHandler} and define behavior
 * in the {@code execute} method.</p>
 *
 * @author Jessica Chen
 * @author ChatGPT for java docs
 */
public interface DarwinCommandHandler {

  /**
   * Executes a Darwin simulation command on a given {@link DarwinCell}.
   *
   * <p>This method processes a {@link DarwinCommand}, modifying the state of the executing
   * cell based on simulation rules. It optionally interacts with the simulation {@link Grid} and
   * parameters to facilitate movement, combat, reproduction, or other behavior.</p>
   *
   * @param command    the Darwin command to execute.
   * @param cell       the {@link DarwinCell} executing the command.
   * @param grid       an optional {@link Grid} representing the simulation space.
   * @param parameters simulation parameters that may influence behavior.
   * @return an {@code OptionalInt} containing the next instruction index if the command requires a
   * jump, otherwise {@code OptionalInt.empty()} to proceed sequentially.
   */
  OptionalInt execute(DarwinCommand command, DarwinCell cell, Optional<Grid> grid,
      GenericParameters parameters);

}
