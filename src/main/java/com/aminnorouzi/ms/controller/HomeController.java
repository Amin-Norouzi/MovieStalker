package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.function.GenreFunction;
import com.aminnorouzi.ms.function.MovieFunction;
import com.aminnorouzi.ms.function.SearchFunction;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.MovieRecord;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.node.*;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@FxmlView("/templates/view/home-view.fxml")
public class HomeController extends Controller {

    @FXML
    private Label todayLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label watchedLabel;
    @FXML
    private VBox sectionPane;

    public HomeController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageLoader image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    public void configure() {
        if (getUser().getMovies().isEmpty()) {
            return;
        }

        MovieRecord data = library.report(getUser());

        List<Movie> sliders = data.getPlaylist().stream().limit(5).toList();
        List<Movie> trending = data.getTrending().stream().limit(5).toList();

        getContent().getChildren().add(1, new SliderNode(sliders, this));

        sectionPane.getChildren().addAll(
                new SectionNode(this, "Recently Added to Library", sliders, new MovieFunction()),
                new SectionNode(this, "Favorite Genres", data.getGenres(), new GenreFunction()),
                new SectionNode(this, "Recently Watched", sliders, new MovieFunction()),
                new SectionNode(this, "Trending Movies", trending, new MovieFunction())
        );

        totalLabel.setText(String.valueOf(data.getTotal()));
        watchedLabel.setText(String.valueOf(data.getWatched()));
        todayLabel.setText(String.valueOf(0));

        TranslateTransition transition = new TranslateTransition();
        transition.setFromY(350);
        transition.setToY(0);
        transition.setDuration(Duration.seconds(0.5));
        transition.setNode(getContent());
        transition.play();
    }
}
