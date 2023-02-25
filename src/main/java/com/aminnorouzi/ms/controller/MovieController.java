package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageInfo;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.view.View;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import static com.aminnorouzi.ms.tool.image.ImageInfo.Type;

@Slf4j
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
        if (movie.getUser() == null) {
            movie.setUser(getUser());
        }

        ImageInfo backdropInfo = new ImageInfo(movie.getBackdrop(), 1280, 832, true, Type.BACKDROP);
        image.load(backdropInfo).thenAccept(image -> {
            ImagePattern pattern = new ImagePattern(image);
            backdropImage.setFill(pattern);
        });

        ImageInfo posterInfo = new ImageInfo(movie.getPoster(), 300, 960, true, Type.POSTER);
        image.load(posterInfo).thenAccept(image -> {
            ImagePattern pattern = new ImagePattern(image);
            posterImage.setFill(pattern);
        });

        movie.getGenres().forEach(genre -> {
            Button button = new Button(genre);
            button.setId("genre-button");

            genrePane.getChildren().add(button);
        });

        titleLabel.setText(movie.getTitle());
        releasedLabel.setText(String.valueOf(movie.getReleased()));
        overviewLabel.setText(movie.getOverview());
        ratingLabel.setText(String.valueOf(movie.getRating()));
        runtimeLabel.setText(movie.getRuntime());

        if (movie.getWatchedAt() != null) {
            watchButton.setText("Watched");
        }
        if (movie.getId() != null) {
            watchButton.setDisable(false);
            addButton.setText("In Library");
            addButton.setId("added-button");
        }
    }

    @FXML
    private void onMore(MouseEvent event) {
        try {
            String command = "open " + movie.getWebsite();
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @FXML
    private void onAdd(ActionEvent event) {
        if (movie.getId() == null) {
            execute(() -> library.add(movie));

            watchButton.setDisable(false);
            addButton.setText("In Library");
            addButton.setId("added-button");
        } else {
            execute(() -> library.delete(movie), View.PREVIOUS);
        }
    }

    @FXML
    private void onWatch(ActionEvent event) {
        if (movie.getWatchedAt() != null) {
            execute(() -> library.unwatch(movie));
            watchButton.setText("Watch Now");
        } else {
            execute(() -> library.watch(movie));
            watchButton.setText("Watched");
        }
    }

    @FXML
    private void onBack(ActionEvent event) {
        switchTo(View.PREVIOUS);
    }
}
