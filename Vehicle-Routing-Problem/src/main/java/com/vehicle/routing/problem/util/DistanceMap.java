package com.vehicle.routing.problem.util;

import com.vehicle.routing.problem.domain.Location;
import com.vehicle.routing.problem.measurements.Distance;

import java.util.HashMap;
import java.util.List;

public class DistanceMap {

    public static void buildDistanceMap(List<Location> allLocations, Distance distance) {

        for(int i = 0; i < allLocations.size(); i++) {
            Location from = allLocations.get(i);
            from.setDistanceMap(new HashMap<Location, Long>());
            for(int j = 0; j < allLocations.size(); j++) {
                Location to = allLocations.get(j);
                from.getDistanceMap().put(to, distance.getDistances().get(i).get(j));
            }
        }

    }

}
