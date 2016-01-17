package pl.bodolsog.GreenWaveFX.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.List;
import java.util.stream.Collectors;

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
                    integerWayEntry.getValue().getBeginMarker() == begin
                            && integerWayEntry.getValue().getEndMarker() == end
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
                        integerWayEntry.getValue().getBeginMarker() == end
                                && integerWayEntry.getValue().getEndMarker() == begin
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

    public void removeWay(int id) {
        Way way = ways.get(id);
        way.destroy();
    }

    protected void remove(int id) {
        ways.remove(id);
    }

    public void performRemoveMarker(Marker marker) {
        List<Way> waysToRemove = ways.entrySet().stream()
                .filter(entry -> entry.getValue().getBeginMarker() == marker
                        || entry.getValue().getEndMarker() == marker)
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());
        waysToRemove.forEach(way -> way.destroy());
    }

    public int size(){
        return ways.size();
    }
}