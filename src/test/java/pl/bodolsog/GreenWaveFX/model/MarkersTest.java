package pl.bodolsog.GreenWaveFX.model;


import de.bechte.junit.runners.context.HierarchicalContextRunner;
import netscape.javascript.JSObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class MarkersTest {
    private Markers markers;
    private JSObject firstJSObject;
    private JSObject secondJSObject;

    public class GivenFreshMarkersClass {
        @Before
        public void setUp() {
            markers = new Markers(new Ways());
            firstJSObject = new JSObjectAdapter();
        }

        public class WhenOneMarkerIsAdded {
            @Before
            public void setUp() {
                markers.addMarker(firstJSObject);
            }

            @Test
            public void thenItsIdShouldBeSettedToActualVariable() {
                assertEquals("Id from active variable should be equal to id of first Marker",
                        markers.getMarker(0), markers.getActiveMarker());
            }
        }

        public class WhenTwoMarkersAreAdded {
            @Before
            public void setUp() {
                secondJSObject = new JSObjectAdapter();
                markers.addMarker(firstJSObject);
                markers.addMarker(secondJSObject);
            }

            @Test
            public void thenIdOfSecondMarkerShouldBeSettedToActualVariable() {
                assertEquals("Id from active variable should be equal to id of first Marker",
                        markers.getMarker(1), markers.getActiveMarker());
            }

            @Test
            public void thenBothAreInMap() {
                assertNotSame("First and second Markers are not equals", markers.getMarker(0), markers.getMarker(1));
                assertSame("First object has a firstJSObject as jsMarker", firstJSObject, markers.getMarker(0).jsMarker);
                assertSame("Second object has a secondJSObject as jsMarker", secondJSObject, markers.getMarker(1).jsMarker);
            }
        }

    }
}
//    /** CRUD: Delete */
//    @Test
//    public void whenWayIsDeletedThenIsGoneAway(){
//        ways.addWay(markerOne, "north", markerTwo, "south", true, response, distance);
//        ways.remove(0);
//        assertNull("Way index 0 should be null", ways.getWay(0));
//        assertSame("A markerTwo should be a begin in Way index 1", markerTwo, ways.getWay(1).getBeginMarker());
//        assertSame("A markerOne should be a end in Way index 1", markerOne, ways.getWay(1).getEndMarker());
//    }
//}