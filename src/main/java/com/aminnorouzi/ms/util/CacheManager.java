package com.aminnorouzi.ms.util;

import com.aminnorouzi.ms.model.View;
import javafx.scene.Parent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CacheManager {

    private static final Map<CacheKey, Parent> caches = new HashMap<>();

    public void cache(CacheKey key, Parent root) {
        caches.computeIfAbsent(key, k -> root);
    }

    public boolean contains(CacheKey key) {
        return caches.containsKey(key);
    }

    public Parent get(CacheKey key) {
        return caches.get(key);
    }

    public void cleanup() {
        caches.clear();
    }

    @Data
    @AllArgsConstructor
    public static class CacheKey {

        private View view;
        private Object input;
    }
}
