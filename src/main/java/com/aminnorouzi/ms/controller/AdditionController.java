package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.core.ApplicationContext;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@FxmlView("/view/addition-view.fxml")
public class AdditionController extends Controller {

    private final LibraryController libraryController;

    @FXML
    private TextField titleField;

    public AdditionController(ApplicationContext configuration, ViewSwitcher switcher, NotificationService notificationService,
                              LibraryService libraryService, LibraryController libraryController) {
        super(configuration, switcher, notificationService, libraryService);
        this.libraryController = libraryController;
    }

    @Override
    protected void configure() {

    }

    @FXML
    private void onChoose(ActionEvent event) {
        Stage stage = (Stage) getRoot().getScene().getWindow();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(stage);

        if (directory != null) {
            List<File> files = libraryService.read(directory);

            Set<Query> queries = libraryService.convert(files);
            queries.forEach(query -> new Thread(() -> addMovieToLibrary(query)).start());
        }
    }

    @FXML
    private void onSearch(ActionEvent event) {
        Query query = Query.of(titleField.getText());
        addMovieToLibrary(query);
    }

    private void addMovieToLibrary(Query query) {
        try {
            Map<String, Search> rows = new HashMap<>();

            ListView<String> listView = new ListView<>();
            listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            List<Search> searches = libraryService.search(query);
            searches.forEach(search -> {
                String row = search.getTitle() + " - " + search.getReleased().getYear();
                listView.getItems().add(row);
                rows.put(row + searches.indexOf(search), search);
            });

            notificationService.showList(listView, "Select a Movie", () -> {
                String selected = listView.getSelectionModel().getSelectedItem();
                String row = selected + listView.getItems().indexOf(selected);

                Search search = rows.get(row);

                Movie movie = libraryService.find(search);
                try {
                    Movie added = libraryService.add(getUser(), movie);
                    libraryController.addToScene(added, true);

                    notificationService.show("info", ("Movie added: " + added.getTitle().toLowerCase()));
                } catch (RuntimeException exception) {
                    notificationService.showError(exception.getMessage());
                }
            });
        } catch (RuntimeException exception) {
            notificationService.showError(exception.getMessage());
        }
    }
}
