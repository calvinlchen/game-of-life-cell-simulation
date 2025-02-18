package cellsociety.view.utils;

import cellsociety.Main;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceAnalyzer {
  /**
   * Dynamically scans the resource folder to find available language files (.properties) and extracts the names of those languages.
   * @return List of available languages
   */
  public static List<String> getAvailableLanguages() {
    List<String> languages = new ArrayList<>();
    try {
      // Get the resource folder path from Main constants
      String resourceFolder = Main.DEFAULT_RESOURCE_FOLDER;

      // Some of the following code was provided by ChatGPT.
      URL resourceURL = ResourceAnalyzer.class.getClassLoader().getResource(resourceFolder);

      if (resourceURL != null) {
        Path resourcePath = Paths.get(resourceURL.toURI());
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
}
