package cellsociety.model.util.darwin;

import cellsociety.model.util.exceptions.SimulationException;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public enum DarwinCommandType {
  MOVE(true), LEFT(true), RIGHT(true), INFECT(true),
  IF_EMPTY(false), IF_WALL(false), IF_SAME(false), IF_ENEMY(false), IF_RANDOM(false),
  GO(false), UNKNOWN(false); // Default for unrecognized commands

  private static final Map<String, DarwinCommandType> commandMap = new LinkedHashMap<>();
  private static final String COMMANDS_BUNDLE = "cellsociety.constants.DarwinCommands";

  private final boolean isAction;

  DarwinCommandType(boolean isAction) {
    this.isAction = isAction;
  }

  public boolean isAction() {
    return isAction;
  }

  static {
    loadCommandMappings();
  }

  private static void loadCommandMappings() {
    try {
      ResourceBundle bundle = ResourceBundle.getBundle(COMMANDS_BUNDLE, Locale.getDefault());

      for (DarwinCommandType type : values()) {
        if (bundle.containsKey(type.name())) {
          String mapping = bundle.getString(type.name());
          for (String alias : mapping.split("\\|")) {
            String normalizedAlias = alias.trim().toLowerCase();
            commandMap.put(normalizedAlias, type);
          }
        }
      }
    } catch (Exception e) {
      throw new SimulationException("InvalidDarwinCommandsFile", e);
    }
  }

  public static DarwinCommandType fromString(String action) {
    action = action.trim().toLowerCase();

    // First, check for exact match
    if (commandMap.containsKey(action)) {
      return commandMap.get(action);
    }

    // Second, check for pattern-based matches
    for (Map.Entry<String, DarwinCommandType> entry : commandMap.entrySet()) {
      String key = entry.getKey();
      if (Pattern.matches(key.replace("?", "\\?"), action)) { // Treat "?" as literal
        return entry.getValue();
      }
    }

    return UNKNOWN;
  }
}
