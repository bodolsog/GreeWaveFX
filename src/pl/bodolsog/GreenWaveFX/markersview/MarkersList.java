package pl.bodolsog.GreenWaveFX.markersview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Manages global observable list of Markers.
 */
public class MarkersList {

    /**
     * Global variable.
     */
    public static ObservableList<Marker> markers = FXCollections.observableArrayList();

    /**
     * Add marker to observable list.
     * @param lat   latitude from Google Maps
     * @param lng   longitude from Google Maps
     */
    public void addMarker(double lat, double lng){
        markers.add(new Marker(lat, lng));
    }

    /**
     * Add existing marker to observable list.
     * @param marker    Markers Object instance.
     */
    /*private void addMarker(Marker marker){
        markers.add(marker);
    }*/

}
