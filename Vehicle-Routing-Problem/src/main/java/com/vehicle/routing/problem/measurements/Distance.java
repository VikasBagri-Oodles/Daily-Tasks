package com.vehicle.routing.problem.measurements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Distance {

    private List<List<Integer>> distances;

    public Distance() {

    }

    public Distance(List<List<Integer>> distances) {
        this.distances = distances;
    }

    public List<List<Integer>> getDistances() {
        return distances;
    }

    public void setDistances(List<List<Integer>> distances) {
        this.distances = distances;
    }


}
