package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.service.*;
import com.aminnorouzi.ms.util.ViewSwitcher;
import com.dlsc.gemsfx.DialogPane;
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

import static com.dlsc.gemsfx.DialogPane.Type.INFORMATION;

@Slf4j
@Component
@FxmlView("/view/addition-view.fxml")
public class AdditionController extends Controller {

    @FXML
    private TextField titleField;

    public AdditionController(ApplicationConfiguration configuration, ViewSwitcher switcher, FileService fileService,
                              NotificationService notificationService, MovieService movieService, UserService userService,
                              LibraryService libraryService) {
        super(configuration, switcher, notificationService, movieService, fileService, userService, libraryService);
    }

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

                Platform.runLater(() -> notificationService.show(INFORMATION, "movies added!"));
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
            notificationService.show(INFORMATION, movie.getTitle());

        } catch (RuntimeException exception) {
            // TODO: handle exception with notifications
        }
    }
}
