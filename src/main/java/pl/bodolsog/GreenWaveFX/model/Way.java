package pl.bodolsog.GreenWaveFX.model;

/**
 * Created by bodolsog on 30.12.15.
 */
public class Way {
    private int id;
    private Marker wayBegin;
    private Marker wayEnd;
    private String beginDirection;
    private String endDirection;
    private int distance;
    private Ways ways;
    private String response;

    public Way(Ways ways, int id, Marker begin, String beginDirection,
               Marker end, String endDirection, String response, int distance) {
        this.ways = ways;
        this.id = id;
        wayBegin = begin;
        wayEnd = end;
        this.beginDirection = beginDirection;
        this.endDirection = endDirection;
        this.response = response;
        this.distance = distance;
        addToMarkers();
    }

    public Marker getWayBegin(){
        return wayBegin;
    }
    public Marker getWayEnd(){
        return wayEnd;
    }

    public String getResponse() {
        return response;
    }

    private void addToMarkers() {
        wayBegin.setWay(this, beginDirection);
        wayEnd.setWay(this, endDirection);
    }

    public void destroy() {
        ways.deleteWay(id);
        wayBegin.removeWay(this);
        wayEnd.removeWay(this);
    }
}
