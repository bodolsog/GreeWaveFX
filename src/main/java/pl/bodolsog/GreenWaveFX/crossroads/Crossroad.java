package pl.bodolsog.GreenWaveFX.crossroads;

import pl.bodolsog.GreenWaveFX.model.Way;

import java.util.ArrayList;

/**
 * Created by bodolsog on 04.12.15.
 */
abstract public class Crossroad {
    public abstract String getType();

    public abstract void setIncomingWay(String direction, Way way);

    public abstract ArrayList<Integer> calculate(String directionA, String directionB);
}
