package pl.bodolsog.GreenWaveFX.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import pl.bodolsog.GreenWaveFX.MainApp;
import pl.bodolsog.GreenWaveFX.model.Marker;
import pl.bodolsog.GreenWaveFX.model.Markers;
import pl.bodolsog.GreenWaveFX.tools.PropertiesManager;

import java.util.*;

public class MapViewController {

    // Reference to main app.
    private MainApp mainApp;

    public Markers markers;

    @FXML
    private WebView webView;
    private WebEngine webEngine;

    /**
     * Initializes view.
     */
    @FXML
    private void initialize(){
        // Initializes web engine and Google Maps into.
        webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/googlemap.html").toString());

        // Register classes for use from javascript.
        JSObject window = (JSObject) webEngine.executeScript("window");
        window.setMember("properties", new PropertiesManager());
        window.setMember("controller", this);
        window.setMember("backThread", new BackThread(webEngine));
    }


    /**
     * Set reference to Markers (called from MainApp).
     * @param markers
     */
    public void passMarkersReference(Markers markers){
        this.markers = markers;
    }

    /**
     * Add new Marker to list.
     * @param jsMarker GoogleMaps marker object
     */
    public void addMarker(JSObject jsMarker){
        markers.addMarker(jsMarker);
    }


    /**
     * Execute script from js which deletes Marker.
     * @param id Marker's id
     */
//    public void deleteMarker(int id){
//        webEngine.executeScript("deleteMarker('"+id+"')");
//    }
//
//    public void setMarkerFocus(int oldMarkerId, int newMarkerId){
//        webEngine.executeScript("setMarkerFocus('"+oldMarkerId+"', '"+newMarkerId+"')");
//    }
//
//    public void setClickedFocus(int id){
//        //mainApp.getMarkersViewController().setClickedFocus(id);
//    }
//
//    public void connectMarkers(String mode, int markerOne, int markerTwo){
//        //mainApp.getMarkersViewController().connectMarkers(mode, markerOne, markerTwo);
//    }



    /**
     * Send works to back thread.
     */
    public class BackThread {

        private WebEngine webEngine;
        private Map<Integer,ObservableList<String>> streetsList = new HashMap<>();
        private Map<Integer,ObservableList<CrossingStreetsNamesThread>> threadsList = new HashMap<>();

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
        public void getStreetsNames(int markerId, double lat, double lng){
            double[][] latLngSquare = getLatLngSquare(lat, lng);
            for(double[] line: latLngSquare )
                startThread(markerId, line[0], line[1]);
        }


        /**
         * Creates new task which calls reverse-geocoding js-script in Maps for this lat-lng. Reference to thread set
         * into map, reference is used when user drags Marker: if threads are not finished yet, this woul'd be canceled.
         * @param markerId    Marker's id
         * @param lat   latitude
         * @param lng   longitude
         */
        public void startThread(int markerId, double lat, double lng){
            // Create variable thread contained new Thread.
            CrossingStreetsNamesThread thread = new CrossingStreetsNamesThread(webEngine, markerId, lat, lng);
            // Set new array in map identified with Marker's id if not exist yet.
            if(!threadsList.containsKey(markerId)){
                threadsList.put(markerId, FXCollections.observableArrayList());
            }
            // Add thread to list.
            threadsList.get(markerId).add(thread);
            // Set into queue.
            Platform.runLater(thread);
        }

        public void cancelThreads(int id){
            // Remove saved streets names.
            streetsList.remove(id);
            // Avoid starting threads in queue.
            for (CrossingStreetsNamesThread t : threadsList.get(id))
                t.cancel();
            // Remove threads list from map.
            threadsList.remove(id);
        }

        /**
         * Add street name to list in map witch Marker's id as key. If this name was setted - sets empty string. This is
         * called when reverse-geocoding script returns status OK.
         * @param markerId     Marker's id
         * @param street street name
         */
        public void putToMap(int markerId, String street){
            // If map haven't this id, set new ArrayList identified by id to map and add listener to it.
            if(!streetsList.containsKey(markerId)){
                streetsList.put(markerId, FXCollections.observableArrayList());
                // Listener will set cross name when all 4 queries is completed.
                streetsList.get(markerId).addListener((ListChangeListener<String>) change -> { while(change.next()){
                    if(change.getList().size() >= 4){
                        // Set cross name to TitledPane
                        String crossName = setCrossName(markerId, change.getList());
                        // Set cross name to marker.
                        webEngine.executeScript("setCrossName('"+markerId+"', '"+crossName+"')");
                    }
                }});
            }
            // When list contains this street name - set string to empty.
            if (streetsList.get(markerId).contains(street))
                street = "";
            // Adds street to list.
            streetsList.get(markerId).add(street);
        }

        public String setCrossName(int markerId, ObservableList<? extends String> streetsNames){
            // Init string.
            String crossName = "";
            // Build name from every not empty name.
            for(String street : streetsNames){
                // Append street when is not empty string.
                if(!street.equals(""))
                    // If that isn't first street - add slash before street name.
                    if(!crossName.equals(""))
                        crossName += "/"+street;
                    else
                        crossName += street;
            }
            // Set name to Marker and return.
            //mainApp.getMarkers().get(markerId).setName(crossName);
            return crossName;
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
                    { lat+r, lng }, // North
                    { lat, lng-r }, // West
                    { lat-r, lng }, // South
                    { lat, lng+r }  // East
            };
            return latLngSquare;
        }
    }

    /**
     * Class for new thread.
     */
    public class CrossingStreetsNamesThread implements Runnable{
        private WebEngine webEngine;
        private int markerId;
        private double lat;
        private double lng;
        private boolean blinker = true;

        /**
         * Constructor.
         * @param webEngine web engine
         * @param markerId  Marker's id
         * @param lat       latitude
         * @param lng       longitude
         */
        public CrossingStreetsNamesThread(WebEngine webEngine, int markerId, double lat, double lng) {
            this.webEngine = webEngine;
            this.markerId = markerId;
            this.lat = lat;
            this.lng = lng;
        }

        public void cancel() {
            blinker = false;
        }

        /**
         * Execute js script as another thread (status OVER_QUERY_LIMIT and 1 second wait will not freeze and not broke
         * functionality of MainApp).
         */
        @Override
        public void run(){
            if (blinker) {
                webEngine.executeScript("getCrossName('" + markerId + "', " + lat + ", " + lng + ")");
            }
        }
    }
}