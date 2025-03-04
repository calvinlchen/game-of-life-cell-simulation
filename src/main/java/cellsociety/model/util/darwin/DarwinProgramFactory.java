package cellsociety.model.util.darwin;

import cellsociety.model.util.exceptions.SimulationException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DarwinProgramFactory {

  private static final Map<String, DarwinProgram> programCache = new ConcurrentHashMap<>();

  public static DarwinProgram getProgram(String species) {
    try {
      return programCache.computeIfAbsent(species, DarwinProgram::new);
    } catch (SimulationException e) {
      throw new SimulationException(e);
    }
  }
}
