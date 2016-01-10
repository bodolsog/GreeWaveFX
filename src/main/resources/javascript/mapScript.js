function test(tex) {
    document.getElementById('test').innerHTML = tex;
}

/** Initialize Map */
function initialize() {
    var counter = 0;
    /* Domestic center on Biala Podlaska. */
    var latlng = new google.maps.LatLng(52.03, 23.14);

    var map = new google.maps.Map(document.getElementById("map_canvas"), {
        zoom: 15,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        mapTypeControl: false,
        navigationControl: false,
        streetViewControl: false,
        backgroundColor: "#666970",
        zoomControl: false,
        scaleControl: true
    });

    /* Services */
    //var directionsService = new google.maps.DirectionsService;
    var overlay = new google.maps.OverlayView();
    overlay.draw = function () {};
    overlay.setMap(map);

//            var directionsDisplay = new google.maps.DirectionsRenderer;
//            directionsDisplay.setMap(map);
//            geocoder = new google.maps.Geocoder();
//            service = new google.maps.DistanceMatrixService;
//            directionsService = new google.maps.DirectionsService;
//

//            var menuItem = menu.querySelectorAll(".context-menu__item")
//            for(i=0; i<menuItem.length; i++){
//                menuItem[i].addEventListener('click', function(){
//                    action = ;
//                    if(action == "connect2w" || action == "connect1w"){
//                        controller.connectMarkers(action, activeMarkerId, menuMarkerId);
//                        pointA = markersMap[activeMarkerId].getPosition();
//                        pointB = markersMap[menuMarkerId].getPosition();
//
//                        document.service.getDistanceMatrix({
//                            origins: [pointA],
//                            destinations: [pointB],
//                            travelMode: google.maps.TravelMode.DRIVING,
//                            unitSystem: google.maps.UnitSystem.METRIC,
//                            avoidHighways: true,
//                            avoidTolls: true
//                        }, function(response, status){
//                            if (status !== google.maps.DistanceMatrixStatus.OK) {
//                                test('Error was: ' + status);
//                            } else {
//                                test(response.rows[0].elements[0].distance.value);
//                            }
//                        });
//
//                        document.directionsService.route({
//                            origin: pointA,
//                            destination: pointB,
//                            travelMode: google.maps.TravelMode.DRIVING
//                        }, function(response, status) {
//                            if (status === google.maps.DirectionsStatus.OK) {
//                                document.directionsDisplay.setDirections(response);
//                            } else {
//                                test('Directions request failed due to ' + status);
//                            }
//                        });
//                    }
//                });
//            }


    map.addListener('mousedown', function () {
        if (activeMenu != null)
            if (activeMenu.isActive())
                activeMenu.close();
            else
                activeMenu = null;
    });

    map.addListener('click', function (event) {

        if (activeMenu == null) {
            // Create marker
            var marker = new google.maps.Marker({
                position: event.latLng,
                draggable: true,
                map: map
            });
            marker.crossroadWays = [];

            toggleMarkersIcons(marker);
            findCrossroadPropereties(marker, overlay);

            // Add marker to lists.
            controller.addMarker(marker);

            // Create infowindow
            marker.infowindow = new google.maps.InfoWindow({
                content: infowindowContent(marker)
            });

            // Create context menu
            marker.contextMenu = new ContextMenu(marker, overlay);

            /* Marker listeners */
            marker.addListener('click', function () {

                if (activeMenu == null) {
                    if (!isMarkerActive(marker))
                        setMarkerActive(marker);
                    else
                        marker.infowindow.open(map, marker);
                }
                else {
                    if (activeMenu.isActive()) {
                        activeMenu.close();
                        activeMenu = null;
                    }
                }
            });

            marker.addListener('drag', function () {
                if(activeMenu != null)
                    activeMenu.close();
            });
            marker.addListener('dragend', function () {
                findCrossroadPropereties(this);
            });

            marker.addListener('rightclick', function () {
                this.contextMenu.toggle();
            });
            /* End listeners */
        }
    });
}


/**
 * Return true if Marker is active.
 * @param marker
 * @returns {boolean}
 */
function isMarkerActive(marker) {
    if (marker.id == controller.getActiveMarkerId())
        return true;
    return false;
}

/**
 * Set marker active and toggle old and new marker icon.
 * @param marker
 */
function setMarkerActive(marker) {
    toggleMarkersIcons(marker);
    controller.setActiveMarkerId(marker.id);
}

/**
 * Set active icon for newMarker and get from variable id of oldMarker. If this exists set domestic unactive
 * icon for him.
 * @param newMarker
 */
function toggleMarkersIcons(newMarker) {
    var oldMarker = controller.getActiveMarker();
    if (oldMarker != null)
        setMarkerIconUnactive(oldMarker);
    setMarkerIconActive(newMarker);
}

/**
 * Set domestic active icon for Marker.
 * @param marker
 */
function setMarkerIconActive(marker) {
    setMarkerIcon(marker, 'yellow', 'dot');
}

/**
 * Set domestic unactive icon for Marker.
 * @param marker
 */
function setMarkerIconUnactive(marker) {
    setMarkerIcon(marker, 'red', 'dot');
}

/**
 * Set marker icon.
 * @param marker
 * @param color     One from available colors: [red, yellow]
 * @param emblem    One from available emblems or nothing: [dot]
 */
function setMarkerIcon(marker, color, emblem) {
    if (typeof emblem !== "undefined" || emblem != null)
        emblem = '-' + emblem;
    else
        emblem = '';

    marker.setIcon('markers/' + color + emblem + '.png');
}


/**
 * Create infowidow content.
 * @param marker
 * @returns {string}    HTML content of infowindow.
 */
 function infowindowContent(marker) {
    return "<div class='infowindow-content'>" +
        "   <dl>" +
        "       <dt>Coordinates:</dt>" +
        "       <dd>Lat:" + marker.getPosition().lat() + ", Lng: " + marker.getPosition().lng() + "</dd>" +
        "       <dt>Type:</dt>" +
        "       <dd>" + marker.crossroadWays.length + "-way</dd>" +
        "       <dt>Ways:</dt>" +
        "       <dd>" + marker.crossroadWays.join(", ") + "</dd>" +
        "   </dl>" +
        "</div>";
}


/**
 * Removes and destroys marker.
 * @param marker
 */
function deleteMarker(marker) {
    activeMenu.close();
    if (controller.deleteMarker(marker.id)) {
        marker.contextMenu.removeMenu();    // Remove marker menu from DOM
        marker.setMap(null);                // Remove Marker from Overlay
        marker = null;                      // Remove reference to Marker
    }
}
