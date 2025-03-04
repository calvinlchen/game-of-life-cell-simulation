package cellsociety.model.simulation.rules;

import cellsociety.model.simulation.rules.darwinhandler.DarwinCommandHandler;
import cellsociety.model.simulation.rules.darwinhandler.GoCommandHandler;
import cellsociety.model.simulation.rules.darwinhandler.IfEmptyCommandHandler;
import cellsociety.model.simulation.rules.darwinhandler.IfEnemyCommandHandler;
import cellsociety.model.simulation.rules.darwinhandler.IfRandomCommandHandler;
import cellsociety.model.simulation.rules.darwinhandler.IfSameCommandHandler;
import cellsociety.model.simulation.rules.darwinhandler.UnknownCommandHandler;
import cellsociety.model.util.darwin.DarwinCommand;
import cellsociety.model.util.darwin.DarwinCommandType;
import java.util.HashMap;
import java.util.Map;

public class DarwinCommandFactory {

  private static final Map<DarwinCommandType, DarwinCommandHandler> handlerMap = new HashMap<>();

  static {
    handlerMap.put(DarwinCommandType.UNKNOWN, new UnknownCommandHandler());
    handlerMap.put(DarwinCommandType.GO, new GoCommandHandler());
    handlerMap.put(DarwinCommandType.IF_RANDOM, new IfRandomCommandHandler());
    handlerMap.put(DarwinCommandType.IF_ENEMY, new IfEnemyCommandHandler());
    handlerMap.put(DarwinCommandType.IF_SAME, new IfSameCommandHandler());
    handlerMap.put(DarwinCommandType.IF_EMPTY, new IfEmptyCommandHandler());
  }

  // factory makes maps by the type only
  public static DarwinCommandHandler getHandler(DarwinCommandType commandType) {
    return handlerMap.get(commandType);
  }

}
