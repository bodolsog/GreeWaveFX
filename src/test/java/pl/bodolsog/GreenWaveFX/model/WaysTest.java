package pl.bodolsog.GreenWaveFX.model;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bodolsog on 01.01.16.
 */
public class WaysTest {

    private Ways ways;
    private JSObject jsObject;
    private Marker markerOne;
    private Marker markerTwo;
    private Marker markerThree;
    private Marker markerFour;

    @Before
    public void setUp(){
        ways = new Ways();
        // JSObject for test
        jsObject = new JSObject(){
            @Override public Object call(String s, Object... objects) throws JSException { return null; }
            @Override public Object eval(String s) throws JSException { return null; }
            @Override public Object getMember(String s) throws JSException { return null; }
            @Override public void setMember(String s, Object o) throws JSException {}
            @Override public void removeMember(String s) throws JSException {}
            @Override public Object getSlot(int i) throws JSException { return null; }
            @Override public void setSlot(int i, Object o) throws JSException {}
        };

        markerOne = new Marker(0, jsObject);
        markerTwo = new Marker(1, jsObject);
        markerThree = new Marker(2, jsObject);
        markerFour = new Marker(3, jsObject);
    }

    /** CRUD: Add */
    @Test
    public void whenAddNewOneWayThenWayIsAdded(){
        ways.addWay(markerOne, markerTwo, false);
        assertSame("A markerOne should be a begin in first Way", markerOne, ways.getWay(0).getWayBegin());
        assertSame("A markerTwo should be a end in first Way", markerTwo, ways.getWay(0).getWayEnd());
        assertNull("Second way don't exists", ways.getWay(1));
    }

    @Test
    public void whenAddNewTwoWayThenBothWaysAreAdded(){
        ways.addWay(markerOne, markerTwo, true);
        assertSame("A markerOne should be a begin in first Way", markerOne, ways.getWay(0).getWayBegin());
        assertSame("A markerTwo should be a end in first Way", markerTwo, ways.getWay(0).getWayEnd());
        assertSame("A markerTwo should be a begin in second Way", markerTwo, ways.getWay(1).getWayBegin());
        assertSame("A markerOne should be a end in second Way", markerOne, ways.getWay(1).getWayEnd());
        assertNull("Third way don't exists", ways.getWay(2));
    }

    @Test
    public void whenAddedOneWayIsDuplicatedThenItIsNotAdded(){
        ways.addWay(markerOne, markerTwo, false);
        ways.addWay(markerOne, markerTwo, false);
        assertNull("Duplicated way should be not added", ways.getWay(1));
    }

    @Test
    public void whenAddedTwoWayAndAFirstWayIsDuplicatedThenOnlySecondIsAdded(){
        ways.addWay(markerOne, markerTwo, false);
        ways.addWay(markerOne, markerTwo, true);
        assertSame("A markerOne should be a begin in first Way", markerOne, ways.getWay(0).getWayBegin());
        assertSame("A markerTwo should be a end in first Way", markerTwo, ways.getWay(0).getWayEnd());
        assertSame("A markerTwo should be a begin in second Way", markerTwo, ways.getWay(1).getWayBegin());
        assertSame("A markerOne should be a end in second Way", markerOne, ways.getWay(1).getWayEnd());
        assertNull("Third way should be not added", ways.getWay(2));
    }

    @Test
    public void whenAddedTwoWayAndASecondWayIsDuplicatedThenOnlyFirstIsAdded(){
        ways.addWay(markerOne, markerTwo, false);
        ways.addWay(markerTwo, markerOne, true);
        assertSame("A markerOne should be a begin in first Way", markerOne, ways.getWay(0).getWayBegin());
        assertSame("A markerTwo should be a end in first Way", markerTwo, ways.getWay(0).getWayEnd());
        assertSame("A markerTwo should be a begin in second Way", markerTwo, ways.getWay(1).getWayBegin());
        assertSame("A markerOne should be a end in second Way", markerOne, ways.getWay(1).getWayEnd());
        assertNull("Third way should be not added", ways.getWay(2));
    }

    @Test
    public void whenTryToAddWayWithSameBeginAndEndThenIsNothingAdded(){
        ways.addWay(markerOne, markerOne, true);
        assertEquals("Size should be 0", 0, ways.size());
    }

    /** CRUD: Delete */
    @Test
    public void whenWayIsDeletedThenIsGoneAway(){
        ways.addWay(markerOne, markerTwo, true);
        ways.deleteWay(0);
        assertNull("Way index 0 should be null", ways.getWay(0));
        assertSame("A markerTwo should be a begin in Way index 1", markerTwo, ways.getWay(1).getWayBegin());
        assertSame("A markerOne should be a end in Way index 1", markerOne, ways.getWay(1).getWayEnd());
    }
}