package com.vehicle.routing.problem.util;

import com.vehicle.routing.problem.matrix.Coordinates;
import com.vehicle.routing.problem.matrix.Point;

public class URLMaker {

    public static String buildURL(String prefix, String suffix, String apiKey, Coordinates coordinates) {

        String url = "";
        for(int i = 0; i < coordinates.getPoints().size(); i++) {
            Point point = coordinates.getPoints().get(i);
            url += "point=" + point.getLat() + "," + point.getLon() + "&";
        }

        url = prefix + url + suffix + apiKey;

        return url;

    }

}
