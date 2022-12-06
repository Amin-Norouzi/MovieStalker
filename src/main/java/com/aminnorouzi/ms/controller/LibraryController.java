package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.service.TaskService;
import com.aminnorouzi.ms.testing.ViewLoader;
import com.aminnorouzi.ms.util.ViewSwitcher;
import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.DialogPane.Dialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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

    private final ViewSwitcher switcher;
    private final ViewLoader viewLoader;
    private final TaskService taskService;

    private final Map<Rectangle, Movie> contents = new HashMap<>();

    @FXML
    private BorderPane root;

    @FXML
    private TilePane content;

    private final DialogPane dialogPane = new DialogPane();

    private final EventHandler<MouseEvent> mouseClickEventHandler = event -> {
        if (event.getButton() == MouseButton.PRIMARY) {
            Rectangle clickedRectangle = (Rectangle) event.getSource();
            if (contents.containsKey(clickedRectangle)) {
                Movie movie = contents.get(clickedRectangle);
                System.out.println(movie.getTitle());

//                onMovieClicked(movie);


//                dialogPane.setAnimateDialogs(true);
//                dialogPane.setFadeInOut(true);
                dialogPane.showInformation("Clicked", movie.getTitle());
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

    public LibraryController(ApplicationConfiguration configuration, ViewSwitcher switcher, MovieService movieService,
                             ViewLoader viewLoader, TaskService taskService) {
        super(configuration, switcher, movieService);
        this.switcher = switcher;
        this.viewLoader = viewLoader;
        this.taskService = taskService;
    }

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

                    content.getChildren().add(rec);
                }
            });
        });

//        viewLoader.getView(movies, content);

//        content.setContent(loader.getView(movies));
//        // close the executor
//        switcher.getStage().setOnCloseRequest(x -> {
//            ViewLoader.executor.shutdown();
//            Platform.exit();
//            System.exit(0);
//        });
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
