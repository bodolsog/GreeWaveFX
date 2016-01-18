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
    private HashMap<Marker, ArrayList<String>> visited;

    public HierarchyBuilder(HashMap<Integer, Way> allWays, HashMap<Integer, Marker> allMarkers, ArrayList<Marker> startpoints) {
        hierarchies = new ArrayList<>();
        visited = new HashMap<>();

        this.ways = new HashMap<>(allWays);
        this.markers = new HashMap<>(allMarkers);
        this.startpoints = new ArrayList<>(startpoints);
    }

    public Hierarchy getHierarchyEntry(int i) {
        return hierarchies.get(i);
    }

    public int size() {
        return hierarchies.size();
    }

    public void buildHierarchy() {
        ArrayList<Marker> crosses = new ArrayList<>();
        startpoints.forEach(startpoint -> {
            Hierarchy hierarchyEntry = new Hierarchy();
            hierarchyEntry.addStartpoint(startpoint);

            Marker endpoint = nextNode(startpoint, crosses);

            hierarchyEntry.addEndpoint(endpoint);
            hierarchies.add(hierarchyEntry);
        });
        hh(crosses);
    }

    private void hh(ArrayList<Marker> markers) {
        ArrayList<Marker> crosses = new ArrayList<>();
        markers.forEach(marker -> {
            Hierarchy hierarchyEntry = new Hierarchy();
            hierarchyEntry.addStartpoint(marker);

            String direction;
            if (visited.containsKey(marker)) {
                direction = marker
                        .getCross()
                        .entrySet()
                        .stream()
                        .filter(e -> e.getValue() != null)
                        .filter(e -> !visited.get(marker).contains(e.getKey()))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);
            } else {
                direction = marker
                        .getCross()
                        .entrySet()
                        .stream()
                        .filter(e -> e.getValue() != null)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);
            }

            Marker endpoint = nextNode(marker, direction, crosses);

            hierarchyEntry.addEndpoint(endpoint);
            if (endpoint != null && marker != null)
                hierarchies.add(hierarchyEntry);
        });
        if (crosses.size() > 0)
            hh(crosses);
    }


    private Marker nextNode(Marker currentNode, ArrayList<Marker> crosses) {
        String outgoingDirection = currentNode
                .getCross()
                .entrySet()
                .stream()
                .filter(e -> e.getValue() != null)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        return nextNode(currentNode, outgoingDirection, crosses);
    }


    private Marker nextNode(Marker currentNode, String outgoingDirection, ArrayList<Marker> crosses) {
        int ct;
        if (visited.containsKey(currentNode))
            ct = (int) currentNode
                    .getCross()
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue() != null)
                    .filter(e -> !visited.get(currentNode).contains(e.getKey()))
                    .count();
        else
            ct = (int) currentNode
                    .getCross()
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue() != null)
                    .count();
        if (ct > 2) {
            crosses.add(currentNode);
        }

        Marker nextNode = currentNode
                .getConnectedNodes()
                .entrySet()
                .stream()
                .filter(e -> e.getValue().get(0).getBeginDirection() == outgoingDirection)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        String nextNodeIncomingDirection = currentNode
                .getConnectedNodes()
                .entrySet()
                .stream()
                .filter(e -> e.getValue().get(0).getBeginDirection() == outgoingDirection)
                .map(e -> e.getValue().get(e.getValue().size() - 1).getEndDirection())
                .findFirst()
                .orElse(null);

        String nextDirection = findNextDirection(nextNode, nextNodeIncomingDirection);

        if (!visited.containsKey(currentNode))
            visited.put(currentNode, new ArrayList<>());
        visited.get(currentNode).add(outgoingDirection);

        if (nextDirection == null)
            return nextNode;

        return nextNode(nextNode, nextDirection, crosses);
    }

    private String findNextDirection(Marker marker, String incomingDirection) {
        if (incomingDirection == null)
            return null;

        String[] possibleDirections = OPPOSITE.DIRECTIONS.get(incomingDirection);

        if (
                marker.getCross().containsKey(possibleDirections[0]) &&
                        marker.getCross().get(possibleDirections[0]) != null)
            return possibleDirections[0];
        else {
            if (marker.getCross().containsKey(possibleDirections[1]) && marker.getCross().get(possibleDirections[1]) != null) {
                // better opposite dir
                if (marker.getCross().get(OPPOSITE.DIRECTIONS.get(possibleDirections[1])) == null)
                    return possibleDirections[1];
            }

            if (marker.getCross().containsKey(possibleDirections[2]) && marker.getCross().get(possibleDirections[2]) != null) {
                // better opposite dir
                if (marker.getCross().get(OPPOSITE.DIRECTIONS.get(possibleDirections[2])) == null)
                    return possibleDirections[2];
            }
        }
        return null;
    }
}
