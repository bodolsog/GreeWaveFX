package pl.bodolsog.GreenWaveFX.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.bodolsog.GreenWaveFX.staticVar.NODE_TYPE;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class MarkerTest {

    private final ArrayList<String> crossDirections = new ArrayList<String>() {{
        add("north");
        add("south");
        add("east");
        add("west");
    }};
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Markers markers;
    private Ways ways;

    @Before
    public void setUp() {
        markers = new Markers();
        ways = new Ways();
        markers.addMarker(new JSObjectAdapter());
        markers.getMarker(0).setCrossDirections(crossDirections);
        markers.addMarker(new JSObjectAdapter());
        markers.getMarker(1).setCrossDirections(crossDirections);
    }

    @Test
    public void whenCallSetCrossDirectionsWithWaluesThenAllDirectionsAreIn() {
        final ArrayList<String> crossAnotherDirections = new ArrayList<String>() {{
            add("north");
            add("south");
            add("northeast");
            add("west");
        }};
        markers.addMarker(new JSObjectAdapter());
        markers.getMarker(2).setCrossDirections(crossAnotherDirections);
        Marker markerA = markers.getMarker(2);

        ArrayList<String> dir = markerA.getCrossDirections();
        assertTrue("Should contain north", dir.contains("north"));
        assertTrue("Should contain south", dir.contains("south"));
        assertTrue("Should contain northeast", dir.contains("northeast"));
        assertTrue("Should contain west", dir.contains("west"));
        assertFalse("Shouldn't contain east", dir.contains("east"));
    }

    @Test
    public void whenCallTwiceSetCrossDirectionsWithDifferentWaluesThenOnlyNewValuesAreInAndWaysDeleted() {
        Marker markerA = markers.getMarker(0);
        markerA.setCrossDirections(crossDirections);

        Marker markerB = markers.getMarker(1);

        String response = "";
        int distance = 100;

        Way way1 = new Way(ways, 0, markerA, "north", markerB, "south", response, distance);
        Way way2 = new Way(ways, 1, markerA, "north", markerB, "south", response, distance);

        markerA.setWay(way1, "north");
        markerA.setWay(way2, "south");

        ArrayList<String> arrr = new ArrayList<String>() {{
            add("south");
            add("west");
        }};
        markerA.setCrossDirections(arrr);

        assertEquals("South should be a Way2", way2, markerA.getCrossDirection("south"));
        assertNull("West should be null.", markerA.getCrossDirection("west"));

        thrown.expect(NullPointerException.class);
        assertNull("North should not exists.", markerA.getCrossDirection("north"));
    }

    @Test
    public void whenOnlyTwoMarkersAreConnectedThenBothAreStartpoints() {
        Marker markerA = markers.getMarker(0);
        Marker markerB = markers.getMarker(1);

        ways.addWay(markerA, "east", markerB, "west", true, "", 100);

        assertEquals("EndNodes size should be 2", 2, markers.endNodesSize());
        assertEquals("Node should be type Startpoint", NODE_TYPE.STARTPOINT, markers.getNode(0).getNodeType());
        assertEquals("Node should be type Startpoint", NODE_TYPE.STARTPOINT, markers.getNode(1).getNodeType());
    }

    @Test
    public void whenTwoMarkersAreAddedAndConnectedThenTheirNodesAreConnected() {
        Marker markerA = markers.getMarker(0);
        Marker markerB = markers.getMarker(1);

        ways.addWay(markerA, "east", markerB, "west", true, "", 100);

        assertTrue("Node from MarkerA is connected to markerB", markerA.getConnectedNodes().containsKey(markerB));
        assertTrue("Node from MarkerB is connected to markerA", markerB.getConnectedNodes().containsKey(markerA));
    }

    @Test
    public void whenTwoMarkersAreAddedAndConnectedThenWaysAreInHisNodeLists() {
        Marker markerA = markers.getMarker(0);
        Marker markerB = markers.getMarker(1);

        ways.addWay(markerA, "east", markerB, "west", true, "", 100);
        Way wayA = ways.getWay(0);
        Way wayB = ways.getWay(1);

        assertTrue("Node from MarkerA should have wayA", markerA.getConnectedNodes().get(markerB).contains(wayA));
        assertTrue("Node from MarkerB should have wayB", markerB.getConnectedNodes().get(markerA).contains(wayB));
    }

}