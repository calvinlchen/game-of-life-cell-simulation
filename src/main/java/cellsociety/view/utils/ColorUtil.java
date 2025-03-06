package cellsociety.view.utils;

import javafx.scene.paint.Color;

public class ColorUtil {
  public static String colorToHexString(Color color) {
    return String.format("#%02X%02X%02X",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
  }

  public static Color hexStringToColor(String hexString) {
    return Color.web(hexString);
  }
}
