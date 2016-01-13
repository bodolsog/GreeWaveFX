package pl.bodolsog.GreenWaveFX.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import netscape.javascript.JSObject;
import pl.bodolsog.GreenWaveFX.staticVar.DIRECTIONS;
import pl.bodolsog.GreenWaveFX.staticVar.NODE_TYPE;

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
    private Markers markers;
    private ObservableMap<String, Way> cross = FXCollections.observableHashMap();

    private HashMap<Marker, ArrayList<Way>> connectedNodes = new HashMap<>();

    private int nodeType = NODE_TYPE.STARTPOINT;


    /**
     * Constructor
     * @param id
     * @param jsMarker GoogleMaps marker object
     */
    public Marker(int id, JSObject jsMarker, Markers markers) {
        // Set variables.
        this.id = new SimpleIntegerProperty(id);
        this.jsMarker = jsMarker;
        this.jsMarker.setMember("id", id+"");
        this.markers = markers;

        cross.addListener((MapChangeListener<String, Way>) change -> {
            if (change.wasAdded() && change.wasRemoved()) {               // updated
                int connectedWays = (int) change.getMap().entrySet().stream()
                        .filter(entry -> entry.getValue() != null)
                        .count();

                if (connectedWays == 2) {
                    nodeType = NODE_TYPE.TRANSITION;
                    connectedNodes.clear();
                } else if (change.getValueAdded() != null) {         // added Way
                    if (connectedWays == 3) {
                        nodeType = NODE_TYPE.CROSSROAD;
                        updateAllNodes();
                    } else
                        updateNode(change.getValueAdded());
                } else {                                           // removed way
                    if (connectedWays == 1) {
                        nodeType = NODE_TYPE.STARTPOINT;
                        updateAllNodes();
                    } else
                        updateNode(change.getValueAdded());
                }
            }
        });
    }

    private void updateNode(Way way) {
        if (way.getWayEnd() != this) {
            ArrayList<Way> waysList = new ArrayList<>();
            connectedNodes.put(findNode(way, waysList), waysList);
        }
    }

    private void updateAllNodes() {
        cross.forEach((s, way) -> updateNode(way));
    }

    private Marker findNode(Way previousWay, ArrayList<Way> ways) {
        ways.add(previousWay);
        Marker previousMarker = previousWay.getWayBegin();
        Marker marker = previousWay.getWayEnd();
        if (marker.getNodeType() != NODE_TYPE.TRANSITION) {
            return marker;
        } else {
            Way nextWay = marker.getCrossWays().stream().filter(way -> way.getWayEnd() != previousMarker).findFirst().get();
            return findNode(nextWay, ways);
        }
    }


    /**
     * Return id as int.
     * @return id
     */
    public int getId(){
        return id.getValue();
    }

    protected Way getCrossDirection(String direction) {
        if (cross.containsKey(direction))
            return cross.get(direction);
        else
            throw new NullPointerException();
    }

    protected ArrayList<String> getCrossDirections() {
        ArrayList<String> r = new ArrayList<>();
        cross.forEach((s, way) -> r.add(s));
        return r;
    }

    protected void setCrossDirections(ArrayList<String> decodedDirections) {
        Arrays.asList(DIRECTIONS.NAMES).forEach(direction -> {
            if (decodedDirections.contains(direction) && !cross.containsKey(direction))
                cross.put(direction, null);
            else if (cross.containsKey(direction) && !decodedDirections.contains(direction)) {
                if (cross.get(direction) != null)
                    cross.get(direction).destroy();
                cross.remove(direction);
            }
        });
    }

    protected ArrayList<Way> getCrossWays() {
        ArrayList<Way> r = new ArrayList<>();
        cross.forEach((s, way) -> {
            if (way != null)
                r.add(way);
        });
        return r;
    }

    public ObservableMap<String, Way> getCross() {
        return cross;
    }

    protected void setWay(Way way, String direction) {
        cross.put(direction, way);
    }

    public Way getWay(String direction) {
        return cross.get(direction);
    }

    protected void removeWay(Way way) {
        List<String> key = cross.entrySet()
                .stream()
                .filter(p -> p.getValue() == way)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        key.forEach(direction -> cross.remove(direction));
    }

    protected boolean isStartPoint() {
        int count = (int) cross.entrySet()
                .stream()
                .filter(stringWayEntry -> stringWayEntry.getValue() != null)
                .count();
        if (count > 1)
            return false;
        return true;
    }

    protected int getNodeType() {
        return nodeType;
    }

    protected HashMap<Marker, ArrayList<Way>> getConnectedNodes() {
        return connectedNodes;
    }

    protected ArrayList<Way> getNodeWaysList(Marker marker) {
        return connectedNodes.get(marker);
    }
}
