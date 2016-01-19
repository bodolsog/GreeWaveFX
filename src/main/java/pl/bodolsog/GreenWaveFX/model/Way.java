package pl.bodolsog.GreenWaveFX.model;

import pl.bodolsog.GreenWaveFX.tools.PropertiesManager;

import java.util.HashMap;

/**
 * Created by bodolsog on 30.12.15.
 */
public class Way {
    private int id;
    private Marker wayBegin;
    private Marker wayEnd;
    private String beginDirection;
    private String endDirection;
    private int distance;
    private Ways ways;
    private String response;
    private HashMap<Integer, Integer> durations;
    private PropertiesManager prop;

    public Way(Ways ways, int id, Marker begin, String beginDirection,
               Marker end, String endDirection, String response, int distance) {
        this.ways = ways;
        this.id = id;
        wayBegin = begin;
        wayEnd = end;
        this.beginDirection = beginDirection;
        this.endDirection = endDirection;
        this.response = response;
        this.distance = distance;
        addToMarker();

        prop = new PropertiesManager();

        calculateDurationTable();
    }

    public int getDistance() {
        return distance;
    }
    public Marker getBeginMarker() {
        return wayBegin;
    }

    public Marker getEndMarker() {
        return wayEnd;
    }

    public String getBeginDirection() {
        return beginDirection;
    }

    public String getEndDirection() {
        return endDirection;
    }
    public String getResponse() {
        return response;
    }

    private void addToMarker() {
        wayBegin.setWay(this, beginDirection);
    }

    public void destroy() {
        ways.remove(id);
        wayBegin.removeWay(this);
    }

    public int getDuration(int speed) {
        return durations.get(speed);
    }

    private void calculateDurationTable() {
        durations = new HashMap<>();
        int[] speeds = prop.getSpeedList();
        for (int i = 0; i < speeds.length; i++) {
            durations.put(speeds[i], calculateDuration(speeds[i]));
        }
    }

    private int calculateDuration(int speed) {
        double t;
        double d;
        double restDist;
        double restTime;

        t = getAccelerationTime(speed);
        d = getAccelerationDistance(t);
        restDist = distance - d;
        restTime = restDist / (double) speed;
        return (int) Math.round(t + restTime);
    }

    private double getAccelerationTime(int speed) {
        return speed / prop.getAcceleration();
    }

    private double getAccelerationDistance(double t) {
        return prop.getAcceleration() * t * t / 2;
    }
}
