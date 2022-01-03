package com.engrisk.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;

public class CallApi {
    public static String get(String route) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.get(route).asJson();
        return apiResponse.getBody().toString();
    }

    public static JSONObject get(String route, String id) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.get(route)
                                                    .routeParam("id", id)
                                                    .asJson();
        return apiResponse.getBody().getObject();
    }

    public static JSONObject post(String route, Object dto) throws JsonProcessingException, UnirestException {
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(dto);
        HttpResponse<JsonNode> apiResponse = Unirest.post(route)
                                                    .body(body)
                                                    .asJson();
        return apiResponse.getBody().getObject();
    }

    public static JSONObject put(String route, Object dto) throws JsonProcessingException, UnirestException {
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(dto);
        HttpResponse<JsonNode> apiResponse = Unirest.put(route)
                                                    .body(dto)
                                                    .asJson();
        return apiResponse.getBody().getObject();
    }

    public static JSONObject post(String route, String dto) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.post(route)
                                                    .body(dto)
                                                    .asJson();
        return apiResponse.getBody().getObject();
    }

    public static JSONObject put(String route, String dto) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.put(route)
                                                    .body(dto)
                                                    .asJson();
        return apiResponse.getBody().getObject();
    }

    public static JSONObject delete(String route, String id) throws UnirestException {
        HttpResponse<JsonNode> apiResponse = Unirest.delete(route)
                                                    .routeParam("id", id)
                                                    .asJson();
        return apiResponse.getBody().getObject();
    }
}
