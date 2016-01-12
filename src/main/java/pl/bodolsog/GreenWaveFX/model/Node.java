package pl.bodolsog.GreenWaveFX.model;

import pl.bodolsog.GreenWaveFX.staticVar.NODE_TYPE;

import java.util.ArrayList;

/**
 * Created by bodolsog on 12.01.16.
 */
public class Node {

    private int id;
    private Marker marker;
    private ArrayList<Node> connectedNodes;
    private int type;


    public Node(int id, Marker marker) {
        connectedNodes = new ArrayList<>();
        this.id = id;
        this.type = NODE_TYPE.STARTPOINT;
    }

}
