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
    private HashMap<Integer, Double> duration;

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

    public void setDuration(){
        //6 s do 30 km/h, ile to m
        //8 s di 35 km/h, ile to m
        //10 s do 40 km/h, ile to m
        //12 s do 45 km/h, ile to m
        //14 s do 50 km/h, ile to m
        duration.put(30, 0.0);
        duration.put(35, 0.0);
        duration.put(40, 0.0);
        duration.put(45, 0.0);
        duration.put(50, 0.0);
    }
}
