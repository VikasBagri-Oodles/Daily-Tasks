package com.vehicle.routing.problem.service;

import com.vehicle.routing.problem.domain.Location;
import com.vehicle.routing.problem.domain.Vehicle;
import com.vehicle.routing.problem.domain.VehicleRouting;
import com.vehicle.routing.problem.matrix.Coordinates;
import com.vehicle.routing.problem.matrix.Distance;
import com.vehicle.routing.problem.matrix.Point;
import com.vehicle.routing.problem.util.DistanceMap;
import com.vehicle.routing.problem.util.LocationFinder;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class ShortestRouteService {

    @Autowired
    private SolverManager<VehicleRouting, UUID> solverManager;
    public List<Location> getShortestRoute(Coordinates coordinates, Distance distance) {

        // location of all the given points
        List<Location> allLocations = LocationFinder.getAllLocations(coordinates);

        // construct distanceMap for each of the given location
        DistanceMap.buildDistanceMap(allLocations, distance);

        // 'start' location of vehicle
        Location startLocation = LocationFinder.getStartLocation(allLocations);

        // 'end' location of vehicle
        Location endLocation = LocationFinder.getEndLocation(allLocations);

        // get location of all the points where vehicle has to pass from
        List<Location> locations = LocationFinder.getLocations(allLocations);

        // construct a vehicle
        Vehicle vehicle = new Vehicle(startLocation, endLocation, locations.size());

        // construct a VEHICLE ROUTING planning problem
        VehicleRouting problem = new VehicleRouting(locations, vehicle);

        // create a random UUID
        UUID uuid = UUID.randomUUID();

        // solve the planning problem using the 'SolverManager'
        SolverJob<VehicleRouting, UUID> solverJob = solverManager.solve(uuid, problem);

        VehicleRouting solution;
        try{
            solution = solverJob.getFinalBestSolution();
        } catch(InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Failed solving :(", e);
        }

        List<Location> shortestRoute = solution.getVehicle().getMinRoute();

        // adding 'start' and 'end' locations
        shortestRoute.add(0, startLocation);
        shortestRoute.add(shortestRoute.size(), endLocation);

        return shortestRoute;

    }

}
