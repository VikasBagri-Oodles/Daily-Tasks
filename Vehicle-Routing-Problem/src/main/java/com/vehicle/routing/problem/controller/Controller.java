package com.vehicle.routing.problem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicle.routing.problem.domain.Location;
import com.vehicle.routing.problem.domain.Vehicle;
import com.vehicle.routing.problem.domain.VehicleRouting;
import com.vehicle.routing.problem.measurements.Coordinates;
import com.vehicle.routing.problem.measurements.Distance;
import com.vehicle.routing.problem.util.DistanceMap;
import com.vehicle.routing.problem.util.JSONJavaConverter;
import com.vehicle.routing.problem.util.LocationFinder;
import com.vehicle.routing.problem.util.URLMaker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/vehicle_routing")
public class Controller {

    @Autowired
    private SolverManager<VehicleRouting, UUID> solverManager;

    @PostMapping("/solve")
    public VehicleRouting solve(@RequestBody Coordinates coordinates) {

        // 1. Fetching all locations(lat/lon) from the incoming request
        /*
        coordinates.getPoints().forEach(point -> System.out.println(point));
         */

        // 2. Send these coordinates to the Matrix API
        //    i.e. a sub API of GraphHopper API

        // First construct the URL to call Matrix API

        String myURL = URLMaker.buildURL("https://graphhopper.com/api/1/matrix?",
                "type=json&profile=car&out_array=weights&out_array=times&out_array=distances&key=",
                "0b93a8dd-c892-405d-9e26-ec07561d1fe2",
                coordinates);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(myURL)
                .get()
                .build();

        Response response = null;
        Distance distance = null;
        try{
            response = client.newCall(request).execute();
            String jsonString = response.body().string();
            System.out.println(jsonString);;
            JSONObject json = new JSONObject(jsonString);

            distance = JSONJavaConverter.JSONToJava(jsonString);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }



        // <----------------- OPTAPLANNER: OPTIMIZATION PART ---------------------->
        // 1. Coordinates
        // 2. Distance matrix


        // location of all the given points
        List<Location> allLocations = LocationFinder.getAllLocations(coordinates);

        // construct distanceMap for each of the given location
        DistanceMap.buildDistanceMap(allLocations, distance);

        // First: get 'start' location of vehicle
        Location startLocation = LocationFinder.getStartLocation(allLocations);

        // Second: get 'end' location of vehicle
        Location endLocation = LocationFinder.getEndLocation(allLocations);

        // Third: get location of all the points where vehicle has to pass from
        List<Location> locations = LocationFinder.getLocations(allLocations);

        // Fourth: Construct a vehicle
        Vehicle vehicle = new Vehicle(startLocation, endLocation, locations.size());

        // Fifth: Construct a VEHICLE ROUTING planning problem
//        VehicleRouting problem = new VehicleRouting(locations, vehicle);
        VehicleRouting problem = new VehicleRouting(locations, vehicle);

        // Sixth: create a random UUID
        UUID uuid = UUID.randomUUID();

        // Seventh: Solve the planning problem using the 'SolverManager'
        SolverJob<VehicleRouting, UUID> solverJob = solverManager.solve(uuid, problem);

        VehicleRouting solution;
        try{
            solution = solverJob.getFinalBestSolution();
        } catch(InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Failed solving :(", e);
        }

        return solution;



    }

}
