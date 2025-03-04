package cellsociety.model.util.darwin;

import cellsociety.model.util.exceptions.SimulationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DarwinProgram implements Iterable<DarwinCommand> {

  private final List<DarwinCommand> commands = new ArrayList<>();
  private final String species;

  public DarwinProgram(String species) {
    try {
      this.species = species;
      new DarwinParser(species, this);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }

  public void addDarwinCommand(DarwinCommand command) {
    commands.add(command);
  }

  public DarwinCommand getDarwinCommand(int instructionNumber) {
    if (instructionNumber < 0 || instructionNumber >= commands.size()) {
      throw new SimulationException("InvalidInstructionNumber", List.of(
          String.valueOf(instructionNumber)
      ));
    }

    return commands.get(instructionNumber);
  }

  public int size() {
    return commands.size();
  }

  public int nextInstructionNumber(int instructionNumber) {
    if (instructionNumber < 0) {
      throw new SimulationException("InvalidInstructionNumber", List.of(
          String.valueOf(instructionNumber)
      ));
    }

    instructionNumber++;

    // if incrementing puts number out of bound wrap it so it is in bounds, if negative just sad
    if (instructionNumber >= commands.size()) {
      return instructionNumber % commands.size();
    }
    return instructionNumber;
  }

  @Override
  public Iterator<DarwinCommand> iterator() {
    return commands.iterator();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < commands.size(); i++) {
      sb.append(i).append(": ").append(commands.get(i)).append("\n");
    }
    return sb.toString();
  }
}
