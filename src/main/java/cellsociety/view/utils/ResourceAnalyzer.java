package cellsociety.view.utils;

import cellsociety.Main;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for dynamically analyzing available resources in the application.
 *
 * <p>This class scans the resource folders to detect available language files and stylesheets,
 * providing a list of options that can be used within the application.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class ResourceAnalyzer {

  /**
   * Dynamically scans the resource folder to find available language files (.properties) and
   * extracts the names of those languages.
   *
   * @return List of available languages
   */
  public static List<String> getAvailableLanguages() {
    List<String> languages = new ArrayList<>();
    try {
      // Get the resource folder path from Main constants
      String resourceFolder = Main.DEFAULT_RESOURCE_FOLDER;

      // Some of the following code was provided by ChatGPT.
      URL resourceUrl = ResourceAnalyzer.class.getClassLoader().getResource(resourceFolder);

      if (resourceUrl != null) {
        Path resourcePath = Paths.get(resourceUrl.toURI());
        DirectoryStream<Path> stream = Files.newDirectoryStream(resourcePath, "*.properties");

        for (Path path : stream) {
          String fileName = path.getFileName().toString();
          if (fileName.endsWith(".properties") && !fileName.startsWith("Errors")) {
            languages.add(fileName.replace(".properties", "")); // Extract language name
          }
        }
      }
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
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
      // Get the resource folder path from Main constants
      String resourceFolder = Main.DEFAULT_STYLESHEET_FOLDER;

      URL resourceUrl = ResourceAnalyzer.class.getClassLoader().getResource(resourceFolder);

      if (resourceUrl != null) {
        Path resourcePath = Paths.get(resourceUrl.toURI());
        DirectoryStream<Path> stream = Files.newDirectoryStream(resourcePath, "*.css");

        for (Path path : stream) {
          String fileName = path.getFileName().toString();
          if (fileName.endsWith(".css")) {
            stylesheets.add(fileName.replace(".css", "")); // Extract language name
          }
        }
      }
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
      System.err.println("Error loading stylesheet files.");
    }

    return stylesheets;
  }
}
