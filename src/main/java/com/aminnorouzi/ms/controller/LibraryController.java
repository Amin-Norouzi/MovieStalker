package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.event.LibraryMouseEventHandler;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.node.MovieNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;
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
    private final Map<MovieNode, Movie> contents = new LinkedHashMap<>();

    @FXML
    private TilePane contentPane;

    public LibraryController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageLoader image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    protected void configure() {
        User user = getUser();

        List<Movie> movies = library.sort(user.getMovies());
        movies.forEach(movie -> {
            MovieNode node = new MovieNode(this, movie);

            contentPane.getChildren().add(node);
            contents.put(node, movie);
        });
    }
}
