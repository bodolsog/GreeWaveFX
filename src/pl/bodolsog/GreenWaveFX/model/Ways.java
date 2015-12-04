package pl.bodolsog.GreenWaveFX.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;

/**
 * Created by bodolsog on 04.12.15.
 */
public class Ways {
    private StringProperty id;
    private Markers begin;
    private Markers end;
    private StringProperty type;
    private IntegerProperty length;
    private HashMap<Integer, Double> duration;
}
