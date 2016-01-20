package pl.bodolsog.GreenWaveFX.engine;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.bodolsog.GreenWaveFX.model.JSObjectAdapter;
import pl.bodolsog.GreenWaveFX.model.Marker;
import pl.bodolsog.GreenWaveFX.model.Markers;
import pl.bodolsog.GreenWaveFX.model.Ways;
import pl.bodolsog.GreenWaveFX.staticVar.DIRECTION;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by bodolsog on 20.01.16.
 */

@RunWith(HierarchicalContextRunner.class)
public class PossibleSpeeds {
    private final ArrayList<String> crossDirections = new ArrayList<String>() {{
        add(DIRECTION.N);
        add(DIRECTION.S);
        add(DIRECTION.E);
        add(DIRECTION.W);
    }};
    private HierarchyBuilder hierarchyBuilder;
    private Ways ways;
    private Markers markers;
    private ArrayList<Marker> markersBound;

    /**
     * Helper method for create and configure Marker.
     *
     * @return Instance of Marker.
     */
    private Marker createMarker() {
        // Create marker.
        markers.addMarker(new JSObjectAdapter());

        // Set domestic north-south-west-east directions.
        Marker marker = markers.getLastMarker();
        marker.setCrossDirections(crossDirections);

        return marker;
    }

