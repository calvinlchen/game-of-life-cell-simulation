package cellsociety.model.simulation.grid;

import cellsociety.model.simulation.grid.edgehandler.EdgeHandler;
import cellsociety.model.simulation.grid.edgehandler.MirrorEdgeHandler;
import cellsociety.model.simulation.grid.edgehandler.NoneEdgeHandler;
import cellsociety.model.simulation.grid.edgehandler.ToroidalEdgeHandler;
import cellsociety.model.util.constants.GridTypes.EdgeType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code EdgeFactory} class serves as a centralized factory for providing instances of
 * {@link EdgeHandler} based on the specified {@link EdgeType}.
 *
 * <p><b>Design Pattern:</b> Implements the <b>Factory Pattern</b> to decouple
 * the creation logic of different edge-handling strategies.</p>
 *
 * <p><b>Supported Edge Types:</b></p>
 * <ul>
 *   <li>{@link NoneEdgeHandler} - Ignores out-of-bounds neighbors.</li>
 *   <li>{@link MirrorEdgeHandler} - Reflects grid edges.</li>
 *   <li>{@link ToroidalEdgeHandler} - Wraps edges around like a torus.</li>
 * </ul>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * Optional<EdgeHandler> handler = EdgeFactory.getHandler(EdgeType.TOROIDAL);
 * handler.ifPresent(h -> System.out.println("Handler retrieved: " + h.getClass().getSimpleName()));
 * </pre>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
class EdgeFactory {
  private static final Logger logger = LogManager.getLogger(EdgeFactory.class);
  private static final Map<EdgeType, EdgeHandler> handlerMap = new HashMap<>();

  static {
    handlerMap.put(EdgeType.NONE, new NoneEdgeHandler());
    handlerMap.put(EdgeType.MIRROR, new MirrorEdgeHandler());
    handlerMap.put(EdgeType.TOROIDAL, new ToroidalEdgeHandler());
  }

  /**
   * Retrieves the {@link EdgeHandler} instance associated with the given {@link EdgeType}.
   *
   * <p>If an unsupported {@code EdgeType} is requested, a warning is logged, and an empty
   * {@code Optional} is returned instead of {@code null}.</p>
   *
   * @param edgeType The edge handling strategy to retrieve.
   * @return An {@code Optional<EdgeHandler>} containing the corresponding handler if available,
   *         or {@code Optional.empty()} if the type is not recognized.
   */
  static Optional<EdgeHandler> getHandler(EdgeType edgeType) {
    return Optional.ofNullable(handlerMap.get(edgeType))
        .or(() -> {
          logger.warn("No EdgeHandler found for EdgeType: {}", edgeType);
          return Optional.empty();
        });
  }
}
