package pl.bodolsog.GreenWaveFX.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Markers {
    private DoubleProperty lat;
    private DoubleProperty lng;

    public final void setLatLng(double lat, double lng){
        this.lat = new SimpleDoubleProperty(lat);
        this.lng = new SimpleDoubleProperty(lng);
    }

    public DoubleProperty latProperty(){
        return lat;
    }
    public DoubleProperty lngProperty(){
        return lng;
    }

    /**
     * Test function
     * @return String "lat: {lat}, lng: {lng}"
     */
    public String getLatLng(){
        return "lat: "+String.valueOf(lat.get())+", lng: "+String.valueOf(lng.get());
    }
}
