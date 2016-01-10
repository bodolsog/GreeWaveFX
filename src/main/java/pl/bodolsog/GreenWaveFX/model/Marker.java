package pl.bodolsog.GreenWaveFX.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import netscape.javascript.JSObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * model for markers.
 */
public class Marker {

    // GoogleMaps marker object
    public JSObject jsMarker;
    // Marker's id.
    private IntegerProperty id;
    // List of all one way connections from marker to another.
    private ObservableList<Integer> connections = FXCollections.<Integer>observableArrayList();
    // List of roads:
    // 0 - north, 1 - east, 2 - south, 3 - west
    // null - way is disabled
    private HashMap<String, Way> cross = new HashMap<>();
    private HashMap<String, Way> tmpCross;

    /**
     * Constructor
     * @param id
     * @param jsMarker GoogleMaps marker object
     */
    public Marker(int id, JSObject jsMarker){
        // Set variables.
        this.id = new SimpleIntegerProperty(id);
        this.jsMarker = jsMarker;
        this.jsMarker.setMember("id", id+"");
    }

    /**
     * Return id as int.
     * @return id
     */
    public int getId(){
        return id.getValue();
    }

    protected void removeWay(Way way) {
        List<String> key = cross.entrySet()
                .stream()
                .filter(p -> p.getValue() == way)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        key.forEach(direction -> cross.remove(direction));
    }

    protected ArrayList<String> getCrossDirections() {
        ArrayList<String> r = new ArrayList<>();
        cross.forEach((s, way) -> r.add(s));

        return r;
    }

    protected void setCrossDirections(String[] directions) {
        // Make copy of old cross hashmap.
        if (cross != null)
            tmpCross = new HashMap<>(cross);

        // Create new cross.
        cross = new HashMap<>();

        // For each direction (if they are allowed in DIRECTIONS) try copy a Way if exists in same direction.
        for (String direction : directions) {
            if (Arrays.asList(Markers.DIRECTIONS).contains(direction))
                if (tmpCross.containsKey(direction)) {
                    cross.put(direction, tmpCross.get(direction));
                    tmpCross.remove(direction);
                } else
                    cross.put(direction, null);
        }

        tmpCross.forEach((s, way) -> way.destroy());
    }

    protected void addWay(Way way, String direction) {
        cross.put(direction, way);
    }

    protected Way getCrossDirection(String direction) {
        if (cross.containsKey(direction))
            return cross.get(direction);
        else
            throw new NullPointerException();
    }
}
