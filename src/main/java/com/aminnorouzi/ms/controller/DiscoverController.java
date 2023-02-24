package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.function.SearchFunction;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.node.SearchNode;
import com.aminnorouzi.ms.node.SectionNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.application.Platform;
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
@FxmlView("/templates/view/discover-view.fxml")
public class DiscoverController extends Controller {

    @FXML
    private TextField searchField;

    public DiscoverController(ViewSwitcher switcher, NotificationService notification, LibraryService library, ActivityService activity, ImageLoader image) {
        super(switcher, notification, library, activity, image);
    }

    @Override
    protected void configure() {
        String text = (String) getInput();
        if (text != null) {
            searchField.setText(text);
            searchField.positionCaret(text.length());

            onSearch(null);
        }
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
        if (getContent().getChildren().size() > 1) {
            getContent().getChildren().remove(1);
        }

        String text = searchField.getText();
        if (!text.isBlank()) {
            Query query = Query.of(text);

            Thread background = new Thread(() -> Platform.runLater(() -> {
                try {
                    List<Search> searches = library.search(query);

                    getContent().getChildren().add(new SectionNode(this, "Your Search Results", false,
                            searches, new SearchFunction(false)));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }));

            background.setDaemon(true);
            background.start();

//            addMovieToLibrary(query);
        } else {
            notification.showError("You should type something first!");
        }
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
                    execute(() -> library.add(getUser(), search));

                    notification.show("info", ("Movie added to your library."));

                    searchField.clear();
                } catch (RuntimeException exception) {
                    notification.showError(exception.getMessage());
                }
            });
        } catch (RuntimeException exception) {
            notification.showError(exception.getMessage());
        }
    }
}
