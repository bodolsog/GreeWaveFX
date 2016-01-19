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
        String outgoing, ingoing;
        if (direction == "ns") {
            outgoing = DIRECTION.S;
            ingoing = DIRECTION.N;
        } else if (direction == "we") {
            outgoing = DIRECTION.E;
            ingoing = DIRECTION.W;
        } else
            return;

        for (int i = start; i < end; i++) {
            ways.addWay(ms.get(i), outgoing, ms.get(i + 1), ingoing, true, "", (i + 1) * 100);
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
            public void thenFindFirstConnection() {
                assertEquals("First connection of hierarchy starts from 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(0).getStartpoint());
                assertEquals("First connection of hierarchy goes to 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(0).getEndpoint());
            }

            @Test
            public void thenFindSecondConnection() {
                assertEquals("Second connection of hierarchy starts from 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(1).getStartpoint());
                assertEquals("Second connection of hierarchy goes to 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(1).getEndpoint());
            }

            @Test
            public void thenFirstAndSecondConnectionDistanceFit() {
                assertEquals("First connection distance is 600.", 600, hierarchyBuilder.getHierarchyEntry(0).getDistance());
                assertEquals("Second connection distance is 600.", 600, hierarchyBuilder.getHierarchyEntry(1).getDistance());
            }
        }
    }

    public class GivenThreeWayCrossState {
        @Before
        public void setUp() {
            markersBound = createBoundOfMarkers(7);
            // 0-1-2-3
            //   4
            //   5-6
            waysInlineShortcut(markersBound, 0, 3, "we");
            ways.addWay(
                    markersBound.get(1), DIRECTION.S,
                    markersBound.get(4), DIRECTION.N,
                    true, "", 125);
            ways.addWay(
                    markersBound.get(4), DIRECTION.S,
                    markersBound.get(5), DIRECTION.N,
                    true, "", 125);
            ways.addWay(
                    markersBound.get(5), DIRECTION.E,
                    markersBound.get(6), DIRECTION.W,
                    true, "", 125);

            hierarchyBuilder = new HierarchyBuilder(ways.getAllWays(), markers.getAllMarkers(), markers.getStartpoints());
        }

        public class WhenGotNoPrinciples {
            @Before
            public void setUp() {
                hierarchyBuilder.buildHierarchy();
            }

            @Test
            public void thenFindFourWays() {
                assertEquals("Ways in hierarchy should be exactly 4.", 4, hierarchyBuilder.size());
            }

            @Test
            public void thenFindFirstConnection() {
                assertEquals("First connection of hierarchy starts from 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(0).getStartpoint());
                assertEquals("First connection of hierarchy goes to 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(0).getEndpoint());
            }

            @Test
            public void thenFindSecondConnection() {
                assertEquals("Second connection of hierarchy starts from 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(1).getStartpoint());
                assertEquals("Second connection of hierarchy goes to 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(1).getEndpoint());
            }

            @Test
            public void thenFirstAndSecondConnectionDistanceFit() {
                assertEquals("First connection distance is 600.", 600, hierarchyBuilder.getHierarchyEntry(0).getDistance());
                assertEquals("Second connection distance is 600.", 600, hierarchyBuilder.getHierarchyEntry(1).getDistance());
            }

            @Test
            public void thenFindThirdConnection() {
                assertEquals("Third connection of hierarchy starts from 6.", markersBound.get(6), hierarchyBuilder.getHierarchyEntry(2).getStartpoint());
                assertEquals("Third connection of hierarchy goes to 1.", markersBound.get(1), hierarchyBuilder.getHierarchyEntry(2).getEndpoint());
            }

            @Test
            public void thenFindFourthConnection() {
                assertEquals("Fourth connection of hierarchy starts from 1.", markersBound.get(1), hierarchyBuilder.getHierarchyEntry(3).getStartpoint());
                assertEquals("Fourth connection of hierarchy goes to 6.", markersBound.get(6), hierarchyBuilder.getHierarchyEntry(3).getEndpoint());
            }

            @Test
            public void thenThirdAndFourthConnectionDistanceFit() {
                assertEquals("Third connection distance is 375.", 375, hierarchyBuilder.getHierarchyEntry(2).getDistance());
                assertEquals("Fourth connection distance is 375.", 375, hierarchyBuilder.getHierarchyEntry(3).getDistance());
            }

        }

        public class WhenGotPrincipleFromZeroToSix {
            @Before
            public void setUp() {
                hierarchyBuilder.setUpPrinciple(markersBound.get(0), markersBound.get(6), true);
                hierarchyBuilder.buildHierarchy();
            }

            @Test
            public void thenFindFourWays() {
                assertEquals("Ways in hierarchy should be exactly 4.", 4, hierarchyBuilder.size());
            }

            @Test
            public void thenFirstConnectionIsFromPrinciple() {
                assertEquals("First connection of hierarchy starts from 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(0).getStartpoint());
                assertEquals("First connection of hierarchy goes to 6.", markersBound.get(6), hierarchyBuilder.getHierarchyEntry(0).getEndpoint());
            }

            @Test
            public void thenSecondConnectionIsFromPrinciple() {
                assertEquals("Second connection of hierarchy starts from 6.", markersBound.get(6), hierarchyBuilder.getHierarchyEntry(1).getStartpoint());
                assertEquals("Second connection of hierarchy goes to 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(1).getEndpoint());
            }

            @Test
            public void thenFindThirdConnection() {
                assertEquals("Third connection of hierarchy starts from 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(2).getStartpoint());
                assertEquals("Third connection of hierarchy goes to 1.", markersBound.get(1), hierarchyBuilder.getHierarchyEntry(2).getEndpoint());
            }

            @Test
            public void thenFindFourthdConnection() {
                assertEquals("Fourth connection of hierarchy starts from 1.", markersBound.get(1), hierarchyBuilder.getHierarchyEntry(3).getStartpoint());
                assertEquals("Fourth connection of hierarchy goes to 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(3).getEndpoint());
            }
        }
    }

    public class GivenFourWayCrossState {
        @Before
        public void setUp() {
            markersBound = createBoundOfMarkers(7);
            //   6
            // 0-1-2-3
            //   4
            //   5
            waysInlineShortcut(markersBound, 0, 3, "we");
            ways.addWay(
                    markersBound.get(1), DIRECTION.S,
                    markersBound.get(4), DIRECTION.N,
                    true, "", 125);
            ways.addWay(
                    markersBound.get(4), DIRECTION.S,
                    markersBound.get(5), DIRECTION.N,
                    true, "", 125);
            ways.addWay(
                    markersBound.get(1), DIRECTION.N,
                    markersBound.get(6), DIRECTION.S,
                    true, "", 125);

            hierarchyBuilder = new HierarchyBuilder(ways.getAllWays(), markers.getAllMarkers(), markers.getStartpoints());
        }

        public class WhenGotNoPrinciples {
            @Before
            public void setUp() {
                hierarchyBuilder.buildHierarchy();
            }

            @Test
            public void thenFindFourWays() {
                assertEquals("Ways in hierarchy should be exactly 4.", 4, hierarchyBuilder.size());
            }

            @Test
            public void thenFindFirstConnection() {
                assertEquals("First connection of hierarchy starts from 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(0).getStartpoint());
                assertEquals("First connection of hierarchy goes to 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(0).getEndpoint());
            }

            @Test
            public void thenFindSecondConnection() {
                assertEquals("Second connection of hierarchy starts from 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(1).getStartpoint());
                assertEquals("Second connection of hierarchy goes to 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(1).getEndpoint());
            }


            @Test
            public void thenFirstAndSecondConnectionDistanceFit() {
                assertEquals("First connection distance is 600.", 600, hierarchyBuilder.getHierarchyEntry(0).getDistance());
                assertEquals("Second connection distance is 600.", 600, hierarchyBuilder.getHierarchyEntry(1).getDistance());
            }

            @Test
            public void thenFindThirdConnection() {
                assertEquals("Third connection of hierarchy starts from 5.", markersBound.get(5), hierarchyBuilder.getHierarchyEntry(2).getStartpoint());
                assertEquals("Third connection of hierarchy goes to 6.", markersBound.get(6), hierarchyBuilder.getHierarchyEntry(2).getEndpoint());
            }

            @Test
            public void thenFindFourthConnection() {
                assertEquals("Fourth connection of hierarchy starts from 6.", markersBound.get(6), hierarchyBuilder.getHierarchyEntry(3).getStartpoint());
                assertEquals("Fourth connection of hierarchy goes to 5.", markersBound.get(5), hierarchyBuilder.getHierarchyEntry(3).getEndpoint());
            }

            @Test
            public void thenThirdAndFourthConnectionDistanceFit() {
                assertEquals("Third connection distance is 375.", 375, hierarchyBuilder.getHierarchyEntry(2).getDistance());
                assertEquals("Fourth connection distance is 375.", 375, hierarchyBuilder.getHierarchyEntry(3).getDistance());
            }
        }
    }

    public class GivenTwoThreeWayCrossState {
        @Before
        public void setUp() {
            markersBound = createBoundOfMarkers(10);
            // 0-1-2-3
            //     8
            //     9
            // 4-5-6-7
            waysInlineShortcut(markersBound, 0, 3, "we");
            waysInlineShortcut(markersBound, 4, 7, "we");
            waysInlineShortcut(markersBound, 8, 9, "ns");

            ways.addWay(
                    markersBound.get(2), DIRECTION.S,
                    markersBound.get(8), DIRECTION.N,
                    true, "", 125);
            ways.addWay(
                    markersBound.get(9), DIRECTION.S,
                    markersBound.get(6), DIRECTION.N,
                    true, "", 125);

            hierarchyBuilder = new HierarchyBuilder(ways.getAllWays(), markers.getAllMarkers(), markers.getStartpoints());
        }

        public class WhenGotNoPrinciples {
            @Before
            public void setUp() {
                hierarchyBuilder.buildHierarchy();
            }

            @Test
            public void thenFindSixWays() {
                assertEquals("Ways in hierarchy should be exactly 6.", 6, hierarchyBuilder.size());
            }

            @Test
            public void thenFindFirstConnection() {
                assertEquals("First connection of hierarchy starts from 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(0).getStartpoint());
                assertEquals("First connection of hierarchy goes to 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(0).getEndpoint());
            }

            @Test
            public void thenFindSecondConnection() {
                assertEquals("Second connection of hierarchy starts from 3.", markersBound.get(3), hierarchyBuilder.getHierarchyEntry(1).getStartpoint());
                assertEquals("Second connection of hierarchy goes to 0.", markersBound.get(0), hierarchyBuilder.getHierarchyEntry(1).getEndpoint());
            }

            @Test
            public void thenFirstAndSecondConnectionDistanceFit() {
                assertEquals("First connection distance is 600.", 600, hierarchyBuilder.getHierarchyEntry(0).getDistance());
                assertEquals("Second connection distance is 600.", 600, hierarchyBuilder.getHierarchyEntry(1).getDistance());
            }

            @Test
            public void thenFindThirdConnection() {
                assertEquals("Third connection of hierarchy starts from 4.", markersBound.get(4), hierarchyBuilder.getHierarchyEntry(2).getStartpoint());
                assertEquals("Third connection of hierarchy goes to 7.", markersBound.get(7), hierarchyBuilder.getHierarchyEntry(2).getEndpoint());
            }

            @Test
            public void thenFindFourthConnection() {
                assertEquals("Fourth connection of hierarchy starts from 7.", markersBound.get(7), hierarchyBuilder.getHierarchyEntry(3).getStartpoint());
                assertEquals("Fourth connection of hierarchy goes to 4.", markersBound.get(4), hierarchyBuilder.getHierarchyEntry(3).getEndpoint());
            }

            @Test
            public void thenThirdAndFourthConnectionDistanceFit() {
                assertEquals("Third connection distance is 1800.", 1800, hierarchyBuilder.getHierarchyEntry(2).getDistance());
                assertEquals("Fourth connection distance is 1800.", 1800, hierarchyBuilder.getHierarchyEntry(3).getDistance());
            }

            @Test
            public void thenFindFifthConnection() {
                assertEquals("Fifth connection of hierarchy starts from 2.", markersBound.get(2), hierarchyBuilder.getHierarchyEntry(4).getStartpoint());
                assertEquals("Fifth connection of hierarchy goes to 6.", markersBound.get(6), hierarchyBuilder.getHierarchyEntry(4).getEndpoint());
            }

            @Test
            public void thenFindsixthConnection() {
                assertEquals("Sixth connection of hierarchy starts from 6.", markersBound.get(6), hierarchyBuilder.getHierarchyEntry(5).getStartpoint());
                assertEquals("Sixth connection of hierarchy goes to 2.", markersBound.get(2), hierarchyBuilder.getHierarchyEntry(5).getEndpoint());
            }

            @Test
            public void thenFifthAndSixthConnectionDistanceFit() {
                assertEquals("Fifth connection distance is 1150.", 1150, hierarchyBuilder.getHierarchyEntry(4).getDistance());
                assertEquals("Sixth connection distance is 1150.", 1150, hierarchyBuilder.getHierarchyEntry(5).getDistance());
            }
        }
    }


}