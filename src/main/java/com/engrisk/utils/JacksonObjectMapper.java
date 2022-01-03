package com.engrisk.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonObjectMapper implements kong.unirest.ObjectMapper {
    final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T readValue(String value, Class<T> valueClass) {
        try {
            return mapper.readValue(value, valueClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String writeValue(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
