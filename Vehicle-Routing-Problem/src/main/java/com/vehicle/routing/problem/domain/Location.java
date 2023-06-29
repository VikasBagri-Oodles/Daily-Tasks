package com.vehicle.routing.problem.domain;

import java.util.Map;

public class Location {

    private Long id;
    private double lat;
    private double lon;
    private Map<Location, Long> distanceMap;

    public Location(Long id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public Long getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public Map<Location, Long> getDistanceMap() {
        return distanceMap;
    }

    public void setDistanceMap(Map<Location, Long> distanceMap) {
        this.distanceMap = distanceMap;
    }

}
