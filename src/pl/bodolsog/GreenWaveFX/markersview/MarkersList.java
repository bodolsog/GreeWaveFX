package pl.bodolsog.GreenWaveFX.markersview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;



/**
 * Manages global observable list of Markers.
 */
public class MarkersList {

    /**
     * Global variable.
     */
    public static ObservableMap<String,Marker> markers = FXCollections.observableHashMap();

    /**
     * Add marker to observable list.
     * @param id  marker's name
     * @param lat   latitude from Google Maps
     * @param lng   longitude from Google Maps
     */
    public void addMarker(String id, double lat, double lng){
        markers.put(id, new Marker(id, lat, lng));
    }

    /**
     * Set name for Marker.
     * @param id    hash
     * @param name  name (cross streets)
     */
    public void setName(String id, String name){
        markers.get(id).setName(name);
    }

    public String setCrossName(String id, ObservableList<? extends String> streetsNames){
        String crossName = "";
        for(String street : streetsNames){
            if(!street.equals(""))
                if(!crossName.equals(""))
                    crossName += "/"+street;
                else
                    crossName += street;
        }
        markers.get(id).setName(crossName);
        return crossName;

    }
}
