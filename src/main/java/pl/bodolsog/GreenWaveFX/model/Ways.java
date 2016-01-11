package pl.bodolsog.GreenWaveFX.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * Created by bodolsog on 30.12.15.
 */
public class Ways {

    private int nextId = 0;
    // Map of markers <id, marker>.
    private ObservableMap<Integer,Way> ways = FXCollections.observableHashMap();

    public void addWay(Marker begin, String beginDir, Marker end, String endDir, boolean twoWay, String response) {
        if(begin != end) {
            // Check if way with this start and end exists in database.
            boolean firstWayOccur = ways.entrySet().stream().anyMatch(integerWayEntry ->
                    integerWayEntry.getValue().getWayBegin() == begin
                            && integerWayEntry.getValue().getWayEnd() == end
            );

            // TODO: 31.12.15 add feedback if fails
            // Add way.
            if (!firstWayOccur) {
                ways.put(nextId, new Way(this, nextId, begin, beginDir, end, endDir, response));
                nextId++;
            }

            if (twoWay) {
                // Check if way with this start and end exists in database.
                boolean secondWayOccur = ways.entrySet().stream().anyMatch(integerWayEntry ->
                        integerWayEntry.getValue().getWayBegin() == end
                                && integerWayEntry.getValue().getWayEnd() == begin
                );

                // Add way.
                if (!secondWayOccur) {
                    ways.put(nextId, new Way(this, nextId, end, endDir, begin, beginDir, response));
                    nextId++;
                }
            }
        }
    }

    public Way getWay(int id){
        return ways.get(id);
    }

    public void deleteWay(int id){
        ways.remove(id);
    }

    public int size(){
        return ways.size();
    }
}

/**
 * Created by bodolsog on 04.12.15.
 *
 public class Waves {
 private StringProperty id;
 private ArrayList<Marker> points = new ArrayList<>();
 private ArrayList<Ways> ways = new ArrayList<>();
 private HashMap<Integer, Integer> duration = new HashMap<>();

 public String getId() {
 return id.get();
 }

 public Marker getStartPoint(){
 return points.get(0);
 }

 public Marker getEndPoint(){
 return points.get(points.size()-1);
 }

 public boolean belongMarkerToPoints(Marker marker){
 if(points.contains(marker)) return true;
 return false;
 }

 public void addPoint(Marker marker, Ways way){
 points.add(marker);
 ways.add(way);
 int[] newValues = {
 way.getDuration(30),
 way.getDuration(35),
 way.getDuration(40),
 way.getDuration(45),
 way.getDuration(50)
 };

 if(duration.get(30) != null){
 newValues[0] += duration.get(30);
 newValues[1] += duration.get(35);
 newValues[2] += duration.get(40);
 newValues[3] += duration.get(45);
 newValues[4] += duration.get(50);
 }

 setDurationValues(newValues);
 }

 public void removePoint(int i){
 int[] newValues = {
 duration.get(30) - ways.get(i).getDuration(30),
 duration.get(35) - ways.get(i).getDuration(35),
 duration.get(40) - ways.get(i).getDuration(40),
 duration.get(45) - ways.get(i).getDuration(45),
 duration.get(50) - ways.get(i).getDuration(50)
 };

 setDurationValues(newValues);

 ways.remove(i);
 points.remove(i);
 }

 private void setDurationValues(int[] val){
 duration.put(30, val[0]);
 duration.put(35, val[1]);
 duration.put(40, val[2]);
 duration.put(45, val[3]);
 duration.put(50, val[4]);
 }
 }
 */

/**
 * Created by bodolsog on 04.12.15.
 *
 public class Ways {
 private IntegerProperty id;
 private Marker begin;
 private Marker end;
 private StringProperty type;
 private IntegerProperty distance;
 private HashMap<Integer, Integer> durations = new HashMap<>();
 private int acceleration = 5;

 public Ways(int id){
 this.id.setValue(id);
 }

 public Ways(int id, Marker beginMarker, Marker endMarker){
 this.id.setValue(id);
 setBegin(beginMarker);
 setEnd(endMarker);
 }

 public void setBegin(Marker beginMarker){
 begin = beginMarker;
 }
 public void setEnd(Marker endMarker){
 end = endMarker;
 }

 public void setType(String type){
 this.type.setValue(type);
 }
 public void setDistance(int distance){
 this.distance.setValue(distance);
 }

 public void setDurations(){
 setDuration(30);
 setDuration(35);
 setDuration(40);
 setDuration(45);
 setDuration(50);
 }

 public void setDuration(int speed){
 double t;
 double d;
 double restDist;
 double restTime;

 t = getAccelerationTime(speed);
 d = getAccelerationDistance(t);
 restDist = distance.getValue()-d;
 restTime = restDist/(double)speed;
 durations.put(speed, (int) Math.round(t+restTime));
 }

 private double getAccelerationTime(int speed){
 return speed/acceleration;
 }
 private double getAccelerationDistance(double t){
 return acceleration*t*t / 2;
 }

 public int getDuration(int speed){
 return durations.get(speed);
 }
 }
 */