package pl.bodolsog.GreenWaveFX.engine;

import pl.bodolsog.GreenWaveFX.model.Marker;
import pl.bodolsog.GreenWaveFX.model.Way;
import pl.bodolsog.GreenWaveFX.staticVar.NODE_TYPE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by bodolsog on 17.01.16.
 */
public class Wave {
    private Marker startpoint;
    private Marker endpoint;

    private int distance;

    private ArrayList<Way> ways = new ArrayList<>();
    private ArrayList<Marker> crosses = new ArrayList<>();
    private HashMap<Marker, ArrayList<Integer>> possibleSpeeds = new HashMap<>();


    public void calculatePossibleSpeeds() {
        for (int i = 1; i < ways.size(); i++) {
            Way outgoingWay = ways.get(i);
            Way incomingWay = ways.get(i - 1);
            Marker node = outgoingWay.getBeginMarker();

            HashMap<Integer, Integer> incomingDurations;
            HashMap<Integer, Integer> outgoingDurations;

            if (incomingWay.getBeginMarker().getNodeType() == NODE_TYPE.STARTPOINT)
                incomingDurations = incomingWay.getDurationsAccelerated();
            else
                incomingDurations = incomingWay.getDurationsConstant();

            if (outgoingWay.getEndMarker().getNodeType() == NODE_TYPE.STARTPOINT)
                outgoingDurations = outgoingWay.getDurationsAccelerated();
            else
                outgoingDurations = outgoingWay.getDurationsConstant();


            possibleSpeeds.put(node, new ArrayList<>());

            incomingDurations.forEach((speedIn, durationIn) -> {
                outgoingDurations.forEach((speedOut, durationOut) -> {
                    if (Math.abs(durationIn - durationOut) < 4)
                        if (!possibleSpeeds.get(node).contains(speedIn))
                            possibleSpeeds.get(node).add(speedIn);
                });
            });

            Collections.sort(possibleSpeeds.get(node), Collections.reverseOrder());
        }
    }

    public ArrayList<Integer> getPossibleSpeeds(Marker marker) {
        return possibleSpeeds.get(marker);
    }

    /**
     * Getter for property 'startpoint'.
     *
     * @return Value for property 'startpoint'.
     */
    public Marker getStartpoint() {
        return startpoint;
    }

    /**
     * Setter for property 'startpoint'.
     *
     * @param marker Value to set for property 'startpoint'.
     */
    public void setStartpoint(Marker marker) {
        startpoint = marker;
        if (!crosses.contains(marker))
            crosses.add(marker);
    }

    /**
     * Getter for property 'endpoint'.
     *
     * @return Value for property 'endpoint'.
     */
    public Marker getEndpoint() {
        return endpoint;
    }

    /**
     * Setter for property 'endpoint'.
     *
     * @param marker Value to set for property 'endpoint'.
     */
    public void setEndpoint(Marker marker) {
        endpoint = marker;
        if (!crosses.contains(marker))
            crosses.add(marker);
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
     * Add Way to ways list.
     *
     * @param way Way to be added to ways list.
     */
    public void addWay(Way way) {
        distance += way.getDistance();
        ways.add(way);
    }

    /**
     * Add Way to ways list at first position..
     *
     * @param way Way to be added to ways list.
     */
    public void addWayReverse(Way way) {
        distance += way.getDistance();
        ways.add(0, way);
    }

    /**
     * Getter for ways list.
     *
     * @return List of Ways.
     */
    public ArrayList<Way> getWays() {
        return ways;
    }

    /**
     * Add Marker to crosses list.
     *
     * @param cross Marker to be added to crosses list.
     */
    public void addCross(Marker cross) {
        crosses.add(cross);
    }

    /**
     * Getter for crosses list.
     *
     * @return List of crosses.
     */
    public ArrayList<Marker> getCrosses() {
        return crosses;
    }
}
