package cellsociety;

import static cellsociety.view.components.UserView.showMessage;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cellsociety.view.components.UserView;


/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main extends Application {

    // internal configuration file
    public static final String INTERNAL_CONFIGURATION = "cellsociety.Version";
    // default to start in the data folder to make it easy on the user to find
    public static final String DATA_FILE_FOLDER = System.getProperty("user.dir") + "/data";

    // width and height of application window
    public static final int SCENE_WIDTH = 1200;
    public static final int SCENE_HEIGHT = 800;

    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start (Stage primaryStage) {
        UserView view = new UserView(SCENE_WIDTH, SCENE_HEIGHT, primaryStage);

        view.resetView();

        showMessage(AlertType.INFORMATION, String.format("Version: %s", getVersion()));
    }

    /**
     * Returns number of blocks needed to cover the width and height given in the data file.
     */
    public int calculateNumBlocks(File xmlFile) {
        try {
            Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
            Element root = xmlDocument.getDocumentElement();
            int width = Integer.parseInt(getTextValue(root, "width"));
            int height = Integer.parseInt(getTextValue(root, "height"));
            return width * height;
        }
        catch (NumberFormatException e) {
            showMessage(AlertType.ERROR, "Invalid number given in data");
            return 0;
        }
        catch (ParserConfigurationException e) {
            showMessage(AlertType.ERROR, "Invalid XML Configuration");
            return 0;
        }
        catch (SAXException | IOException e) {
            showMessage(AlertType.ERROR, "Invalid XML Data");
            return 0;
        }
    }

    /**
     * A method to test getting internal resources.
     */
    public double getVersion () {
        ResourceBundle resources = ResourceBundle.getBundle(INTERNAL_CONFIGURATION);
        return Double.parseDouble(resources.getString("Version"));
    }

    // get value of Element's text
    private String getTextValue (Element e, String tagName) {
        NodeList nodeList = e.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        else {
            // FIXME: empty string or exception? In some cases it may be an error to not find any text
            return "";
        }
    }

    /**
     * Start the program, give complete control to JavaFX.
     *
     * Default version of main() is actually included within JavaFX, so this is not technically necessary!
     */
    public static void main (String[] args) {
        launch(args);
    }
}
