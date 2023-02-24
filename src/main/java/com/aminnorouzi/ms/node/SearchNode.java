package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.model.movie.Search;
import com.aminnorouzi.ms.tool.image.ImageInfo;
import com.aminnorouzi.ms.tool.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class SearchNode extends StackPane implements Loadable {

    private final Controller controller;
    private final Search search;
    private final Boolean forced;

    private Movie movie;

    @FXML
    private Rectangle posterPic;
    @FXML
    private Label genreLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label yearLabel;

    public SearchNode(Controller controller, Search search, Boolean forced) {
        this.controller = controller;
        this.search = search;
        this.forced = forced;

        load(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread background = new Thread(() -> Platform.runLater(() -> {

            new Thread(() -> {
                if (forced) {
                    setMovie(controller.getLibrary().find(search));
                }

                Platform.runLater(() -> {
                    if (movie != null) {
                        genreLabel.setText(movie.getGenres().get(0));
                    }
                });
            }).start();

            ImageInfo backdropInfo = new ImageInfo(search.getPoster(), 181 * 4, 236 * 4, true);
            controller.getImage().load(backdropInfo).thenAccept(image -> {
                posterPic.setFill(new ImagePattern(image));
            });

            titleLabel.setText(search.getTitle());
            yearLabel.setText(String.valueOf(search.getReleased().getYear()));
        }));

        background.setDaemon(true);
        background.start();
    }

    @Override
    public String getPath() {
        return "/templates/node/search-node.fxml";
    }

    @FXML
    private void onMovie(ActionEvent event) {
        if (movie == null) {
            setMovie(controller.getLibrary().find(search));
        }

        controller.switchTo(View.MOVIE, movie);
    }
}