    /**
     * Helper method for create markers.
     *
     * @param count The number of Markers to be done.
     * @return List of markers.
     */
    private ArrayList<Marker> createBoundOfMarkers(int count) {
        ArrayList<Marker> bound = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            bound.add(createMarker());
        }
        return bound;
    }

    /**
     * Helper method for create ways connected from Marker number 'start' to Marker number 'end' from markers list. Ways
     * will be connected in one of two directions (to south or to east).
     *
     * @param markersBound List of Markers.
     * @param start        Index of first connected Marker.
     * @param end          Index of last connected Marker.
     * @param direction    One of two: 'ns' for south or 'we' for east direction.
     */
    private void waysInlineShortcut(ArrayList<Marker> markersBound, int start, int end, String direction) {
        // Chose direction.
        String outgoing, ingoing;
        if (direction == "ns") {
            outgoing = DIRECTION.S;
            ingoing = DIRECTION.N;
        } else if (direction == "we") {
            outgoing = DIRECTION.E;
            ingoing = DIRECTION.W;
        } else
            return;

        // Create ways.
        for (int i = start; i < end; i++) {
            ways.addWay(markersBound.get(i), outgoing, markersBound.get(i + 1), ingoing, true, "", (i + 1) * 100);
        }
    }

    @Before
    public void setUp() {
        ways = new Ways();
        markers = new Markers(ways);
        hierarchyBuilder = new HierarchyBuilder(ways, markers);
    }

    public class GivenThreerCrossType {
        @Before
        public void setUp() {
            markersBound = createBoundOfMarkers(5);
        }

        public class WhenConnectAllInlineWithConstantDistance {
            Wave connection;

            @Before
            public void setUp() {
                // 0 -[300m] - 1 -[300m]- 2 -[300m]- 3 -[300m]- 4
                ways.addWay(
                        markersBound.get(0), DIRECTION.E,
                        markersBound.get(1), DIRECTION.W,
                        true, "", 300);
                ways.addWay(
                        markersBound.get(1), DIRECTION.E,
                        markersBound.get(2), DIRECTION.W,
                        true, "", 300);
                ways.addWay(
                        markersBound.get(2), DIRECTION.E,
                        markersBound.get(3), DIRECTION.W,
                        true, "", 300);
                ways.addWay(
                        markersBound.get(3), DIRECTION.E,
                        markersBound.get(4), DIRECTION.W,
                        true, "", 300);

                hierarchyBuilder.buildHierarchy();
                hierarchyBuilder.getHierarchies().forEach(Wave::calculatePossibleSpeeds);
                connection = hierarchyBuilder.getHierarchyEntry(0);
            }

            @Test
            public void thenFirstCrossContainAllPossibleSpeeds() {
                assertEquals("Contain possible speed 50 km/h.", 50, connection.getPossibleSpeeds(markersBound.get(1)).get(0), 0);
                assertEquals("Contain possible speed 45 km/h.", 45, connection.getPossibleSpeeds(markersBound.get(1)).get(1), 0);
                assertEquals("Contain possible speed 40 km/h.", 40, connection.getPossibleSpeeds(markersBound.get(1)).get(2), 0);
                assertEquals("Contain possible speed 35 km/h.", 35, connection.getPossibleSpeeds(markersBound.get(1)).get(3), 0);
            }

            @Test
            public void thenSecondCrossContainAllPossibleSpeeds() {
                assertEquals("Contain possible speed 50 km/h.", 50, connection.getPossibleSpeeds(markersBound.get(2)).get(0), 0);
                assertEquals("Contain possible speed 45 km/h.", 45, connection.getPossibleSpeeds(markersBound.get(2)).get(1), 0);
                assertEquals("Contain possible speed 40 km/h.", 40, connection.getPossibleSpeeds(markersBound.get(2)).get(2), 0);
                assertEquals("Contain possible speed 35 km/h.", 35, connection.getPossibleSpeeds(markersBound.get(2)).get(3), 0);
                assertEquals("Contain possible speed 30 km/h.", 30, connection.getPossibleSpeeds(markersBound.get(2)).get(4), 0);
            }

            @Test
            public void thenThirdCrossContainAllPossibleSpeeds() {
                assertEquals("Contain possible speed 40 km/h.", 40, connection.getPossibleSpeeds(markersBound.get(3)).get(0), 0);
                assertEquals("Contain possible speed 35 km/h.", 35, connection.getPossibleSpeeds(markersBound.get(3)).get(1), 0);
                assertEquals("Contain possible speed 30 km/h.", 30, connection.getPossibleSpeeds(markersBound.get(3)).get(2), 0);
            }
        }

        public class WhenConnectAllInlineWithDifferentDistances {
            Wave connection;

            @Before
            public void setUp() {
                // 0 -[300m] - 1 -[400m]- 2 -[300m]- 3 -[450m]- 4
                ways.addWay(
                        markersBound.get(0), DIRECTION.E,
                        markersBound.get(1), DIRECTION.W,
                        true, "", 300);
                ways.addWay(
                        markersBound.get(1), DIRECTION.E,
                        markersBound.get(2), DIRECTION.W,
                        true, "", 400);
                ways.addWay(
                        markersBound.get(2), DIRECTION.E,
                        markersBound.get(3), DIRECTION.W,
                        true, "", 300);
                ways.addWay(
                        markersBound.get(3), DIRECTION.E,
                        markersBound.get(4), DIRECTION.W,
                        true, "", 450);

                hierarchyBuilder.buildHierarchy();
                hierarchyBuilder.getHierarchies().forEach(Wave::calculatePossibleSpeeds);
                connection = hierarchyBuilder.getHierarchyEntry(0);
            }

            @Test
            public void thenFirstCrossContainAllPossibleSpeeds() {
                assertEquals("Contain possible speed 50 km/h.", 50, connection.getPossibleSpeeds(markersBound.get(1)).get(0), 0);
                assertEquals("Contain possible speed 45 km/h.", 45, connection.getPossibleSpeeds(markersBound.get(1)).get(1), 0);
                assertEquals("Contain possible speed 40 km/h.", 40, connection.getPossibleSpeeds(markersBound.get(1)).get(2), 0);
                assertEquals("Contain possible speed 35 km/h.", 35, connection.getPossibleSpeeds(markersBound.get(1)).get(3), 0);
                assertEquals("Contain possible speed 30 km/h.", 30, connection.getPossibleSpeeds(markersBound.get(1)).get(4), 0);
            }

            @Test
            public void thenSecondCrossContainAllPossibleSpeeds() {
                assertEquals("Contain possible speed 50 km/h.", 50, connection.getPossibleSpeeds(markersBound.get(2)).get(0), 0);
                assertEquals("Contain possible speed 45 km/h.", 45, connection.getPossibleSpeeds(markersBound.get(2)).get(1), 0);
                assertEquals("Contain possible speed 40 km/h.", 40, connection.getPossibleSpeeds(markersBound.get(2)).get(2), 0);
            }

            @Test
            public void thenThirdCrossContainAllPossibleSpeeds() {
                assertEquals("Contain possible speed 30 km/h.", 30, connection.getPossibleSpeeds(markersBound.get(3)).get(0), 0);
            }

        }
    }
}
