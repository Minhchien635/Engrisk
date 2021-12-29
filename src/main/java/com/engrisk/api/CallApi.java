package com.engrisk.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;


public class CallApi {
    public static final String URL = "http://localhost:8081/api/";

    public static String get(String route) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.get(URL + route).asJson();
        return apiResponse.getBody().toString();
    }

    public static JSONObject get(String route, String id) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.get(URL + route)
                .routeParam("id", id)
                .asJson();
        return apiResponse.getBody().getObject();
    }

    public static JSONObject post(String route, String dto) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.post(URL + route)
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(dto)
                .asJson();
        return apiResponse.getBody().getObject();
    }

    public static JSONObject put(String route, JsonNode dto) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.post(URL + route)
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(dto)
                .asJson();
        return apiResponse.getBody().getObject();
    }

    public static JSONObject delete(String route, String id) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.post(URL + route)
                .routeParam("id", id)
                .asJson();
        return apiResponse.getBody().getObject();
    }
}
