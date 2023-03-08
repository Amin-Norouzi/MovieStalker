package com.aminnorouzi.ms.controller.impl;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.controller.Emptiable;
import com.aminnorouzi.ms.function.MovieFunction;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.node.MovieNode;
import com.aminnorouzi.ms.node.SectionNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import lombok.Getter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
@FxmlView("/templates/view/library-view.fxml")
public class LibraryController extends Controller implements Emptiable {

    private final Map<MovieNode, Movie> contents = new LinkedHashMap<>();

    public LibraryController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageLoader image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    protected void configure() {
        if (getUser().getMovies().isEmpty()) {
            onEmpty(getContent(), this, "Your library has no movies!");
            return;
        }

        List<Movie> movies = library.sort(getUser().getMovies());
        supply(new SectionNode(this, "Your Library", false, movies, new MovieFunction()));
    }
}
