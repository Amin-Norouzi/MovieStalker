package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.service.*;
import com.aminnorouzi.ms.util.ComponentUtils;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@FxmlView("/view/library-view.fxml")
public class LibraryController extends Controller {

    private final Map<Rectangle, Movie> contents = new HashMap<>();

    @FXML
    private TilePane body;

    public LibraryController(ApplicationConfiguration configuration, ViewSwitcher switcher, FileService fileService,
                             NotificationService notificationService, MovieService movieService, UserService userService,
                             LibraryService libraryService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService, libraryService);
    }

    private final EventHandler<MouseEvent> mouseClickEventHandler = event -> {
        if (event.getButton() == MouseButton.PRIMARY) {
            Rectangle clickedRectangle = (Rectangle) event.getSource();
            if (contents.containsKey(clickedRectangle)) {
                Movie movie = contents.get(clickedRectangle);

                onMovieClicked(movie);
            }
        }
    };

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

    @Override
    protected void configure() {
        Set<Movie> movies = getUser().getMovies();
        System.out.println(movies.size());

        movies.forEach(movie -> {
            Image image = new Image("https://image.tmdb.org/t/p/w400" + movie.getPoster(),
                    200, 300, false, false, true);
            image.progressProperty().addListener((observable, oldValue, progress) -> {
                if ((Double) progress == 1.0 && !image.isError()) {
                    Rectangle rec = ComponentUtils.roundImage(image);
                    rec.setCursor(Cursor.HAND);

                    rec.setOnMouseEntered(mouseEnterEventHandler);
                    rec.setOnMouseExited(mouseExitEventHandler);
                    rec.setOnMouseClicked(mouseClickEventHandler);

                    contents.put(rec, movie);

                    body.getChildren().add(rec);
                }
            });
        });
    }

    private void onMovieClicked(Movie movie) {
        switcher.switchTo(View.MOVIE, movie);
    }

    @FXML
    private void onAddition(ActionEvent event) {
        switcher.switchTo(View.ADDITION);
    }
}
