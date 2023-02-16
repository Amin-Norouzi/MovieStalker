package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.image.ImageInfo;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.aminnorouzi.ms.tool.image.ImageInfo.*;

@Slf4j
@Component
@FxmlView("/templates/view/movie-view.fxml")
public class MovieController extends Controller {

    @FXML
    private Rectangle posterPic;
    @FXML
    private ImageView backdropPic;
    @FXML
    private Label seasons;
    @FXML
    private Label episodes;
    @FXML
    private Label overview;
    @FXML
    private Label rating;
    @FXML
    private Label release;
    @FXML
    private Label runtime;
    @FXML
    private Label title;
    @FXML
    private Button watch;

    private Movie movie;

    @Value("${movie.client.api.imdb-base-url}")
    private String imdbBaseUrl;

    public MovieController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageLoader image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    protected void configure() {
        movie = (Movie) getInput();

        ImageInfo backdropInfo = new ImageInfo(movie.getBackdrop(), 1280, 832, true, Type.BACKDROP);
        image.load(backdropInfo).thenAccept(image -> backdropPic.setImage(image));

        ImageInfo posterInfo = new ImageInfo(movie.getPoster(), 300, 960, true, Type.POSTER);
        image.load(posterInfo).thenAccept(image -> {
            ImagePattern pattern = new ImagePattern(image);
            posterPic.setFill(pattern);
        });

        title.setText(movie.getTitle());
        release.setText(String.valueOf(movie.getReleased()));
        overview.setText(movie.getOverview());
        rating.setText(String.valueOf(movie.getRating()));
        episodes.setText(String.valueOf(movie.getEpisodes()));
        seasons.setText(String.valueOf(movie.getSeasons()));

        initRuntime(movie);

        if (movie.getWatchedAt() != null) {
            watch.setText("Watched");
        }
    }

    private void initRuntime(Movie movie) {
        int hours = movie.getRuntime() / 60;
        int minutes = movie.getRuntime() % 60;

        StringBuilder builder = new StringBuilder();
        if (hours != 0) {
            builder.append(hours).append("h ");
        }
        if (minutes != 0) {
            builder.append(minutes).append("m");
        }

        runtime.setText(builder.toString());
    }

    @FXML
    private void onImdb(MouseEvent event) {
        try {
            String url = imdbBaseUrl + movie.getImdbId();
            String command = "open " + url;

            Runtime rt = Runtime.getRuntime();
            rt.exec(command);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @FXML
    private void onWatch(ActionEvent event) {
        execute(() -> library.watch(movie));

        watch.setText("Watched");
    }

    @FXML
    private void onUnwatch(ActionEvent event) {
        execute(() -> library.unwatch(movie));

        watch.setText("Watch Now");
    }

    @FXML
    private void onDelete(ActionEvent event) {
        notification.showConfirmation("Are you sure you want to delete this?", () -> {
            try {
                execute(() -> library.delete(movie), View.LIBRARY);
            } catch (RuntimeException exception) {
                exception.printStackTrace();
                notification.showError(exception.getMessage());
            }
        });
    }

    @FXML
    private void onBack(MouseEvent event) {
        switchTo(View.PREVIOUS);
    }
}
