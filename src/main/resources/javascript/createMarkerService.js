// Define static variables
/**
 * Standard dimension.
 * @type {number}
 * @const
 */
var DIMENSION = 0.00009;
/**
 * Smaller dimension factor.
 * @type {number}
 * @const
 */
var SMALLER_FACTOR = 0.55;
/**
 * Bigger dimension factor.
 * @type {number}
 * @const
 */
var BIGGER_FACTOR = 1.6;
/**
 * Upper limes for directional factor.
 * @type {number}
 * @const
 */
var LIMES_UPPER = 2;
/**
 * Lower limes for directional factor.
 * @type {number}
 * @const
 */
var LIMES_LOWER = 0.5;

/**
 * Calculates longitude factor for given latitude. Factor is how many longitude degrees on given latitude is equal in
 * length to one latitude degree.
 * @param {number} lat  latitude
 * @returns {number}
 */
function lngDegreeFactor(lat) {
    // Transform latitude degree to radians.
    var rad = lat * Math.PI / 180;
    // Return factor with 4 digits after coma.
    return Math.round(10000 * Math.cos(rad)) / 10000;
}


/**
 * Create dimension Object with standard, smaller and bigger latitude's and longitude's dimensions for given latitude.
 * @param {number} lat latitude
 * @returns {{lat: number, lng: number, smaller: {lat: number, lng: number}, bigger: {lat: number, lng: number}}}
 */
function generateDimensions(lat) {
    // Save DIMENSION for latitude nad longitude with schema.
    var latDimension = DIMENSION;
    var lngDimension = DIMENSION / lngDegreeFactor(lat);

    return {
        lat: latDimension,
        lng: lngDimension,
        smaller: {
            lat: latDimension * SMALLER_FACTOR,
            lng: lngDimension * SMALLER_FACTOR
        },
        bigger: {
            lat: latDimension * BIGGER_FACTOR,
            lng: lngDimension * BIGGER_FACTOR
        }
    };
}


/**
 * Create directions object that contains arrays for eight world directions. At first two indexes are latLng of marker
 * used to cound direction and function that return boolean if direction factor is right for this direction.
 * @param {latLng} latLngPoint  latLng of center point from where is created directions object
 * @param {Object} dimensions   object dimensions generated by generateDimensions() with reachable lat and lng variables
 *                              (can be dimensions, dimensions.smaller or dimensions.bigger)
 * @returns {{north: *[], south: *[], southwest: *[], northeast: *[], east: *[], west: *[], northwest: *[], southeast: *[]}}
 */
function generateDirections(latLngPoint, dimensions) {
    var lat = latLngPoint.lat();
    var lng = latLngPoint.lng();

    return {
        north: [
            {lat: lat + dimensions.lat, lng: lng},
            function (a) {
                return Math.abs(a) >= LIMES_UPPER ? true : false;
            }
        ],
        south: [
            {lat: (lat - dimensions.lat), lng: lng},
            function (a) {
                return Math.abs(a) >= LIMES_UPPER ? true : false;
            }
        ],
        southwest: [
            {lat: (lat - dimensions.lat), lng: (lng - dimensions.lng)},
            function (a) {
                return Math.abs(a) < LIMES_UPPER && Math.abs(a) > LIMES_LOWER ? true : false;
            }
        ],
        northeast: [
            {lat: (lat + dimensions.lat), lng: (lng + dimensions.lng)},
            function (a) {
                return Math.abs(a) < LIMES_UPPER && Math.abs(a) > LIMES_LOWER ? true : false;
            }
        ],
        east: [
            {lat: lat, lng: (lng + dimensions.lng)},
            function (a) {
                return Math.abs(a) <= LIMES_LOWER ? true : false;
            }
        ],
        west: [
            {lat: lat, lng: (lng - dimensions.lng)},
            function (a) {
                return Math.abs(a) <= LIMES_LOWER ? true : false;
            }
        ],
        northwest: [
            {lat: (lat + dimensions.lat), lng: (lng - dimensions.lng)},
            function (a) {
                return Math.abs(a) < LIMES_UPPER && Math.abs(a) > LIMES_LOWER ? true : false;
            }
        ],
        southeast: [
            {lat: (lat - dimensions.lat), lng: (lng + dimensions.lng)},
            function (a) {
                return Math.abs(a) < LIMES_UPPER && Math.abs(a) > LIMES_LOWER ? true : false;
            }
        ]
    };
}


/**
 * Create waypoints for DirectionsService.
 * @param {Object} dirArr   directions object generated by generateDirections()
 * @returns {[]}            array of waypoints
 */
function generateWaypointsArray(dirArr) {
    return [
        {location: dirArr.north[0], stopover: true},
        {location: dirArr.south[0], stopover: true},
        {location: dirArr.southwest[0], stopover: true},
        {location: dirArr.northeast[0], stopover: true},
        {location: dirArr.east[0], stopover: true},
        {location: dirArr.west[0], stopover: true},
        {location: dirArr.northwest[0], stopover: true}
    ];
}


