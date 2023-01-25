package com.aminnorouzi.ms.tool.image;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ImageService {

    private String url = "https://image.tmdb.org/t/p/w400%s";
    private String directory = "res%s";
    private String type = "jpg";

    private final ImageCacher imageCacher;

    static {
        // https://stackoverflow.com/a/22742898/11552184
        System.setProperty("java.awt.headless", "false");
    }

    // TODO: handle exceptions
    public CompletableFuture<Image> load(String filename) {
        if (exists(filename)) {
            return CompletableFuture.supplyAsync(()-> find(filename));
        }

        System.out.println("loaded from url");
        return CompletableFuture.supplyAsync(()-> download(filename));
    }

    private Image find(String filename) {
        if (imageCacher.contains(filename)) {
            System.out.println("loaded from cache");
            return imageCacher.get(filename);
        }

        System.out.println("loaded from disk");
        File file = new File(format(directory, filename));

        Image loaded = new Image(file.toURI().toString());
        imageCacher.cache(filename, loaded);

        return loaded;
    }

    private Image download(String filename) {
        File file = new File(format(directory, filename));
        try {
            BufferedImage image = ImageIO.read(new URL(format(url, filename)));
            ImageIO.write(image, type, file);

            Image downloaded = SwingFXUtils.toFXImage(image, null);
            imageCacher.cache(filename, downloaded);

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
