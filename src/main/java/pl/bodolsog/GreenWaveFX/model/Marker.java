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
            if (change.wasAdded() && change.wasRemoved()) {               // Added, removed or updated
                int connectedWays = (int) change.getMap().entrySet().stream()
                        .filter(entry -> entry.getValue() != null)
                        .count();

                if (connectedWays == 2) {
                    nodeType = NODE_TYPE.TRANSITION;
                    updateConnectedNodes();
                    connectedNodes.clear();
                    markers.removeEndNode(this);
                } else if (change.getValueAdded() != null) {         // added or updated Way
                    if (connectedWays == 3) {
                        nodeType = NODE_TYPE.CROSSROAD;
                        crawlForNodes();
                    } else
                        findNode(change.getValueAdded());
                } else {                                           // removed way
                    if (connectedWays == 1) {
                        nodeType = NODE_TYPE.STARTPOINT;
                        markers.addStartpoint(this);
                        crawlForNodes();
                    } else
                        checkNodes(change.getValueRemoved());
                }
            }
        });
    }

    private void checkNodes(Way way) {
        ArrayList<Marker> nodesToRemove = new ArrayList<>();
        connectedNodes.forEach((marker, list) -> {
            if (list.get(0) == way) {
                nodesToRemove.add(marker);
            }
        });
        nodesToRemove.forEach(node -> connectedNodes.remove(node));
    }

    private void crawlForNodes() {
        cross.forEach((dir, way) -> {
            if (way != null)
                findNode(way);
        });

        connectedNodes.forEach((marker, list) -> {
            String wayFromDirection = list.get(list.size() - 1).getEndDirection();
            if (marker.getCross().containsKey(wayFromDirection) && marker.getCross().get(wayFromDirection) != null) {
                Way outgoingWay = marker.getCross().get(wayFromDirection);
                ArrayList<Marker> mm = new ArrayList<Marker>();

                marker.getConnectedNodes().forEach((m, l) -> {
                    if (l.get(0) == outgoingWay)
                        mm.add(m);
                });
                mm.forEach(m -> marker.getConnectedNodes().remove(m));
                marker.findNode(outgoingWay);
            }
        });
    }

    private void updateConnectedNodes() {
        HashMap<Marker, Way> nodesToUpdate = performUpdateNodes();
        nodesToUpdate.forEach((marker, way) -> marker.findNode(way));
    }

    public void findNode(Way way) {
        ArrayList<Way> listOfWays = new ArrayList<>();
        connectedNodes.put(findNode(way, listOfWays), listOfWays);
    }

    private Marker findNode(Way way, ArrayList<Way> listOfWays) {
        listOfWays.add(way);
        Marker marker = way.getEndMarker();

        if (marker.getNodeType() == NODE_TYPE.TRANSITION) {

            ArrayList<Way> possibleWays = new ArrayList<>();
            ArrayList<Way> cWays = marker.getCrossWays();
            for (int i = 0; i < cWays.size(); i++) {
                if (cWays.get(i).getBeginDirection() != way.getEndDirection())
                    possibleWays.add(cWays.get(i));
            }

            if (possibleWays.size() > 1) {
                return marker;
            }
            return findNode(possibleWays.get(0), listOfWays);
        }
        return marker;
    }

    public HashMap<Marker, Way> performUpdateNodes() {
        HashMap<Marker, Way> nodesToUpdate = new HashMap<>();
        connectedNodes.forEach((marker, list) -> {
            if (marker.getConnectedNodes().containsKey(this)) {
                Way backWay = marker.getConnectedNode(this).get(0);
                nodesToUpdate.put(marker, backWay);
                marker.getConnectedNodes().remove(this);
            }
        });
        return nodesToUpdate;
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
        List<String> key = cross.entrySet().stream()
                .filter(p -> p.getValue() == way)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        key.forEach(direction -> cross.put(direction, null));
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

    protected ArrayList<Way> getConnectedNode(Marker marker) {
        if (connectedNodes.containsKey(marker))
            return connectedNodes.get(marker);
        else
            throw new NullPointerException();
    }

    protected ArrayList<Way> getNodeWaysList(Marker marker) {
        return connectedNodes.get(marker);
    }
}
