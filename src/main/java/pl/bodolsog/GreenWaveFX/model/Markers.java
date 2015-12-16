package pl.bodolsog.GreenWaveFX.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import netscape.javascript.JSObject;

public class Markers {

    private int nextId = 0;

    // Map of markers <id, marker>.
    private ObservableMap<Integer,Marker> markers = FXCollections.observableHashMap();

    /**
     * Add marker to markers list.
     * @param jsMarker GoogleMaps marker object
     */
    public void addMarker(JSObject jsMarker){
        while(markers.containsKey(nextId)){
            nextId++;
        }
        markers.put(nextId, new Marker(nextId, jsMarker));
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
}
