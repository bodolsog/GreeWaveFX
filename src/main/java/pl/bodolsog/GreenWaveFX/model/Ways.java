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

    public void addWay(Marker begin, String beginDir, Marker end, String endDir, boolean twoWay,
                       String response, int distance) {
        if(begin != end) {
            // Check if way with this start and end exists in database.
            boolean firstWayOccur = ways.entrySet().stream().anyMatch(integerWayEntry ->
                    integerWayEntry.getValue().getWayBegin() == begin
                            && integerWayEntry.getValue().getWayEnd() == end
            );

            // TODO: 31.12.15 add feedback if fails
            // Add way.
            if (!firstWayOccur) {
                ways.put(nextId, new Way(this, nextId, begin, beginDir, end, endDir, response, distance));
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
                    ways.put(nextId, new Way(this, nextId, end, endDir, begin, beginDir, response, distance));
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