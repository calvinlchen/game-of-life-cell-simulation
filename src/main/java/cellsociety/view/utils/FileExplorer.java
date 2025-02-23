package cellsociety.view.utils;

import cellsociety.Main;
import java.io.File;
import javafx.stage.FileChooser;

public class FileExplorer {

  // kind of data files to look for
  public static final String DATA_FILE_EXTENSION = "*.xml";
  // NOTE: make ONE chooser since generally accepted behavior is that it remembers where user left it last
  private final static FileChooser FILE_LOAD_CHOOSER = makeChooser("Open Data File");
  private static final FileChooser FILE_SAVE_CHOOSER = makeChooser("Save Data File");

  // set some sensible defaults when the FileChooser is created
  private static FileChooser makeChooser(String title) {
    FileChooser result = new FileChooser();
    result.setTitle(title);
    // pick a reasonable place to start searching for files
    result.setInitialDirectory(new File(Main.DEFAULT_DATA_FOLDER));
    result.getExtensionFilters()
        .setAll(new FileChooser.ExtensionFilter("Data Files", DATA_FILE_EXTENSION));
    return result;
  }

  /**
   * Get the FileChooser object for opening File Explorer to LOAD a file
   *
   * @return FileChooser object which allows a file to be selected
   */
  public static FileChooser getFileLoadChooser() {
    return FILE_LOAD_CHOOSER;
  }


  /**
   * Get the FileChooser object for opening File Explorer to SAVE a file
   *
   * @return FileChooser object which allows a save-file location to be selected
   */
  public static FileChooser getSaveFileChooser() {
    return FILE_SAVE_CHOOSER;
  }
}
