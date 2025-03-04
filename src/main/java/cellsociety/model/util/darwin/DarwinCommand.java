package cellsociety.model.util.darwin;

public class DarwinCommand {
  private final DarwinCommandType type;
  private final String argument;

  public DarwinCommand(DarwinCommandType type, String argument) {
    this.type = type;
    this.argument = argument;
  }

  public DarwinCommandType getType() {
    return type;
  }

  public String getArgument() {
    return argument;
  }

  @Override
  public String toString() {
    return type + (argument != null ? " " + argument : "");
  }
}
