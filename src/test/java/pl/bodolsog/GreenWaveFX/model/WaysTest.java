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
    private JSObject js;
    Marker markerOne;
    Marker markerTwo;
    Marker markerThree;
    Marker markerFour;

    @Before
    public void setUp(){
        ways = new Ways();
        js = new JSObject() {
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
        };

        markerOne = new Marker(0, js);
        markerTwo = new Marker(1, js);
        markerThree = new Marker(2, js);
        markerFour = new Marker(3, js);

    }

    @Test
    public void whenAddNewOneWaysThenWayAreAdded(){
        ways.addWay(markerOne, markerTwo, false);
        // First object (index 0) is begin-end right setted.
        assertEquals(markerOne, ways.getWay(0).getWayBegin());
        assertEquals(markerTwo, ways.getWay(0).getWayEnd());

        ways.addWay(markerOne, markerThree, false);
        // Second object (index 1) is begin-end right setted.
        assertEquals(markerOne, ways.getWay(1).getWayBegin());
        assertEquals(markerThree, ways.getWay(1).getWayEnd());

        ways.addWay(markerOne, markerFour, false);
        // Third object (index 2) is begin-end right setted.
        assertEquals(markerOne, ways.getWay(2).getWayBegin());
        assertEquals(markerFour, ways.getWay(2).getWayEnd());
    }

    @Test
    public void whenAddNewTwoWayThenWaysAreAdded(){
        ways.addWay(markerOne, markerTwo, true);
        // First object (index 0) is begin-end right setted.
        assertEquals(markerOne, ways.getWay(0).getWayBegin());
        assertEquals(markerTwo, ways.getWay(0).getWayEnd());
        // Second object (index 1) is begin-end as end-begin right setted.
        assertEquals(markerTwo, ways.getWay(1).getWayBegin());
        assertEquals(markerOne, ways.getWay(1).getWayEnd());

        ways.addWay(markerOne, markerThree, true);
        // Third object (index 2) is begin-end right setted.
        assertEquals(markerOne, ways.getWay(2).getWayBegin());
        assertEquals(markerThree, ways.getWay(2).getWayEnd());
        // Fourth object (index 3) is begin-end as end-begin right setted.
        assertEquals(markerThree, ways.getWay(3).getWayBegin());
        assertEquals(markerOne, ways.getWay(3).getWayEnd());

        ways.addWay(markerOne, markerFour, true);
        // Fifth object (index 4) is begin-end right setted.
        assertEquals(markerOne, ways.getWay(4).getWayBegin());
        assertEquals(markerFour, ways.getWay(4).getWayEnd());
        // Sixth object (index 5) is begin-end as end-begin right setted.
        assertEquals(markerFour, ways.getWay(5).getWayBegin());
        assertEquals(markerOne, ways.getWay(5).getWayEnd());
    }

    @Test
    public void whenNewWaysAreAddedWhichExistsThenTheyAreNotAdded(){
        ways.addWay(markerOne, markerTwo, false);   // 1. Should be added, one way, index: 0
        ways.addWay(markerTwo, markerThree, true);  // 2. Should be added, two way, index: 1 and 2
        ways.addWay(markerThree, markerFour, false);// 3. Should be added, one way, index: 3
        ways.addWay(markerThree, markerTwo, false); // 4. Shouldn't be added, way exists (index 2)
        ways.addWay(markerOne, markerFour, true);   // 5. Should be added, two way, index: 4 and 5
        ways.addWay(markerTwo, markerOne, true);    // 6. One way should be added, index: 6, but second way exists (index 0)

        // First, index 0
        assertEquals("First object, index 0 begin should be markerOne.", markerOne, ways.getWay(0).getWayBegin());
        assertEquals("First object, index 0 end should be markerTwo.", markerTwo, ways.getWay(0).getWayEnd());

        // Second, index 1 and 2
        assertEquals("Second object, index 1, begin should be markerTwo.", markerTwo, ways.getWay(1).getWayBegin());
        assertEquals("Second object, index 1, end should be markerThree.", markerThree, ways.getWay(1).getWayEnd());
        assertEquals("Second object, index 2, begin should be markerThree.", markerThree, ways.getWay(2).getWayBegin());
        assertEquals("Second object, index 2, end should be markerTwo.", markerTwo, ways.getWay(2).getWayEnd());

        // Third, index 3
        assertEquals("Third object, index 3, begin should be markerThree.", markerThree, ways.getWay(3).getWayBegin());
        assertEquals("Third object, index 3, end should be markerFour.", markerFour, ways.getWay(3).getWayEnd());

        // Fourth shouldn't be added.
        assertNotEquals("Fourth object, begin shouldn't be markerThree.", markerThree, ways.getWay(4).getWayBegin());
        assertNotEquals("Fourth object, end shouldn't be markerTwo.", markerTwo, ways.getWay(4).getWayEnd());

        // Fifth, index 4 and 5
        assertEquals("Fifth object, index 4, begin should be markerOne.", markerOne, ways.getWay(4).getWayBegin());
        assertEquals("Fifth object, index 4, end should be markerFour.", markerFour, ways.getWay(4).getWayEnd());
        assertEquals("Fifth object, index 5, begin should be markerFour.", markerFour, ways.getWay(5).getWayBegin());
        assertEquals("Fifth object, index 5, end should be markerOne.", markerOne, ways.getWay(5).getWayEnd());

        // Six, index 6, but not 7
        assertEquals("Sixth object, index 6, begin should be markerTwo.", markerTwo, ways.getWay(6).getWayBegin());
        assertEquals("Sixth object, index 6, end should be markerOne.", markerOne, ways.getWay(6).getWayEnd());
        // Size 7 - index from 0 to 6, but not 7 (size 8)
        assertEquals("Fifth object, size should be 7.", 7, ways.size());
    }

}