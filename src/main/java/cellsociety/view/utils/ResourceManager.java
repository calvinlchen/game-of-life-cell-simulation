package cellsociety.view.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Utility class for dynamically analyzing and providing available resources in the application.
 *
 * <p>This class scans the resource folders to detect available language files and stylesheets,
 * providing a list of options that can be used within the application.
 *
 * @author Calvin Chen
 * @author Jessica Chen
 * @author ChatGPT - helped with some of the JavaDocs
 */
public class ResourceManager {

  /**
   * The base package paths for main and error messages.
   */
  public static final String MAIN_RESOURCE_PACKAGE = "cellsociety.resourceproperty.";
  public static final String MAIN_RESOURCE_FOLDER = MAIN_RESOURCE_PACKAGE.replace(".", "/");

  public static final String ERROR_RESOURCE_PACKAGE =
      "cellsociety.resourceproperty.Errors";

  /**
   * The base package path for css stylesheets
   */
  public static final String DEFAULT_STYLESHEET_FOLDER = "cellsociety/stylesheets/";

  private static String currentLanguage = "English";  // default
  private static ResourceBundle currentMainBundle = getMainBundleFromLanguage(currentLanguage);
  private static ResourceBundle currentErrorBundle = getErrorBundleFromLanguage(currentLanguage);

  /**
   *
   * @param language Name of language in String form, as it appears in the resource files
   */
  public static void setLanguage(String language) {
    currentLanguage = language;
    currentMainBundle = getMainBundleFromLanguage(language);
    currentErrorBundle = getErrorBundleFromLanguage(language);
  }

  /**
   * Return the resource bundle for main UI elements
   * @return standard program-text ResourceBundle for current program language
   */
  public static ResourceBundle getCurrentMainBundle() {
    return currentMainBundle;
  }

  /**
   * Return the resource bundle for error elements
   * @return error-message ResourceBundle for current program language
   */
  public static ResourceBundle getCurrentErrorBundle() {
    return currentErrorBundle;
  }

  private static ResourceBundle getMainBundleFromLanguage(String language) {
    ResourceBundle resources;
    try {
      resources = ResourceBundle.getBundle(MAIN_RESOURCE_PACKAGE + language);
    } catch (Exception e) {
      // if an error occurs, such as no available resource for the given language,
      // then default to English
      resources = ResourceBundle.getBundle(MAIN_RESOURCE_PACKAGE + "English");
    }
    return resources;
  }

  /**
   * Retrieves the error-message resource bundle for the specified language.
   *
   * <p>If the specified language bundle is unavailable, it defaults to English.
   *
   * @param language - the name of the language for error message localization
   * @return the resource bundle containing error messages
   */
  private static ResourceBundle getErrorBundleFromLanguage(String language) {
    ResourceBundle resources;
    try {
      resources = ResourceBundle.getBundle(ERROR_RESOURCE_PACKAGE + language);
    } catch (Exception e) {
      // if an error occurs, such as no available resource for the given language,
      // then default to English
      resources = ResourceBundle.getBundle(ERROR_RESOURCE_PACKAGE + "English");
    }
    return resources;
  }

  /**
   * Dynamically scans the resource folder to find available language files (.properties) and
   * extracts the names of those languages.
   *
   * @return List of available languages
   */
  public static List<String> getAvailableLanguages() {
    List<String> languages = new ArrayList<>();
    try {
      // Some of the following code was provided by ChatGPT.
      URL resourceUrl = ResourceManager.class.getClassLoader().getResource(MAIN_RESOURCE_FOLDER);

      if (resourceUrl != null) {
        Path resourcePath = Paths.get(resourceUrl.toURI());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(resourcePath, "*.properties")) {
          for (Path path : stream) {
            String fileName = path.getFileName().toString();
            if (fileName.endsWith(".properties") && !fileName.startsWith("Errors")) {
              languages.add(fileName.replace(".properties", "")); // Extract language name
            }
          }
        }
      }
    } catch (IOException | URISyntaxException e) {
      // e.printStackTrace();
      System.err.println("Error loading language files.");
    }
    // If no languages are found, default to only English.
    if (languages.isEmpty()) {
      languages.add("English");
    }
    return languages;
  }

  /**
   * Dynamically scans the resource folder to find available style files (.css) and extracts their
   * filenames.
   *
   * @return List of available stylesheets
   */
  public static List<String> getAvailableStylesheets() {
    List<String> stylesheets = new ArrayList<>();
    try {
      URL resourceUrl = ResourceManager.class.getClassLoader().getResource(MAIN_RESOURCE_FOLDER);

      if (resourceUrl != null) {
        Path resourcePath = Paths.get(resourceUrl.toURI());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(resourcePath, "*.css")) {
          for (Path path : stream) {
            String fileName = path.getFileName().toString();
            if (fileName.endsWith(".css")) {
              // Extract the stylesheet name by removing the ".css" extension
              stylesheets.add(fileName.replace(".css", ""));
            }
          }
        }
      }
    } catch (IOException | URISyntaxException e) {
      // e.printStackTrace();
      System.err.println("Error loading stylesheet files.");
    }

    return stylesheets;
  }
}
