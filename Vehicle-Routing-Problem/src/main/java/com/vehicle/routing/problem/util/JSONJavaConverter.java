package com.vehicle.routing.problem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicle.routing.problem.measurements.Distance;

public class JSONJavaConverter {

    public static Distance JSONToJava(String jsonString) {

        ObjectMapper objectMapper = new ObjectMapper();
        Distance distance = null;
        try{
            distance = objectMapper.readValue(jsonString, Distance.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return distance;

    }

}
