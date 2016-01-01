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

    public void addWay(Marker begin, Marker end, boolean twoWay){
        boolean firstWayOccur = ways.entrySet().stream().anyMatch(integerWayEntry ->
                integerWayEntry.getValue().getWayBegin() == begin
                && integerWayEntry.getValue().getWayEnd() == end
        );

        // TODO: 31.12.15 add feedback if fails
        if(!firstWayOccur) {
            ways.put(nextId, new Way(nextId, begin, end));
            nextId++;
        }

        if(twoWay){
            boolean secondWayOccur = ways.entrySet().stream().anyMatch(integerWayEntry ->
                    integerWayEntry.getValue().getWayBegin() == end
                    && integerWayEntry.getValue().getWayEnd() == begin
            );
            if(!secondWayOccur){
                ways.put(nextId, new Way(nextId, end, begin));
                nextId++;
            }
        }
    }

    public Way getWay(int id){
        return ways.get(id);
    }

    public int size(){
        return ways.size();
    }
}
