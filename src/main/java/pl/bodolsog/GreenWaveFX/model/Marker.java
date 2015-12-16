package pl.bodolsog.GreenWaveFX.model;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import netscape.javascript.JSObject;
import pl.bodolsog.GreenWaveFX.crossroads.Crossroad;
import pl.bodolsog.GreenWaveFX.crossroads.FourWayCrossroad;
import pl.bodolsog.GreenWaveFX.crossroads.ThreeWayCrossroad;

/**
 * model for markers.
 */
public class Marker {

    // Marker's id.
    private IntegerProperty id;
    // Marker's name - this is a roads cross name.
    private StringProperty name;
    // GoogleMaps marker object
    public JSObject jsMarker;
    // List of all one way connections from marker to another.
    private ObservableList<Integer> connections = FXCollections.<Integer>observableArrayList();

    /**
     * Constructor
     * @param id
     * @param jsMarker GoogleMaps marker object
     */
    public Marker(int id, JSObject jsMarker){
        // Set variables.
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty("");
        this.jsMarker = jsMarker;
        this.jsMarker.setMember("id", id+"");
    }

    /**
     * Sets marker name.
     * @param newName  cross streets
     */
    public void setName(String newName){ name.setValue(newName); }

    /**
     * Gets marker name.
     */
    public String getName(){
        return name.toString();
    }

    /**
     * Returns name as StringProperty
     * @return
     */
    public StringProperty nameProperty(){
        return name;
    }


    /**
     * Return id as int.
     * @return id
     */
    public int getId(){
        return id.getValue();
    }

}
