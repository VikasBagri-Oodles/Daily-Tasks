package com.vehicle.routing.problem.service;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.util.PointList;
import com.vehicle.routing.problem.matrix.Coordinates;
import com.vehicle.routing.problem.matrix.Distance;
import com.vehicle.routing.problem.matrix.Point;
import com.vehicle.routing.problem.util.JSONJavaConverter;
import com.vehicle.routing.problem.util.URLMaker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class MatrixService {

    public Distance getDistanceMatrix(Coordinates coordinates) {

        String locOSM = "osm_data/northern-zone-latest.osm.pbf";
        GraphHopper hopper = createGraphHopperInstance(locOSM);

        // calculate distance matrix for the given coordinates
        Distance matrix = new Distance();
        matrix.setDistances(new ArrayList<List<Long>>());
        for(int i = 0; i < coordinates.getPoints().size(); i++) {
            List<Long> row = new ArrayList<>();
            for(int j = 0; j < coordinates.getPoints().size(); j++) {
                row.add(routing(hopper, coordinates.getPoints().get(i), coordinates.getPoints().get(j)));
            }
            matrix.getDistances().add(row);
        }

        return matrix;

    }

    /*
    public Distance getDistanceMatrix(Coordinates coordinates) {

        String locOSM = "osm_data/andorra.osm.pbf";
        GraphHopper hopper = createGraphHopperInstance(locOSM);

        // calculate distance matrix for the given coordinates
        Distance matrix = new Distance();
        matrix.setDistances(new ArrayList<List<Long>>());
        for(int i = 0; i < coordinates.getPoints().size(); i++) {
            List<Long> row = new ArrayList<>();
            for(int j = 0; j < coordinates.getPoints().size(); j++) {
                row.add(routing(hopper, coordinates.getPoints().get(i), coordinates.getPoints().get(j)));
            }
            matrix.getDistances().add(row);
        }

        return matrix;

    }
     */

    private GraphHopper createGraphHopperInstance(String ghLoc) {

        GraphHopper hopper = new GraphHopper();
        hopper.setOSMFile(ghLoc);

        hopper.setGraphHopperLocation("target/routing-graph-cache");

        hopper.setProfiles(new Profile("car").setVehicle("car").setWeighting("fastest").setTurnCosts(false));

        hopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));

        hopper.importOrLoad();
        return hopper;

    }

    private long routing(GraphHopper hopper, Point from, Point to) {

        GHRequest req = new GHRequest(from.getLat(), from.getLon(), to.getLat(), to.getLon())
                .setProfile("car")
                .setLocale(Locale.US);

        GHResponse rsp = hopper.route(req);

        if(rsp.hasErrors()) {
            throw new RuntimeException(rsp.getErrors().toString());
        }

        ResponsePath path = rsp.getBest();

        PointList pointList = path.getPoints();
        long distance = (long)path.getDistance();
        long timeInMs = path.getTime();

        return distance;

    }

    /*
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
     */

}
