package com.vehicle.routing.problem.measurements;

public class Point {

    private double lat;
    private double lon;

    public Point() {

    }

    public Point(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "lat : " + lat + ", lon : " + lon;
    }



}
