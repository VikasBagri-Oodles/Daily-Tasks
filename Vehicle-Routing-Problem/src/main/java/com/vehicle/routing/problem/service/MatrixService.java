package com.vehicle.routing.problem.service;

import com.vehicle.routing.problem.matrix.Coordinates;
import com.vehicle.routing.problem.matrix.Distance;
import com.vehicle.routing.problem.util.JSONJavaConverter;
import com.vehicle.routing.problem.util.URLMaker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

@Service
public class MatrixService {

    public Distance getDistanceMatrix(Coordinates coordinates) {

        // construct a URL to call the Matrix API provided by the GraphHopper
        String myURL = URLMaker.buildMatrixURL("https://graphhopper.com/api/1/matrix?",
                "type=json&profile=car&out_array=weights&out_array=times&out_array=distances&key=",
                "0b93a8dd-c892-405d-9e26-ec07561d1fe2",
                coordinates);

        // call the Matrix API: returns Distance Matrix
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(myURL)
                .get()
                .build();

        Distance distance = null;
        try{
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            distance = JSONJavaConverter.JSONToJava(jsonString);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        return distance;

    }

}
