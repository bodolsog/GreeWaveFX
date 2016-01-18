package pl.bodolsog.GreenWaveFX.model;

import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Markers {

    private int nextId = 0;
    private Ways ways;
    // Map of markers <id, marker>.
    private HashMap<Integer, Marker> markers = new HashMap<>();
    private ArrayList<Marker> startpoints = new ArrayList<>();
    private int active;

    public Markers(Ways ways) {
        this.ways = ways;
    }

    /**
     * Add marker to markers list.
     * @param jsMarker GoogleMaps marker object
     */
    public void addMarker(JSObject jsMarker){
        markers.put(nextId, new Marker(nextId, jsMarker, this));
        startpoints.add(markers.get(nextId));
        setActiveMarkerId(nextId);
        nextId++;
    }

    /**
     * Return marker asociated with id.
     * @param id
     * @return Marker
     */
    public Marker getMarker(int id){
        return markers.get(id);
    }

    /**
     * Deletes marker from map.
     * @param id
     */
    public void deleteMarker(int id){
        deleteMarker(id, markers.get(id));
    }

    public void deleteMarker(Marker marker) {
        deleteMarker(marker.getId(), marker);
    }

    public void deleteMarker(int id, Marker marker) {
        ways.performRemoveMarker(marker);
        HashMap<Marker, Way> nodesToUpdate = marker.performUpdateNodes();
        removeStartpoint(marker);
        markers.remove(id);
        nodesToUpdate.forEach((node, way) -> node.findNode(way));
    }

    /**
     * Return whole map.
     * @return markers
     */
    public HashMap<Integer, Marker> getAllMarkers() {
        return markers;
    }

    /**
     * Return markersSize of Markers.
     * @return markersSize
     */
    public int markersSize() {
        return markers.size();
    }

    public int startpointsCount() {
        return startpoints.size();
    }

    /**
     * Get id of active Marker.
     * @return active
     */
    public int getActiveMarkerId(){
        return active;
    }

    /**
     * Set as active Marker's id.
     * @param id
     */
    public void setActiveMarkerId(int id){
        active = id;
    }

    /**
     * Return Marker that is now active.
     * @return Marker
     */
    public Marker getActiveMarker(){
        return markers.get(getActiveMarkerId());
    }

    public void setCrossDirections(int markerId, ArrayList<String> directions) {
        getMarker(markerId).setCrossDirections(directions);
    }

    public boolean isStartPoint(int id) {
        return markers.get(id).isStartPoint();
    }

    public Marker getNode(int i) {
        return startpoints.get(i);
    }

    public Marker getLastMarker() {
        return markers.get(nextId - 1);
    }

    public void removeStartpoint(Marker marker) {
        if (startpoints.contains(marker))
            startpoints.remove(marker);
    }

    public void addStartpoint(Marker marker) {
        if (!startpoints.contains(marker))
            startpoints.add(marker);
    }

    public ArrayList<Marker> getStartpoints() {
        return startpoints;
    }
}
