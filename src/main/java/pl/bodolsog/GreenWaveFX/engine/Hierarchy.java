package pl.bodolsog.GreenWaveFX.engine;

import pl.bodolsog.GreenWaveFX.model.Marker;

/**
 * Created by bodolsog on 17.01.16.
 */
public class Hierarchy {
    private Marker startpoint;
    private Marker endpoint;

    public void addStartpoint(Marker marker) {
        startpoint = marker;
    }

    public void addEndpoint(Marker marker) {
        endpoint = marker;
    }

    public Marker getStartpoint() {
        return startpoint;
    }

    public Marker getEndpoint() {
        return endpoint;
    }
}
