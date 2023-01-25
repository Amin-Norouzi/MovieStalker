package com.aminnorouzi.ms.tool.view;

import com.aminnorouzi.ms.model.user.User;
import javafx.scene.Parent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ViewCacher {

    private static final Map<CacheKey, Parent> cache = new HashMap<>();

    public void cache(CacheKey key, Parent root) {
        cache.computeIfAbsent(key, k -> root);
    }

    public boolean contains(CacheKey key) {
        return cache.containsKey(key);
    }

    public Parent get(CacheKey key) {
        return cache.get(key);
    }

    public void cleanup() {
        cache.clear();
    }

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class CacheKey {

        private View view;
        private User user;
        private Object input;
    }
}
