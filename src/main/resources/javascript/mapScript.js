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

    var overlay = new google.maps.OverlayView();
    overlay.draw = function () {};
    overlay.setMap(map);


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
    var directionsDisplay = {};
    var direction;
    var response;
    for (var i = 0; i < marker.crossroadWays.length; i++) {
        direction = marker.crossroadWays[i];
        response = JSON.parse(controller.getActiveMarkerWay(direction));
        if (response != null) {
            directionsDisplay[direction] = new google.maps.DirectionsRenderer;
            directionsDisplay[direction].setMap(marker.map);
            directionsDisplay[direction].setDirections(response);
        }
    }

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
    controller.log(controller.isStartPoint(marker.id));

    if (controller.isStartPoint(marker.id))
        setMarkerIcon(marker, 'green', 'dot');
    else
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
