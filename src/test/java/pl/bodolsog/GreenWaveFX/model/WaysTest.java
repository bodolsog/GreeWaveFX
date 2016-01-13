package pl.bodolsog.GreenWaveFX.model;

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
    private String response;
    private int distance = 0;
    private Markers markers;

    @Before
    public void setUp(){
        ways = new Ways();
        jsObject = new JSObjectAdapter();

        markers = new Markers();
        markerOne = new Marker(0, jsObject, markers);
        markerTwo = new Marker(1, jsObject, markers);
        markerThree = new Marker(2, jsObject, markers);
        markerFour = new Marker(3, jsObject, markers);
        response = "";

    }

    /** CRUD: Add */
    @Test
    public void whenAddNewOneWayThenWayIsAdded(){
        ways.addWay(markerOne, "north", markerTwo, "south", false, response, distance);
        assertSame("A markerOne should be a begin in first Way", markerOne, ways.getWay(0).getWayBegin());
        assertSame("A markerTwo should be a end in first Way", markerTwo, ways.getWay(0).getWayEnd());
        assertNull("Second way don't exists", ways.getWay(1));
    }

    @Test
    public void whenAddNewTwoWayThenBothWaysAreAdded(){
        ways.addWay(markerOne, "north", markerTwo, "south", true, response, distance);
        assertSame("A markerOne should be a begin in first Way", markerOne, ways.getWay(0).getWayBegin());
        assertSame("A markerTwo should be a end in first Way", markerTwo, ways.getWay(0).getWayEnd());
        assertSame("A markerTwo should be a begin in second Way", markerTwo, ways.getWay(1).getWayBegin());
        assertSame("A markerOne should be a end in second Way", markerOne, ways.getWay(1).getWayEnd());
        assertNull("Third way don't exists", ways.getWay(2));
    }

    @Test
    public void whenAddedOneWayIsDuplicatedThenItIsNotAdded(){
        ways.addWay(markerOne, "north", markerTwo, "south", false, response, distance);
        ways.addWay(markerOne, "north", markerTwo, "south", false, response, distance);
        assertNull("Duplicated way should be not added", ways.getWay(1));
    }

    @Test
    public void whenAddedTwoWayAndAFirstWayIsDuplicatedThenOnlySecondIsAdded(){
        ways.addWay(markerOne, "north", markerTwo, "south", false, response, distance);
        ways.addWay(markerOne, "north", markerTwo, "south", true, response, distance);
        assertSame("A markerOne should be a begin in first Way", markerOne, ways.getWay(0).getWayBegin());
        assertSame("A markerTwo should be a end in first Way", markerTwo, ways.getWay(0).getWayEnd());
        assertSame("A markerTwo should be a begin in second Way", markerTwo, ways.getWay(1).getWayBegin());
        assertSame("A markerOne should be a end in second Way", markerOne, ways.getWay(1).getWayEnd());
        assertNull("Third way should be not added", ways.getWay(2));
    }

    @Test
    public void whenAddedTwoWayAndASecondWayIsDuplicatedThenOnlyFirstIsAdded(){
        ways.addWay(markerOne, "north", markerTwo, "south", false, response, distance);
        ways.addWay(markerTwo, "north", markerOne, "south", true, response, distance);
        assertSame("A markerOne should be a begin in first Way", markerOne, ways.getWay(0).getWayBegin());
        assertSame("A markerTwo should be a end in first Way", markerTwo, ways.getWay(0).getWayEnd());
        assertSame("A markerTwo should be a begin in second Way", markerTwo, ways.getWay(1).getWayBegin());
        assertSame("A markerOne should be a end in second Way", markerOne, ways.getWay(1).getWayEnd());
        assertNull("Third way should be not added", ways.getWay(2));
    }

    @Test
    public void whenTryToAddWayWithSameBeginAndEndThenIsNothingAdded(){
        ways.addWay(markerOne, "north", markerOne, "south", true, response, distance);
        assertEquals("Size should be 0", 0, ways.size());
    }

    /** CRUD: Delete */
    @Test
    public void whenWayIsDeletedThenIsGoneAway(){
        ways.addWay(markerOne, "north", markerTwo, "south", true, response, distance);
        ways.deleteWay(0);
        assertNull("Way index 0 should be null", ways.getWay(0));
        assertSame("A markerTwo should be a begin in Way index 1", markerTwo, ways.getWay(1).getWayBegin());
        assertSame("A markerOne should be a end in Way index 1", markerOne, ways.getWay(1).getWayEnd());
    }
}