package pl.bodolsog.GreenWaveFX.model;


import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import pl.bodolsog.GreenWaveFX.staticVar.DIRECTIONS;
import pl.bodolsog.GreenWaveFX.staticVar.NODE_TYPE;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class MarkerTest {
    private final ArrayList<String> crossDirections = new ArrayList<String>() {{
        add(DIRECTIONS.NORTH);
        add(DIRECTIONS.SOUTH);
        add(DIRECTIONS.EAST);
        add(DIRECTIONS.WEST);
    }};
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Markers markers;
    private Ways ways;
    private Marker markerA;
    private Marker markerB;
    private Marker markerC;
    private Marker markerD;
    private Way wayNorth;

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

    private void nodesFromTwoOnSidesAreConnected() {
        // Way ABC
        assertTrue("Node from MarkerA is connected to markerC.", markerA.getConnectedNodes().containsKey(markerC));

        Way wayAB = ways.getWay(0);
        Way wayBC = ways.getWay(2);

        assertTrue("Node from MarkerA should have wayAB and wayBC.",
                markerA.getConnectedNodes().get(markerC).contains(wayAB) &&
                        markerA.getConnectedNodes().get(markerC).contains(wayBC)
        );

        // Way CBA
        assertTrue("Node from MarkerC is connected to markerA.", markerC.getConnectedNodes().containsKey(markerA));

        Way wayCB = ways.getWay(3);
        Way wayBA = ways.getWay(1);

        assertTrue("Node from MarkerC should have wayCB and wayBA.",
                markerC.getConnectedNodes().get(markerA).contains(wayCB) &&
                        markerC.getConnectedNodes().get(markerA).contains(wayBA)
        );
    }

    public class GivenFreshMarkersClass {
        public class WhenCallSetCrossDirectionsWithDirections {
            @Before
            public void setUp() {
                ArrayList<String> directions = new ArrayList<String>() {{
                    add(DIRECTIONS.NORTH);
                    add(DIRECTIONS.SOUTH);
                    add(DIRECTIONS.NORTHEAST);
                    add(DIRECTIONS.WEST);
                }};
                markers.addMarker(new JSObjectAdapter());
                markers.setCrossDirections(0, directions);
                markerA = markers.getMarker(0);
            }

            @Test
            public void thenAllDirectionsAreInAndArentAnother() {
                ArrayList<String> directions = markerA.getCrossDirections();
                assertTrue("Should contain north", directions.contains(DIRECTIONS.NORTH));
                assertTrue("Should contain south", directions.contains(DIRECTIONS.SOUTH));
                assertTrue("Should contain northeast", directions.contains(DIRECTIONS.NORTHEAST));
                assertTrue("Should contain west", directions.contains(DIRECTIONS.WEST));
                assertFalse("Shouldn't contain east", directions.contains(DIRECTIONS.EAST));
            }
        }
    }

    public class GivenMarkerClassWithData {
        @Before
        public void setUp() {
            markerA = createMarker();
            markerB = createMarker();
            markerC = createMarker();

            ways.addWay(markerA, DIRECTIONS.EAST, markerB, DIRECTIONS.WEST, true, "", 100);
            ways.addWay(markerA, DIRECTIONS.NORTH, markerC, DIRECTIONS.SOUTH, true, "", 200);
        }

        public class WhenCallSetCrossDirectionsWithAnotherData {
            @Before
            public void setUp() {
                wayNorth = ways.getWay(2);

                ArrayList<String> directions = new ArrayList<String>() {{
                    add(DIRECTIONS.NORTH);
                    add(DIRECTIONS.NORTHEAST);
                    add(DIRECTIONS.WEST);
                }};
                markerA.setCrossDirections(directions);
            }

            @Test
            public void thenOnlyNewDataShouldBeInMap() {
                ArrayList<String> directions = markerA.getCrossDirections();
                assertTrue("Should contain north.", directions.contains(DIRECTIONS.NORTH));
                assertTrue("Should contain northeast.", directions.contains(DIRECTIONS.NORTHEAST));
                assertTrue("Should contain west.", directions.contains(DIRECTIONS.WEST));

                assertFalse("Should contain south.", directions.contains(DIRECTIONS.SOUTH));
                assertFalse("Should contain east.", directions.contains(DIRECTIONS.EAST));
            }

            @Test
            public void thenWayOnSameDirectionShouldStay() {
                assertEquals("Way from north should stay.",
                        wayNorth, markerA.getCrossDirection(DIRECTIONS.NORTH));
            }

            @Test
            public void thenWayFromRemovedDirectionShouldBeDestroyed() {
                thrown.expect(NullPointerException.class);
                assertNull("", markerA.getCrossDirection(DIRECTIONS.EAST));
                thrown.expect(NullPointerException.class);
                assertNull("", markerB.getCrossDirection(DIRECTIONS.WEST));
            }
        }

    }

    public class GivenTwoMarkers {
        @Before
        public void setUp() {
            markerA = createMarker();
            markerB = createMarker();
        }

        public class WhenAreConnected {
            @Before
            public void setUp() {
                // A-B
                ways.addWay(markerA, DIRECTIONS.EAST, markerB, DIRECTIONS.WEST, true, "", 100);
            }

            @Test
            public void thenCountOfStartpointsAreEqualToTwo() {
                assertEquals("EndNodes size should be 2.", 2, markers.startpointsCount());
            }

            @Test
            public void thenBothAreStartpoints() {
                assertEquals("MarkerA Node should be type Startpoint.", NODE_TYPE.STARTPOINT,
                        markers.getNode(0).getNodeType());
                assertEquals("MarkerB Node should be type Startpoint.", NODE_TYPE.STARTPOINT,
                        markers.getNode(1).getNodeType());
            }

            @Test
            public void thenTheirNodesAreConnected() {
                assertTrue("Node from MarkerA is connected to markerB.", markerA.getConnectedNodes().containsKey(markerB));
                assertTrue("Node from MarkerB is connected to markerA.", markerB.getConnectedNodes().containsKey(markerA));

                Way wayAB = ways.getWay(0);
                Way wayBA = ways.getWay(1);

                assertTrue("Node from MarkerA should have wayAB.", markerA.getConnectedNodes().get(markerB).contains(wayAB));
                assertTrue("Node from MarkerB should have wayBA.", markerB.getConnectedNodes().get(markerA).contains(wayBA));
            }
        }
    }

    public class GivenTwoConnectedMarkers {
        @Before
        public void setUp() {
            markerA = createMarker();
            markerB = createMarker();

            // A-B
            ways.addWay(markerA, DIRECTIONS.EAST, markerB, DIRECTIONS.WEST, true, "", 100);
        }

        public class WhenThirdIsConnected {
            @Before
            public void setUp() {
                markerC = createMarker();

                // A-B-C
                ways.addWay(markerB, DIRECTIONS.EAST, markerC, DIRECTIONS.WEST, true, "", 200);
            }

            @Test
            public void thenCountOfStartpointsAreEqualToTwo() {
                assertEquals("EndNodes size should be 2.", 2, markers.startpointsCount());
            }

            @Test
            public void thenTwoOnSidesAreStartpoints() {
                assertEquals("MarkerA Node should be type Startpoint.", NODE_TYPE.STARTPOINT,
                        markerA.getNodeType());
                assertEquals("MarkerC Node should be type Startpoint.", NODE_TYPE.STARTPOINT,
                        markerC.getNodeType());
            }

            @Test
            public void thenAMiddleOneIsATransition() {
                assertEquals("B node should be transition.", NODE_TYPE.TRANSITION, markerB.getNodeType());
                assertEquals("B node should have no connections.", 0, markerB.getConnectedNodes().size());
            }

            @Test
            public void thenNodesConnectionToAMiddleOneNotExists() {
                thrown.expect(NullPointerException.class);
                assertNull("MarkerA has no node connections to markerB.", markerA.getConnectedNode(markerB));

                thrown.expect(NullPointerException.class);
                assertNull("MarkerC has no node connections to markerB.", markerC.getConnectedNode(markerB));
            }

            @Test
            public void thenNodesConnectionFromAMiddleOneNotExists() {
                assertEquals("MarkerB shold have zero nodes connections.", 0, markerB.getConnectedNodes().size());
            }

            @Test
            public void thenNodesFromTwoOnSidesAreConnected() {
                nodesFromTwoOnSidesAreConnected();
            }
        }
    }

    public class GivenThreeConnectedMarkers {
        @Before
        public void setUp() {
            markerA = createMarker();
            markerB = createMarker();
            markerC = createMarker();

            // A-B-C
            ways.addWay(markerA, DIRECTIONS.EAST, markerB, DIRECTIONS.WEST, true, "", 100);
            ways.addWay(markerB, DIRECTIONS.EAST, markerC, DIRECTIONS.WEST, true, "", 200);
        }

        public class WhenFourthIsConnectedToMiddleOne {
            @Before
            public void setUp() {
                markerD = createMarker();
                // A-B-C
                //   |
                //   D
                ways.addWay(markerB, DIRECTIONS.SOUTH, markerD, DIRECTIONS.NORTH, true, "", 220);
            }

            @Test
            public void thenCountOfStartpointsAreEqualToThree() {
                assertEquals("EndNodes size should be 3.", 3, markers.startpointsCount());
            }

            @Test
            public void thenAllOutsideNodesAreStartpoints() {
                assertEquals("MarkerA Node should be type Startpoint.", NODE_TYPE.STARTPOINT,
                        markerA.getNodeType());
                assertEquals("MarkerC Node should be type Startpoint.", NODE_TYPE.STARTPOINT,
                        markerC.getNodeType());
                assertEquals("MarkerD Node should be type Startpoint.", NODE_TYPE.STARTPOINT,
                        markerD.getNodeType());
            }

            @Test
            public void thenMiddleOneIsACrossroad() {
                assertEquals("MarkerB should be a Crossroad.", NODE_TYPE.CROSSROAD, markerB.getNodeType());
            }

            @Test
            public void thenNodesConnectionFromAMiddleOneIsEqualThree() {
                assertEquals("MarkerB shold have three nodes connections.", 3, markerB.getConnectedNodes().size());
            }

            @Test
            public void thenCrossroadNodeIsConnectedToAllStartpoints() {
                assertTrue("Node from MarkerB is connected to markerA.", markerB.getConnectedNodes().containsKey(markerA));
                assertTrue("Node from MarkerB is connected to markerC.", markerB.getConnectedNodes().containsKey(markerC));
                assertTrue("Node from MarkerB is connected to markerD.", markerB.getConnectedNodes().containsKey(markerD));
            }

            @Test
            public void thenAllStartpointNodesAreConnectedToCrossoad() {
                assertTrue("Node from MarkerA is connected to markerB.", markerA.getConnectedNodes().containsKey(markerB));
                assertFalse("Node from MarkerA isn't connected to MarkerC or MarkerD",
                        markerA.getConnectedNodes().containsKey(markerC) || markerA.getConnectedNodes().containsKey(markerD)
                );

                assertTrue("Node from MarkerC is connected to markerB.", markerC.getConnectedNodes().containsKey(markerB));
                assertFalse("Node from MarkerC isn't connected to MarkerA or MarkerD",
                        markerC.getConnectedNodes().containsKey(markerA) || markerC.getConnectedNodes().containsKey(markerD)
                );

                assertTrue("Node from MarkerD is connected to markerB.", markerD.getConnectedNodes().containsKey(markerB));
                assertFalse("Node from MarkerD isn't connected to MarkerA or MarkerC",
                        markerD.getConnectedNodes().containsKey(markerA) || markerD.getConnectedNodes().containsKey(markerC)
                );
            }
        }

        public class WhenOneStartpointIsRemoved {
            @Before
            public void setUp() {
                markers.deleteMarker(markerC);
            }

            @Test
            public void thenMiddleOneIsNowStartpoint() {
                assertEquals("MarkerB should be a startpoint.", NODE_TYPE.STARTPOINT, markerB.getNodeType());
            }

            @Test
            public void thenCountOfStartpointsAreEqualTwo() {
                assertEquals("EndNodes size should be 2.", 2, markers.startpointsCount());
            }

            @Test
            public void thenTwoOthersAreStillConnected() {
                Way wayAB = ways.getWay(0);
                Way wayBA = ways.getWay(1);
                assertTrue("MarkerA is connected to MarkerB.", markerA.getCrossWays().contains(wayAB));
                assertEquals("MarkerA node is connected to MarkerB.", 1, markerA.getConnectedNodes().size());
                assertTrue("MarkerB is connected to MarkerA.", markerB.getCrossWays().contains(wayBA));
                assertEquals("MarkerB node is connected to MarkerA.", 1, markerB.getConnectedNodes().size());
            }

            @Test
            public void thenWaysToDeletedMarkerAreRemoved() {
                assertNull("Way from B to C not exist.", ways.getWay(2));
                assertNull("Way from C to B not exist.", ways.getWay(3));
            }
        }

        public class WhenMiddleMarkerIsRemoved {
            @Before
            public void setUp() {
                markers.deleteMarker(markerB);
            }

            @Test
            public void thenCountOfStartpointsAreEqualTwo() {
                assertEquals("EndNodes size should be 2.", 2, markers.startpointsCount());
            }

            @Test
            public void thenNoOneHaveConnectedNodes() {
                assertEquals("MarkerA has no connected nodes.", 0, markerA.getConnectedNodes().size());
                assertEquals("MarkerC has no connected nodes.", 0, markerC.getConnectedNodes().size());
            }
        }
    }

    public class GivenFourConnectedMarkersAsThreeToOne {
        @Before
        public void setUp() {
            markerA = createMarker();
            markerB = createMarker();
            markerC = createMarker();
            markerD = createMarker();

            // A-B-C
            //   |
            //   D
            ways.addWay(markerA, DIRECTIONS.EAST, markerB, DIRECTIONS.WEST, true, "", 100);
            ways.addWay(markerB, DIRECTIONS.EAST, markerC, DIRECTIONS.WEST, true, "", 200);
            ways.addWay(markerB, DIRECTIONS.SOUTH, markerD, DIRECTIONS.NORTH, true, "", 220);
        }

        public class WhenConnectionsFromOneStartpointIsRemoved {
            @Before
            public void setUp() {
                // Remove connection to and from markerD
                ways.removeWay(4);  // B-D
                ways.removeWay(5);  // D-B
            }

            @Test
            public void thenCountOfStartpointsAreEqualToThree() {
                assertEquals("EndNodes size should be 3.", 3, markers.startpointsCount());
            }

            @Test
            public void thenAllOutsideNodesAreStartpoints() {
                assertEquals("MarkerA Node should be type Startpoint.", NODE_TYPE.STARTPOINT,
                        markerA.getNodeType());
                assertEquals("MarkerC Node should be type Startpoint.", NODE_TYPE.STARTPOINT,
                        markerC.getNodeType());
            }

            @Test
            public void thenMiddleOneIsATransition() {
                assertEquals("MarkerB should be a Transition.", NODE_TYPE.TRANSITION, markerB.getNodeType());
            }

            @Test
            public void thenNodesConnectionToAMiddleOneNotExists() {
                thrown.expect(NullPointerException.class);
                assertNull("MarkerA has no node connections to markerB.", markerA.getConnectedNode(markerB));

                thrown.expect(NullPointerException.class);
                assertNull("MarkerC has no node connections to markerB.", markerC.getConnectedNode(markerB));
            }

            @Test
            public void thenNodesConnectionFromAMiddleOneNotExists() {
                assertEquals("MarkerB shold have zero nodes connections.", 0, markerB.getConnectedNodes().size());
            }

            @Test
            public void thenNodesFromTwoOnSidesAreConnected() {
                nodesFromTwoOnSidesAreConnected();
            }
        }


        public class WhenOneStartpointMarkerIsRemoved {
            @Before
            public void setUp() {
                markers.deleteMarker(markerD);
            }

            @Test
            public void thenCountOfStartpointsAreEqualToTwo() {
                assertEquals("EndNodes size should be 2.", 2, markers.startpointsCount());
            }

            @Test
            public void thenMiddleOneIsATransition() {
                assertEquals("MarkerB should be a Transition.", NODE_TYPE.TRANSITION, markerB.getNodeType());
            }


            @Test
            public void thenNodesConnectionToAMiddleOneNotExists() {
                thrown.expect(NullPointerException.class);
                assertNull("MarkerA has no node connections to markerB.", markerA.getConnectedNode(markerB));

                thrown.expect(NullPointerException.class);
                assertNull("MarkerC has no node connections to markerB.", markerC.getConnectedNode(markerB));
            }

            @Test
            public void thenNodesConnectionFromAMiddleOneNotExists() {
                assertEquals("MarkerB shold have zero nodes connections.", 0, markerB.getConnectedNodes().size());
            }

            @Test
            public void thenNodesFromTwoOnSidesAreConnected() {
                nodesFromTwoOnSidesAreConnected();
            }
        }

        public class WhenOneConnectionToOneStartpointIsRemoved {
            @Before
            public void setUp() {
                // Remove connection from markerB to markerD
                ways.removeWay(4);  // B->D
            }

            @Test
            public void thenMiddleOneIsATtransition() {
                assertEquals("MarkerB should be a Transition.", NODE_TYPE.TRANSITION, markerB.getNodeType());
            }

            @Test
            public void thenOneWayNodeIsStillConnectedToMiddle() {
                assertTrue("MarkerD has node connection to markerB.", markerD.getConnectedNodes().containsKey(markerB));
            }

            @Test
            public void thenNodesConnectionFromAMiddleOneNotExists() {
                assertEquals("MarkerB shold have zero nodes connections.", 0, markerB.getConnectedNodes().size());
            }
        }

        public class WhenOneConnectionFromOneStartpointIsRemoved {
            @Before
            public void setUp() {
                // Remove connection from markerB to markerD
                ways.removeWay(5);  // D->B
            }

            @Test
            public void thenMiddleOneIsACrossroad() {
                assertEquals("MarkerB should be a Crossroad.", NODE_TYPE.CROSSROAD, markerB.getNodeType());
            }

            @Test
            public void thenNodeConnectionToStartpointExists() {
                assertTrue("MarkerB has node connection to markerD.", markerB.getConnectedNodes().containsKey(markerD));
            }

            @Test
            public void thenConnectionFromStartpointToMiddleNotExists() {
                assertFalse("Node connection from MarkerD to MarkerB not exists.", markerD.getConnectedNodes().containsKey(markerB));
            }
        }

        public class WhenConnectionFromMiddleOneIsRemoved {
            @Before
            public void setUp() {
                markers.deleteMarker(markerB);
            }

            @Test
            public void thenAllOtherAreStartpoints() {
                assertEquals("MarkerA is a Startpoint.", NODE_TYPE.STARTPOINT, markerA.getNodeType());
                assertEquals("MarkerC is a Startpoint.", NODE_TYPE.STARTPOINT, markerC.getNodeType());
                assertEquals("MarkerD is a Startpoint.", NODE_TYPE.STARTPOINT, markerD.getNodeType());
            }

            @Test
            public void thenNoOneHaveNodesConnection() {
                assertEquals("MarkerA has no connected nodes.", 0, markerA.getConnectedNodes().size());
                assertEquals("MarkerC has no connected nodes.", 0, markerC.getConnectedNodes().size());
                assertEquals("MarkerD has no connected nodes.", 0, markerD.getConnectedNodes().size());
            }
        }


    }
}
