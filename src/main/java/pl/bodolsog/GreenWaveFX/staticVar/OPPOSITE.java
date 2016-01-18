package pl.bodolsog.GreenWaveFX.staticVar;

import java.util.HashMap;

/**
 * Created by bodolsog on 18.01.16.
 */
public class OPPOSITE {
    public static final String[] N = {DIRECTION.S, DIRECTION.SE, DIRECTION.SW};
    public static final String[] S = {DIRECTION.N, DIRECTION.NE, DIRECTION.NW};
    public static final String[] E = {DIRECTION.W, DIRECTION.NW, DIRECTION.SW};
    public static final String[] W = {DIRECTION.E, DIRECTION.NE, DIRECTION.NW};
    public static final String[] NW = {DIRECTION.SE, DIRECTION.S, DIRECTION.E};
    public static final String[] NE = {DIRECTION.SW, DIRECTION.S, DIRECTION.W};
    public static final String[] SW = {DIRECTION.NE, DIRECTION.N, DIRECTION.E};
    public static final String[] SE = {DIRECTION.NW, DIRECTION.N, DIRECTION.W};

    public static final HashMap<String, String[]> DIRECTIONS = new HashMap<String, String[]>() {{
        put(DIRECTION.N, N);
        put(DIRECTION.S, S);
        put(DIRECTION.E, E);
        put(DIRECTION.W, W);
        put(DIRECTION.NW, NW);
        put(DIRECTION.NE, NE);
        put(DIRECTION.SW, SW);
        put(DIRECTION.SE, SE);
    }};
}
