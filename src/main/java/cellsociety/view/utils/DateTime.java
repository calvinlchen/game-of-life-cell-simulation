package cellsociety.view.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for retrieving the current date and time in a formatted string.
 *
 * <p>This class provides a method to get the local date and time formatted as
 * "yyyy-MM-dd_HH-mm-ss" for consistent timestamping.
 *
 * @author Calvin Chen
 * @author Jessica Chen and ChatGPT, helped with some of the JavaDocs
 */
public class DateTime {

  /**
   * Retrieves the current local date and time as a formatted string.
   *
   * @return a string representing the current date and time in the format "yyyy-MM-dd_HH-mm-ss"
   */
  public static String getLocalDateTime() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
  }
}
