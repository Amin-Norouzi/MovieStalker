package com.aminnorouzi.ms.controller.impl;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.controller.Emptiable;
import com.aminnorouzi.ms.function.CategoryFunction;
import com.aminnorouzi.ms.function.MovieFunction;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.MovieRecord;
import com.aminnorouzi.ms.node.SectionNode;
import com.aminnorouzi.ms.node.SliderNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Component
@FxmlView("/templates/view/home-view.fxml")
public class HomeController extends Controller implements Emptiable {

    @FXML
    private Label todayLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label watchedLabel;
    @FXML
    private VBox sectionPane;

    private MovieRecord data;

    public HomeController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageLoader image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    public void configure() {
        List<Movie> movies = getUser().getMovies();

        if (movies.isEmpty()
                || movies.size() < 10
                || movies.stream().filter(m -> m.getWatchedAt() != null).toList().size() < 5) {
            onEmpty(getContent(), this, "Your library doesn't have enough content!");
            return;
        }

        setData(library.report(getUser()));
        if (!data.getIsAvailable()) {
            onEmpty(getContent(), this, "Your library doesn't have enough content!");
            return;
        }

        getContent().getChildren().add(1, new SliderNode(data.getSlider(), this));
        sectionPane.getChildren().addAll(
                new SectionNode(this, "Recently Added to Library", data.getAdded(), new MovieFunction()),
                new SectionNode(this, "Favorite Genres", data.getGenres(), new CategoryFunction()),
                new SectionNode(this, "Recently Watched", data.getPlaylist(), new MovieFunction()),
                new SectionNode(this, "Trending Movies", data.getTrending(), new MovieFunction())
        );

        totalLabel.setText(String.valueOf(data.getTotal()));
        watchedLabel.setText(String.valueOf(data.getWatched()));
        todayLabel.setText(String.valueOf(0));
    }
}
