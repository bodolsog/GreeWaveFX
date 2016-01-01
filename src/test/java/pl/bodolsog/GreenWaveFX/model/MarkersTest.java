package pl.bodolsog.GreenWaveFX.model;

import netscape.javascript.JSException;
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

    public class JSObjectTest extends JSObject {
        @Override
        public Object call(String s, Object... objects) throws JSException {
            return null;
        }

        @Override
        public Object eval(String s) throws JSException {
            return null;
        }

        @Override
        public Object getMember(String s) throws JSException {
            return null;
        }

        @Override
        public void setMember(String s, Object o) throws JSException {

        }

        @Override
        public void removeMember(String s) throws JSException {

        }

        @Override
        public Object getSlot(int i) throws JSException {
            return null;
        }

        @Override
        public void setSlot(int i, Object o) throws JSException {

        }
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
    public void whenObjectIsDeletedThenMapIsSmallestAndIndexIsntDuplicated(){
        markers.addMarker(firstJSObject);
        assertEquals("After first add: Map size should be 1", 1, markers.size());
        assertTrue("After first add, his id should be reachable", markers.containsKey(0));

        markers.addMarker(secondJSObject);
        assertEquals("After second add: Map size should be 2", 2, markers.size());
        assertTrue("After second add, his id should be reachable", markers.containsKey(1));

        markers.deleteMarker(0);
        assertEquals("After delete first item: Map size should be 1", 1, markers.size());
        assertFalse("After delete first item, his id should be not reachable", markers.containsKey(0));

        markers.deleteMarker(1);
        assertEquals("After delete second item: Map size should be 0", 0, markers.size());
        assertFalse("After delete second item, his id should be not reachable", markers.containsKey(1));

        markers.addMarker(thirdJSObject);
        assertEquals("After third add: Map size should be 1", 1, markers.size());
        assertFalse("After third add, id 0 should be not reachable", markers.containsKey(0));
        assertTrue("After third add, his id (2) should be reachable", markers.containsKey(2));
    }
}