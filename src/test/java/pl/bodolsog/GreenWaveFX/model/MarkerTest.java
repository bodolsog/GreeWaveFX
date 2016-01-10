package pl.bodolsog.GreenWaveFX.model;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class MarkerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    JSObject jsMarker;
    Marker markerA;
    Marker markerB;

    @Before
    public void setUp() {
        markerA = new Marker(0, new JSObjectTest());
        markerB = new Marker(0, new JSObjectTest());
    }

    @Test
    public void whenCallSetCrossDirectionsWithWaluesThenAllDirectionsAreIn() {
        String[] arr = new String[]{"north", "south", "northeast", "west"};
        markerA.setCrossDirections(arr);

        ArrayList<String> dir = markerA.getCrossDirections();
        assertTrue("Should contain north", dir.contains("north"));
        assertTrue("Should contain south", dir.contains("south"));
        assertTrue("Should contain northeast", dir.contains("northeast"));
        assertTrue("Should contain west", dir.contains("west"));
        assertFalse("Shouldn't contain east", dir.contains("east"));
    }

    @Test
    public void whenCallTwiceSetCrossDirectionsWithDifferentWaluesThenOnlyNewValuesAreInAndWaysDeleted() {
        String[] arr = new String[]{"north", "south"};
        markerA.setCrossDirections(arr);

        Ways ways = new Ways();
        Way way1 = new Way(ways, 0, markerA, markerB);
        Way way2 = new Way(ways, 1, markerA, markerB);

        markerA.addWay(way1, "north");
        markerA.addWay(way2, "south");

        arr = new String[]{"south", "west"};
        markerA.setCrossDirections(arr);

        assertEquals("South should be a Way2", way2, markerA.getCrossDirection("south"));
        assertNull("West should be null.", markerA.getCrossDirection("west"));

        thrown.expect(NullPointerException.class);
        assertNull("North should not exists.", markerA.getCrossDirection("north"));
    }

    /**
     * JSObject adapter for test.
     */
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
}