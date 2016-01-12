package pl.bodolsog.GreenWaveFX.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import netscape.javascript.JSObject;

import java.util.ArrayList;

public class Markers {

    public static final String[] DIRECTIONS = new String[] {"north", "east", "south", "west", "northeast", "southeast",
            "southwest", "northwest"};

    private int nextId = 0;

    // Map of markers <id, marker>.
    private ObservableMap<Integer,Marker> markers = FXCollections.observableHashMap();

    private int active;

    /**
     * Add marker to markers list.
     * @param jsMarker GoogleMaps marker object
     */
    public void addMarker(JSObject jsMarker){
        markers.put(nextId, new Marker(nextId, jsMarker));
        setActiveMarkerId(nextId);
        nextId++;
    }

    /**
     * Return marker asociated with id.
     * @param id
     * @return Marker
     */
    public Marker getMarker(int id){
        return markers.get(id);
    }

    /**
     * Deletes marker from map.
     * @param id
     */
    public void deleteMarker(int id){
        markers.remove(id);
    }

    /**
     * Return whole map.
     * @return markers
     */
    public ObservableMap<Integer,Marker> getAllMarkers(){
        return markers;
    }

    /**
     * Return size of Markers.
     * @return size
     */
    public int size(){
        return markers.size();
    }

    /**
     * Get id of active Marker.
     * @return active
     */
    public int getActiveMarkerId(){
        return active;
    }

    /**
     * Set as active Marker's id.
     * @param id
     */
    public void setActiveMarkerId(int id){
        active = id;
    }

    /**
     * Return Marker that is now active.
     * @return Marker
     */
    public Marker getActiveMarker(){
        return markers.get(getActiveMarkerId());
    }

    public void setCrossDirections(int id, ArrayList<String> directions) {
        getMarker(id).setCrossDirections(directions);
    }

    public boolean isStartPoint(int id) {
        return markers.get(id).isStartPoint();
    }

}
