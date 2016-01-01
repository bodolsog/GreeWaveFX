package pl.bodolsog.GreenWaveFX.model;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by bodolsog on 01.01.16.
 */
public class MarkersTest {

    private Markers markers;
    private JSObject firstJSObject;
    private JSObject secondJSObject;
    private JSObject thirdJSObject;

    /**
     * JSObject adapter for test.
     */
    public class JSObjectTest extends JSObject {
        @Override public Object call(String s, Object... objects) throws JSException { return null; }
        @Override public Object eval(String s) throws JSException { return null; }
        @Override public Object getMember(String s) throws JSException { return null; }
        @Override public void setMember(String s, Object o) throws JSException {}
        @Override public void removeMember(String s) throws JSException {}
        @Override public Object getSlot(int i) throws JSException { return null; }
        @Override public void setSlot(int i, Object o) throws JSException {}
    }

    @Before
    public void setUp(){
        markers = new Markers();
        firstJSObject = new JSObjectTest();
        secondJSObject = new JSObjectTest();
        thirdJSObject = new JSObjectTest();
    }

    @Test
    public void whenNewMarkerIsAddedThenItIdIsInActualVariable(){
        markers.addMarker(firstJSObject);
        assertEquals("Id from active variable should be equal to id of first Marker", markers.getMarker(0), markers.getActiveMarker());
        markers.addMarker(secondJSObject);
        assertEquals("Id from active variable should be equal to id of second Marker", markers.getMarker(1), markers.getActiveMarker());
    }

    @Test
    public void whenTwoMarkersAreAddedThenTheyAreInMap(){
        markers.addMarker(firstJSObject);
        markers.addMarker(secondJSObject);
        assertNotSame("First and second Markers are not equals", markers.getMarker(0), markers.getMarker(1));
        assertSame("First object has a firstJSObject as jsMarker", firstJSObject, markers.getMarker(0).jsMarker);
        assertSame("Second object has a secondJSObject as jsMarker", secondJSObject, markers.getMarker(1).jsMarker);
    }
    @Test
    public void whenObjectIsDeletedAndNewAddedAfterThenIndexIsntDuplicated(){
        markers.addMarker(firstJSObject);
        markers.addMarker(secondJSObject);
        markers.deleteMarker(1);
        markers.addMarker(thirdJSObject);
        assertSame("First element should be index 0, firstJSObject", firstJSObject, markers.getMarker(0).jsMarker);
        assertSame("Second element should be index 2, thirdJSObject", thirdJSObject, markers.getMarker(2).jsMarker);
        assertNull("Index 1 should get null", markers.getMarker(1));
    }
}