package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.event.LibraryMouseEventHandler;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.image.ImageInfo;
import com.aminnorouzi.ms.tool.image.ImageService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import com.aminnorouzi.ms.util.GraphicsManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
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

    private final LibraryMouseEventHandler handler = new LibraryMouseEventHandler(this);
    private final Map<Rectangle, Movie> contents = new LinkedHashMap<>();

    @FXML
    private TilePane body;

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

            ImageInfo posterInfo = new ImageInfo(movie.getPoster(), 300, 960, true);
            image.load(posterInfo).thenAccept(image -> {
                ImagePattern pattern = new ImagePattern(image);

                rectangle.setFill(pattern);
                rectangle.setCursor(Cursor.HAND);

                rectangle.setOnMouseEntered(handler.onMouseEntered());
                rectangle.setOnMouseExited(handler.onMouseExited());
                rectangle.setOnMouseClicked(handler.onMouseClicked());
            });
        });
    }

    @FXML
    private void onAddition(ActionEvent event) {
        switchTo(View.ADDITION);
    }
}
