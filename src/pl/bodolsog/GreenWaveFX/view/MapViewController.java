package pl.bodolsog.GreenWaveFX.view;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import pl.bodolsog.GreenWaveFX.PropertiesManager;

public class MapViewController
{
    @FXML
    private WebView webView;

    @FXML
    private void initialize(){
        PropertiesManager properties = new PropertiesManager();
        WebEngine engine = webView.getEngine();
        engine.load(getClass().getResource("googlemap.html").toString());

        JSObject window = (JSObject) engine.executeScript("window");
        window.setMember("properties", new PropertiesManager());
    }
}

