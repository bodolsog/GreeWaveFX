package pl.bodolsog.GreenWaveFX.view;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MapViewController
{
    @FXML
    private WebView webView;

    @FXML
    private void initialize()
    {
        WebEngine engine = webView.getEngine();
        engine.load(getClass().getResource("googlemap.html").toString());
        //engine.load("http://google.pl");

    }
}
