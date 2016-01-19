package pl.bodolsog.GreenWaveFX.engine;

import pl.bodolsog.GreenWaveFX.model.Marker;
import pl.bodolsog.GreenWaveFX.model.Way;

import java.util.ArrayList;

/**
 * Created by bodolsog on 17.01.16.
 */
public class Hierarchy {
    private Marker startpoint;
    private Marker endpoint;
    private int distance;
    private ArrayList<Way> ways = new ArrayList<>();

    public void addStartpoint(Marker marker) {
        startpoint = marker;
    }

    public void addEndpoint(Marker marker) {
        endpoint = marker;
    }

    public Marker getStartpoint() {
        return startpoint;
    }

    public Marker getEndpoint() {
        return endpoint;
    }

    /**
     * Getter for property 'distance'.
     *
     * @return Value for property 'distance'.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Setter for property 'distance'.
     *
     * @param distance Value to set for property 'distance'.
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void addWay(Way way) {
        distance += way.getDistance();
        ways.add(way);
    }

    public void addWayReverse(Way way) {
        distance += way.getDistance();
        ways.add(0, way);
    }

    public ArrayList<Way> getWays() {
        return ways;
    }
}
