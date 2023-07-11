package com.vehicle.routing.problem.controller;

import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.vehicle.routing.problem.domain.Location;
import com.vehicle.routing.problem.domain.Vehicle;
import com.vehicle.routing.problem.domain.VehicleRouting;
import com.vehicle.routing.problem.matrix.Coordinates;
import com.vehicle.routing.problem.matrix.Distance;
import com.vehicle.routing.problem.matrix.Point;
import com.vehicle.routing.problem.service.MatrixService;
import com.vehicle.routing.problem.service.ShortestRouteService;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/vehicle_routing")
public class Controller {


    @Autowired
    private MatrixService matrixService;
    @Autowired
    private ShortestRouteService shortestRouteService;

    // controller to get the shortest route
    // responds back with a JSON data
    @PostMapping("/solve")
    public List<Location> solve(@RequestBody Coordinates coordinates) {

        // construct a distance matrix using Distance Matrix API
        Distance distanceMatrix = matrixService.getDistanceMatrix(coordinates);

        // optimize route using OptaPlanner
        return shortestRouteService.getShortestRoute(coordinates, distanceMatrix);

    }

    // controller to get the shortest route
    // responds back with an Encoded Polyline of the shortest route
    @PostMapping("/solve/polyline_encode")
    public String polylineEncode(@RequestBody Coordinates coordinates) {

        // construct a distance matrix using Distance Matrix API
        Distance distanceMatrix = matrixService.getDistanceMatrix(coordinates);

        // optimize route using OptaPlanner
        List<Location> route = shortestRouteService.getShortestRoute(coordinates, distanceMatrix);

        List<LatLng> latLng = new ArrayList<>();
        route.forEach(location -> {
            latLng.add(new LatLng(location.getLat(), location.getLon()));
        });

        EncodedPolyline encodedPolyline = new EncodedPolyline(latLng);

        return encodedPolyline.getEncodedPath();

    }

}
