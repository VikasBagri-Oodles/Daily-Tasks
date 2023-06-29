package com.vehicle.routing.problem.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class VehicleRouting {

    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<Location> locationList;

    @PlanningEntityProperty
    private Vehicle vehicle;

    @PlanningScore
    private HardSoftScore score;

    public VehicleRouting() {

    }

    public VehicleRouting(List<Location> locationList, Vehicle vehicle) {
        this.locationList = locationList;
        this.vehicle = vehicle;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public HardSoftScore getScore() {
        return score;
    }

}
