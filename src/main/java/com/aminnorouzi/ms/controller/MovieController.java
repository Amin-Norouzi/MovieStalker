package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.MovieInput;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.util.view.View;
import com.aminnorouzi.ms.util.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FxmlView("/view/movie-view.fxml")
public class MovieController extends Controller {

    @FXML
    private Rectangle poster;
    @FXML
    private ImageView backdrop;
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

    private final LibraryController libraryController;

    private MovieInput input;
    private Movie movie;

    @Value("${movie.client.api.imdb-base-url}")
    private String imdbBaseUrl;

    public MovieController(ApplicationContext configuration, ViewSwitcher switcher, NotificationService notificationService,
                             LibraryService libraryService, LibraryController libraryController) {
        super(configuration, switcher, notificationService, libraryService);
        this.libraryController = libraryController;
    }

    @Override
    protected void configure() {
        input = (MovieInput) getInput();
        movie = input.getMovie();

        initMovie(movie);
    }

    private void initMovie(Movie movie) {
        initImages(movie);

        title.setText(movie.getTitle());
        release.setText(String.valueOf(movie.getReleased()));
        overview.setText(movie.getOverview());
        rating.setText(String.valueOf(movie.getRating()));
        episodes.setText(String.valueOf(movie.getEpisodes()));
        seasons.setText(String.valueOf(movie.getSeasons()));

        initRuntime(movie);

        if (movie.getWatchedAt() != null) {
            changeState(true);
        }
    }

    private void initImages(Movie movie) {
        Image backdropImage = new Image(movie.getBackdrop(),
                800, 600, false, false, true);
        backdropImage.progressProperty().addListener((observable, oldValue, progress) -> {
            if ((Double) progress == 1.0 && !backdropImage.isError()) {
                backdrop.setImage(backdropImage);
            }
        });

        Image posterImage = new Image(movie.getPoster(),
                200, 300, false, false, true);

        posterImage.progressProperty().addListener((observable, oldValue, progress) -> {
            if ((Double) progress == 1.0 && !posterImage.isError()) {
                ImagePattern pattern = new ImagePattern(posterImage);

                poster.setFill(pattern);
            }
        });
    }

    private void initRuntime(Movie movie) {
        int hours = movie.getRuntime() / 60;
        int minutes = movie.getRuntime() % 60;

        StringBuilder builder = new StringBuilder();
        if (hours != 0) {
            builder.append(hours).append("h ");
        }
        builder.append(minutes).append("m");

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
        libraryService.watch(movie);
        changeState(true);
    }

    @FXML
    private void onUnwatch(ActionEvent event) {
        libraryService.unwatch(movie);
        changeState(false);
    }

    private void changeState(boolean watched) {
        if (watched) {
            watch.setDisable(true);
            watch.setText("Watched");
        } else {
            watch.setDisable(false);
            watch.setText("Watch Now");
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        notificationService.showConfirmation("Are you sure you want to delete this?", () -> {
            try {
                libraryService.delete(movie);
                libraryController.getContents().remove(input.getRectangle());

                getSwitcher().switchTo(View.LIBRARY);
            } catch (RuntimeException exception) {
                notificationService.showError(exception.getMessage());
            }
        });
    }

    @FXML
    private void onBack(MouseEvent event) {
        getSwitcher().switchTo(View.LIBRARY);
    }
}
