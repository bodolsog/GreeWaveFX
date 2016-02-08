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

    /**
     * Constructor.
     *
     * @param ways           Reference to Ways instance.
     * @param id             Way id.
     * @param begin          Marker from where way begins.
     * @param beginDirection Direction in which way goes from begin Marker.
     * @param end            Marker where way ends.
     * @param endDirection   Direction from which way goes to end Marker.
     * @param response       JSON Google Maps response needed to rendering way.
     * @param distance       Length of way.
     */
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

        // Adds way to begin marker.
        addToMarker();

        // Properties instance to read options.
        prop = new PropertiesManager();

        // Calculate durations tables.
        calculateDurationsTables();
    }

    /**
     * Add way to begin marker as outgoing.
     */
    private void addToMarker() {
        wayBegin.setWay(this, beginDirection);
    }


    /**
     * Calculates two duration tables:
     * - duration accelerated - used when way begins and car starts from stop;
     * - duration constant - used in flow wave, when car is going with constant speed.
     */
    private void calculateDurationsTables() {
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


    /**
     * Calculates duration for acceleration to speed and going with speed to end way.
     *
     * @param speedInKmH Endspeed given in kmph.
     * @return Duration in seconds.
     */
    private int calculateDurationAccelerated(int speedInKmH) {
        // Convert speed to mps.
        double speed = convertKmHtoMS(speedInKmH);
        // Calculate time of acceleration.
        double t = getAccelerationTime(speed);
        // Calculate distance of acceleration.
        double d = getAccelerationDistance(t);
        // Calculate distance of constant speed.
        double restDist = distance - d;
        // Calculate constant speed duration.
        double restTime = restDist / speed;

        // Return rounded duration.
        return (int) Math.round(t + restTime);
    }


    /**
     * Calculates duration for constant speed to end way.
     *
     * @param speedInKmph Speed given in kmph.
     * @return Duration in seconds.
     */
    private int calculateDurationConstant(int speedInKmph) {
        // Speed in mps.
        double speed = convertKmHtoMS(speedInKmph);
        return (int) Math.round(distance / speed);
    }


    /**
     * Convert kmph to mps.
     *
     * @param kmph Value in kmph.
     * @return Rounded value in mps.
     */
    private double convertKmHtoMS(int kmph) {
        return Math.round((kmph * 10 / 36) * 10) / 10;
    }


    /**
     * Calculate acceleration time: t = (v_end - v_0) / a
     *
     * @param speed Speed in mps
     * @return Duration in seconds.
     */
    private double getAccelerationTime(double speed) {
        return Math.round((speed / convertKmHtoMS(prop.getAcceleration())) * 10) / 10;
    }


    /**
     * Calculate acceleration distance: s = (a * t^2) / 2
     *
     * @param t Time in seconds
     * @return Distance in meters.
     */
    private double getAccelerationDistance(double t) {
        return convertKmHtoMS(prop.getAcceleration()) * t * t / 2;
    }


    /**
     * Destroys way by removing from ways list and begin of his marker begin.
     */
    public void destroy() {
        ways.remove(id);
        wayBegin.removeWay(this);
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



}
