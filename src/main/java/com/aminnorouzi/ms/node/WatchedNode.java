package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.controller.Controller;
import com.aminnorouzi.ms.exception.IllegalViewException;
import com.aminnorouzi.ms.model.movie.Movie;
import com.aminnorouzi.ms.tool.image.ImageService;
import com.aminnorouzi.ms.tool.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WatchedNode extends HBox {

    private static final String PATH = "/templates/node/watched-node.fxml";
    private final Controller controller;
    private final Movie movie;

    public WatchedNode(Movie movie, Integer count, Controller controller) {
        this.controller = controller;
        this.movie = movie;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PATH));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

            controller.getImage().load(movie.getBackdrop()).thenAccept(image -> {
                ((Rectangle) lookup("#poster")).setFill(new ImagePattern(image));
            });

            ((Label) lookup("#rank")).setText(String.valueOf(count));

            ((Label) lookup("#title")).setText(movie.getTitle());
            ((Label) lookup("#year")).setText(String.valueOf(movie.getReleased().getYear()));
            ((Label) lookup("#genre")).setText(movie.getGenres().get(0));
        } catch (Exception exception) {
            throw new IllegalViewException(exception.getMessage());
        }
    }

    @FXML
    private void onMovie(MouseEvent event) {
        controller.switchTo(View.MOVIE, movie);
    }
}

