package com.aminnorouzi.ms.controller.impl;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import com.aminnorouzi.ms.util.GraphicsManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
import lombok.Getter;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
@FxmlView("/view/library-view.fxml")
public class LibraryController extends Controller {

//    private final LibraryTaskService task = new LibraryTaskService();

    private final Map<Rectangle, Movie> contents = new LinkedHashMap<>();

    @FXML
    private TilePane body;

    public LibraryController(ApplicationContext context, ViewSwitcher switcher, NotificationService notification,
                             LibraryService library, ActivityService activity) {
        super(context, switcher, notification, library, activity);
    }

    private final EventHandler<MouseEvent> mouseClickEventHandler = event -> {
        if (event.getButton() == MouseButton.PRIMARY) {
            Rectangle clickedRectangle = (Rectangle) event.getSource();
            if (contents.containsKey(clickedRectangle)) {
                Movie movie = contents.get(clickedRectangle);

                switchTo(View.MOVIE, movie);
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
        contents.clear();

        User user = getUser();
        List<Movie> movies = user.getMovies();
        movies.forEach(movie -> {
            int index = movies.indexOf(movie) + 1;
            System.out.println("#" + index + " " + movie.getTitle());
        });

        System.out.println();

        movies.forEach(movie -> {
//            addToScene(movie, false);
            LibraryTaskService task = new LibraryTaskService();
            task.setMovie(movie);
            task.restart();
        });
    }

    public void addToScene(Movie movie, boolean index) {
        Image image = new Image(movie.getPoster(), 200, 300, false, false, true);
        image.progressProperty().addListener((observable, oldValue, progress) -> {
            if ((Double) progress == 1.0 && !image.isError()) {
                Rectangle rec = GraphicsManager.roundImage(image);
                rec.setCursor(Cursor.HAND);

                rec.setOnMouseEntered(mouseEnterEventHandler);
                rec.setOnMouseExited(mouseExitEventHandler);
                rec.setOnMouseClicked(mouseClickEventHandler);

                contents.put(rec, movie);

                if (index) {
                    body.getChildren().add(0, rec);
                    return;
                }

                body.getChildren().add(rec);
            }
        });
    }

    @FXML
    private void onAddition(ActionEvent event) {
        switchTo(View.ADDITION);
    }

    @Getter
    @Setter
    public class LibraryTaskService extends Service<Rectangle> {

        private Movie movie;

        @Override
        protected Task<Rectangle> createTask() {
            return new Task<>() {

                @Override
                protected Rectangle call() throws Exception {
//                    Image image = new Image(movie.getPoster(), 200, 300, false, false, false);
//                    return GraphicsManager.roundImage(image);
                    return new Rectangle();
                }

                @Override
                protected void succeeded() {
                    Rectangle rec = getValue();
                    rec.setCursor(Cursor.HAND);
                    rec.setOnMouseEntered(mouseEnterEventHandler);
                    rec.setOnMouseExited(mouseExitEventHandler);
                    rec.setOnMouseClicked(mouseClickEventHandler);

                    contents.put(rec, movie);
                    body.getChildren().add(rec);
                    System.out.println(movie.getTitle() + " added to scene.");
                }
            };
        }
    }
}
