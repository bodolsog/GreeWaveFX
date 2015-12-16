package pl.bodolsog.GreenWaveFX.model;

import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bodolsog on 04.12.15.
 */
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
