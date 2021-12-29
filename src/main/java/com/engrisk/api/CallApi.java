package com.engrisk.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.parser.ParseException;

public class CallApi {
    public static final String URL = "http://localhost:8081/api/";

    public static JsonNode get(String route) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.get(URL + route).asJson();
        return apiResponse.getBody();
    }

    public static JsonNode post(String route, String dto) throws UnirestException, ParseException {
        HttpResponse<JsonNode> apiResponse = Unirest.post(URL + route)
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(dto)
                .asJson();
        return apiResponse.getBody();
    }

    public static JsonNode put(String route, JsonNode dto) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.post(URL + route)
                .header("accept", "application/json")
                .body(dto)
                .asJson();
        return apiResponse.getBody();
    }

    public static JsonNode delete(String route, String id) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.post(URL + route)
                .routeParam("id", id)
                .asJson();
        return apiResponse.getBody();
    }
}
