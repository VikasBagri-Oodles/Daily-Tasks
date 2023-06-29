package com.vehicle.routing.problem.util;

import com.vehicle.routing.problem.domain.Location;
import com.vehicle.routing.problem.measurements.Coordinates;

public class LocationFinder {

    public static Location getStartLocation(Coordinates coordinates) {
        // assume first location is always the start and the end location
        return new Location(1L, coordinates.getPoints().get(0).getLat()
        ,coordinates.getPoints().get(0).getLon());
    }

}
