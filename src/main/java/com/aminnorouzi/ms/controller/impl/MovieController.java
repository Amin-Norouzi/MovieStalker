package com.aminnorouzi.ms.controller.impl;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.node.GenreNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.image.Info;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FxmlView("/templates/view/movie-view.fxml")
public class MovieController extends Controller {

    @FXML
    private Rectangle backdropImage;
    @FXML
    private Rectangle posterImage;
    @FXML
    private Label ratingLabel;
    @FXML
    private Label overviewLabel;
    @FXML
    private Label releasedLabel;
    @FXML
    private Label runtimeLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Button watchButton;
    @FXML
    private Button addButton;
    @FXML
    private Button backButton;
    @FXML
    private HBox genrePane;

    private Movie movie;

    public MovieController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageLoader image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    protected void configure() {
        movie = (Movie) getInput();

        changeState(confirms(), movie.getWatchedAt() != null);

        image.load(movie.getBackdrop(), Info.MOVIE_BACKDROP, backdropImage);
        image.load(movie.getPoster(), Info.MOVIE_POSTER, posterImage);

        movie.getGenres().forEach(genre -> genrePane.getChildren().add(new GenreNode(this, genre)));

        titleLabel.setText(movie.getTitle());
        releasedLabel.setText(String.valueOf(movie.getReleased()));
        overviewLabel.setText(movie.getOverview());
        ratingLabel.setText(String.valueOf(movie.getRating()));
        runtimeLabel.setText(movie.getRuntime());
    }

    @FXML
    private void onMore(MouseEvent event) {
        try {
            String command = "open " + movie.getWebsite();
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onAdd(ActionEvent event) {
        try {
            if (!confirms()) {
                execute(() -> library.add(getUser(), movie));
                changeState(true);

                List<Movie> movies = getUser().getMovies();
                movie = movies.get(movies.size() - 1);
            } else {
                execute(() -> library.delete(movie));
                changeState(false);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onWatch(ActionEvent event) {
        if (movie.getWatchedAt() != null) {
            execute(() -> library.unwatch(movie));
            changeState(true, false);
        } else {
            execute(() -> library.watch(movie));
            changeState(true, true);
        }
    }

    @FXML
    private void onBack(ActionEvent event) {
        switchTo(View.PREVIOUS);
    }

    @FXML
    private void onEscape(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            switchTo(View.PREVIOUS);
        }
    }

    private void changeState(boolean added) {
        changeState(added, false);
    }

    private void changeState(boolean added, boolean watched) {
        if (added) {
            watchButton.setDisable(false);
            addButton.setText("In Library");
            addButton.setId("added-button");

            if (watched) {
                watchButton.setText("Watched");
            } else {
                watchButton.setText("Watch Now");
            }
        } else {
            watchButton.setDisable(true);
            addButton.setText("Add to Library");
            addButton.setId("add-button");

            movie.setUser(null);
            movie.setId(null);
        }
    }

    private boolean confirms() {
        return movie.getUser() != null || movie.getId() != null;
    }
}
