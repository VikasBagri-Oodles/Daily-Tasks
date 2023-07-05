package com.vehicle.routing.problem.matrix;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Distance {

    private List<List<Long>> distances;

    public Distance() {

    }

    public Distance(List<List<Long>> distances) {
        this.distances = distances;
    }

    public List<List<Long>> getDistances() {
        return distances;
    }

    public void setDistances(List<List<Long>> distances) {
        this.distances = distances;
    }


}
