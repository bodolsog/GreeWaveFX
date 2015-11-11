package pl.bodolsog.GreenWaveFX.markersview;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class Marker {
    private DoubleProperty lat;
    private DoubleProperty lng;

    /**
     * Constructor.
     * @param lat   latitude
     * @param lng   longitude
     */
    public Marker(double lat, double lng){
        this.lat = new SimpleDoubleProperty(lat);
        this.lng = new SimpleDoubleProperty(lng);
    }

    /**
     * Returns latitude as DoubleProperty
     * @return
     */
    public DoubleProperty latProperty(){
        return lat;
    }

    /**
     * Returns longitude as DoubleProperty
     * @return
     */
    public DoubleProperty lngProperty(){
        return lng;
    }
}
