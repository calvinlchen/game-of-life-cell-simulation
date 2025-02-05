package cellsociety.view.components;

import cellsociety.Main;
import java.io.File;
import javafx.stage.FileChooser;

public class FileExplorer {
  // kind of data files to look for
  public static final String DATA_FILE_EXTENSION = "*.xml";
  // NOTE: make ONE chooser since generally accepted behavior is that it remembers where user left it last
  private final static FileChooser FILE_CHOOSER = makeChooser(DATA_FILE_EXTENSION);

  // set some sensible defaults when the FileChooser is created
  public static FileChooser makeChooser (String extensionAccepted) {
    FileChooser result = new FileChooser();
    result.setTitle("Open Data File");
    // pick a reasonable place to start searching for files
    result.setInitialDirectory(new File(Main.DATA_FILE_FOLDER));
    result.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Data Files", extensionAccepted));
    return result;
  }

  /**
   * Get the FileChooser object for opening File Explorer to pick a file
   * @return FileChooser object which allows a file to be selected
   */
  public static FileChooser getFileChooser() {
    return FILE_CHOOSER;
  }
}
