//package pl.bodolsog.GreenWaveFX.model;
//
//import pl.bodolsog.GreenWaveFX.staticVar.NODE_TYPE;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Node {
//
//    private Marker marker;
//    private int type;
//    private HashMap<Node, ArrayList<Way>> connectedNodes;
//    private Nodes nodes;
//
//
//    public Node(Marker marker, Nodes nodes) {
//        this.nodes = nodes;
//        this.marker = marker;
//        connectedNodes = new HashMap<Node, ArrayList<Way>>();
//
//        if(marker.isStartPoint())
//            setStartpointType();
//        else
//            setCrossroadType();
//
//        updateWays();
//    }
//
//
//    public void updateWays(){
//        ArrayList<Way> markerWays = marker.getCrossWays();
//        markerWays.forEach(way -> {
//            ArrayList<Way> ways = new ArrayList<Way>();
//            Node node = findWay(way, ways);
//            connectedNodes.put(node, ways);
//        });
//    }
//
//    private Node findWay(Way way, ArrayList<Way> ways){
//        ArrayList<Way> cross = way.getWayEnd().getCrossWays();
//        if(cross.markersSize() == 2){
//            Way nextWay = cross.stream().filter(w -> w != way).findFirst().get();
//            ways.add(nextWay);
//            return findWay(nextWay, ways);
//        }
//        return nodes.getNode(way.getWayEnd());
//    }
//
//    public void setType(String type){
//        if(type.equals("startpoint")){
//            this.type = NODE_TYPE.STARTPOINT;
//        }
//        else if(type.equals("crossroad")){
//            this.type = NODE_TYPE.CROSSROAD;
//        }
//    }
//    public void setStartpointType(){
//        type = NODE_TYPE.STARTPOINT;
//    }
//    public void setCrossroadType(){
//        type = NODE_TYPE.CROSSROAD;
//    }
//    public int getType(){
//        return type;
//    }
//    public String getTypeAsText(){
//        if(type == NODE_TYPE.STARTPOINT)
//            return "startpoint";
//        else if(type == NODE_TYPE.CROSSROAD)
//            return "crossroad";
//        else
//            return "unknown";
//    }
//
//    public HashMap<Node, ArrayList<Way>> getConnectedNodes(){
//        return connectedNodes;
//    }
//
//}
