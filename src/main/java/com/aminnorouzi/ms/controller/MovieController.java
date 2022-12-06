package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.service.FileService;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.service.UserService;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.fxml.FXML;
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

    public MovieController(ApplicationConfiguration configuration, ViewSwitcher switcher,
                           NotificationService notificationService, MovieService movieService, FileService fileService,
                           UserService userService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService);
    }

    @Override
    protected void configure() {
        Movie movie = (Movie) input;

        Image backdropImage = new Image("https://image.tmdb.org/t/p/original" + movie.getBackdrop(),
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
        release.setText(movie.getRelease());
        overview.setText(movie.getOverview());
    }

    @FXML
    private void onBack(MouseEvent event) {
        switcher.switchTo(View.LIBRARY);
//        DialogPane.Dialog<Object> dialog = new DialogPane.Dialog(dialogPane, DialogPane.Type.WARNING);
//        dialog.setTitle("Maximized");
//        dialog.setContent(new Label("Dialog using all available width and height."));
//        dialog.setMaximize(true);
//        dialogPane.showDialog(dialog);
    }
}
