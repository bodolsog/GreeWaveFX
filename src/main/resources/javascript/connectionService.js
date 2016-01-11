/**
 * Return options for directionService.
 * @param {latLng} latLngBegin  begin point
 * @param {latLng} latLngEnd    end point
 * @returns {{origin: *, destination: *, travelMode: *}}
 */
function directionsServiceConnectionOptions(latLngBegin, latLngEnd) {
    return {
        origin: latLngBegin,
        destination: latLngEnd,
        travelMode: google.maps.TravelMode.DRIVING
    };
}


/**
 * Return string with direction name.
 * @param {latLng} startPoint   latLng startpoint
 * @param {latLng} endPoint     latLng endpoint
 * @param {number} factor       directional factor
 * @returns {string}
 */
function directionsInterpreter(startPoint, endPoint, factor) {
    var latShift = startPoint.lat() - endPoint.lat();
    var lngShift = startPoint.lng() - endPoint.lng();

    if (Math.abs(factor) >= LIMES_UPPER) {
        if (latShift < 0)
            return "north";
        else
            return "south";
    }
    else if (Math.abs(factor) <= LIMES_LOWER) {
        if (lngShift < 0)
            return "east";
        else
            return "west";
    }
    else {
        if (latShift < 0) {
            if (lngShift < 0)
                return "northeast";
            else
                return "northwest";
        } else {
            if (lngShift < 0)
                return "southeast";
            else
                return "southwest";
        }
    }
}


/**
 * Call setConnection from controller, which add new Way(s) between Markers.
 *
 * @param {Object} endMarker
 * @param {boolean} twoWay   boolean: true - two-way connection or false - one-way connection
 */
function connectMarkers(endMarker, twoWay) {
    var beginMarker = controller.getActiveMarker();     // Get active marker (as begin point).
    var directionsService = new google.maps.DirectionsService;

    directionsService.route(
        directionsServiceConnectionOptions(beginMarker.getPosition(), endMarker.getPosition()), function (response, status) {
            if (status === google.maps.DirectionsStatus.OK) {
                var stepsList = response.routes[0].overview_path;
                var stepsCount = stepsList.length;

                var stepBegin = stepsList[0];
                var stepNextFromBegin = stepsList[1];
                var beginFactor = calculateDirectionalFactor(stepNextFromBegin, stepBegin);
                var beginDirection = directionsInterpreter(stepBegin, stepNextFromBegin, beginFactor);

                var stepEnd = stepsList[stepsCount - 1];
                var stepLastBeforeEnd = stepsList[stepsCount - 2];
                var endFactor = calculateDirectionalFactor(stepLastBeforeEnd, stepEnd);
                var endDirection = directionsInterpreter(stepEnd, stepLastBeforeEnd, endFactor);

                controller.setConnection(beginMarker.id, beginDirection, endMarker.id, endDirection, twoWay, JSON.stringify(response));
            }
            else {
                controller.log("Google Service error: " + status);
            }
        });
}