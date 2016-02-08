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
import static org.junit.Assert.assertTrue;

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

    public class GivenOneWayState {
        @Before
        public void setUp() {
            markersBound = createBoundOfMarkers(4);
            // 0-1-2-3
            waysInlineShortcut(markersBound, 0, 3, "we"); //ways 0-5
        }

        public class WhenGotNoPrinciples {
            @Before
            public void setUp() {
                hierarchyBuilder.buildHierarchy();
                hierarchyBuilder.getHierarchies().forEach(Wave::calculatePossibleSpeeds);
            }

            public class ThenFirstConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(0);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#0.", markersBound.get(0), connection.getStartpoint());
                    assertEquals("Should ends in Marker#3.", markersBound.get(3), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#0", connection.getCrosses().contains(markersBound.get(0)));
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#2", connection.getCrosses().contains(markersBound.get(2)));
                    assertTrue("Should contain Marker#3", connection.getCrosses().contains(markersBound.get(3)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#0.", connection.getWays().contains(ways.getWay(0)));
                    assertTrue("Should contain way#2.", connection.getWays().contains(ways.getWay(2)));
                    assertTrue("Should contain way#4.", connection.getWays().contains(ways.getWay(4)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 600.", 600, connection.getDistance());
                }
            }

            public class ThenSecondConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(1);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#3.", markersBound.get(3), connection.getStartpoint());
                    assertEquals("Should ends in Marker#0.", markersBound.get(0), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#0", connection.getCrosses().contains(markersBound.get(0)));
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#2", connection.getCrosses().contains(markersBound.get(2)));
                    assertTrue("Should contain Marker#3", connection.getCrosses().contains(markersBound.get(3)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#1.", connection.getWays().contains(ways.getWay(1)));
                    assertTrue("Should contain way#3.", connection.getWays().contains(ways.getWay(3)));
                    assertTrue("Should contain way#5.", connection.getWays().contains(ways.getWay(5)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 600.", 600, connection.getDistance());
                }
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

            hierarchyBuilder = new HierarchyBuilder(ways, markers);
        }

        public class WhenGotNoPrinciples {
            @Before
            public void setUp() {
                hierarchyBuilder.buildHierarchy();
            }

            @Test
            public void thenFindFourConnections() {
                assertEquals("Ways in hierarchy should be exactly 4.", 4, hierarchyBuilder.size());
            }

            public class ThenFirstConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(0);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#0.", markersBound.get(0), connection.getStartpoint());
                    assertEquals("Should ends in Marker#3.", markersBound.get(3), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#0", connection.getCrosses().contains(markersBound.get(0)));
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#2", connection.getCrosses().contains(markersBound.get(2)));
                    assertTrue("Should contain Marker#3", connection.getCrosses().contains(markersBound.get(3)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#0.", connection.getWays().contains(ways.getWay(0)));
                    assertTrue("Should contain way#2.", connection.getWays().contains(ways.getWay(2)));
                    assertTrue("Should contain way#4.", connection.getWays().contains(ways.getWay(4)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 600.", 600, connection.getDistance());
                }
            }

            public class ThenSecondConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(1);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#3.", markersBound.get(3), connection.getStartpoint());
                    assertEquals("Should ends in Marker#0.", markersBound.get(0), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#0", connection.getCrosses().contains(markersBound.get(0)));
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#2", connection.getCrosses().contains(markersBound.get(2)));
                    assertTrue("Should contain Marker#3", connection.getCrosses().contains(markersBound.get(3)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#1.", connection.getWays().contains(ways.getWay(1)));
                    assertTrue("Should contain way#3.", connection.getWays().contains(ways.getWay(3)));
                    assertTrue("Should contain way#5.", connection.getWays().contains(ways.getWay(5)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 600.", 600, connection.getDistance());
                }
            }

            public class ThenThirdConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(2);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#6.", markersBound.get(6), connection.getStartpoint());
                    assertEquals("Should ends in Marker#1.", markersBound.get(1), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#4", connection.getCrosses().contains(markersBound.get(4)));
                    assertTrue("Should contain Marker#5", connection.getCrosses().contains(markersBound.get(5)));
                    assertTrue("Should contain Marker#6", connection.getCrosses().contains(markersBound.get(6)));
                }


                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#7.", connection.getWays().contains(ways.getWay(7)));
                    assertTrue("Should contain way#9.", connection.getWays().contains(ways.getWay(9)));
                    assertTrue("Should contain way#11.", connection.getWays().contains(ways.getWay(11)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 375.", 375, connection.getDistance());
                }
            }

            public class ThenFourthConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(3);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#1.", markersBound.get(1), connection.getStartpoint());
                    assertEquals("Should ends in Marker#6.", markersBound.get(6), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#4", connection.getCrosses().contains(markersBound.get(4)));
                    assertTrue("Should contain Marker#5", connection.getCrosses().contains(markersBound.get(5)));
                    assertTrue("Should contain Marker#6", connection.getCrosses().contains(markersBound.get(6)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#6.", connection.getWays().contains(ways.getWay(6)));
                    assertTrue("Should contain way#8.", connection.getWays().contains(ways.getWay(8)));
                    assertTrue("Should contain way#10.", connection.getWays().contains(ways.getWay(10)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 375.", 375, connection.getDistance());
                }
            }
        }

        public class WhenGotPrincipleFromZeroToSix {
            @Before
            public void setUp() {
                hierarchyBuilder.setUpPrinciple(markersBound.get(0), markersBound.get(6), true);
                hierarchyBuilder.buildHierarchy();
                hierarchyBuilder.getHierarchies().forEach(Wave::calculatePossibleSpeeds);
            }

            @Test
            public void thenFindFourWays() {
                assertEquals("Ways in hierarchy should be exactly 4.", 4, hierarchyBuilder.size());
            }

            public class ThenFirstConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(0);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#0.", markersBound.get(0), connection.getStartpoint());
                    assertEquals("Should ends in Marker#6.", markersBound.get(6), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#0", connection.getCrosses().contains(markersBound.get(0)));
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#4", connection.getCrosses().contains(markersBound.get(4)));
                    assertTrue("Should contain Marker#5", connection.getCrosses().contains(markersBound.get(5)));
                    assertTrue("Should contain Marker#6", connection.getCrosses().contains(markersBound.get(6)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#0.", connection.getWays().contains(ways.getWay(0)));
                    assertTrue("Should contain way#6.", connection.getWays().contains(ways.getWay(6)));
                    assertTrue("Should contain way#8.", connection.getWays().contains(ways.getWay(8)));
                    assertTrue("Should contain way#10.", connection.getWays().contains(ways.getWay(10)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 475.", 475, connection.getDistance());
                }
            }

            public class ThenSecondConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(1);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#6.", markersBound.get(6), connection.getStartpoint());
                    assertEquals("Should ends in Marker#0.", markersBound.get(0), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#0", connection.getCrosses().contains(markersBound.get(0)));
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#4", connection.getCrosses().contains(markersBound.get(4)));
                    assertTrue("Should contain Marker#5", connection.getCrosses().contains(markersBound.get(5)));
                    assertTrue("Should contain Marker#6", connection.getCrosses().contains(markersBound.get(6)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#1.", connection.getWays().contains(ways.getWay(1)));
                    assertTrue("Should contain way#7.", connection.getWays().contains(ways.getWay(7)));
                    assertTrue("Should contain way#9.", connection.getWays().contains(ways.getWay(9)));
                    assertTrue("Should contain way#11.", connection.getWays().contains(ways.getWay(11)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 475.", 475, connection.getDistance());
                }
            }

            public class ThenThirdConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(2);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#3.", markersBound.get(3), connection.getStartpoint());
                    assertEquals("Should ends in Marker#1.", markersBound.get(1), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#2", connection.getCrosses().contains(markersBound.get(2)));
                    assertTrue("Should contain Marker#3", connection.getCrosses().contains(markersBound.get(3)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#3.", connection.getWays().contains(ways.getWay(3)));
                    assertTrue("Should contain way#5.", connection.getWays().contains(ways.getWay(5)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 500.", 500, connection.getDistance());
                }
            }

            public class ThenFourthConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(3);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#1.", markersBound.get(1), connection.getStartpoint());
                    assertEquals("Should ends in Marker#3.", markersBound.get(3), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#2", connection.getCrosses().contains(markersBound.get(2)));
                    assertTrue("Should contain Marker#3", connection.getCrosses().contains(markersBound.get(3)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#2.", connection.getWays().contains(ways.getWay(2)));
                    assertTrue("Should contain way#4.", connection.getWays().contains(ways.getWay(4)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 500.", 500, connection.getDistance());
                }
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

            hierarchyBuilder = new HierarchyBuilder(ways, markers);
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

            public class ThenFirstConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(0);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#0.", markersBound.get(0), connection.getStartpoint());
                    assertEquals("Should ends in Marker#6.", markersBound.get(3), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#0", connection.getCrosses().contains(markersBound.get(0)));
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#2", connection.getCrosses().contains(markersBound.get(2)));
                    assertTrue("Should contain Marker#3", connection.getCrosses().contains(markersBound.get(3)));
                }


                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#0.", connection.getWays().contains(ways.getWay(0)));
                    assertTrue("Should contain way#2.", connection.getWays().contains(ways.getWay(2)));
                    assertTrue("Should contain way#4.", connection.getWays().contains(ways.getWay(4)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 600.", 600, connection.getDistance());
                }
            }

            public class ThenSecondConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(1);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#3.", markersBound.get(3), connection.getStartpoint());
                    assertEquals("Should ends in Marker#0.", markersBound.get(0), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#0", connection.getCrosses().contains(markersBound.get(0)));
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#2", connection.getCrosses().contains(markersBound.get(2)));
                    assertTrue("Should contain Marker#3", connection.getCrosses().contains(markersBound.get(3)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#1.", connection.getWays().contains(ways.getWay(1)));
                    assertTrue("Should contain way#3.", connection.getWays().contains(ways.getWay(3)));
                    assertTrue("Should contain way#5.", connection.getWays().contains(ways.getWay(5)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 600.", 600, connection.getDistance());
                }
            }

            public class ThenThirdConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(2);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#5.", markersBound.get(5), connection.getStartpoint());
                    assertEquals("Should ends in Marker#6.", markersBound.get(6), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#4", connection.getCrosses().contains(markersBound.get(4)));
                    assertTrue("Should contain Marker#5", connection.getCrosses().contains(markersBound.get(5)));
                    assertTrue("Should contain Marker#6", connection.getCrosses().contains(markersBound.get(6)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#7.", connection.getWays().contains(ways.getWay(7)));
                    assertTrue("Should contain way#9.", connection.getWays().contains(ways.getWay(9)));
                    assertTrue("Should contain way#10.", connection.getWays().contains(ways.getWay(10)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 375.", 375, connection.getDistance());
                }
            }

            public class ThenFourthConnection {
                Wave connection;

                @Before
                public void setUp() {
                    connection = hierarchyBuilder.getHierarchyEntry(3);
                }

                @Test
                public void haveRightStartAndEnd() {
                    assertEquals("Should starts from Marker#6.", markersBound.get(6), connection.getStartpoint());
                    assertEquals("Should ends in Marker#5.", markersBound.get(5), connection.getEndpoint());
                }

                @Test
                public void containsAllCrosses() {
                    assertTrue("Should contain Marker#1", connection.getCrosses().contains(markersBound.get(1)));
                    assertTrue("Should contain Marker#4", connection.getCrosses().contains(markersBound.get(4)));
                    assertTrue("Should contain Marker#5", connection.getCrosses().contains(markersBound.get(5)));
                    assertTrue("Should contain Marker#6", connection.getCrosses().contains(markersBound.get(6)));
                }

                @Test
                public void containsAllWays() {
                    assertTrue("Should contain way#6.", connection.getWays().contains(ways.getWay(6)));
                    assertTrue("Should contain way#8.", connection.getWays().contains(ways.getWay(8)));
                    assertTrue("Should contain way#11.", connection.getWays().contains(ways.getWay(11)));
                }

                @Test
                public void haveRightDistance() {
                    assertEquals("Distance should be 375.", 375, connection.getDistance());
                }
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

            hierarchyBuilder = new HierarchyBuilder(ways, markers);
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