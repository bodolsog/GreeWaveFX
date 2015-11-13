package pl.bodolsog.GreenWaveFX.markersview;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Marker {
    private StringProperty id;
    private StringProperty name;
    private DoubleProperty lat;
    private DoubleProperty lng;

    /**
     * Constructor.
     * @param id  marker's name
     * @param lat   latitude
     * @param lng   longitude
     */
    public Marker(String id, double lat, double lng){
        this.id = new SimpleStringProperty(id);
        this.lat = new SimpleDoubleProperty(lat);
        this.lng = new SimpleDoubleProperty(lng);
    }

    /**
     * Sets marker name (cross streets)
     * @param name  cross streets
     */
    public void setName(String name){
        this.name = new SimpleStringProperty(name);
    }

    /**
     * Returns name as StringProperty
     * @return
     */
    public StringProperty idProperty(){
        return id;
    }


    /**
     * Returns name as StringProperty
     * @return
     */
    public StringProperty nameProperty(){
        return name;
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