/**
 * Return options for directionService.
 * @param {latLng} latLng       latLng object with start and end position
 * @param {Object[]} waypoints  array of latLng and options generated by generateWaypointsArray()
 * @returns {{origin: Object, destination: Object, waypoints: Object[], optimizeWaypoints: boolean, travelMode: *}}
 */
function directionsServiceOptions(latLng, waypoints) {
    return {
        origin: latLng,
        destination: latLng,
        waypoints: waypoints,
        optimizeWaypoints: false,
        travelMode: google.maps.TravelMode.DRIVING
    };
}


/**
 * Search for point in center of nearest crossroad.
 * @param {JSON} points         list of points as json from DirectionService response (response.route[0].overview_path)
 * @param {latLng} startPoint   marker position
 * @param {Object} dimensions   smaller dimensions object
 * @returns {latLng}            latLng of center crossroad point
 */
function findCenterPoint(points, startPoint, dimensions) {
    var mostCount = 0;
    var tmpCount;
    var centerPoint = startPoint;
    var pointsLength = points.length;
    var i, j;
    for (i = 0; i < pointsLength; i++) {
        // Check if actual one isn't duplicate a used one and if is in posible location (near click point).
        if ( points[i].lat() != centerPoint.lat() && points[i].lng() != centerPoint.lng() &&
            isNear(points[i], startPoint, dimensions))
        {
            tmpCount = 1;
            // Check only next items from list.
            for (j = i; j < pointsLength; j++) {
                // If i and j object are same, make count imcrement.
                if (points[i].lat() == points[j].lat() && points[i].lng() == points[j].lng()) {
                    tmpCount++;
                }
            }

            // When current occur more times as previously saved - save new object.
            if (tmpCount > mostCount) {
                mostCount = tmpCount;
                centerPoint = points[i];
            }
        }
    }
    return centerPoint;
}


/**
 * Calculates directional factor for direction marker and center of crossroad.
 * @param {latLng} point        latLng object that contains coordinates of route marker
 * @param {latLng} centerPoint  latLng object that contains coordinates of center crossroad point
 * @return {number}             directional factor from center crossroad to marker on route
 */
function calculateDirectionalFactor(point, centerPoint) {
    // Latitude shift. Length of degree latitude is constant.
    var latShift = point.lat() - centerPoint.lat();
    // Longitude shift. Length of degree longitude is dependent on latitude (proportion calculated from lngDegreeFactor.
    var lngShift = (point.lng() - centerPoint.lng()) * lngDegreeFactor(centerPoint.lat());
    return latShift / lngShift;
}


/**
 * Check if given point is in dimension.
 * @param {latLng} point                      latLng object of checked point
 * @param {latLng|latLngLiteral} centerPoint  latLng object or literal of checked from point
 * @param dimensions                          dimension in which should be checked point
 * @returns {boolean}                         is near (true) or not (false)
 */
function isNear(point, centerPoint, dimensions) {
    // Handle two ways of variable (as function or as variable).
    var cPoint;
    if (typeof centerPoint.lat === "function")
        cPoint = {lat: centerPoint.lat(), lng: centerPoint.lng()};
    else
        cPoint = {lat: centerPoint.lat, lng: centerPoint.lng};

    // Check if given point is in DIMENSION.
    if (Math.abs(point.lat() - cPoint.lat) < dimensions.lat && Math.abs(point.lng() - cPoint.lng) < dimensions.lng)
        return true;
    return false;
}


/**
 * Check if bounds are not too wide from center crossroad.
 * @param {Bounds} bounds       bounds from GoogleMaps
 * @param {latLng} centerPoint  center point of crossroad
 * @param {Object} dimensions   dimension object
 * @returns {boolean}
 */
function allMarkersAreInBounds(bounds, centerPoint, dimensions) {
    var north = bounds.getNorthEast().lat();
    var east = bounds.getNorthEast().lng();
    var south = bounds.getSouthWest().lat();
    var west = bounds.getSouthWest().lng();

    if (Math.abs(north - centerPoint.lat()) > dimensions.lat || Math.abs(south - centerPoint.lat()) > dimensions.lat ||
        Math.abs(west - centerPoint.lng()) > dimensions.lng || Math.abs(east - centerPoint.lng()) > dimensions.lng)
        return false;
    return true;
}


/**
 * Set false for all routes, that are out of bounds.
 * @param {Object} directions   directions object
 * @param {JSON} legs           GoogleMaps json respond cutted to legs
 * @param {latLng} centerPoint  center of crossroad
 * @param {Object} dimensions   dimensions object
 */
