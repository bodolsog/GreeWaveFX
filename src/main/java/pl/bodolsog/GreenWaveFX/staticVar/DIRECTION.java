package pl.bodolsog.GreenWaveFX.staticVar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bodolsog on 12.01.16.
 */
public class DIRECTION {
    public static final String N = "N";
    public static final String S = "S";
    public static final String E = "E";
    public static final String W = "W";
    public static final String NW = "NW";
    public static final String NE = "NE";
    public static final String SW = "SW";
    public static final String SE = "SE";

    public static final ArrayList<String> NAMES = new ArrayList<String>() {{
        add(N);
        add(S);
        add(E);
        add(W);
        add(NE);
        add(NW);
        add(SE);
        add(SW);
    }};

    public static final HashMap<String, String> CNAMES = new HashMap<String, String>() {{
        put("north", N);
        put("south", S);
        put("east", E);
        put("west", W);
        put("northeast", NE);
        put("northwest", NW);
        put("southeast", SE);
        put("southwest", SW);
    }};
}
