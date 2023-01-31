package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.image.ImageService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import com.aminnorouzi.ms.util.GraphicsManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
@FxmlView("/templates/view/library-view.fxml")
public class LibraryController extends Controller {

    private final Map<Rectangle, Movie> contents = new LinkedHashMap<>();

    @FXML
    private TilePane body;


    private final EventHandler<MouseEvent> mouseClickEventHandler = event -> {
        if (event.getButton() == MouseButton.PRIMARY) {
            Rectangle clickedRectangle = (Rectangle) event.getSource();
            if (contents.containsKey(clickedRectangle)) {
                Movie movie = contents.get(clickedRectangle);

                switchTo(View.MOVIE, movie);
            }
        }
    };

    // TODO: contains design part of program
    private final EventHandler<MouseEvent> mouseEnterEventHandler = event -> {
        Rectangle clickedRectangle = (Rectangle) event.getSource();
        if (contents.containsKey(clickedRectangle)) {

            clickedRectangle.setStroke(Color.rgb(59, 96, 228));
            clickedRectangle.setStrokeWidth(3);
            clickedRectangle.setStrokeType(StrokeType.INSIDE);
            clickedRectangle.setStrokeLineCap(StrokeLineCap.ROUND);
            clickedRectangle.setStrokeLineJoin(StrokeLineJoin.ROUND);
            clickedRectangle.setEffect(new DropShadow(35, 10, 15, Color.GRAY));
        }
    };
    private final EventHandler<MouseEvent> mouseExitEventHandler = event -> {
        Rectangle clickedRectangle = (Rectangle) event.getSource();
        if (contents.containsKey(clickedRectangle)) {

            clickedRectangle.setStroke(null);
            clickedRectangle.setStrokeWidth(0);
            clickedRectangle.setStrokeType(null);
            clickedRectangle.setStrokeLineCap(null);
            clickedRectangle.setStrokeLineJoin(null);
            clickedRectangle.setEffect(null);
        }
    };

    public LibraryController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageService image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    protected void configure() {
        User user = getUser();

        List<Movie> movies = library.sort(user.getMovies());
        movies.forEach(movie -> {
            Rectangle rectangle = GraphicsManager.getARectangle();

            body.getChildren().add(rectangle);
            contents.put(rectangle, movie);

            image.load(movie.getPoster()).thenAccept(image -> {
                ImagePattern pattern = new ImagePattern(image);

                rectangle.setFill(pattern);
                rectangle.setCursor(Cursor.HAND);
                rectangle.setOnMouseEntered(mouseEnterEventHandler);
                rectangle.setOnMouseExited(mouseExitEventHandler);
                rectangle.setOnMouseClicked(mouseClickEventHandler);
            });
        });
    }

    @FXML
    private void onAddition(ActionEvent event) {
        switchTo(View.ADDITION);
    }
}