function removeWaysOutOfBounds(directions, legs, centerPoint, dimensions) {
    var i, j, direction;    // Iterators.
    var steps;              // All steps for current leg. Simplyfy variable.
    var doBreak;            // Break variable for break second loop. If one step is found that goes away from
                            // crossroad, then whole way should be removed.
    var near;               // Temporary variable that contains previous step location (in crossroad area: 1,
                            // outside: -1 and not set yet: 0.
    var leg = 0;            // Current leg, initial to 0.

    for (direction in directions) {
        steps = legs[leg++].steps;  // Set current steps and increment leg.
        near = 0;                   // Reset near.

        // Iterate over every step between start and end location (without start and end).
        for (i = 0; i < steps.length; i++) {
            // Iterate over every point in step.
            for (j = 1; j < steps[i].path.length; j++) {

                /** Check actual road element if is in crossroad area.
                 *
                 * When not - set near variable to false. When near
                 * was not set (0), then iterate continues (way may start outside, but ends inside, and endpoint is for
                 * us important).
                 *
                 * If variable was true and switch to false - break and set way to false.
                 *
                 * If true - sets true (anyway was true or false).
                 */
                if (!isNear(steps[i].path[j], centerPoint, dimensions.bigger)) {
                    if (near == 0) { near = -1; }
                    else if (near == 1) {
                        near = -1; // false
                        doBreak = true;
                        break;
                    }
                } else {
                    near = 1; // true
                }
            }

            // If should break inner iteration, steps iterator should be too.
            if (doBreak) {
                break;
            }
        }

        // If near is false - sets way wariable to false.
        if (near < 0) {
            directions[direction][4] = false;
        }
    }
}


/**
 * Set array of detected way directions to marker.
 * @param {Marker} marker       marker
 * @param {Object} directions   detected directions
 */
function setCrossDirections(marker, directions) {
    var crossroadWays = [];
    for (var direction in directions) {
        if (directions[direction][4]) {
            crossroadWays.push(direction);
        }
    }

    controller.setCrossDirections(marker.id, crossroadWays.join(";"));
    marker.crossroadWays = crossroadWays;
    marker.infowindow.setContent(infowindowContent(marker));
}


/**
 * Calibrate marker and search crossroad type (and ways directions).
 * @param {Marker} marker
 */
function findCrossroadPropereties(marker) {
    var clickPoint = marker.getPosition();
    var directionsService = new google.maps.DirectionsService;

    var dimensions = generateDimensions(clickPoint.lat());
    var clickDirections = generateDirections(clickPoint, dimensions);
    var clickWaypoints = generateWaypointsArray(clickDirections);
    var startEndClickPoint = clickDirections.southeast[0];

    // Create route from startEndPoint via all waypoints in DIMENSION from clickPoint. GoogleMaps should be shift all
    // points from outside road to nearest road. When all roads are connected with stopover flag - route should be
    // leading multiple times by center of crossroad. This point is our Graal.
    directionsService.route(
        directionsServiceOptions(startEndClickPoint, clickWaypoints), function (response, status) {
            if (status === google.maps.DirectionsStatus.OK) {
                // Find centerPoint.
                var centerPoint = findCenterPoint(response.routes[0].overview_path, clickPoint, dimensions.smaller);
                marker.setPosition(centerPoint);

                // Create new directions and waypoints. Dimensions difference should be so small, that not mean.
                var directions = generateDirections(centerPoint, dimensions);
                var waypoints = generateWaypointsArray(directions);
                var startEndPoint = directions.southeast[0];

                directionsService.route(
                    directionsServiceOptions(startEndPoint, waypoints), function (response, status) {
                        if (status === google.maps.DirectionsStatus.OK) {
                            var direction;      // Key for each entry in loop via directions.
                            var currentLeg;     // Sets leg for current.
                            var leg = 0;        // Additional variable for iteration over response (each leg is for one
                                                // direction).
                            var legEndLocation; // Endlocation for every leg (direction).

                            // Set becamed from GoogleMaps latLng of endpoint for each direction (index 2), calculate
                            // directional factor (3) and set true if both are in conditions (4).
                            for (direction in directions) {
                                currentLeg = response.routes[0].legs[leg++];
                                legEndLocation = currentLeg.end_location;
                                // [2] location of marker for direction route.
                                directions[direction].push(legEndLocation);
                                // [3] Directional factor.
                                directions[direction].push(calculateDirectionalFactor(legEndLocation, centerPoint));
                                // [4] True if endlocation is near his inital place and have right directional factor.
                                if (isNear(legEndLocation, directions[direction][0], dimensions.smaller) &&
                                    directions[direction][1](directions[direction][3])) {
                                    directions[direction].push(true);
                                }
                                else { directions[direction].push(false); }
                            }

                            // Check if all bounds are in crossroad area. If not then calculate which routes goes away
                            // and set for them in directions-direction array at index 4 a false value.
                            if (!allMarkersAreInBounds(response.routes[0].bounds, centerPoint, dimensions.bigger))
                                removeWaysOutOfBounds(directions, response.routes[0].legs, centerPoint, dimensions);

                            setCrossDirections(marker, directions);

                        } else {
                            controller.log("Google second response error: " + status);
                        }
                    });
            } else {
                controller.log("Google first response error: " + status);
            }
        });

}