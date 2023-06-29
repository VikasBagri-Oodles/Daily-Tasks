package com.vehicle.routing.problem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicle.routing.problem.domain.Location;
import com.vehicle.routing.problem.measurements.Coordinates;
import com.vehicle.routing.problem.measurements.Distance;
import com.vehicle.routing.problem.util.JSONJavaConverter;
import com.vehicle.routing.problem.util.LocationFinder;
import com.vehicle.routing.problem.util.URLMaker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicle_routing")
public class Controller {

    @PostMapping("/solve")
    public void solve(@RequestBody Coordinates coordinates) {

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
            JSONObject json = new JSONObject(jsonString);

            distance = JSONJavaConverter.JSONToJava(jsonString);;
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        // <----------------- OPTAPLANNER: OPTIMIZATION PART ---------------------->
        // 1. Coordinates
        // 2. Distance matrix

        // First get 'start' location of vehicle
        Location startLocation = LocationFinder.getStartLocation(coordinates);

    }

}
