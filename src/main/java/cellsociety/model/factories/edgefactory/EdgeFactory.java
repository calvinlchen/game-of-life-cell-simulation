package cellsociety.model.factories.edgefactory;

import cellsociety.model.factories.edgefactory.handler.EdgeHandler;
import cellsociety.model.factories.edgefactory.handler.MirrorEdgeHandler;
import cellsociety.model.factories.edgefactory.handler.NoneEdgeHandler;
import cellsociety.model.factories.edgefactory.handler.ToroidalEdgeHandler;
import cellsociety.model.util.constants.GridTypes.EdgeType;
import java.util.HashMap;
import java.util.Map;

/**
 * The EdgeFactory class serves as a factory for managing and providing EdgeHandler instances based
 * on the specific type of edge behavior required. It maintains a mapping between EdgeType enums and
 * their corresponding EdgeHandler implementations.
 * <p>
 * This class supports different edge-handling strategies, such as none, mirror, and toroidal,
 * allowing grid-based simulations or similar systems to handle edge cases consistently and
 * flexibly.
 *
 * @author Jessica Chen
 * @author ChatGPT helped with the javadoc
 */
public class EdgeFactory {

  private static final Map<EdgeType, EdgeHandler> handlerMap = new HashMap<>();

  static {
    handlerMap.put(EdgeType.NONE, new NoneEdgeHandler());
    handlerMap.put(EdgeType.MIRROR, new MirrorEdgeHandler());
    handlerMap.put(EdgeType.TOROIDAL, new ToroidalEdgeHandler());
  }

  /**
   * Retrieves the EdgeHandler instance corresponding to the specified EdgeType.
   *
   * @param edgeType - the type of edge behavior for which the associated handler is to be
   *                 retrieved
   * @return the EdgeHandler instance mapped to the given EdgeType, or null if no mapping exists
   */
  public static EdgeHandler getHandler(EdgeType edgeType) {
    return handlerMap.get(edgeType);
  }
}
