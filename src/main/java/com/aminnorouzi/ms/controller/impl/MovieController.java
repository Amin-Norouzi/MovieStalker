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

    private Movie movie;

    @Value("${movie.client.api.imdb-base-url}")
    private String imdbBaseUrl;

    public MovieController(ApplicationContext context, ViewSwitcher switcher, NotificationService notification,
                           LibraryService library, ActivityService activity) {
        super(context, switcher, notification, library, activity);
    }


    @Override
    protected void configure() {
        movie = (Movie) getInput();

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

    // TODO: there is a bug here. if we don't set the updated user from service
    // to the controller, the view cacher will not work as expected.

    // here it will properly
    @FXML
    private void onWatch(ActionEvent event) {
//        execute(new Task<>() {
//            @Override
//            protected User call() throws Exception {
//                changeState(true);
//                User user = libraryService.watch(movie);
//                user.setFullName("new full name is amin");
//                return user;
//            }
//        });
        User current = getUser();

        User updated = library.watch(movie);

        if (current.equals(updated)) {
            System.out.println("equals");
        }
        changeState(false);
    }

    @FXML
    private void onUnwatch(ActionEvent event) {
        // but here it won't update the user object
        User updated = library.unwatch(movie);

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
        notification.showConfirmation("Are you sure you want to delete this?", () -> {
            try {
                library.delete(movie);

                switchTo(View.LIBRARY);
            } catch (RuntimeException exception) {
                notification.showError(exception.getMessage());
            }
        });
    }

    @FXML
    private void onBack(MouseEvent event) {
        switchTo(View.LIBRARY);
    }
}
