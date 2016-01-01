package pl.bodolsog.GreenWaveFX.model;

/**
 * Created by bodolsog on 30.12.15.
 */
public class Way {
    private int id;
    private Marker wayBegin;
    private Marker wayEnd;
    private int distance;

    public Way(int id, Marker begin, Marker end){
        this.id = id;
        wayBegin = begin;
        wayEnd = end;
    }


    public Marker getWayBegin(){
        return wayBegin;
    }
    public Marker getWayEnd(){
        return wayEnd;
    }

}
