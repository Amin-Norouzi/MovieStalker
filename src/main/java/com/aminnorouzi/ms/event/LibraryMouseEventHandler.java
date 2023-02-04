package com.aminnorouzi.ms.event;

import com.aminnorouzi.ms.controller.LibraryController;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.tool.view.View;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LibraryMouseEventHandler implements MouseEventHandler {

    private final LibraryController controller;

    @Override
    public EventHandler<MouseEvent> onMouseClicked() {
        return event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Rectangle clickedRectangle = (Rectangle) event.getSource();
                if (controller.getContents().containsKey(clickedRectangle)) {
                    Movie movie = controller.getContents().get(clickedRectangle);

                    controller.switchTo(View.MOVIE, movie);
                }
            }
        };
    }

    @Override
    public EventHandler<MouseEvent> onMouseEntered() {
        return event -> {
            Rectangle clickedRectangle = (Rectangle) event.getSource();
            if (controller.getContents().containsKey(clickedRectangle)) {

                clickedRectangle.setStroke(Color.rgb(59, 96, 228));
                clickedRectangle.setStrokeWidth(3);
                clickedRectangle.setStrokeType(StrokeType.INSIDE);
                clickedRectangle.setStrokeLineCap(StrokeLineCap.ROUND);
                clickedRectangle.setStrokeLineJoin(StrokeLineJoin.ROUND);
                clickedRectangle.setEffect(new DropShadow(35, 10, 15, Color.GRAY));
            }
        };
    }

    @Override
    public EventHandler<MouseEvent> onMouseExited() {
        return event -> {
            Rectangle clickedRectangle = (Rectangle) event.getSource();
            if (controller.getContents().containsKey(clickedRectangle)) {

                clickedRectangle.setStroke(null);
                clickedRectangle.setStrokeWidth(0);
                clickedRectangle.setStrokeType(null);
                clickedRectangle.setStrokeLineCap(null);
                clickedRectangle.setStrokeLineJoin(null);
                clickedRectangle.setEffect(null);
            }
        };
    }
}
