package com.vehicle.routing.problem.matrix;

import java.util.List;

public class Coordinates {

    private List<Point> points;

    public Coordinates() {

    }

    public Coordinates(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

}
