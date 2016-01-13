

//
//    @Test
//    public void whenThreeMarkersAreAddedAndConnectedThenSidesAreStartpointAndMiddleIsntANode(){
//        markers.addMarker(new JSObjectAdapter(), nodes);
//        markers.setCrossDirections(0, directions);
//
//        markers.addMarker(new JSObjectAdapter(), nodes);
//        markers.setCrossDirections(1, directions);
//
//        markers.addMarker(new JSObjectAdapter(), nodes);
//        markers.setCrossDirections(2, directions);
//
//        ways.addWay(markers.getMarker(0), "east", markers.getMarker(1), "west", true, "", 300);
//        System.out.println("first way");
//        ways.addWay(markers.getMarker(1), "east", markers.getMarker(2), "west", true, "", 500);
//        System.out.println("second way");
//        Node firstNode = nodes.getNode(markers.getMarker(0));
//        Node thirdNode = nodes.getNode(markers.getMarker(2));
//
//        assertEquals("Connected modes should be 1 (a third one).", 1, firstNode.getConnectedNodes().markersSize());
//
//        System.out.println(firstNode.toString());
//        System.out.println(thirdNode.toString());
//        System.out.println(nodes.getNode(markers.getMarker(1)));
//        System.out.println(firstNode.getConnectedNodes().entrySet());
//
//        assertTrue("Node from first marker should be connected to third one",
//                firstNode.getConnectedNodes().containsKey(thirdNode));
//
//        assertTrue("Node from third marker should be connected to first one",
//                thirdNode.getConnectedNodes().containsKey(firstNode));
//
////        assertEquals("Second node should be moved to marker 2, not deleted and for marker 2 new created",
////                thirdNode, markers.getNode(markers.getMarker(2)));
//
//        Way way1 = ways.getWay(0);
//        Way way2 = ways.getWay(2);
//
//        ArrayList<Way> listOfWays = nodes.getNode(markers.getMarker(0)).getConnectedNodes().get(
//                nodes.getNode(markers.getMarker(2)));
//
//        assertTrue("List of ways in node should have Way1 and Way2",
//                        listOfWays.contains(way1)
//                        && listOfWays.contains(way2)
//        );
//    }
//}