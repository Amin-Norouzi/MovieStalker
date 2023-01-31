package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.image.ImageService;
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

    public MovieController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageService image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    protected void configure() {
        movie = (Movie) getInput();

        image.load(movie.getBackdrop()).thenAccept(image -> backdropPic.setImage(image));

        image.load(movie.getPoster()).thenAccept(image -> {
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
        User current = getUser();
        User user = library.watch(movie);

        // the result is true! NOW works fine thanks to user equals and hashcode methods.
        if (user.equals(current)) {
            System.out.println("equals 🎊");
        } else {
            System.out.println("nah bro 🥹");
        }

        setUser(user);

        watch.setText("Watched");
    }

    @FXML
    private void onUnwatch(ActionEvent event) {
        User user = library.unwatch(movie);
        setUser(user);

        watch.setText("Watch Now");
    }

    @FXML
    private void onDelete(ActionEvent event) {
        notification.showConfirmation("Are you sure you want to delete this?", () -> {
            try {
                User user = library.delete(movie);
                setUser(user);

                switchTo(View.LIBRARY);
            } catch (RuntimeException exception) {
                exception.printStackTrace();
                notification.showError(exception.getMessage());
            }
        });
    }

    @FXML
    private void onBack(MouseEvent event) {
        switchTo(View.LIBRARY);
    }
}
