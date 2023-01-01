package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.MovieInput;
import com.aminnorouzi.ms.model.movie.Type;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.util.GraphicsManager;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
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
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
@FxmlView("/view/library-view.fxml")
public class LibraryController extends Controller {

    private final Map<Rectangle, Movie> contents = new LinkedHashMap<>();

    private boolean isOnBoth = true;

    @FXML
    private TilePane body;

    public LibraryController(ApplicationContext configuration, ViewSwitcher switcher,
                             NotificationService notificationService, LibraryService libraryService) {
        super(configuration, switcher, notificationService, libraryService);
    }

    private final EventHandler<MouseEvent> mouseClickEventHandler = event -> {
        if (event.getButton() == MouseButton.PRIMARY) {
            Rectangle clickedRectangle = (Rectangle) event.getSource();
            if (contents.containsKey(clickedRectangle)) {
                Movie movie = contents.get(clickedRectangle);
                MovieInput input = MovieInput.of(clickedRectangle, movie);

                getSwitcher().switchTo(View.MOVIE, input);
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

        movies.forEach(movie -> addToScene(movie, false));
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
        getSwitcher().switchTo(View.ADDITION);
    }

    @FXML
    private void onBoth(ActionEvent event) {
        if (!isOnBoth) {
            body.getChildren().clear();

            contents.values().forEach(m-> System.out.println(m.getTitle()));

            body.getChildren().addAll(contents.keySet());
        }
    }

    @FXML
    private void onMovies(ActionEvent event) {
        isOnBoth = false;
        body.getChildren().clear();

        contents.values().forEach(m-> System.out.println(m.getTitle()));

        contents.entrySet().stream()
                .filter(entry -> entry.getValue().getType().equals(Type.MOVIE))
                .forEach(entry -> body.getChildren().add(entry.getKey()));

    }

    @FXML
    private void onSeries(ActionEvent event) {
        isOnBoth = false;
        body.getChildren().clear();

        contents.values().forEach(m-> System.out.println(m.getTitle()));

        contents.entrySet().stream()
                .filter(entry -> entry.getValue().getType().equals(Type.TV))
                .forEach(entry -> body.getChildren().add(entry.getKey()));
    }
}
