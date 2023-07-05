package com.vehicle.routing.problem.controller;

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

    @GetMapping("/reverse_geocoding")
    public void reverseGeocoding() {

        OkHttpClient client = new OkHttpClient();

        String geocodingURL = URLMaker.buildGeocodingURL(
                "https://graphhopper.com/api/1/geocode?",
                "reverse=true&",
                "point=28.4141132,77.0461935&",
                "key=0b93a8dd-c892-405d-9e26-ec07561d1fe2");

        Request request = new Request.Builder()
                .url(geocodingURL)
                .get()
                .build();


        try{
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @PostMapping("/solve")
    public List<Location> solve(@RequestBody Coordinates coordinates) {

        // construct a distance matrix using Distance Matrix API
        Distance distanceMatrix = matrixService.getDistanceMatrix(coordinates);

        // optimize route using OptaPlanner
        return shortestRouteService.getShortestRoute(coordinates, distanceMatrix);

    }

}
