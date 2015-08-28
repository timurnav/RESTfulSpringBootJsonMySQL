package ru.timurnav.matcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import ru.timurnav.LoggerWrapper;

import java.io.IOException;
import java.util.List;

/**
 * User: gkislin
 * Date: 30.04.2014
 */
public class JsonUtil {

    private static final LoggerWrapper LOG = LoggerWrapper.get(JsonUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper(){
        return mapper;
    }

    public static <T> List<T> readValues(String json, Class<T> clazz) {
        ObjectReader reader = getMapper().reader(clazz);
        try {
            return reader.<T>readValues(json).readAll();
        } catch (IOException e) {
            throw LOG.getIllegalArgumentException("Invalid read array from JSON:\n'" + json + "'", e);
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return getMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw LOG.getIllegalArgumentException("Invalid read from JSON:\n'" + json + "'", e);
        }
    }

    public static <T> String writeValue(T obj) {
        try {
            return getMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Invalid write to JSON:\n'" + obj + "'", e);
        }
    }
}
