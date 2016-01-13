package pl.bodolsog.GreenWaveFX.model;

import netscape.javascript.JSObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bodolsog on 01.01.16.
 */
public class MarkersTest {

    private Markers markers;
    private JSObject firstJSObject;
    private JSObject secondJSObject;
    private JSObject thirdJSObject;

    @Before
    public void setUp(){
        markers = new Markers();
        firstJSObject = new JSObjectAdapter();
        secondJSObject = new JSObjectAdapter();
        thirdJSObject = new JSObjectAdapter();
    }

    // CRUD: Create
    @Test
    public void whenTwoMarkersAreAddedThenTheyBothAreInMap(){
        markers.addMarker(firstJSObject);
        markers.addMarker(secondJSObject);
        assertNotSame("First and second Markers are not equals", markers.getMarker(0), markers.getMarker(1));
        assertSame("First object has a firstJSObject as jsMarker", firstJSObject, markers.getMarker(0).jsMarker);
        assertSame("Second object has a secondJSObject as jsMarker", secondJSObject, markers.getMarker(1).jsMarker);

        markers.deleteMarker(1);
        markers.addMarker(thirdJSObject);
        assertSame("First element should be index 0, firstJSObject", firstJSObject, markers.getMarker(0).jsMarker);
        assertSame("Second element should be index 2, thirdJSObject", thirdJSObject, markers.getMarker(2).jsMarker);
        assertNull("Index 1 should get null", markers.getMarker(1));
    }

    @Test
    public void whenNewMarkerIsAddedThenItIdIsInActualVariable(){
        markers.addMarker(firstJSObject);
        assertEquals("Id from active variable should be equal to id of first Marker", markers.getMarker(0), markers.getActiveMarker());
        markers.addMarker(secondJSObject);
        assertEquals("Id from active variable should be equal to id of second Marker", markers.getMarker(1), markers.getActiveMarker());
    }

}