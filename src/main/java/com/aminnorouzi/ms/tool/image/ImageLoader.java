package com.aminnorouzi.ms.tool.image;

import com.aminnorouzi.ms.util.StringUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
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
public class ImageLoader {

    private final ImageCacher cacher;
    private final StringUtil stringUtil;

    @Value("${image.service.url}")
    private String url;

    @Value("${image.service.directory}")
    private String directory;

    @Value("${image.service.format}")
    private String format;

    static {
        // https://stackoverflow.com/a/22742898/11552184
        System.setProperty("java.awt.headless", "false");

        File root = new File("res");
        if (!root.exists()) root.mkdir();
    }

    // TODO: handle exceptions
    public CompletableFuture<Image> load(String fileName, Info info) {
        if (exists(fileName)) {
            return CompletableFuture.supplyAsync(() -> find(fileName, info));
        }

        return CompletableFuture.supplyAsync(() -> download(fileName, info));
    }

    public void load(String fileName, Info info, Rectangle rec) {
        load(fileName, info).thenAccept(image -> rec.setFill(new ImagePattern(image)));
    }

    private Image find(String fileName, Info info) {
        if (cacher.contains(fileName)) {
            return cacher.get(fileName);
        }

        File file = new File(stringUtil.format(directory, fileName));

        Image loaded = new Image(file.toURI().toString(), info.getWidth(), info.getHeight(), true, true);
        if (info.isCacheable()) {
            cacher.cache(fileName, loaded);
        }

        return loaded;
    }

    private Image download(String fileName, Info info) {
        File file = new File(stringUtil.format(directory, fileName));
        try {
            BufferedImage image = ImageIO.read(new URL(stringUtil.format(url, fileName)));
            if (info.isSavable()) {
                ImageIO.write(image, format, file);
            }

            Image downloaded = SwingFXUtils.toFXImage(image, null);
            if (info.isCacheable()) {
                cacher.cache(fileName, downloaded);
            }

            return downloaded;
        } catch (IOException e) {
            System.out.println("exception in image loader class: " + e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    private boolean exists(String filename) {
        File file = new File(stringUtil.format(directory, filename));
        return file.exists();
    }
}
