package pl.bodolsog.GreenWaveFX.engine;


import pl.bodolsog.GreenWaveFX.model.Marker;
import pl.bodolsog.GreenWaveFX.model.Way;
import pl.bodolsog.GreenWaveFX.staticVar.OPPOSITE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HierarchyBuilder {
    private HashMap<Integer, Way> ways;
    private HashMap<Integer, Marker> markers;
    private ArrayList<Marker> startpoints;
    private ArrayList<Hierarchy> hierarchies;

    public HierarchyBuilder(HashMap<Integer, Way> allWays, HashMap<Integer, Marker> allMarkers, ArrayList<Marker> startpoints) {
        hierarchies = new ArrayList<>();

        this.ways = new HashMap<>(allWays);
        this.markers = new HashMap<>(allMarkers);
        this.startpoints = new ArrayList<>(startpoints);
    }

    public Hierarchy getHierarchyEntry(int i) {
        return hierarchies.get(i);
    }

    public void buildHierarchy() {
        startpoints.forEach(startpoint -> {
            Hierarchy hierarchyEntry = new Hierarchy();
            hierarchyEntry.addStartpoint(startpoint);

            Marker nextNode = startpoint.getConnectedNodes().entrySet().stream().map(Map.Entry::getKey).findFirst().orElse(null);
            String direction = startpoint.getConnectedNodes().get(nextNode).get(0).getEndDirection();

            Marker endpoint = dfs(nextNode, direction, startpoint);

            hierarchyEntry.addEndpoint(endpoint);
            hierarchies.add(hierarchyEntry);
        });
    }

    private Marker dfs(Marker currentNode, String incomingDirection, Marker previousNode) {
        String nextDirection = findNextDirection(currentNode, incomingDirection);
        if (nextDirection == null)
            return currentNode;
        return null;
    }

    private String findNextDirection(Marker marker, String incomingDirection) {
        String[] possibleDirections = OPPOSITE.DIRECTIONS.get(incomingDirection);
        if (marker.getCross().get(possibleDirections[0]) != null)
            return possibleDirections[0];
        else {
            if (marker.getCross().get(possibleDirections[1]) != null) {
                // better opposite dir
                if (marker.getCross().get(OPPOSITE.DIRECTIONS.get(possibleDirections[1])) == null)
                    return possibleDirections[1];
            }

            if (marker.getCross().get(possibleDirections[2]) != null) {
                // better opposite dir
                if (marker.getCross().get(OPPOSITE.DIRECTIONS.get(possibleDirections[2])) == null)
                    return possibleDirections[2];
            }
        }
        return null;
    }
}
