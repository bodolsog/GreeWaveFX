package pl.bodolsog.GreenWaveFX.staticVar;

import java.util.ArrayList;

/**
 * Created by bodolsog on 12.01.16.
 */
public class DIRECTIONS {
    public static final String NORTH = "NORTH";
    public static final String SOUTH = "SOUTH";
    public static final String EAST = "EAST";
    public static final String WEST = "WEST";
    public static final String NORTHWEST = "NORTHWEST";
    public static final String NORTHEAST = "NORTHEAST";
    public static final String SOUTHWEST = "SOUTHWEST";
    public static final String SOUTHEAST = "SOUTHEAST";

    public static final ArrayList<String> NAMES = new ArrayList<String>() {{
        add(NORTH);
        add(SOUTH);
        add(EAST);
        add(WEST);
        add(NORTHEAST);
        add(NORTHWEST);
        add(SOUTHEAST);
        add(SOUTHWEST);
    }};

}
