package com.aminnorouzi.ms.node;

import com.aminnorouzi.ms.exception.IllegalViewException;
import com.aminnorouzi.ms.model.movie.Movie;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class WatchedNode extends HBox {

    private static final String PATH = "/templates/node/watched-node.fxml";

    public WatchedNode(Movie movie, Integer count) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PATH));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

            Image image = new Image(movie.getBackdrop(), true);
            image.progressProperty().addListener((observable, oldValue, progress) -> {
                if ((Double) progress == 1.0 && !image.isError()) {
                    ((Rectangle) lookup("#poster")).setFill(new ImagePattern(image));
                }
            });

            ((Label) lookup("#rank")).setText(String.valueOf(count));

            ((Label) lookup("#title")).setText(movie.getTitle());
            ((Label) lookup("#year")).setText(String.valueOf(movie.getReleased().getYear()));
            ((Label) lookup("#genre")).setText(movie.getGenres().get(0));
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new IllegalViewException(exception.getMessage());
        }
    }
}

