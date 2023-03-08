package com.aminnorouzi.ms.controller.impl;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.controller.Emptiable;
import com.aminnorouzi.ms.controller.Searchable;
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
public class DiscoverController extends Controller implements Searchable, Emptiable {

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

            find(text);
        }
    }

    @FXML
    private void onSearch(ActionEvent event) {
        String text = searchField.getText();
        if (text.isBlank()) return;

        find(text);
    }

    @Override
    public void find(String text) {
        clear(getContent());

        Thread background = new Thread(() -> {
            Query query = Query.of(text);

            try {
                List<Search> result = library.search(query);

                Platform.runLater(() -> {
                    List<Search> movies = result.stream()
                            .filter(s -> s.getMediaType().equalsIgnoreCase("movie")).toList();

                    if (!movies.isEmpty()) {
                        SectionNode moviesSection = new SectionNode(this, "Movies",
                                false, movies, new SearchFunction());
                        supply(moviesSection);
                    }

                    List<Search> tvs = result.stream()
                            .filter(s -> s.getMediaType().equalsIgnoreCase("tv")).toList();

                    if (!tvs.isEmpty()) {
                        SectionNode tvsSection = new SectionNode(this, "Series",
                                false, tvs, new SearchFunction());
                        supply(tvsSection);
                    }
                });
            } catch (Exception exception) {
                Platform.runLater(() -> {
                    onEmpty(getContent(), this, "No result found, try something else!");
                });
            }
        });

        background.setDaemon(true);
        background.start();
    }
}
