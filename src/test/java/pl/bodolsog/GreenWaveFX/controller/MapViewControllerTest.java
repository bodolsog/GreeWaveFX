package pl.bodolsog.GreenWaveFX.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.bodolsog.GreenWaveFX.model.Marker;
import pl.bodolsog.GreenWaveFX.model.Markers;

import static org.junit.Assert.*;


public class MapViewControllerTest {

    private MapViewController ctr;
    private Markers markers;

//    @Before
//    public void setUp(){
//        ctr = new MapViewController();
//        markers = new Markers();
//        ctr.passMarkersReference(markers);
//    }

//    @Test
//    public void whenAddNewMarkerThenItIsInList(){
//        ctr.addMarker(0, 0.0, 0.0);
//        assertEquals("Size of markers should be 1", 1, markers.size());
//        assertTrue("Can get marker with id 0", markers.getMarker(0).getId() == 0);
//
//        ctr.addMarker(1, 0.0, 0.0);
//        assertEquals("Size of markers should be 2", 2, markers.size());
//        assertTrue("Can get marker with id 1", markers.getMarker(1).getId() == 1);
//
//        ctr.addMarker(2, 0.0, 0.0);
//        assertEquals("Size of markers should be 3", 3, markers.size());
//        assertTrue("Can get marker with id 2", markers.getMarker(2).getId() == 2);
//
//        ctr.addMarker(100, 1.0, 1.0);
//        assertEquals("Size of markers should be 4", 4, markers.size());
//        assertTrue("Can get marker with id 100", markers.getMarker(100).getId() == 100);
//
//        assertNull("Not existing object should be null", markers.getMarker(3));
//    }
//
//    @Test
//    public void whenTryAddMarkerWithExistingIdThenMainAppPutsBackNewId(){
//        ctr.addMarker(0, 0.0, 0.0);
//        ctr.addMarker(1, 0.1, 1.0);
//        ctr.addMarker(2, 0.2, 2.0);
//
//        // Doubled id 0.
//        ctr.addMarker(0, 0.3, 3.0);
//
//        assertEquals("Latitude of Marker 0 should be 0.0", 0.0, markers.getMarker(0).getLat(), 0);
//        assertEquals("Longitude of Marker 0 should be 0.0", 0.0, markers.getMarker(0).getLng(), 0);
//
//        assertEquals("Latitude of Marker 2 should be 0.2", 0.2, markers.getMarker(2).getLat(), 0);
//        assertEquals("Longitude of Marker 2 should be 2.0", 2.0, markers.getMarker(2).getLng(), 0);
//
//        assertNotNull("Doubled marker 0 should be marker 3", markers.getMarker(3));
//        assertEquals("Latitude of last Marker should be 0.3", 0.3, markers.getMarker(3).getLat(), 0);
//        assertEquals("Longitude of last Marker should be 3.0", 3.0, markers.getMarker(3).getLng(), 0);
//    }

}