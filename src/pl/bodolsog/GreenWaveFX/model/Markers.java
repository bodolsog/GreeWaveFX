package pl.bodolsog.GreenWaveFX.model;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import pl.bodolsog.GreenWaveFX.crossroads.Crossroad;
import pl.bodolsog.GreenWaveFX.crossroads.FourWayCrossroad;
import pl.bodolsog.GreenWaveFX.crossroads.ThreeWayCrossroad;

/**
 * model for markers.
 */
public class Markers {
    // Marker's id.
    private IntegerProperty id;
    // Marker's name - this is a roads cross name.
    // TODO: set this to null
    private StringProperty name = new SimpleStringProperty("");
    // Latitude and longitude of marker's place.
    private DoubleProperty lat;
    private DoubleProperty lng;
    // List of all one way connections from marker to another.
    private ObservableList<Integer> connections = FXCollections.<Integer>observableArrayList();
    private Crossroad crossroad;

    /**
     * Constructor.
     * @param id  marker's id
     * @param lat   latitude
     * @param lng   longitude
     */
    public Markers(int id, double lat, double lng){
        // Set variables.
        this.id = new SimpleIntegerProperty(id);
        this.lat = new SimpleDoubleProperty(lat);
        this.lng = new SimpleDoubleProperty(lng);

        // Add listener. When something is added to connections list - launch method that adds this connection
        // to TitledPane.
//        connections.addListener((ListChangeListener.Change<? extends String> change) -> {
//            while(change.next()){
//                if(change.wasAdded()){
//                    controller.addConnectionToTitledPane(this, change.getFrom());
//                }
//            }
//        });
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
    public IntegerProperty idProperty(){
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
    public void addConnection(int id){
        if(!connections.contains(id))
            connections.add(id);
    }

    /**
     * Return list of Marker's connected other's ids.
     * @return ListProperty
     */
    public ObservableList<Integer> getConnections(){
        return connections;
    }


    public void setCrossroad(String type){
        if(type.equals("3_WAY")){
            crossroad = new ThreeWayCrossroad();
        }
        else if(type.equals("4_WAY")){
            crossroad = new FourWayCrossroad();
        }
    }

    public String getCrossroadType(){
        return crossroad.getType();
    }
}
