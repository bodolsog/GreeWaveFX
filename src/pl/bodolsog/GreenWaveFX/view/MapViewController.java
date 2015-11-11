package pl.bodolsog.GreenWaveFX.view;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import pl.bodolsog.GreenWaveFX.MainApp;
import pl.bodolsog.GreenWaveFX.model.Markers;
import pl.bodolsog.GreenWaveFX.tools.PropertiesManager;

public class MapViewController
{
    // Reference to main app.
    private MainApp mainApp;

    @FXML
    private WebView webView;

    @FXML
    private void initialize(){
        PropertiesManager properties = new PropertiesManager();
        WebEngine engine = webView.getEngine();
        engine.load(getClass().getResource("googlemap.html").toString());

        JSObject window = (JSObject) engine.executeScript("window");
        window.setMember("properties", new PropertiesManager());
        window.setMember("markers", new Markers());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        //personTable.setItems(mainApp.getPersonData());
    }
}

