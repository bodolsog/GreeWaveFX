package pl.bodolsog.GreenWaveFX.model;

/**
 * Created by bodolsog on 30.12.15.
 */
public class Way {
    private int id;
    private Marker wayBegin;
    private Marker wayEnd;
    private int distance;
    private Ways ways;

    public Way(Ways ways, int id, Marker begin, Marker end) {
        this.ways = ways;
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

    public void destroy() {
        ways.deleteWay(id);
        wayBegin.removeWay(this);
        wayEnd.removeWay(this);
    }
}
