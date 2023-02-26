package com.aminnorouzi.ms.controller;

import com.aminnorouzi.ms.function.SearchFunction;
import com.aminnorouzi.ms.model.movie.Query;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.node.SectionNode;
import com.aminnorouzi.ms.service.ActivityService;
import com.aminnorouzi.ms.service.LibraryService;
import com.aminnorouzi.ms.tool.image.ImageLoader;
import com.aminnorouzi.ms.tool.notification.NotificationService;
import com.aminnorouzi.ms.tool.view.ViewSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FxmlView("/templates/view/discover-view.fxml")
public class DiscoverController extends Controller implements Searchable {

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

            search(text);
        }
    }

    @FXML
    private void onChoose(ActionEvent event) {
//        Stage stage = (Stage) getRoot().getScene().getWindow();
//
//        DirectoryChooser directoryChooser = new DirectoryChooser();
//        File directory = directoryChooser.showDialog(stage);
//
//        if (directory != null) {
//            Set<Query> queries = library.generate(directory);
//            queries.forEach(query -> new Thread(() -> addMovieToLibrary(query)).start());
//        }
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String text = searchField.getText();
        if (!text.isBlank()) {
            search(text);
        } else {
            notification.showError("You should type something first!");
        }
    }

    @Override
    public void search(String text) {
        clear(getContent());

        Query query = Query.of(text);
        Thread background = new Thread(() -> Platform.runLater(() -> {
            try {
                List<Search> searches = library.search(query);

                getContent().getChildren().add(new SectionNode(this, "Your Search Results",
                        false, searches, new SearchFunction(false)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }));

        background.setDaemon(true);
        background.start();
    }
}
