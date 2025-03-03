/**
 * The {@code cellsociety.model.simulation.parameters} package is responsible for managing
 * simulation-specific parameters, ensuring **flexibility, type safety, and scalability**.
 *
 * <p>This package provides:
 * <ul>
 *   <li>A **base class** {@link cellsociety.model.simulation.parameters.Parameters} that stores
 *   simulation parameters in a **String → Double** format.</li>
 *   <li>A **generic subclass** {@link cellsociety.model.simulation.parameters.GenericParameters}
 *   that extends support to non-double parameters when required (e.g., lists in Game of Life).</li>
 *   <li>**Encapsulation of default values** for different simulation types, ensuring automatic
 *   initialization without requiring separate subclasses for each simulation.</li>
 * </ul>
 *
 * <h2>📌 Why This Package?</h2>
 *
 * <p>Simulations require different rules and settings. Instead of hardcoding individual
 * configurations, this package provides **centralized, flexible storage** of simulation
 * parameters.</p>
 *
 * <h2>📌 Key Features</h2>
 * <ul>
 *   <li>🔹 **Automatic default parameter initialization** based on simulation type.</li>
 *   <li>🔹 **Efficient key-value storage**, reducing the need for multiple subclasses.</li>
 *   <li>🔹 **Logging and error handling** for invalid or missing parameters.</li>
 *   <li>🔹 **Reflection-friendly design**, making it easy to integrate with dynamic rule creation.
 *   </li>
 * </ul>
 *
 * <h2>🚀 How It Works</h2>
 *
 * <p>Parameters are stored in two ways:</p>
 * <ul>
 *   <li>**Double-based parameters** – Stored in {@code Parameters}, ensuring type safety.</li>
 *   <li>**Object-based parameters** – Stored in {@code GenericParameters}, with logging warnings
 *   when non-standard parameters are used.</li>
 * </ul>
 *
 * <h2>🔧 Example Usage</h2>
 * <pre>
 * GenericParameters params = new GenericParameters(SimType.WaTor);
 * double reproductionTime = params.getParameter("fishReproductionTime");
 *
 * params.setAdditionalParameter("customSetting", List.of(1, 2, 3));
 * List&lt;Integer> setting = params.getAdditionalParameter("customSetting", List.class);
 * </pre>
 *
 * <h2>📌 Package Classes</h2>
 * <ul>
 *   <li>{@link cellsociety.model.simulation.parameters.Parameters} – Base class for managing
 *   **double-based** simulation parameters.</li>
 *   <li>{@link cellsociety.model.simulation.parameters.GenericParameters} – Extends support to
 *   **non-double** parameters for simulations requiring complex data types.</li>
 * </ul>
 *
 * <h2>📌 Design Considerations</h2>
 *
 * <p>This package initially contained **one subclass per simulation**, but was refactored for:</p>
 * <ul>
 *   <li>✅ **Scalability** – New simulations no longer require separate subclasses.</li>
 *   <li>✅ **Simplified Maintenance** – Avoids redundant classes with minimal duplication.</li>
 *   <li>✅ **Better Type Safety** – Only exceptional cases (like Game of Life) allow non-double
 *   parameters.</li>
 * </ul>
 *
 * <p>By structuring parameter handling in a **flexible yet controlled** manner, this package
 * ensures **safe, scalable, and maintainable** simulation management.</p>
 *
 * @author Jessica Chen
 * @author ChatGPT helped with JavaDocs
 */
package cellsociety.model.simulation.parameters;
