package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.service.FileService;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.service.UserService;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@FxmlView("/view/addition-view.fxml")
public class AdditionController extends Controller {

    public AdditionController(ApplicationConfiguration configuration, ViewSwitcher switcher, FileService fileService,
                              NotificationService notificationService, MovieService movieService, UserService userService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService);
    }

    @FXML
    private TextField titleField;

    @Override
    protected void configure() {

    }

    @FXML
    private void onChoose(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);

        if (directory != null) {
            List<File> files = Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                    .filter(File::isFile)
                    .filter(fileService::verify)
                    .sorted().toList();

            Set<Query> queries = fileService.convert(files);

            // TODO: handle threads properly
            new Thread(() -> {
                List<Movie> movies = movieService.getByQueries(queries);

                Platform.runLater(() -> switcher.switchTo(View.RESULT, movies));
            }).start();
        }
    }

    @FXML
    private void onSearch(ActionEvent event) {
        Query query = Query.builder()
                .title(titleField.getText())
                .release("").build();

        try {
            Movie movie = movieService.getByQuery(query);
            Result result = Result.builder()
                    .found(List.of(movie)).build();

            switcher.switchTo(View.RESULT, result);
        } catch (RuntimeException exception) {
            // TODO: handle exception with notifications
        }
    }
}
