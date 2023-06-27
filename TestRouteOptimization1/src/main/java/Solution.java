import okhttp3.*;
import org.json.JSONObject;

public class Solution {

    public static void main(String[] args) throws Exception {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"vehicles\": [\n    {\n      \"vehicle_id\": \"my_vehicle\",\n      \"start_address\": {\n        \"location_id\": \"berlin\",\n        \"lon\": 13.406,\n        \"lat\": 52.537\n      }\n    }\n  ],\n  \"services\": [\n    {\n      \"id\": \"hamburg\",\n      \"name\": \"visit_hamburg\",\n      \"address\": {\n        \"location_id\": \"hamburg\",\n        \"lon\": 9.999,\n        \"lat\": 53.552\n      }\n    },\n    { \n     \"id\": \"munich\",\n      \"name\": \"visit_munich\",\n      \"address\": {\n        \"location_id\": \"munich\",\n        \"lon\": 11.57,\n        \"lat\": 48.145\n      }\n    }\n  ]}");
        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/vrp?key=0b93a8dd-c892-405d-9e26-ec07561d1fe2")
                .post(body)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        String jsonString = response.body().string();
        JSONObject json = new JSONObject(jsonString);
        System.out.println(json.get("solution"));
        System.out.println("Route Optimization is working fine :)");

    }

}
