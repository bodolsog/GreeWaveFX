package pl.bodolsog.GreenWaveFX.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import netscape.javascript.JSObject;

public class Markers {

    private int nextId;

    // Map of markers <id, marker>.
    private ObservableMap<Integer,Marker> markers = FXCollections.observableHashMap();

    private int active;

    /**
     * Add marker to markers list.
     * @param jsMarker GoogleMaps marker object
     */
    public void addMarker(JSObject jsMarker){
        while(markers.containsKey(nextId)){
            nextId++;
        }
        markers.put(nextId, new Marker(nextId, jsMarker));
        setMarkerActiveId(nextId);
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
     * Set as active Marker's id.
     * @param id
     */
    public void setMarkerActiveId(int id){
        active = id;
    }

    /**
     * Get id of active Marker.
     * @return
     */
    public int getMarkerActiveId(){
        return active;
    }

}
