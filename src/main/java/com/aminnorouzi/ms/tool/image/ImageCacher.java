package com.aminnorouzi.ms.tool.image;

import com.aminnorouzi.ms.tool.view.ViewCacher;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ImageCacher {

    private static final Map<String, Image> cache = new HashMap<>();

    // TODO: load all images on application startup
    public void initialize() {

    }

    public void cache(String key, Image value) {
        cache.computeIfAbsent(key, k -> value);
    }

    public boolean contains(String key) {
        return cache.containsKey(key);
    }

    public Image get(String key) {
        return cache.get(key);
    }

    public void cleanup() {
        cache.clear();
    }
}
