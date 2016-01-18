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

@RunWith(HierarchicalContextRunner.class)
public class HierarchyBuilderTest {
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

    private ArrayList<Marker> createBoundOfMarkers(int count) {
        ArrayList<Marker> bound = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            bound.add(createMarker());
        }
        return bound;
    }

    private void waysInlineShortcut(ArrayList<Marker> ms, int start, int end, String direction) {
        String previous, next;
        if (direction == "ns") {
            previous = DIRECTION.N;
            next = DIRECTION.S;
        } else if (direction == "we") {
            previous = DIRECTION.W;
            next = DIRECTION.E;
        } else
            return;

        for (int i = start; i < end; i++) {
            ways.addWay(ms.get(i), previous, ms.get(i + 1), next, true, "", i * 25);
        }
    }

    private Marker createMarker() {
        markers.addMarker(new JSObjectAdapter());
        Marker marker = markers.getLastMarker();
        marker.setCrossDirections(crossDirections);
        return marker;
    }

    @Before
    public void setUp() {
        ways = new Ways();
        markers = new Markers(ways);
    }

    public class GivenOneWayState {
        @Before
        public void setUp() {
            markersBound = createBoundOfMarkers(4);
            // 0-1-2-3
            waysInlineShortcut(markersBound, 0, 3, "we");
            hierarchyBuilder = new HierarchyBuilder(ways.getAllWays(), markers.getAllMarkers(), markers.getStartpoints());

        }

        public class WhenGotNoPrinciples {
            @Before
            public void setUp() {
                hierarchyBuilder.buildHierarchy();
            }

            @Test
            public void thenFindOneConnection() {
                assertEquals("First connection of hierarchy starts from 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(0).getStartpoint());
                assertEquals("First connection of hierarchy goes to 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(0).getEndpoint());
            }

            @Test
            public void thenFindSecondConnection() {
                assertEquals("Second connection of hierarchy starts from 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(1).getStartpoint());
                assertEquals("Second connection of hierarchy goes to 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(1).getEndpoint());
            }
        }
    }

}