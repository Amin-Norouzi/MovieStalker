package com.aminnorouzi.ms.tool.image;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${image.service.url}")
    private String url;

    @Value("${image.service.directory}")
    private String directory;

    @Value("${image.service.type}")
    private String type;

    private final ImageCacher cacher;

    static {
        // https://stackoverflow.com/a/22742898/11552184
        System.setProperty("java.awt.headless", "false");
    }

    // TODO: handle exceptions
    public CompletableFuture<Image> load(ImageInfo info) {
        if (exists(info.getName())) {
            return CompletableFuture.supplyAsync(() -> find(info));
        }
        return CompletableFuture.supplyAsync(() -> download(info));
    }

    private Image find(ImageInfo info) {
        if (cacher.contains(info.getName())) {
            return cacher.get(info.getName());
        }

        File file = new File(format(directory, info.getName()));

        Image loaded = new Image(file.toURI().toString(), info.getWidth(), info.getHeight(),
                false, false);
        cacher.cache(info.getName(), loaded);

        return loaded;
    }

    private Image download(ImageInfo info) {
        File file = new File(format(directory, info.getName()));
        try {
            BufferedImage image = ImageIO.read(new URL(format(url, info.getName())));
            if (info.isCacheable()) {
                ImageIO.write(image, type, file);
            }

            Image downloaded = SwingFXUtils.toFXImage(image, null);
            cacher.cache(info.getName(), downloaded);

            return downloaded;
        } catch (IOException e) {
            // TODO handle exception
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean exists(String filename) {
        File file = new File(format(directory, filename));
        return file.exists();
    }

    private String format(String str, Object... args) {
        return String.format(str, args);
    }
}
