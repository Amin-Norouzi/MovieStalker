package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.service.*;
import com.aminnorouzi.ms.util.ViewManager;
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
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FxmlView("/view/movie-view.fxml")
public class MovieController extends Controller {

    @FXML
    private ImageView backdrop;
    @FXML
    private Rectangle poster;
    @FXML
    private Label overview;
    @FXML
    private Label release;
    @FXML
    private Label title;
    @FXML
    private Button watch;
    @FXML
    private Button unwatch;

    private Movie movie;

    public MovieController(ApplicationConfiguration configuration, ViewManager switcher, FileService fileService,
                           NotificationService notificationService, MovieService movieService, UserService userService,
                           LibraryService libraryService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService, libraryService);
    }

    @Override
    protected void configure() {
        movie = (Movie) getInput();

        Image backdropImage = new Image("https://image.tmdb.org/t/p/w400" + movie.getBackdrop(),
                800, 600, false, false, true);
        backdropImage.progressProperty().addListener((observable, oldValue, progress) -> {
            if ((Double) progress == 1.0 && !backdropImage.isError()) {
                backdrop.setImage(backdropImage);
            }
        });

        Image posterImage = new Image("https://image.tmdb.org/t/p/w400" + movie.getPoster(),
                200, 300, false, false, true);

        posterImage.progressProperty().addListener((observable, oldValue, progress) -> {
            if ((Double) progress == 1.0 && !posterImage.isError()) {
                ImagePattern pattern = new ImagePattern(posterImage);

                poster.setFill(pattern);
            }
        });

        title.setText(movie.getTitle());
        release.setText(movie.getReleased());
        overview.setText(movie.getOverview());

        if (movie.getWatchedAt() != null) {
            changeState(true);
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
            watch.setText("Watched 👀");

            unwatch.setVisible(true);
        } else {
            watch.setDisable(false);
            watch.setText("Watch Now");

            unwatch.setVisible(false);
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        try {
            libraryService.delete(movie);
            switchTo(View.LIBRARY);
        } catch (RuntimeException exception) {}
    }

    @FXML
    private void onBack(MouseEvent event) {
        switchTo(View.LIBRARY);
//        DialogPane.Dialog<Object> dialog = new DialogPane.Dialog(dialogPane, DialogPane.Type.WARNING);
//        dialog.setTitle("Maximized");
//        dialog.setContent(new Label("Dialog using all available width and height."));
//        dialog.setMaximize(true);
//        dialogPane.showDialog(dialog);
    }
}
