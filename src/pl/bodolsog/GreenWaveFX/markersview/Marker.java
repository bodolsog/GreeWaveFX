package pl.bodolsog.GreenWaveFX.markersview;

import javafx.beans.property.*;


public class Marker {
    private StringProperty id;
    private StringProperty name = new SimpleStringProperty("");
    private DoubleProperty lat;
    private DoubleProperty lng;

    final private ListProperty<String> connections = new SimpleListProperty<>();

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
     * @param newName  cross streets
     */
    public void setName(String newName){ name.setValue(newName); }

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
        return this.name;
    }

    /**
     * Set new value for latProperty.
     * @param lat latitude
     */
    public void setLat(double lat){
        latProperty().setValue(lat);
    }

    /**
     * Returns latitude as DoubleProperty
     * @return
     */
    public DoubleProperty latProperty(){
        return lat;
    }

    /**
     * Set new value for latProperty.
     * @param lng latitude
     */
    public void setLng(double lng){
        lngProperty().setValue(lng);
    }

    /**
     * Returns longitude as DoubleProperty
     * @return
     */
    public DoubleProperty lngProperty(){
        return lng;
    }

    /**
     * Add Marker's id to connection list.
     * @param id Marker's id
     */
    public void addConnection(String id){
        connections.add(id);
    }

    /**
     * Return list of Marker's connected other's ids.
     * @return ListProperty
     */
    public ListProperty<String> getConnections(){
        return connections;
    }
}
