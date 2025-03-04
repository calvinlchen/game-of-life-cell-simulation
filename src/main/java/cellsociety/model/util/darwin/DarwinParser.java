package cellsociety.model.util.darwin;

import cellsociety.model.util.exceptions.SimulationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DarwinParser {

  private static final String DATA_DIR = "data/darwin/";
  private static final Logger logger = LogManager.getLogger(DarwinParser.class);

  private final DarwinProgram program;

  public DarwinParser(String species, DarwinProgram program) {
    this.program = program;
    parseFile(species);
  }

  private void parseFile(String species) {
    Path filePath = Paths.get(DATA_DIR, species + ".txt");
    StringBuilder currentCommand = new StringBuilder();

    try (BufferedReader reader = Files.newBufferedReader(filePath)) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) {
          continue;
        }

        if (!currentCommand.isEmpty() && isNewCommand(line)) {
          processCommand(currentCommand.toString());
          currentCommand.setLength(0);
        }

        if (!currentCommand.isEmpty()) {
          currentCommand.append(" ");
        }
        currentCommand.append(line);
      }

      if (!currentCommand.isEmpty()) {
        processCommand(currentCommand.toString());
      }

    } catch (IOException e) {
      logger.error("Error reading Darwin file for species {}: {}", species, e.getMessage());
      throw new SimulationException("InvalidDarwinFile", List.of(species), e);
    }
  }

  private boolean isNewCommand(String line) {
    String firstWord = line.split("\\s+")[0].toLowerCase();
    return DarwinCommandType.fromString(firstWord) != DarwinCommandType.UNKNOWN;
  }

  private void processCommand(String command) {
    String[] parts = command.split("\\s+", 2);
    String action = parts[0].toLowerCase();
    String argument = (parts.length > 1) ? parts[1] : null;

    // Get the command type from DarwinCommandType
    DarwinCommandType type = DarwinCommandType.fromString(action);
    if (type != DarwinCommandType.UNKNOWN) {
      program.addDarwinCommand(new DarwinCommand(type, argument));
      logger.debug("Processed command: {}{}", type, argument != null ? " " + argument : "");
    } else {
      logger.warn("Unknown command ignored: {}", command);
    }
  }
}
