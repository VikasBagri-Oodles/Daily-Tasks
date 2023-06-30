package com.vehicle.routing.problem.util;

import com.vehicle.routing.problem.domain.Location;
import com.vehicle.routing.problem.measurements.Coordinates;
import com.vehicle.routing.problem.measurements.Point;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class LocationFinder {

    public static Location getStartLocation(List<Location> allLocations) {
        // assume first location is always the start and the end location
        return allLocations.get(0);
    }

    public static Location getEndLocation(List<Location> allLocations) {
        // here, first and end locations are same
        // as vehicle has to start from a location and return back to the same location
        return allLocations.get(0);
    }

    public static List<Location> getLocations(List<Location> allLocations) {

        // except the start and end locations
        // fetch all the rest locations from the given coordinates
        List<Location> locations = new ArrayList<>();

        for(int i = 1; i < allLocations.size(); i++) {
            locations.add(allLocations.get(i));
        }

        return locations;

    }

    public static List<Location> getAllLocations(Coordinates coordinates) {

        List<Location> allLocations = new ArrayList<>();
        for(int i = 0; i < coordinates.getPoints().size(); i++) {
            Point point = coordinates.getPoints().get(i);
            Location location = new Location(i + 1L, point.getLat(), point.getLon());
            allLocations.add(location);
        }

        return allLocations;

    }

}
