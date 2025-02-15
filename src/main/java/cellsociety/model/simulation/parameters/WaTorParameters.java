package cellsociety.model.simulation.parameters;

import java.util.HashMap;
import java.util.Map;

public class WaTorParameters extends Parameters {
  public WaTorParameters() {
    super();

    Map<String, Double> parameters = new HashMap<>();
    parameters.put("fishReproductionTime", 3.);
    parameters.put("sharkEnergyGain", 2.);
    parameters.put("sharkReproductionTime", 3.);
    parameters.put("sharkInitialEnergy", 5.);

    setParameters(parameters);
  }
}
