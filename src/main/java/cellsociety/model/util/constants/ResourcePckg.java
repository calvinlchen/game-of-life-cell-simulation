package cellsociety.model.util.constants;

import java.util.ResourceBundle;

public class ResourcePckg {
  public static final String ERROR_SIMULATION_RESOURCE_PACKAGE = "cellsociety.resourceproperty.ErrorsSimulation";

  public static ResourceBundle getErrorSimulationResourceBundle(String language) {
    ResourceBundle resources;
    try {
      resources = ResourceBundle.getBundle(ERROR_SIMULATION_RESOURCE_PACKAGE + language);
    }
    catch (Exception e) {
      // if an error occurs, such as no available resource for the given language, then default to English
      resources = ResourceBundle.getBundle(ERROR_SIMULATION_RESOURCE_PACKAGE + "English");
    }
    return resources;
  }
}
