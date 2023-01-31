package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.model.user.User;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.service.NotificationService;
import com.aminnorouzi.ms.tool.image.ImageService;
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
@FxmlView("/templates/view/addition-view.fxml")
public class AdditionController extends Controller {

    @FXML
    private TextField titleField;

    public AdditionController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageService image) {
        super(switcher, notification, library, activity, image);
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
            Set<Query> queries = library.generate(directory);
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

            List<Search> searches = library.search(query);
            searches.forEach(search -> {
                String row = search.getTitle() + " - " + search.getReleased().getYear();
                listView.getItems().add(row);
                rows.put(row + searches.indexOf(search), search);
            });

            notification.showList(listView, "Select a Movie", () -> {
                String selected = listView.getSelectionModel().getSelectedItem();
                String row = selected + listView.getItems().indexOf(selected);

                Search search = rows.get(row);
                try {
                    User user = library.add(getUser(), search);
                    setUser(user);

                    notification.show("info", ("Movie added to your library."));

                    titleField.clear();
                } catch (RuntimeException exception) {
                    notification.showError(exception.getMessage());
                }
            });
        } catch (RuntimeException exception) {
            notification.showError(exception.getMessage());
        }
    }
}
