package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.service.*;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
            Movie movie = contents.get(clickedRectangle);
            System.out.println("Mouse entered: " + movie.getTitle());

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
            Movie movie = contents.get(clickedRectangle);
            System.out.println("Mouse exited: " + movie.getTitle());

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
        List<Movie> movies = getUser().getMovies();
        System.out.println(movies.size());
//            MovieTask task = new MovieTask(movie, content);
        movies.forEach(movie -> {
//            Task task = new Task() {
//                final ImageView imageView = new ImageView();
//
//                @Override
//                protected Object call() throws Exception {
//                    imageView.setFitHeight(300);
//                    imageView.setFitWidth(200);
////                    imageView.setPreserveRatio(true);
//                    imageView.setImage(new Image("https://image.tmdb.org/t/p/original" + movie.getPoster())); // 2000*3000
//
//                    return null;
//                }
//
//                @Override
//                protected void succeeded() {
//                    super.succeeded();
//                    Platform.runLater(() -> {
//                        contents.put(imageView, movie);
//
//                        ComponentUtils.roundImage(imageView);
//                        imageView.setOnMouseClicked(mouseEventEventHandler);
//
//                        content.getChildren().add(imageView);
//                    });
//                }
//            };
//
//            taskService.run(task);

            Image image = new Image("https://image.tmdb.org/t/p/w400" + movie.getPoster(),
                    200, 300, false, false, true);
            image.progressProperty().addListener((observable, oldValue, progress) -> {
                if ((Double) progress == 1.0 && !image.isError()) {
                    Rectangle rec = new Rectangle(200, 300);
                    rec.setArcHeight(30);
                    rec.setArcWidth(30);

                    ImagePattern pattern = new ImagePattern(image);

                    rec.setFill(pattern);
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
        switcher.switchTo(View.ADDITION, getUser());
    }

    @FXML
    private void onOpen(ActionEvent event) {
//        switcher.switchTo(View.MOVIE, getUser());
        switcher.switchTo(View.MOVIE, new Movie());
    }
}
