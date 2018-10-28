package com.tyuhgce.LoweAlpine.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Help class to convert object to json for logging
 *
 * @version 1.0
 * @since 1.0
 */
public class Mapper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toString(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
