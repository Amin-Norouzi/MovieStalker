package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.function.GenreFunction;
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
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

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

        if (data.getPlaylist().size() != 10) return;

        List<Movie> slider = data.getTrending().subList(0, 5);
        List<Movie> trending = data.getTrending().subList(5, 10);
        List<Movie> watched = data.getPlaylist().subList(0, 5);
        List<Movie> added = data.getPlaylist().subList(5, 10);

        getContent().getChildren().add(1, new SliderNode(slider, this));

        sectionPane.getChildren().addAll(
                new SectionNode(this, "Recently Added to Library", added, new MovieFunction()),
                new SectionNode(this, "Favorite Genres", data.getGenres(), new GenreFunction()),
                new SectionNode(this, "Recently Watched", watched, new MovieFunction()),
                new SectionNode(this, "Trending Movies", trending, new MovieFunction())
        );

        totalLabel.setText(String.valueOf(data.getTotal()));
        watchedLabel.setText(String.valueOf(data.getWatched()));
        todayLabel.setText(String.valueOf(0));

//        TranslateTransition transition = new TranslateTransition();
//        transition.setFromY(350);
//        transition.setToY(0);
//        transition.setDuration(Duration.seconds(0.5));
//        transition.setNode(getContent());
//        transition.play();
    }
}
