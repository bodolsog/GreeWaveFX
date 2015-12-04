package pl.bodolsog.GreenWaveFX.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;

/**
 * Created by bodolsog on 04.12.15.
 */
public class Ways {
    private IntegerProperty id;
    private Markers begin;
    private Markers end;
    private StringProperty type;
    private IntegerProperty distance;
    private HashMap<Integer, Double> durations = new HashMap<>();
    private int acceleration = 5;

    public Ways(int id){
        this.id.setValue(id);
    }

    public Ways(int id, Markers beginMarker, Markers endMarker){
        this.id.setValue(id);
        setBegin(beginMarker);
        setEnd(endMarker);
    }

    public void setBegin(Markers beginMarker){
        begin = beginMarker;
    }
    public void setEnd(Markers endMarker){
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
        durations.put(speed, t+restTime);
     }

    private double getAccelerationTime(int speed){
        return speed/acceleration;
    }
    private double getAccelerationDistance(double t){
        return acceleration*t*t / 2;
    }
}
