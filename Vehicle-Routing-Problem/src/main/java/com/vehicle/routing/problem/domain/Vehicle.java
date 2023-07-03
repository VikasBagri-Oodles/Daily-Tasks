package com.vehicle.routing.problem.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningListVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.List;

@PlanningEntity
public class Vehicle {

    private Location start;
    private Location end;

    private int locationCount;

    @PlanningListVariable
    private List<Location> route = new ArrayList<>();

    private List<Location> minRoute;

    private Long minDistance = Long.MAX_VALUE;

    public Vehicle() {

    }
    public Vehicle(Location start, Location end, int locationCount) {
        this.start = start;
        this.end = end;
        this.locationCount = locationCount;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    public int getLocationCount() {
        return locationCount;
    }

    public List<Location> getRoute() {
        return route;
    }

    public void setRoute(List<Location> route) {
        this.route = route;
    }

    public Long getMinDistance() {
        return minDistance;
    }

    public List<Location> getMinRoute(){
        return minRoute;
    }

    // gets route distance
    public Long getRouteDistance() {

        if(route.size() < locationCount) {
            return Long.MAX_VALUE;
        }

        Long routeDistance = 0L;

        // Car moving from 'start' to the first location in the 'route'
        routeDistance += start.getDistanceMap().get(route.get(0));

        // Car moving via the current route
        for(int i = 0; i < route.size() - 1; i++) {
            routeDistance += route.get(i).getDistanceMap().get(route.get(i + 1));
        }

        // Car returning back from the last location to the end location
        routeDistance += route.get(route.size() - 1).getDistanceMap().get(end);

        if(routeDistance < minDistance) {
            minDistance = routeDistance;
            updateMinRoute();
        }

        return routeDistance;

    }

    private void updateMinRoute() {
        // first empty the previous min route
        minRoute = new ArrayList<>();
        route.forEach(location -> minRoute.add(location));
    }

}
