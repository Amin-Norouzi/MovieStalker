package com.aminnorouzi.ms.util;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonUtil {

    private final Gson gson;

    public <O> O fromJson(String json, Class<O> clazz) {
        return gson.fromJson(json, clazz);
    }

    public String toJson(Object object) {
        return gson.toJson(object);
    }
}
