package pl.bodolsog.GreenWaveFX.engine;


import pl.bodolsog.GreenWaveFX.model.Marker;
import pl.bodolsog.GreenWaveFX.model.Markers;
import pl.bodolsog.GreenWaveFX.model.Way;
import pl.bodolsog.GreenWaveFX.model.Ways;
import pl.bodolsog.GreenWaveFX.staticVar.NODE_TYPE;
import pl.bodolsog.GreenWaveFX.staticVar.OPPOSITE;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HierarchyBuilder {
    ArrayList<Marker> startpoints;
    private Ways ways;
    private Markers markers;
    private HashMap<Integer, Way> waysList;
    private HashMap<Integer, Marker> markersList;
    private ArrayList<Wave> hierarchies;
    private Map<Marker, ArrayList<String>> visited;
    private ArrayList<Marker[]> principles;


    /**
     * Constructor.
     *
     * @param ways    Reference to Ways instance.
     * @param markers Reference to Markers instance.
     */
    public HierarchyBuilder(Ways ways, Markers markers) {
        hierarchies = new ArrayList<>();
        visited = new HashMap<>();
        principles = new ArrayList<>();

        this.ways = ways;
        this.markers = markers;
    }


    /**
     * Build a hierarchy of ways.
     *
     */
    public void buildHierarchy() {
        waysList = new HashMap<>(ways.getAllWays());
        markersList = new HashMap<>(markers.getAllMarkers());
        startpoints = new ArrayList<>(markers.getStartpoints());

        // First user given principles.
        if (principles.size() > 0)
            principles.forEach(this::findPrinciple);

        // Second all ways started from startpoints.
        findWaves(startpoints);

        // Check crossroads with not used ways.
        ArrayList<Marker> uncheckedCrosses = findUncheckedCrosses();

        // Third all not used ways from crossroads.
        findWaves(uncheckedCrosses);
    }


    /**
     * Slightly modfied Dijkstra alghoritm for search user given principle connection.
     *
     * If it is possible should find shortest connection between two startpoints type Markers..
     *
     * @param markersArr Two Markers in array - startpoint and endpoint node for search.
     */
    private void findPrinciple(Marker[] markersArr) {
        Marker startNode = markersArr[0];
        Marker endNode = markersArr[1];
        Set<Marker> checkedNodes = new HashSet<>();
        Map<Marker, Integer> distances = new HashMap<>();
        Map<Marker, Marker> predecessors = new HashMap<>();

        checkedNodes.add(startNode);
        // From all connected nodes (next node - should be only one in startpoint type):
        startNode.getConnectedNodes()
                .forEach((nextNode, waysList) -> {
                    // Calculate distance to node.
                    AtomicInteger distance = new AtomicInteger(0);
                    waysList.forEach(way -> distance.addAndGet(way.getDistance()));

                    // Add distance to node to map.
                    distances.put(nextNode, distance.get());

                    // Add predecessor for node to map.
                    predecessors.put(nextNode, startNode);

                    // Calculate next ways DFS.
                    principleNextNode(nextNode, checkedNodes, distances, predecessors);
                });

        // Create wave and sets startpoint, endpoint and distance.
        Wave wave = new Wave();
        wave.setStartpoint(startNode);
        wave.setEndpoint(endNode);

        // As finish set principle way visited and Wave parts into.
        setPrincipleVisited(endNode, predecessors, startNode, wave);

        // Add wave to hierarchies list.
        hierarchies.add(wave);
    }


    /**
     * Recursive part of adapted Dijkstra alghoritm for all nodes except startpoint.
     *
     * @param node Current node (Marker).
     * @param checkedNodes List of checked nodes.
     * @param distances Map of shortest distances from startpoint to node.
     * @param predecessors Map of predcessors of node (updated when found shortest way).
     */
    private void principleNextNode(Marker node, Set<Marker> checkedNodes, Map<Marker, Integer> distances, Map<Marker, Marker> predecessors) {
        // Add to checked nodes.
        checkedNodes.add(node);

        // For all connected nodes:
        node.getConnectedNodes()
                .entrySet()
                .stream()
                .forEach(e -> {
                    // For readability.
                    Marker nextNode = e.getKey();
                    ArrayList<Way> waysList = e.getValue();

                    // Calculate distance to node.
                    AtomicInteger distance = new AtomicInteger(distances.get(node));
                    waysList.forEach(way -> distance.addAndGet(way.getDistance()));

                    // If new distance to nextNode from this node is shortest as from another - set ancessors of
                    // nextNode to unchecked.
                    if (distances.containsKey(nextNode) && distances.get(node) > distance.get())
                        setAncessorsUnchecked(nextNode, checkedNodes, distances, predecessors);

                    // Add new distance, predecessors and find next node only if next node isn't mark as checked.
                    if (!checkedNodes.contains(nextNode)) {
                        // Add new distance.
                        distances.put(nextNode, distance.get());

                        // Add predsc
                        predecessors.put(nextNode, node);

                        // Check next node.
                        principleNextNode(nextNode, checkedNodes, distances, predecessors);
                    }
                });
    }


    /**
     * Mark ancessors of node unchecked.
     *
     * @param node         Current node.
     * @param checkedNodes List of checked nodes.
     * @param distances    Map of distances.
     * @param predecessors Map of predecessors.
     */
    private void setAncessorsUnchecked(Marker node, Set<Marker> checkedNodes, Map<Marker, Integer> distances, Map<Marker, Marker> predecessors) {
        predecessors.entrySet()
                .stream()
                .filter(e -> e.getValue() == node)
                .forEach(e -> {
                    // For readability.
                    Marker ancessor = e.getKey();

                    // Remove checked flag.
                    checkedNodes.remove(ancessor);

                    // Clear distances.
                    distances.remove(ancessor);

                    // Mark ancessors of node unchecked.
                    setAncessorsUnchecked(ancessor, checkedNodes, distances, predecessors);
                });
    }


    /**
     * For way found by Dijkstra alghoritm add Ways from Wave to visited. That prevents another Waves use same Ways.
     *
     * @param node         Current node (Marker).
     * @param predecessors List of predcessors.
     * @param stopNode     Start node (startpoint) in which iteration should be stopped.
     * @param wave         Wave instance.
     */
    private void setPrincipleVisited(Marker node, Map<Marker, Marker> predecessors, Marker stopNode, Wave wave) {
        // Add way to Wave ways list. Iteration starts from end, so new items are added to first list position.
        ArrayList<Way> ways = predecessors.get(node).getConnectedNodes().get(node);
        ways.forEach(way -> {
            wave.addWayReverse(way);
            wave.addCross(way.getBeginMarker());
        });

        // Mark way as visited.
        String direction = node.getConnectedNodes().get(predecessors.get(node)).get(0).getBeginDirection();
        markVisited(node, direction);

        // Check next predcessor.
        if (predecessors.get(node) != stopNode)
            setPrincipleVisited(predecessors.get(node), predecessors, stopNode, wave);
    }


    /**
     * Check all crosses for more unchecked directions.
     *
     * @return List of unchecked crosses.
     */
    private ArrayList<Marker> findUncheckedCrosses() {
        ArrayList<Marker> crosses = new ArrayList<>();

        markersList.forEach((integer, marker) -> {
            // Only if marker isn't transition.
            if (marker.getNodeType() != NODE_TYPE.TRANSITION) {
                int uvisitedWaysCount = (int) marker.getCross().entrySet()
                        .stream()
                        // Filter out empty.
                        .filter(e -> e.getValue() != null)
                        // Filter out visited.
                        .filter(e -> !visited.containsKey(marker) || !visited.get(marker).contains(e.getKey()))
                        .count();

                // If found - add to array.
                if (uvisitedWaysCount > 0)
                    crosses.add(marker);
            }
        });

        return crosses;
    }

    /**
     * Search ways starts from startpoint type Marker and always on crossroad try to get straight (or if not possible
     * - half-straight).
     *
     * @param markers List of startpoints Markers.
     */
    private void findWaves(ArrayList<Marker> markers) {
        // List to collect startpoints to check from crossroads if another way ends here.
        ArrayList<Marker> crosses = new ArrayList<>();

        markers.forEach(marker -> {
            Wave wave = new Wave();
            wave.setStartpoint(marker);

            // Detect next direction. In startpoint should be one.
            String direction = marker
                    .getCross()
                    .entrySet()
                    .stream()
                    // Exclude empty directions.
                    .filter(e -> e.getValue() != null)
                    // Exclude used directions for another way.
                    .filter(e -> !visited.containsKey(marker) || !visited.get(marker).contains(e.getKey()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            // Calculate endpoint.
            Marker endpoint = nextNode(marker, direction, crosses, wave);
            if (endpoint != null) {
                wave.setEndpoint(endpoint);

                // Add wave to hierarchy list.
                hierarchies.add(wave);
            }
        });

        // If unchecked crosses exists, then find waves them.
        if (crosses.size() > 0)
            findWaves(crosses);
    }


    /**
     * Find next node till end.
     *
     * @param currentNode       Current node (Marker).
     * @param outgoingDirection Outgoing direction from current node.
     * @param crosses           List of crosses to check.
     * @param wave              Wave instance.
     * @return Endnode (Marker).
     */
    private Marker nextNode(Marker currentNode, String outgoingDirection, ArrayList<Marker> crosses, Wave wave) {
        // Count unchecked directions in cross.
        int ct = (int) currentNode
                .getCross()
                .entrySet()
                .stream()
                .filter(e -> e.getValue() != null)
                .filter(e -> !visited.containsKey(currentNode) || !visited.get(currentNode).contains(e.getKey()))
                .count();
        // If more than two - add to crosses.
        if (ct > 1)
            crosses.add(currentNode);

        // Flag used ways as visited.
        markVisited(currentNode, outgoingDirection);

        // Get ways list leading to next node.
        ArrayList<Way> nextWays = currentNode
                .getConnectedNodes()
                .entrySet()
                .stream()
                // Way that begins in outgoingDirection of current node.
                .filter(e -> e.getValue().get(0).getBeginDirection() == outgoingDirection)
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        Way lastWay;
        Marker nextNode;
        String nextNodeIncomingDirection;
        String nextDirection;

        if (nextWays != null) {
            lastWay = nextWays.get(nextWays.size() - 1);

            // Get next node and direction.
            nextNode = lastWay.getEndMarker();
            nextNodeIncomingDirection = lastWay.getEndDirection();

            // Find next direction.
            nextDirection = findNextDirection(nextNode, nextNodeIncomingDirection);
        } else {
            nextNode = null;
            nextDirection = null;
        }

        // Add way and cross to Wave.
        if (nextNode != null) {
            currentNode
                    .getConnectedNodes()
                    .get(nextNode)
                    .forEach(way -> {
                        wave.addWay(way);
                        wave.addCross(way.getBeginMarker());
                    });
        }

        // If that is end of way, return next (last) node.
        if (nextDirection == null)
            return nextNode;

        // Find next node.
        return nextNode(nextNode, nextDirection, crosses, wave);
    }


    /**
     * Return next possible direction. Try to go straight or half-straignt.
     *
     * @param marker Current marker.
     * @param incomingDirection Incoming direction.
     * @return Outgoing direction.
     */
    private String findNextDirection(Marker marker, String incomingDirection) {
        if (incomingDirection == null)
            return null;

        // Get list of possible directions (straight, and 2x half-straigh).
        String[] possibleDirections = OPPOSITE.DIRECTIONS.get(incomingDirection);

        // Straight. Return if is possible and isn't visited
        if (marker.getCross().containsKey(possibleDirections[0]) && marker.getCross().get(possibleDirections[0]) != null &&
                (!visited.containsKey(marker) || !visited.get(marker).contains(possibleDirections[0])))
            return possibleDirections[0];
        else {
            // Half-straight. Return if is possible and isn't visited
            if (marker.getCross().containsKey(possibleDirections[1]) && marker.getCross().get(possibleDirections[1]) != null &&
                    (!visited.containsKey(marker) || !visited.get(marker).contains(possibleDirections[1]))) {
                // better opposite dir
                if (marker.getCross().get(OPPOSITE.DIRECTIONS.get(possibleDirections[1])) == null)
                    return possibleDirections[1];
            }

            // Half-straight. Return if is possible and isn't visited
            if (marker.getCross().containsKey(possibleDirections[2]) && marker.getCross().get(possibleDirections[2]) != null &&
                    (!visited.containsKey(marker) || !visited.get(marker).contains(possibleDirections[2]))) {
                // better opposite dir
                if (marker.getCross().get(OPPOSITE.DIRECTIONS.get(possibleDirections[2])) == null)
                    return possibleDirections[2];
            }
        }

        return null;
    }


    /**
     * Set direction as used.
     *
     * @param currentNode       Current node.
     * @param outgoingDirection Outgoing direction.
     */
    private void markVisited(Marker currentNode, String outgoingDirection) {
        if (!visited.containsKey(currentNode))
            visited.put(currentNode, new ArrayList<>());
        visited.get(currentNode).add(outgoingDirection);
    }


    /**\
     * Set up principle connection.
     *
     * @param markerA Startpoint node.
     * @param markerB Endpoint node.
     * @param twoWay False if way should be only one way and no back.
     */
    public void setUpPrinciple(Marker markerA, Marker markerB, boolean twoWay) {
        principles.add(new Marker[]{markerA, markerB});
        if (twoWay)
            principles.add(new Marker[]{markerB, markerA});
    }


    /**
     * Getter for Wave from hierarchy list.
     *
     * @param i Index.
     * @return Wave instance.
     */
    public Wave getHierarchyEntry(int i) {
        return hierarchies.get(i);
    }

    /**
     * Return hierarchy array.
     *
     * @return Hierarchy array of Waves.
     */
    public ArrayList<Wave> getHierarchies() {
        return hierarchies;
    }


    /**
     * Return count of Waves in hierarchies array.
     *
     * @return Size of hierarchies array.
     */
    public int size() {
        return hierarchies.size();
    }

}
