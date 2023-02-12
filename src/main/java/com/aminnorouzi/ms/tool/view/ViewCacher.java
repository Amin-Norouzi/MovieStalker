package com.aminnorouzi.ms.tool.view;

import javafx.scene.Parent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ViewCacher {

    private static final Map<CacheKey, Parent> cache = new HashMap<>();

    public void cache(CacheKey key, Parent root) {
        if (!contains(key)) {
            cache.put(key, root);
        }
    }

    // TODO: delete old cache after caching
    private void cleanup(CacheKey key) {
        for (Entry<CacheKey, Parent> entry : cache.entrySet()) {
            if (entry.getKey().getView().equals(key.getView()) && !entry.getKey().equals(key)) {
                cache.remove(entry.getKey());
            }
        }
    }

    public boolean contains(CacheKey key) {
        return cache.containsKey(key) && confirms(key);
    }

    private boolean confirms(CacheKey key) {
        for (Entry<CacheKey, Parent> entry : cache.entrySet()) {
            if (entry.getKey().equals(key)) {
                return Objects.equals(entry.getKey().getTotal(), key.getTotal()) &&
                        Objects.equals(entry.getKey().getWatched(), key.getWatched());
            }
        }

        return false;
    }

    public Parent get(CacheKey key) {
        return cache.get(key);
    }

    public void cleanup() {
        cache.clear();
    }

}
