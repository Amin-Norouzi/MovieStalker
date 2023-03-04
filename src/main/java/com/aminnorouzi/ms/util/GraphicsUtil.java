package com.aminnorouzi.ms.util;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.springframework.stereotype.Component;

@Component
public class GraphicsUtil {

    public static Rectangle roundImage(Image image) {
        Rectangle rectangle = new Rectangle(200, 300);
        rectangle.setArcHeight(30);
        rectangle.setArcWidth(30);

        ImagePattern pattern = new ImagePattern(image);

        rectangle.setFill(pattern);

        return rectangle;
    }

    public static Rectangle getARectangle() {
        Rectangle rectangle = new Rectangle(200, 300);
        rectangle.setFill(Color.WHITE);
        rectangle.setArcHeight(30);
        rectangle.setArcWidth(30);

        return rectangle;
    }
}
