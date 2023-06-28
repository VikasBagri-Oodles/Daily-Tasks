package com.vehicle.routing.problem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicle.routing.problem.measurements.Coordinates;
import com.vehicle.routing.problem.measurements.Distance;
import com.vehicle.routing.problem.util.JSONJavaConverter;
import com.vehicle.routing.problem.util.URLMaker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicle_routing")
public class Controller {

    @PostMapping("/solve")
    public Response solve(@RequestBody Coordinates coordinates) {

        // 1. Fetching the points from the incoming request
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
        try{
            response = client.newCall(request).execute();
            String jsonString = response.body().string();
            JSONObject json = new JSONObject(jsonString);

            Distance distance = JSONJavaConverter.JSONToJava(jsonString);;
            distance.getDistances().forEach(row -> {
                for(int i = 0; i < row.size(); i++) {
                    System.out.print(row.get(i) + " ");
                }
                System.out.println();
            });
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        return response;

    }

}
