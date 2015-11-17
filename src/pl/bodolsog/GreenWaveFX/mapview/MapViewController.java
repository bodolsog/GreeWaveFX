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
        // Initializes web engine and Google Maps into.
        webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("googlemap.html").toString());

        // Register classes for use from javascript.
        JSObject window = (JSObject) webEngine.executeScript("window");
        window.setMember("properties", new PropertiesManager());
        window.setMember("controller", this);
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


    /**
     * Bridge to MarkersViewController. Adds new Marker to list.
     * @param id    marker's id
     * @param lat   latitude
     * @param lng   longitude
     */
    public void addMarker(String id, double lat, double lng){
        mainApp.getMarkersViewController().addMarker(id, lat, lng);
    }

    /**
     * Call MarkersViewController's method for set new Marker's latLng.
     * @param id Marker's id
     * @param lat latitude
     * @param lng longitude
     */
    public void setMarkerLatLng(String id, double lat, double lng){
        mainApp.getMarkersViewController().setMarkerLatLng(id, lat, lng);
    }

    /**
     * Execute script from js which deletes Marker.
     * @param id Marker's id
     */
    public void deleteMarker(String id){
        webEngine.executeScript("deleteMarker('"+id+"')");
    }

    /**
     * Send works to back thread.
     */
    public class BackThread {

        public WebEngine webEngine;
        private Map<String,ObservableList<String>> ol = new HashMap<String,ObservableList<String>>();

        /**
         * Constructor.
         * @param webEngine
         */
        public BackThread(WebEngine webEngine){ this.webEngine = webEngine; }

        /**
         * Is called from javascript on Maps after set new Marker. Call latLngSquare for get array contains neighbours
         * points used to identify crossing streets. For every point call startThread which set geolocation task to
         * another thread.
         * @param markerId  Marker's id
         * @param lat       latitude
         * @param lng       longitude
         */
        public void getStreetsNames(String markerId, double lat, double lng){
            double[][] latLngSquare = getLatLngSquare(lat, lng);
            for(double[] line: latLngSquare )
                startThread(markerId, line[0], line[1]);
        }


        /**
         * Creates new task which calls reverse-geocoding js-script in Maps for this lat-lng.
         * @param markerId  Marker's id
         * @param lat       latitude
         * @param lng       longitude
         */
        public void startThread(String markerId, double lat, double lng){
            Platform.runLater(new getStreetsNamesThread(webEngine, markerId, lat, lng));
        }

        /**
         * Add street name to list in map witch Marker's id as key. If this name was setted - sets empty string. This is
         * called when reverse-geocoding script returns status OK.
         * @param id     Marker's id
         * @param street street name
         */
        //TODO przejrzeć
        public void putToMap(String id, String street){
            if(!ol.containsKey(id)){
                ol.put(id, FXCollections.observableArrayList());
                ol.get(id).addListener((ListChangeListener<String>) change -> { while(change.next()){
                    if(change.getList().size() >= 4){
                        String crossName = mainApp.getMarkersViewController().setCrossName(id, change.getList());
                        webEngine.executeScript("setCrossName('"+id+"', '"+crossName+"')");
                    }
                }});
            }
            if (ol.get(id).contains(street))
                street = "";
            ol.get(id).add(street);
        }

        /**
         * Create 2D list of doubles 4 pairs lat-lng for points neighbour to cross.
         * @param lat   latitude
         * @param lng   longitude
         * @return  2D array, 4 lat-lng pairs.
         */
        public double[][] getLatLngSquare(double lat, double lng){
            // Radius.
            double r = 0.00015;
            double[][] latLngSquare = {
                    { lat+r, lng }, // N
                    { lat, lng-r }, // E
                    { lat-r, lng }, // S
                    { lat, lng+r }  // W
            };
            return latLngSquare;
        }
    }

    /**
     * Class for new thread.
     */
    public class getStreetsNamesThread implements Runnable{
        private WebEngine webEngine;
        private String markerId;
        private double lat;
        private double lng;

        /**
         * Constructor.
         * @param webEngine web engine
         * @param markerId  Marker's id
         * @param lat       latitude
         * @param lng       longitude
         */
        public getStreetsNamesThread(WebEngine webEngine, String markerId, double lat, double lng) {
            this.webEngine = webEngine;
            this.markerId = markerId;
            this.lat = lat;
            this.lng = lng;
        }

        /**
         * Execute js script as another thread (status OVER_QUERY_LIMIT and 1 second wait will not freeze and not broke
         * functionality of MainApp).
         */
        @Override
        public void run(){
            webEngine.executeScript("getCrossName('" + markerId + "', " + lat + ", " + lng + ")");
        }
    }
}