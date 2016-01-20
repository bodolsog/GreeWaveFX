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
    private HashMap<Integer, Integer> durationsAccelerated;
    private HashMap<Integer, Integer> durationsConstant;
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

    public HashMap<Integer, Integer> getDurationsAccelerated() {
        return durationsAccelerated;
    }

    public HashMap<Integer, Integer> getDurationsConstant() {
        return durationsConstant;
    }


    public int getDurationAccelerated(int speed) {
        return durationsAccelerated.get(speed);
    }

    public int getDurationConstant(int speed) {
        return durationsConstant.get(speed);
    }

    private void calculateDurationTable() {
        durationsAccelerated = new HashMap<>();
        int[] speeds = prop.getSpeedList();
        for (int i = 0; i < speeds.length; i++) {
            durationsAccelerated.put(speeds[i], calculateDurationAccelerated(speeds[i]));
        }

        durationsConstant = new HashMap<>();
        for (int i = 0; i < speeds.length; i++) {
            durationsConstant.put(speeds[i], calculateDurationConstant(speeds[i]));
        }
    }

    private int calculateDurationAccelerated(int speedInKmH) {
        double speed;
        double t;
        double d;
        double restDist;
        double restTime;

        // Speed in mps.
        speed = convertKmHtoMS(speedInKmH);
        t = getAccelerationTime(speed);
        d = getAccelerationDistance(t);
        restDist = distance - d;
        restTime = restDist / speed;
        return (int) Math.round(t + restTime);
    }

    private int calculateDurationConstant(int speedInKmH) {
        // Speed in mps.
        double speed = convertKmHtoMS(speedInKmH);
        return (int) Math.round(distance / speed);
    }

    private double getAccelerationTime(double speed) {
        return Math.round((speed / convertKmHtoMS(prop.getAcceleration())) * 10) / 10;
    }

    private double getAccelerationDistance(double t) {
        return convertKmHtoMS(prop.getAcceleration()) * t * t / 2;
    }

    private double convertKmHtoMS(int kmH) {
        return Math.round((kmH * 10 / 36) * 10) / 10;
    }
}
