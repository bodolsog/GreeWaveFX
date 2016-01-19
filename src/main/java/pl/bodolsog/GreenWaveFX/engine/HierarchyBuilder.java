package pl.bodolsog.GreenWaveFX.engine;


import pl.bodolsog.GreenWaveFX.model.Marker;
import pl.bodolsog.GreenWaveFX.model.Way;
import pl.bodolsog.GreenWaveFX.staticVar.NODE_TYPE;
import pl.bodolsog.GreenWaveFX.staticVar.OPPOSITE;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HierarchyBuilder {
    private HashMap<Integer, Way> ways;
    private HashMap<Integer, Marker> markers;
    private ArrayList<Marker> startpoints;
    private ArrayList<Hierarchy> hierarchies;
    private HashMap<Marker, ArrayList<String>> visited;
    private ArrayList<Marker[]> principles;

    public HierarchyBuilder(HashMap<Integer, Way> allWays, HashMap<Integer, Marker> allMarkers, ArrayList<Marker> startpoints) {
        hierarchies = new ArrayList<>();
        visited = new HashMap<>();
        principles = new ArrayList<>();

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
        if (principles.size() > 0)
            principles.forEach(markersArr -> findPrinciple(markersArr));

        findWaves(startpoints);

        ArrayList<Marker> uncheckedCrosses = findUncheckedCrosses();
        findWaves(uncheckedCrosses);
    }

    private ArrayList<Marker> findUncheckedCrosses() {
        ArrayList<Marker> crosses = new ArrayList<>();

        markers.forEach((integer, marker) -> {
            if (marker.getNodeType() != NODE_TYPE.TRANSITION) {
                int uvisitedWaysCount = (int) marker.getCross().entrySet()
                        .stream()
                        .filter(e -> !visited.containsKey(marker) || !visited.get(marker).contains(e.getKey()))
                        .filter(e -> e.getValue() != null)
                        .count();
                if (uvisitedWaysCount > 0)
                    crosses.add(marker);
            }
        });
        return crosses;
    }

    private void findPrinciple(Marker[] markersArr) {
        Marker startNode = markersArr[0];
        Marker endNode = markersArr[1];
        Set<Marker> checkedNodes = new HashSet<>();
        Map<Marker, Integer> distances = new HashMap<>();
        Map<Marker, Marker> predecessors = new HashMap<>();

        Hierarchy hierarchyEntry = new Hierarchy();
        hierarchyEntry.addStartpoint(startNode);

        startNode.getConnectedNodes()
                .forEach((nextNode, waysList) -> {
                    checkedNodes.add(nextNode);
                    AtomicInteger distance = new AtomicInteger(0);
                    waysList.forEach(way -> distance.addAndGet(way.getDistance()));
                    distances.put(nextNode, distance.get());
                    predecessors.put(nextNode, startNode);
                    principleNextNode(nextNode, checkedNodes, distances, predecessors);
                });

        setPrincipleVisited(endNode, predecessors, startNode, hierarchyEntry);

        hierarchyEntry.addEndpoint(endNode);
        hierarchyEntry.setDistance(distances.get(endNode));
        hierarchies.add(hierarchyEntry);

    }

    private void setPrincipleVisited(Marker node, Map<Marker, Marker> predecessors, Marker stopNode, Hierarchy hierarchyEntry) {
        ArrayList<Way> ways = predecessors.get(node).getConnectedNodes().get(node);
        ways.forEach(way -> hierarchyEntry.addWayReverse(way));

        String direction = node.getConnectedNodes().get(predecessors.get(node)).get(0).getBeginDirection();
        markVisited(node, direction);

        if (predecessors.get(node) != stopNode)
            setPrincipleVisited(predecessors.get(node), predecessors, stopNode, hierarchyEntry);
    }

    private void markVisited(Marker currentNode, String outgoingDirection) {
        if (!visited.containsKey(currentNode))
            visited.put(currentNode, new ArrayList<>());
        visited.get(currentNode).add(outgoingDirection);
    }

    private void principleNextNode(Marker node, Set<Marker> checkedNodes, Map<Marker, Integer> distances, Map<Marker, Marker> predecessors) {
        node.getConnectedNodes()
                .entrySet()
                .stream()
                .filter(e -> !checkedNodes.contains(e.getKey()))
                .forEach(e -> {
                    Marker nextNode = e.getKey();
                    ArrayList<Way> waysList = e.getValue();

                    checkedNodes.add(nextNode);
                    AtomicInteger distance = new AtomicInteger(distances.get(node));
                    waysList.forEach(way -> distance.addAndGet(way.getDistance()));
                    if (!distances.containsKey(nextNode) || (distances.containsKey(nextNode) && distances.get(node) > distance.get())) {
                        distances.put(nextNode, distance.get());
                        predecessors.put(nextNode, node);
                    }
                    principleNextNode(nextNode, checkedNodes, distances, predecessors);
                });
    }

    private void findWaves(ArrayList<Marker> markers) {
        ArrayList<Marker> crosses = new ArrayList<>();
        markers.forEach(marker -> {
            Hierarchy hierarchyEntry = new Hierarchy();
            hierarchyEntry.addStartpoint(marker);

            String direction = marker
                    .getCross()
                    .entrySet()
                    .stream()
                    .filter(e -> !visited.containsKey(marker) || !visited.get(marker).contains(e.getKey()))
                    .filter(e -> e.getValue() != null)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            Marker endpoint = nextNode(marker, direction, crosses, hierarchyEntry);

            if (endpoint != null) {
                hierarchyEntry.addEndpoint(endpoint);
                hierarchies.add(hierarchyEntry);
            }
        });
        if (crosses.size() > 0)
            findWaves(crosses);
    }

    private Marker nextNode(Marker currentNode, String outgoingDirection, ArrayList<Marker> crosses, Hierarchy hierarchyEntry) {
        int ct;
        ct = (int) currentNode
                .getCross()
                .entrySet()
                .stream()
                .filter(e -> e.getValue() != null)
                .filter(e -> !visited.containsKey(currentNode) || !visited.get(currentNode).contains(e.getKey()))
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

        markVisited(currentNode, outgoingDirection);

        if (nextNode != null) {
            currentNode
                    .getConnectedNodes()
                    .get(nextNode)
                    .forEach(way -> hierarchyEntry.addWay(way));
        }

        if (nextDirection == null)
            return nextNode;

        return nextNode(nextNode, nextDirection, crosses, hierarchyEntry);
    }

    private String findNextDirection(Marker marker, String incomingDirection) {
        if (incomingDirection == null)
            return null;

        String[] possibleDirections = OPPOSITE.DIRECTIONS.get(incomingDirection);

        if ((!visited.containsKey(marker) || !visited.get(marker).contains(possibleDirections[0])) &&
                marker.getCross().containsKey(possibleDirections[0]) && marker.getCross().get(possibleDirections[0]) != null
                )
            return possibleDirections[0];
        else {
            if ((!visited.containsKey(marker) || !visited.get(marker).contains(possibleDirections[1])) &&
                    marker.getCross().containsKey(possibleDirections[1]) && marker.getCross().get(possibleDirections[1]) != null) {
                // better opposite dir
                if (marker.getCross().get(OPPOSITE.DIRECTIONS.get(possibleDirections[1])) == null)
                    return possibleDirections[1];
            }

            if ((!visited.containsKey(marker) || !visited.get(marker).contains(possibleDirections[2])) &&
                    marker.getCross().containsKey(possibleDirections[2]) && marker.getCross().get(possibleDirections[2]) != null) {
                // better opposite dir
                if (marker.getCross().get(OPPOSITE.DIRECTIONS.get(possibleDirections[2])) == null)
                    return possibleDirections[2];
            }
        }
        return null;
    }

    public void setUpPrinciple(Marker markerA, Marker markerB, boolean twoWay) {
        principles.add(new Marker[]{markerA, markerB});
        if (twoWay)
            principles.add(new Marker[]{markerB, markerA});
    }
}
