package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.service.TaskService;
import com.aminnorouzi.ms.util.ViewSwitcher;
import com.dlsc.gemsfx.DialogPane;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import static com.dlsc.gemsfx.DialogPane.Type.INFORMATION;

@Slf4j
@Component
@FxmlView("/view/movie-view.fxml")
public class MovieController extends Controller {

    private final ViewSwitcher switcher;
    private final MovieService movieService;
    private final TaskService taskService;

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
    private StackPane root;

    private final DialogPane dialogPane = new DialogPane();

    public MovieController(ApplicationConfiguration configuration, ViewSwitcher switcher, ViewSwitcher switcher1, MovieService movieService, TaskService taskService) {
        super(configuration, switcher, movieService);
        this.switcher = switcher1;
        this.movieService = movieService;
        this.taskService = taskService;
    }

    @Override
    protected void configure() {
        Movie movie = (Movie) getInput();

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

//        Task task = new Task() {
//            Image image;
//
//            @Override
//            protected Object call() throws Exception {
//                image = new Image("https://image.tmdb.org/t/p/original" + movie.getPoster());
//                return null;
//            }
//
//            @Override
//            protected void succeeded() {
//                super.succeeded();
//                Platform.runLater(() -> {
//                    imageView.setImage(image);
//                    ComponentUtils.roundImage(imageView);
//                });
//            }
//        };
//
//        taskService.run(task);

        title.setText(movie.getTitle());
        release.setText(movie.getRelease());
        overview.setText(movie.getOverview());

//        root.getChildren().add(dialogPane);
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
