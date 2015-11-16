package pl.bodolsog.GreenWaveFX.mapview;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import pl.bodolsog.GreenWaveFX.MainApp;
import pl.bodolsog.GreenWaveFX.markersview.MarkersViewController;
import pl.bodolsog.GreenWaveFX.tools.PropertiesManager;

import java.util.*;

public class MapViewController {

    // Reference to main app.
    private MainApp mainApp;

    @FXML
    private WebView webView;

    private WebEngine webEngine;

    /**
     * Initializes controller.
     */
    @FXML
    private void initialize(){
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("googlemap.html").toString());

        // Register classes for JS window
        JSObject window = (JSObject) webEngine.executeScript("window");
        window.setMember("properties", new PropertiesManager());
        window.setMember("markerList", this);
        window.setMember("backThread", new BackThread(webEngine));
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void addMarker(String id, double lat, double lng){
        mainApp.getMarkersViewController().addMarker(id, lat, lng);
    }

    public void setMarkerCrossName(String id, String newName){
        //TODO connections between controllers (via mainApp) and set new name here
    }

    /**
     * Send works to back thread.
     */
    public class BackThread {

        public WebEngine webEngine;
        private Map<String,ObservableList<String>> ol = new HashMap<String,ObservableList<String>>();

        public BackThread(WebEngine webEngine){ this.webEngine = webEngine; }

        public void putToMap(String id, String street){
            if(!ol.containsKey(id)){
                ol.put(id, FXCollections.observableArrayList());
                ol.get(id).addListener((ListChangeListener<String>) change -> { while(change.next()){
                    if(change.getList().size() >= 4){
                        //TODO uprościć, nowy obiekt?
                        String crossName = mainApp.getMarkersViewController().setCrossName(id, change.getList());
                        webEngine.executeScript("setCrossName('"+id+"', '"+crossName+"')");
                    }
                }});
            }
            if (ol.get(id).contains(street))
                street = "";
            ol.get(id).add(street);
        }

        public void error(String e){
            System.out.println(e);
        }

        public double[][] getLatLngSquare(double lat, double lng){
            double mod = 0.00015;
            double[][] latLngSquare = {
                    { lat+mod, lng },
                    { lat, lng-mod },
                    { lat-mod, lng },
                    { lat, lng+mod }
            };
            return latLngSquare;
        }

        public void startThread(String markerId, double lat, double lng){
            Platform.runLater(new getStreetsNamesThread(webEngine, markerId, lat, lng));
        }

        public void getStreetsNames(String markerId, double lat, double lng){
            double[][] latLngSquare = getLatLngSquare(lat, lng);
            for(double[] line: latLngSquare )
                startThread(markerId, line[0], line[1]);
        }
    }


    public class getStreetsNamesThread implements Runnable{
        private WebEngine webEngine;
        private String markerId;
        private double lat;
        private double lng;

        public getStreetsNamesThread(WebEngine webEngine, String markerId, double lat, double lng) {
            this.webEngine = webEngine;
            this.markerId = markerId;
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        public void run(){
            webEngine.executeScript("getCrossName('" + markerId + "', " + lat + ", " + lng + ")");
        }
    }
}