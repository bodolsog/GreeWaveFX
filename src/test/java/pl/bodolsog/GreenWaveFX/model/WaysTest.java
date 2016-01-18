package pl.bodolsog.GreenWaveFX.model;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.bodolsog.GreenWaveFX.staticVar.DIRECTION;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class WaysTest {
    int waysSize;
    private Ways ways;
    private ArrayList<Marker> markersList;

    private ArrayList<Marker> createMarkersList(int count) {
        Markers m = new Markers(ways);
        ArrayList<Marker> markersList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            markersList.add(new Marker(i, new JSObjectAdapter(), m));
        }
        return markersList;
    }

    @Before
    public void setUp(){
        ways = new Ways();
    }

    public class GivenFreshWaysClass {

        @Before
        public void setUp() {
            markersList = createMarkersList(2);
        }

        public class WhenAddAOneWay {
            @Before
            public void setUp() {
                ways.addWay(markersList.get(0), DIRECTION.E, markersList.get(1), DIRECTION.W, false, "", 100);
            }

            @Test
            public void thenOneWayIsCreated() {
                assertEquals("Should be created only one way.", 1, ways.size());
            }

            @Test
            public void thenWayLeadsFromTheFirstMarkerToSecond() {
                assertEquals("Way should started from first Marker.", markersList.get(0), ways.getWay(0).getBeginMarker());
                assertEquals("Way should ended on second Marker.", markersList.get(1), ways.getWay(0).getEndMarker());
            }

        }

        public class WhenAddATwoWay {
            @Before
            public void setUp() {
                ways.addWay(markersList.get(0), DIRECTION.E, markersList.get(1), DIRECTION.W, true, "", 100);
            }

            @Test
            public void thenTwoWaysAreCreated() {
                assertEquals("Should be created two ways.", 2, ways.size());
            }

            @Test
            public void thenFirstWayLeadsFromTheFirstMarkerToSecond() {
                assertEquals("Way should started from first Marker.", markersList.get(0), ways.getWay(0).getBeginMarker());
                assertEquals("Way should ended on second Marker.", markersList.get(1), ways.getWay(0).getEndMarker());
            }

            @Test
            public void thenSecondWayLeadsFromTheSecondMarkerToFirst() {
                assertEquals("Way should started from second Marker.", markersList.get(1), ways.getWay(1).getBeginMarker());
                assertEquals("Way should ended on first Marker.", markersList.get(0), ways.getWay(1).getEndMarker());
            }
        }

        public class WhenAddWayWithSameStartAndEnd {
            @Before
            public void setUp() {
                ways.addWay(markersList.get(0), DIRECTION.E, markersList.get(0), DIRECTION.W, true, "", 100);
            }

            @Test
            public void thenNothingIsAdded() {
                assertEquals("Way shouldn't be added.", 0, ways.size());
            }
        }
    }

    public class GivenSomeWaysAdded {
        @Before
        public void setUp() {
            markersList = createMarkersList(7);
            // 4   5
            // |   ^
            // 0-1-2
            //   ^
            //   3
            ways.addWay(markersList.get(0), DIRECTION.E, markersList.get(1), DIRECTION.W, true, "", 100);
            ways.addWay(markersList.get(1), DIRECTION.E, markersList.get(2), DIRECTION.W, true, "", 100);
            ways.addWay(markersList.get(1), DIRECTION.S, markersList.get(3), DIRECTION.N, false, "", 100);
            ways.addWay(markersList.get(0), DIRECTION.N, markersList.get(4), DIRECTION.S, true, "", 100);
            ways.addWay(markersList.get(2), DIRECTION.N, markersList.get(5), DIRECTION.S, false, "", 100);
            waysSize = ways.size();
        }

        public class WhenAddWayTwice {
            @Before
            public void setUp() {
                ways.addWay(markersList.get(1), DIRECTION.S, markersList.get(3), DIRECTION.N, false, "", 100);
            }

            @Test
            public void thenWayIsntAdded() {
                assertEquals("Way shouldn't be added.", waysSize, ways.size());
            }
        }

        public class WhenAddTwoWayWithExistingFirst {
            @Before
            public void setUp() {
                ways.addWay(markersList.get(1), DIRECTION.S, markersList.get(3), DIRECTION.N, true, "", 100);
            }

            @Test
            public void thenOnlySecondWayIsAdded() {
                assertEquals("Only one way should be added.", waysSize + 1, ways.size());
                Way way = ways.getWay(waysSize);
                assertTrue("The second way should be added.", way.getBeginMarker() == markersList.get(3));
            }
        }

        public class WhenAddTwoWayWithExistingRevert {
            @Before
            public void setUp() {
                ways.addWay(markersList.get(3), DIRECTION.N, markersList.get(1), DIRECTION.S, true, "", 100);
            }

            @Test
            public void thenOnlyFirstWayIsAdded() {
                assertEquals("Only one way should be added.", waysSize + 1, ways.size());
                Way way = ways.getWay(waysSize);
                assertTrue("The first way should be added.", way.getBeginMarker() == markersList.get(3));
            }
        }

        public class WhenDeleteMarker {
            @Before
            public void setUp() {
                ways.remove(2);
            }

            @Test
            public void thenHisIndexGotNull() {
                assertNull("Deleted index should be nuull.", ways.getWay(2));
            }
        }


    }
}