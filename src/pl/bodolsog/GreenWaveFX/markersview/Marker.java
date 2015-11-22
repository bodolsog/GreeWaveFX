package pl.bodolsog.GreenWaveFX.markersview;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;


public class Marker {
    private MarkersViewController controller;
    private StringProperty id;
    private StringProperty name = new SimpleStringProperty("");
    private DoubleProperty lat;
    private DoubleProperty lng;

    private ObservableList<String> connections = FXCollections.<String>observableArrayList();

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
        connections.addListener((ListChangeListener.Change<? extends String> change) -> {
            while(change.next()){
                if(change.wasAdded()){
                    controller.addConnectionToTitledPane(this, change.getFrom());
                }
            }
        });
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
        if(!connections.contains(id))
            connections.add(id);
    }

    /**
     * Return list of Marker's connected other's ids.
     * @return ListProperty
     */
    public ObservableList<String> getConnections(){
        return connections;
    }

    public void setController(MarkersViewController controller){
        this.controller = controller;
    }
}
