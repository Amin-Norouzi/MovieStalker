package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.configuration.ApplicationConfiguration;
import com.aminnorouzi.ms.model.View;
import com.aminnorouzi.ms.model.input.Result;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.service.FileService;
import com.aminnorouzi.ms.service.MovieService;
import com.aminnorouzi.ms.util.ViewSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    private final ViewSwitcher switcher;
    private final FileService fileService;
    private final MovieService movieService;

    public AdditionController(ApplicationConfiguration configuration, ViewSwitcher switcher, MovieService movieService
            , FileService fileService) {
        super(configuration, switcher, movieService);
        this.switcher = switcher;
        this.fileService = fileService;
        this.movieService = movieService;
    }

//    @FXML
//    private Button chooseButton;
//
//    @FXML
//    private TextField imdbField;
//
//    @FXML
//    private Button searchButton;
//
//    @FXML
//    private TextField titleField;

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
                    .filter(fileService::isValid)
                    .sorted().toList();

            Set<Query> queries = fileService.convertFilesToQueries(files);

            // TODO: handle threads properly
            new Thread(() -> {
                Result result = movieService.getByQueries(queries);

                Platform.runLater(() -> switcher.switchTo(View.RESULT, result));
            }).start();
        }
    }

    @FXML
    private void onSearch(ActionEvent event) {

    }
}
