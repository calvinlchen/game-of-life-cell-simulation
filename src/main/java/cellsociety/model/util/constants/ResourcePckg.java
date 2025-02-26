package cellsociety.model.util.constants;

import java.util.ResourceBundle;

/**
 * Utility class for handling resource bundles related to simulation errors.
 *
 * <p>This class provides methods to retrieve localized error messages for the simulation
 * based on the specified language.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class ResourcePckg {

  /**
   * The base package path for simulation error messages.
   */
  public static final String ERROR_SIMULATION_RESOURCE_PACKAGE = "cellsociety.resourceproperty.Errors";

  /**
   * Retrieves the error message resource bundle for the specified language.
   *
   * <p>If the specified language bundle is unavailable, it defaults to English.
   *
   * @param language - the name of the language for error message localization
   * @return the resource bundle containing error messages
   */
  public static ResourceBundle getErrorSimulationResourceBundle(String language) {
    ResourceBundle resources;
    try {
      resources = ResourceBundle.getBundle(ERROR_SIMULATION_RESOURCE_PACKAGE + language);
    } catch (Exception e) {
      // if an error occurs, such as no available resource for the given language, then default to English
      resources = ResourceBundle.getBundle(ERROR_SIMULATION_RESOURCE_PACKAGE + "English");
    }
    return resources;
  }
}
