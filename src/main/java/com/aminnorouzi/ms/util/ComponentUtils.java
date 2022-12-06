package com.aminnorouzi.ms.util;

import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.springframework.stereotype.Component;

@Component
public class ComponentUtils {

    public static void roundImage(ImageView imageView) {
        Rectangle clip = new Rectangle(400, 600);

        clip.setArcWidth(30);
        clip.setArcHeight(30);
        imageView.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage newImage = imageView.snapshot(parameters, null);

        imageView.setClip(null);
//        imageView.setEffect(new DropShadow(5, Color.BLACK));
        imageView.setImage(newImage);
    }
}
